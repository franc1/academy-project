package com.logate.academy.unit;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.logate.academy.domains.Employee;
import com.logate.academy.services.EmployeeService;
import com.logate.academy.web.controllers.EmployeeController;
import com.logate.academy.web.dto.EmployeeDTO;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class EmployeeControllerUnitTest {
	
	private static final String BASE_API = "/api/employees";
	
	private MockMvc mockMvc;      // NE anotira se sa @Autowired!

	@Mock
	private EmployeeService employeeService;
	
	@InjectMocks
	private EmployeeController employeeController;
	
	
	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(employeeController)
				.build();
	}
	
	@Test
	public void getShortEmployeeTest() throws Exception
	{
		List<EmployeeDTO> employees = prepareMockData();
		
		// when
		given(employeeService.findEmployeesShort()).willReturn(employees);
		
		// request
		MvcResult result = mockMvc.perform(get(BASE_API + "/short")
				.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk())
			.andReturn();
		
		List<EmployeeDTO> response = new ObjectMapper().readValue(
				result.getResponse().getContentAsString(), new TypeReference<List<EmployeeDTO>>(){}
		);
		
		// then
		assertThat(response.size()).isEqualTo(3);
		assertThat(response.get(0).getFirstName()).isEqualTo("Fran");
		assertThat(response.get(1).getFirstName()).isEqualTo("Danka");
		assertThat(response.get(2).getFirstName()).isEqualTo("Ivana");
	}
	
	private List<EmployeeDTO> prepareMockData()
	{
		EmployeeDTO fran = new EmployeeDTO();
		fran.setId(1);
		fran.setHireDate(new Date());
		fran.setFirstName("Fran");
		
		EmployeeDTO danka = new EmployeeDTO();
		danka.setId(2);
		danka.setHireDate(new Date());
		danka.setFirstName("Danka");
		
		EmployeeDTO ivana = new EmployeeDTO();
		ivana.setId(3);
		ivana.setHireDate(new Date());
		ivana.setFirstName("Ivana");
		
		List<EmployeeDTO> employees = new ArrayList<>();
		employees.add(fran);
		employees.add(danka);
		employees.add(ivana);
		
		return employees;
	}
	
	
	
	// NEW
	@Test
	public void getEmployeeTest() throws Exception
	{
		List<Employee> employees = prepareData();
		
		// when
		given(employeeService.findEmployeesByJobDescriptionJPEL("First Job Desc")).willReturn(employees);
		
		// request
		MvcResult result = mockMvc.perform(get("/api/employees/First Job Desc")
				.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk())
			.andReturn();
		
		List<Employee> response = new ObjectMapper().readValue(
				result.getResponse().getContentAsString(), new TypeReference<List<Employee>>(){}
		);
		
		// then
		assertThat(response.size()).isEqualTo(1);
		assertThat(response.get(0).getJobDescription()).isEqualTo("First Job Desc");
		assertThat(response.get(0).getId()).isEqualTo(1);
	}
	
	private List<Employee> prepareData()
	{
		Employee fran = new Employee();
		fran.setId(1);
		fran.setHireDate(new Date());
		fran.setJobDescription("First Job Desc");
		
		List<Employee> employees = new ArrayList<>();
		employees.add(fran);
		
		return employees;
	}
}
