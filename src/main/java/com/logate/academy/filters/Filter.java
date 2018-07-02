package com.logate.academy.filters;

import java.io.Serializable;

public class Filter implements Serializable {

private static final long serialVersionUID = 1L;
	
	private String firstName;
	private String lastName;
	private String jobDesc;
	
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
	
	public String getJobDesc() {
		return jobDesc;
	}
	
	public void setJobDesc(String jobDesc) {
		this.jobDesc = jobDesc;
	}
	
	@Override
	public String toString() {
		return "Filter [firstName=" + firstName + ", lastName=" + lastName + ", jobDesc=" + jobDesc + "]";
	}
}
