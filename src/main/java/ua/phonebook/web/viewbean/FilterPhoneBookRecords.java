package ua.phonebook.web.viewbean;

import javax.validation.constraints.NotNull;

public class FilterPhoneBookRecords {
	
	@NotNull
	private String firstName;
	
	@NotNull
	private String lastName;
	
	@NotNull
	private String mobilePhone;

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}
	
	
	@Override
	public String toString(){
		return "firstName="+firstName+", lastName="+lastName
				+", mobilePhone="+mobilePhone;
	}
	
}
