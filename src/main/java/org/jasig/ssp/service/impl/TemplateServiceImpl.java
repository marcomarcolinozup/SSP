/**
 * Licensed to Jasig under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * Jasig licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a
 * copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.jasig.ssp.service.impl;

import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.jasig.ssp.dao.TemplateDao;
import org.jasig.ssp.model.MapTemplateVisibility;
import org.jasig.ssp.model.SubjectAndBody;
import org.jasig.ssp.model.Template;
import org.jasig.ssp.service.ObjectNotFoundException;
import org.jasig.ssp.service.PersonService;
import org.jasig.ssp.service.SecurityService;
import org.jasig.ssp.service.TemplateService;
import org.jasig.ssp.service.external.ExternalDepartmentService;
import org.jasig.ssp.service.external.ExternalDivisionService;
import org.jasig.ssp.service.external.ExternalProgramService;
import org.jasig.ssp.service.reference.ConfigService;
import org.jasig.ssp.transferobject.TemplateOutputTO;
import org.jasig.ssp.transferobject.TemplateSearchTO;
import org.jasig.ssp.transferobject.TemplateTO;
import org.jasig.ssp.transferobject.reference.AbstractMessageTemplateMapPrintParamsTO;
import org.jasig.ssp.transferobject.reference.MessageTemplatePlanPrintParams;
import org.jasig.ssp.transferobject.reference.MessageTemplatePlanTemplatePrintParamsTO;
import org.jasig.ssp.util.sort.PagingWrapper;
import org.jasig.ssp.util.sort.SortingAndPaging;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * 
 * @author tony.arland
 */
@Service
@Transactional
public  class TemplateServiceImpl extends AbstractPlanServiceImpl<Template,
TemplateTO,TemplateOutputTO, MessageTemplatePlanTemplatePrintParamsTO> implements TemplateService {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(TemplateServiceImpl.class);
	
	private static String ANONYMOUS_MAP_TEMPLATE_ACCESS="anonymous_map_template_access";

	@Autowired
	private transient TemplateDao dao;
	
	@Autowired
	private PersonService personService;
	
	@Autowired
	private transient ExternalDepartmentService departmentService;
	
	@Autowired
	private transient ExternalDivisionService divisionService;
	
	@Autowired
	private transient ExternalProgramService programService;
	
	@Autowired
	private transient SecurityService securityService;
	
	@Autowired
	private transient ConfigService configService;
	
	@Override
	protected TemplateDao getDao() {
		return dao;
	}

	@Override
	protected UUID getPersonIdPlannedFor(TemplateTO model) {
		// templates are not "planned for" anybody in particular
		return null;
	}

	@Override
	@Transactional(readOnly=true)
	public SubjectAndBody createOutput(TemplateOutputTO templateOutputDataTO) throws ObjectNotFoundException {

		SubjectAndBody output = null;
		
		MessageTemplatePlanTemplatePrintParamsTO params = new MessageTemplatePlanTemplatePrintParamsTO();
		params.setMessageTemplateId(templateOutputDataTO.getMessageTemplateMatrixId());
		params.setOutputPlan(templateOutputDataTO);
		
		TemplateTO to = templateOutputDataTO.getNonOutputTO();
		if(StringUtils.isNotBlank(to.getDepartmentCode())) {
			try {
				params.setDepartmentName(departmentService.getByCode(to.getDepartmentCode()).getName());
			} catch ( ObjectNotFoundException e ) {
				LOGGER.info("Template {} has invalid department code {}", to.getId(), to.getDepartmentCode());
			}
		}

		if(StringUtils.isNotBlank(to.getDivisionCode())) {
			try {
				params.setDivisionName(divisionService.getByCode(to.getDivisionCode()).getName());
			} catch ( ObjectNotFoundException e ) {
				LOGGER.info("Template {} has invalid division code {}", to.getId(), to.getDivisionCode());
			}
		}

		if(StringUtils.isNotBlank(to.getProgramCode())) {
			try {
				params.setProgramName(programService.getByCode(to.getProgramCode()).getName());
			} catch ( ObjectNotFoundException e ) {
				LOGGER.info("Template {} has invalid program code {}", to.getId(), to.getProgramCode());
			}
		}
		
		params.setInstitutionName(getInstitutionName());
		
		if(StringUtils.isNotBlank(templateOutputDataTO.getPlan().getOwnerId())){
			params.setOwner(personService.get(
					UUID.fromString(templateOutputDataTO.getPlan().getOwnerId())));
		}
		
		if(templateOutputDataTO.getOutputFormat().equals(TemplateService.OUTPUT_FORMAT_MATRIX)) {
			output = createMatrixOutput(params);
		} else{
			output = createFullOutput(templateOutputDataTO);
		}

		return output;
	}

	@Override
	public PagingWrapper<Template> getAll(
			SortingAndPaging createForSingleSortWithPaging,TemplateSearchTO searchTO) {

		if(!securityService.isAuthenticated() || !securityService.hasAuthority("ROLE_PERSON_MAP_READ")){
			if(!anonymousUsersAllowed()){
				LOGGER.info("Invalid request for templates, requested by anonymous user, anonymous access is not enabled");
				throw new AccessDeniedException("Invalid request for templates, requested by anonymous user, anonymous access is not enabled");
			}
			if(searchTO.visibilityAll())
				searchTO.setVisibility(MapTemplateVisibility.ANONYMOUS);
			if(!searchTO.getVisibility().equals(MapTemplateVisibility.ANONYMOUS)){
				LOGGER.info("Invalid request for templates, request by anonymous user request was for private/authenticated templates only.");
				throw new AccessDeniedException("Invalid request for templates, request by anonymous user request was for private/authenticated templates only.");
			}
		}
		return getDao().getAll(createForSingleSortWithPaging, searchTO);
	}
	
	private Boolean anonymousUsersAllowed() {
		return Boolean.parseBoolean(configService.getByName(ANONYMOUS_MAP_TEMPLATE_ACCESS).getValue().toLowerCase());
	}
}