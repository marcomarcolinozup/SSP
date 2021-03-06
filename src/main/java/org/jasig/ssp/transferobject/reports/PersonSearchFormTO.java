/**
 * Licensed to Apereo under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * Apereo licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License.  You may obtain a
 * copy of the License at the following location:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.jasig.ssp.transferobject.reports;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.jasig.ssp.model.external.Term;
import org.jasig.ssp.transferobject.PersonTO;

/**
 * Use this for storing criteria used to search for persons (primariy students) transfer object
 */
public class PersonSearchFormTO
		implements Serializable {

	private static final long serialVersionUID = 3118831549819428989L;

	private UUID programStatus;

	private List<UUID> specialServiceGroupIds;

	private List<UUID> referralSourcesIds;

	private String anticipatedStartTerm;

	private Integer anticipatedStartYear;

	private List<UUID> studentTypeIds;

	private List<UUID> homeCampusIds;

	private List<UUID> serviceReasonsIds;

	private Date createDateFrom;
	
	private Date studentIntakeCompleteDateFrom;
	
	private Date studentIntakeCompleteDateTo;

	private Date createDateTo;
	
	private PersonTO coach;
	
	private PersonTO watcher;
	
	private PersonTO odsCoach;
	
	private UUID disabilityStatusId;
	
	private UUID disabilityTypeId;
	
	private Integer actualStartYear;
	
	private List<String> actualStartTerms;
	
	private Boolean disabilityIsNotNull = false;
	
	private String homeDepartment;
	
	private String rosterStatus;
	
	private Boolean specialServiceGroupRequired = false;
	
	private Term termRegisteredFor;  // use this to search for students registered in a specific term. Currently manually implemented

	public String getHomeDepartment() {
		return homeDepartment;
	}

	public void setHomeDepartment(String homeDepartment) {
		this.homeDepartment = homeDepartment;
	}

	public PersonTO getOdsCoach() {
		return odsCoach;
	}

	public void setOdsCoach(PersonTO odsCoach) {
		this.odsCoach = odsCoach;
	}

	public UUID getDisabilityStatusId() {
		return disabilityStatusId;
	}

	public void setDisabilityStatusId(UUID disabilityStatus) {
		this.disabilityStatusId = disabilityStatus;
	}

	public Integer getActualStartYear() {
		return actualStartYear;
	}

	public void setActualStartYear(Integer actualStartYear) {
		this.actualStartYear = actualStartYear;
	}

	public List<String> getActualStartTerms() {
		return actualStartTerms;
	}

	public void setActualStartTerms(List<String> actualStartTerms) {
		this.actualStartTerms = actualStartTerms;
	}

	public PersonSearchFormTO(){
		
	}
	public PersonSearchFormTO(final PersonTO coach,
								final UUID programStatus,
								final List<UUID> specialServiceGroupId,
								final List<UUID> referralSourcesId,
								final String anticipatedStartTerm,
								final Integer anticipatedStartYear,
								final List<UUID> studentTypeIds,
								final Date createDateFrom, final Date createDateTo,
							  	final List<UUID> homeCampusIds) {
		super();
		this.coach = coach;
		this.programStatus = programStatus;
		specialServiceGroupIds = specialServiceGroupId;
		referralSourcesIds = referralSourcesId;
		this.anticipatedStartTerm = anticipatedStartTerm;
		this.anticipatedStartYear = anticipatedStartYear;
		this.studentTypeIds = studentTypeIds;
		this.createDateFrom = createDateFrom == null ? null : new Date(
				createDateFrom.getTime());
		this.createDateTo = createDateTo == null ? null : new Date(
				createDateTo.getTime());
		this.homeCampusIds = homeCampusIds;
	}

	public UUID getProgramStatus() {
		return programStatus;
	}

	public void setProgramStatus(UUID programStatus) {
		this.programStatus = programStatus;
	}

	public Date getCreateDateFrom() {
		return createDateFrom == null ? null : new Date(
				createDateFrom.getTime());
	}

	public void setCreateDateFrom(final Date createDateFrom) {
		this.createDateFrom = createDateFrom == null ? null : new Date(
				createDateFrom.getTime());
	}

	public Date getCreateDateTo() {
		return createDateTo == null ? null : new Date(createDateTo.getTime());
	}

	public void setCreateDateTo(final Date createDateTo) {
		this.createDateTo = createDateTo == null ? null : new Date(
				createDateTo.getTime());
	}

	public Date getStudentIntakeCompleteDateFrom() {
		return studentIntakeCompleteDateFrom == null ? null : new Date(studentIntakeCompleteDateFrom.getTime());
	}

	public void setStudentIntakeCompleteDateFrom(final Date studentIntakeCompleteDateFrom) {
		this.studentIntakeCompleteDateFrom = studentIntakeCompleteDateFrom == null ? null : new Date(studentIntakeCompleteDateFrom.getTime());
	}
	
	public Date getStudentIntakeCompleteDateTo() {
		return studentIntakeCompleteDateTo == null ? null : new Date(studentIntakeCompleteDateTo.getTime());
	}

	public void setStudentIntakeCompleteDateTo(final Date studentIntakeCompleteDateTo) {
		this.studentIntakeCompleteDateTo = studentIntakeCompleteDateTo == null ? null : new Date(studentIntakeCompleteDateTo.getTime());

	}


	public List<UUID> getStudentTypeIds() {
		return studentTypeIds;
	}

	public void setStudentTypeIds(final List<UUID> studentTypeIds) {
		this.studentTypeIds = studentTypeIds;
	}

	public List<UUID> getHomeCampusIds() {
		return homeCampusIds;
	}

	public void setHomeCampusIds(final List<UUID> homeCampusIds) {
		this.homeCampusIds = homeCampusIds;
	}


	public List<UUID> getSpecialServiceGroupIds() {
		return specialServiceGroupIds;
	}

	public void setSpecialServiceGroupIds(
			final List<UUID> specialServiceGroupIds) {
		this.specialServiceGroupIds = specialServiceGroupIds;
	}

	public List<UUID> getReferralSourcesIds() {
		return referralSourcesIds;
	}

	public void setReferralSourcesIds(final List<UUID> referralSourcesIds) {
		this.referralSourcesIds = referralSourcesIds;
	}

	/**
	 * @return the serviceReasonsIds
	 */
	public List<UUID> getServiceReasonsIds() {
		return serviceReasonsIds;
	}

	/**
	 * @param serviceReasonsIds the serviceReasonsIds to set
	 */
	public void setServiceReasonsIds(List<UUID> serviceReasonsIds) {
		this.serviceReasonsIds = serviceReasonsIds;
	}

	public String getAnticipatedStartTerm() {
		return anticipatedStartTerm;
	}

	public void setAnticipatedStartTerm(final String anticipatedStartTerm) {
		this.anticipatedStartTerm = anticipatedStartTerm;
	}

	public Integer getAnticipatedStartYear() {
		return anticipatedStartYear;
	}

	public void setAnticipatedStartYear(final Integer anticipatedStartYear) {
		this.anticipatedStartYear = anticipatedStartYear;
	}

	public PersonTO getCoach() {
		return coach;
	}

	public void setCoach(PersonTO coach) {
		this.coach = coach;
	}

	public Boolean getDisabilityIsNotNull() {
		return disabilityIsNotNull;
	}

	public void setDisabilityIsNotNull(Boolean disabilityIsNotNull) {
		this.disabilityIsNotNull = disabilityIsNotNull;
	}

	public UUID getDisabilityTypeId() {
		return disabilityTypeId;
	}

	public void setDisabilityTypeId(UUID disabilityTypeId) {
		this.disabilityTypeId = disabilityTypeId;
	}

	public String getRosterStatus() {
		return rosterStatus;
	}

	public void setRosterStatus(String rosterStatus) {
		this.rosterStatus = rosterStatus;
	}

	/**
	 * @return the termRegisteredFor
	 */
	public Term getTermRegisteredFor() {
		return termRegisteredFor;
	}

	/**
	 * @param termRegisteredFor the termRegisteredFor to set
	 */
	public void setTermRegisteredFor(Term termRegisteredFor) {
		this.termRegisteredFor = termRegisteredFor;
	}

	public Boolean getSpecialServiceGroupRequired() {
		return specialServiceGroupRequired;
	}

	public void setSpecialServiceGroupRequired(
			Boolean specialServiceGroupRequired) {
		this.specialServiceGroupRequired = specialServiceGroupRequired;
	}

	public PersonTO getWatcher() {
		return watcher;
	}

	public void setWatcher(PersonTO watcher) {
		this.watcher = watcher;
	}

}