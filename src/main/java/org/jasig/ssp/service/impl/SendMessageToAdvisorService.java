package org.jasig.ssp.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang.StringUtils;
import org.jasig.ssp.model.*;
import org.jasig.ssp.model.external.FacultyCourse;
import org.jasig.ssp.model.external.Term;
import org.jasig.ssp.model.reference.EnrollmentStatus;
import org.jasig.ssp.service.EarlyAlertRoutingService;
import org.jasig.ssp.service.MessageService;
import org.jasig.ssp.service.ObjectNotFoundException;
import org.jasig.ssp.service.PersonService;
import org.jasig.ssp.service.external.FacultyCourseService;
import org.jasig.ssp.service.external.TermService;
import org.jasig.ssp.service.reference.ConfigService;
import org.jasig.ssp.service.reference.EnrollmentStatusService;
import org.jasig.ssp.service.reference.MessageTemplateService;
import org.jasig.ssp.transferobject.messagetemplate.CoachPersonLiteMessageTemplateTO;
import org.jasig.ssp.transferobject.messagetemplate.EarlyAlertMessageTemplateTO;
import org.jasig.ssp.util.sort.PagingWrapper;
import org.jasig.ssp.util.sort.SortingAndPaging;
import org.jasig.ssp.web.api.validation.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.SendFailedException;
import javax.validation.constraints.NotNull;
import java.util.*;

@Service
public class SendMessageToAdvisorService {

    //1
    @Autowired
    private transient ConfigService configService;

    //1
    @Autowired
    private transient MessageTemplateService messageTemplateService;

    @Autowired
    private transient EarlyAlertValidatorUtils earlyAlertValidatorUtils;

    //1
    @Autowired
    private transient MessageService messageService;

    //1
    @Autowired
    private transient EarlyAlertRoutingService earlyAlertRoutingService;

    //1
    @Autowired
    private transient PersonService personService;

    //1
    @Autowired
    private transient FacultyCourseService facultyCourseService;

    //1
    @Autowired
    private transient TermService termService;

    //1
    @Autowired
    private transient EnrollmentStatusService enrollmentStatusService;

    private static final Logger LOGGER = LoggerFactory
            .getLogger(EarlyAlertServiceImpl.class);

    public void sendMessageToAdvisor(EarlyAlert saved,String emailCC,Logger LOGGER ) throws ValidationException {
        // Send e-mail to assigned advisor (coach)
//1

        try {
            sendMessageToAdvisorEfective(saved, emailCC, LOGGER);
//1
        } catch (final SendFailedException | ObjectNotFoundException e) {
            LOGGER.warn(
                    "Could not send Early Alert message to advisor.",
                    e);
            throw new ValidationException(
                    "Early Alert notification e-mail could not be sent to advisor. Early Alert was NOT created.",
                    e);
        }
    }

    public void sendConfirmationMessageToFaculty(EarlyAlert saved,Logger LOGGER) throws ValidationException {
        try {
            sendConfirmationMessageToFacultyEfective(saved,LOGGER);
//1
        } catch (final SendFailedException | ObjectNotFoundException | ValidationException e) {
            LOGGER.warn(
                    "Could not send Early Alert confirmation to faculty.",
                    e);
            throw new ValidationException(
                    "Early Alert confirmation e-mail could not be sent. Early Alert was NOT created.",
                    e);
        }
    }

