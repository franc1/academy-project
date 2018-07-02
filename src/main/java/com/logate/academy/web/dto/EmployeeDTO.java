package com.logate.academy.web.dto;

import java.io.Serializable;
import java.util.Date;

public class EmployeeDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private String firstName;
	private Date hireDate;
	
	public EmployeeDTO() {}
	
	public EmployeeDTO(Integer id, String firstName, Date hireDate) {
		this.id = id;
		this.firstName = firstName;
		this.hireDate = hireDate;
	}

	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public Date getHireDate() {
		return hireDate;
	}
	
	public void setHireDate(Date hireDate) {
		this.hireDate = hireDate;
	}

	@Override
	public String toString() {
		return "EmployeeDTO [id=" + id + ", firstName=" + firstName + ", hireDate=" + hireDate + "]";
	}
	
}
