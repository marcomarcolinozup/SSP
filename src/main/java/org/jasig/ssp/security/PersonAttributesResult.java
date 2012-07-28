package org.jasig.ssp.security;

public class PersonAttributesResult {

	public String schoolId, firstName, lastName, primaryEmailAddress, phone;

	public PersonAttributesResult() {
		super();
	}

	public PersonAttributesResult(final String schoolId,
			final String firstName, final String lastName,
			final String primaryEmailAddress, final String phone) {
		super();
		this.schoolId = schoolId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.primaryEmailAddress = primaryEmailAddress;
		this.phone = phone;
	}

	public String getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(final String schoolId) {
		this.schoolId = schoolId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(final String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(final String lastName) {
		this.lastName = lastName;
	}

	public String getPrimaryEmailAddress() {
		return primaryEmailAddress;
	}

	public void setPrimaryEmailAddress(final String primaryEmailAddress) {
		this.primaryEmailAddress = primaryEmailAddress;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(final String phone) {
		this.phone = phone;
	}

}