    /**
     * Send e-mail ({@link Message}) to the assigned advisor for the student.
     *
     * @param earlyAlert
     *            Early Alert
     * @param emailCC
     *            Email address to also CC this message
     * @throws ObjectNotFoundException
     * @throws SendFailedException
     * @throws ValidationException
     */
    private void sendMessageToAdvisorEfective(@NotNull final EarlyAlert earlyAlert, // NOPMD
                                      final String emailCC,Logger LOGGER) throws ObjectNotFoundException,
            SendFailedException, ValidationException {

        earlyAlertValidatorUtils.earlyAlertCannotBeNull(earlyAlert,new IllegalArgumentException("Early alert was missing."));
        earlyAlertValidatorUtils.earlyAlertPersonCannotBeNull(earlyAlert,new IllegalArgumentException("EarlyAlert Person is missing."));

        final Person person = earlyAlert.getPerson().getCoach();

        final SubjectAndBody subjAndBody = messageTemplateService
                .createEarlyAlertAdvisorConfirmationMessage(fillTemplateParameters(earlyAlert));

        Set<String> watcherEmailAddresses = new HashSet<String>(earlyAlert.getPerson().getWatcherEmailAddresses());
//1
        if(emailCC != null && !emailCC.isEmpty())
        {
            watcherEmailAddresses.add(emailCC);
        }
//1
        if ( person == null ) {
            LOGGER.warn("Student {} had no coach when EarlyAlert {} was"
                            + " created. Unable to send message to coach.",
                    earlyAlert.getPerson(), earlyAlert);
//1
        } else {
            // Create and queue the message
            final Message message = messageService.createMessage(person, org.springframework.util.StringUtils.arrayToCommaDelimitedString(watcherEmailAddresses
                            .toArray(new String[watcherEmailAddresses.size()])),
                    subjAndBody);
            LOGGER.info("Message {} created for EarlyAlert {}", message, earlyAlert);
        }

        // Send same message to all applicable Campus Early Alert routing
        // entries
        final PagingWrapper<EarlyAlertRouting> routes = earlyAlertRoutingService
                .getAllForCampus(earlyAlert.getCampus(), new SortingAndPaging(
                        ObjectStatus.ACTIVE));
//1
        if (routes.getResults() > 0) {
            final ArrayList<String> alreadySent = Lists.newArrayList();
//1
            for (final EarlyAlertRouting route : routes.getRows()) {
                // Check that route applies
//1
                if ( route.getEarlyAlertReason() == null ) {
                    throw new ObjectNotFoundException(
                            "EarlyAlertRouting missing EarlyAlertReason.", "EarlyAlertReason");
                }

//1                // Only routes that are for any of the Reasons in this EarlyAlert should be applied.
                if ( (earlyAlert.getEarlyAlertReasons() == null)
                        || !earlyAlert.getEarlyAlertReasons().contains(route.getEarlyAlertReason()) ) {
                    continue;
                }

                // Send e-mail to specific person
                final Person to = route.getPerson();
//1
                if ( to != null && StringUtils.isNotBlank(to.getPrimaryEmailAddress()) ) {
                    //check if this alert has already been sent to this recipient, if so skip
//1
                    if ( alreadySent.contains(route.getPerson().getPrimaryEmailAddress()) ) {
                        continue;
//1
                    } else {
                        alreadySent.add(route.getPerson().getPrimaryEmailAddress());
                    }

                    final Message message = messageService.createMessage(to, null, subjAndBody);
                    LOGGER.info("Message {} for EarlyAlert {} also routed to {}",
                            new Object[]{message, earlyAlert, to}); // NOPMD
                }

                // Send e-mail to a group
//1
                if ( !StringUtils.isEmpty(route.getGroupName()) && !StringUtils.isEmpty(route.getGroupEmail()) ) {
                    final Message message = messageService.createMessage(route.getGroupEmail(), null,subjAndBody);
                    LOGGER.info("Message {} for EarlyAlert {} also routed to {}", new Object[] { message, earlyAlert, // NOPMD
                            route.getGroupEmail() });
                }
            }
        }
    }


    /**
     * Send confirmation e-mail ({@link Message}) to the faculty who created
     * this alert.
     *
     * @param earlyAlert
     *            Early Alert
     * @throws ObjectNotFoundException
     * @throws SendFailedException
     * @throws ValidationException
     */
    private void sendConfirmationMessageToFacultyEfective(final EarlyAlert earlyAlert,Logger LOGGER)
            throws ObjectNotFoundException, SendFailedException,
            ValidationException {
        earlyAlertValidatorUtils.earlyAlertCannotBeNull(earlyAlert,new IllegalArgumentException("EarlyAlert was missing."));
        earlyAlertValidatorUtils.earlyAlertPersonCannotBeNull(earlyAlert,new IllegalArgumentException("EarlyAlert.Person is missing."));

//1
        if (configService.getByNameOrDefaultValue("send_faculty_mail") != true) {
            LOGGER.debug("Skipping Faculty Early Alert Confirmation Email: Config Turned Off");
            return; //skip if faculty early alert email turned off
        }

        final UUID personId = earlyAlert.getCreatedBy().getId();
        Person person = personService.get(personId);
//1
        if ( person == null ) {
            LOGGER.warn("EarlyAlert {} has no creator. Unable to send"
                    + " confirmation message to faculty.", earlyAlert);
//1
        } else {
            final SubjectAndBody subjAndBody = messageTemplateService
                    .createEarlyAlertFacultyConfirmationMessage(fillTemplateParameters(earlyAlert));

            // Create and queue the message
            final Message message = messageService.createMessage(person, null,
                    subjAndBody);

            LOGGER.info("Message {} created for EarlyAlert {}", message, earlyAlert);
        }
    }


     public Map<String, Object> fillTemplateParameters(
     @NotNull final EarlyAlert earlyAlert) {

     earlyAlertValidatorUtils.earlyAlertCannotBeNull(earlyAlert,new IllegalArgumentException("EarlyAlert was missing."));
     earlyAlertValidatorUtils.earlyAlertPersonCannotBeNull(earlyAlert,new IllegalArgumentException("EarlyAlert.Person is missing."));

     //1
     if (earlyAlert.getCreatedBy() == null) {
     throw new IllegalArgumentException(
     "EarlyAlert.CreatedBy is missing.");
     }
     //1
     if (earlyAlert.getCampus() == null) {
     throw new IllegalArgumentException("EarlyAlert.Campus is missing.");
     }

     //1		// ensure earlyAlert.createdBy is populated
     if ((earlyAlert.getCreatedBy() == null)
     || (earlyAlert.getCreatedBy().getFirstName() == null)) {
     //1
     if (earlyAlert.getCreatedBy() == null) {
     throw new IllegalArgumentException(
     "EarlyAlert.CreatedBy is missing.");
     }
     }

     final Map<String, Object> templateParameters = Maps.newHashMap();

     final String courseName = earlyAlert.getCourseName();
     //1
     if ( StringUtils.isNotBlank(courseName) ) {
     Person creator;
     //1
     try {
     creator = personService.get(earlyAlert.getCreatedBy().getId());
     //1
     } catch (ObjectNotFoundException e1) {
     throw new IllegalArgumentException(
     "EarlyAlert.CreatedBy.Id could not be loaded.", e1);
     }
     final String facultySchoolId = creator.getSchoolId();
     //1
     if ( (StringUtils.isNotBlank(facultySchoolId)) ) {
     String termCode = earlyAlert.getCourseTermCode();
     FacultyCourse course = null;
     //1
     try {
     //1
     if ( StringUtils.isBlank(termCode) ) {
     course = facultyCourseService.
     getCourseByFacultySchoolIdAndFormattedCourse(
     facultySchoolId, courseName);
     //1
     } else {
     course = facultyCourseService.
     getCourseByFacultySchoolIdAndFormattedCourseAndTermCode(
     facultySchoolId, courseName, termCode);
     }
     //1
     } catch ( ObjectNotFoundException e ) {
     // Trace irrelevant. see below for logging. prefer to
     // do it there, after the null check b/c not all service
     // methods implement ObjectNotFoundException reliably.
     }
     //1
     if ( course != null ) {
     templateParameters.put("course", course);
     //1
     if ( StringUtils.isBlank(termCode) ) {
     termCode = course.getTermCode();
     }
     //1
     if ( StringUtils.isNotBlank(termCode) ) {
     Term term = null;
     //1
     try {
     term = termService.getByCode(termCode);
     //1
     } catch ( ObjectNotFoundException e ) {
     // Trace irrelevant. See below for logging.
     }
     //1
     if ( term != null ) {
     templateParameters.put("term", term);
     //1
     } else {
     LOGGER.info("Not adding term to message template"
     + " params or early alert {} because"
     + " the term code {} did not resolve to"
     + " an external term record",
     earlyAlert.getId(), termCode);
     }
     }
     //1
     } else {
     LOGGER.info("Not adding course nor term to message template"
     + " params for early alert {} because the associated"
     + " course {} and faculty school id {} did not"
     + " resolve to an external course record.",
     new Object[] { earlyAlert.getId(), courseName,
     facultySchoolId});
     }
     }
     }
     Person creator = null;
     //1
     try{
     creator = personService.get(earlyAlert.getCreatedBy().getId());
     //1
     }catch(ObjectNotFoundException exp)	{
     LOGGER.error("Early Alert Creator Not found sending message for early alert:" + earlyAlert.getId(), exp);
     }

     EarlyAlertMessageTemplateTO eaMTO = new EarlyAlertMessageTemplateTO(earlyAlert,creator);

     //Only early alerts response late messages sent to coaches
     //1
     if(eaMTO.getCoach() == null){
     //1
     try{
     // if no earlyAlert.getCampus()  error thrown by design, should never not be a campus.
     eaMTO.setCoach(new CoachPersonLiteMessageTemplateTO
             (personService.get(earlyAlert.getCampus().getEarlyAlertCoordinatorId())));
     //1
     }catch(ObjectNotFoundException exp){
     LOGGER.error("Early Alert with id: " + earlyAlert.getId() + " does not have valid campus coordinator, no coach assigned: " + earlyAlert.getCampus().getEarlyAlertCoordinatorId(), exp);
     }
     }

     String statusCode = eaMTO.getEnrollmentStatus();
     //1
     if(statusCode != null) {
     EnrollmentStatus enrollmentStatus = enrollmentStatusService.getByCode(statusCode);
     //1
     if(enrollmentStatus != null) {

     //if we have made it here... we can add the status!
     templateParameters.put("enrollment", enrollmentStatus);
     }
     }




     templateParameters.put("earlyAlert", eaMTO);
     templateParameters.put("termToRepresentEarlyAlert",
     configService.getByNameEmpty("term_to_represent_early_alert"));
     templateParameters.put("TermToRepresentEarlyAlert",
     configService.getByNameEmpty("term_to_represent_early_alert"));
     templateParameters.put("termForEarlyAlert",
     configService.getByNameEmpty("term_to_represent_early_alert"));
     templateParameters.put("linkToSSP",
     configService.getByNameEmpty("serverExternalPath"));
     templateParameters.put("applicationTitle",
     configService.getByNameEmpty("app_title"));
     templateParameters.put("institutionName",
     configService.getByNameEmpty("inst_name"));

     templateParameters.put("FirstName", eaMTO.getPerson().getFirstName());
     templateParameters.put("LastName", eaMTO.getPerson().getLastName());
     templateParameters.put("CourseName", eaMTO.getCourseName());

     return templateParameters;
     }

}
