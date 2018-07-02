package com.logate.academy.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Description;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.logate.academy.domains.Employee;
import com.logate.academy.repository.EmployeeRepository;
import com.logate.academy.web.dto.EmployeeDTO;
import com.logate.academy.web.specifications.EmployeeSpecification;


@Description(value = "Employee Service")
@Service
public class EmployeeService {
	
	@Autowired
	private EmployeeRepository employeeRepository;
	
	@PersistenceContext
	private EntityManager entityManager;     //da bismo pozvali neke upite preko entityManager-a (nativne upite)

	public Page<Employee> findByPageable(Pageable pageable) {
		return employeeRepository.findAll(pageable);
	}

	public List<EmployeeDTO> findEmployeesShort() {
		return employeeRepository.findShort();
	}

	public EmployeeDTO findEmployeeDTO(Integer id, String firstName) {
		return employeeRepository.findOnlyDTO(id, firstName);
	}
	
	public List<Employee> findEmployeesByJPEL(Integer id, String jobDescription) {
		return employeeRepository.findAllByIdAndJobDescription(id, jobDescription);
	}
	
	public List<Employee> findEmployeesByJobDescriptionJPEL(String jobDescription) {
		return employeeRepository.findByJobDescription(jobDescription);
	}

	public List<Employee> findAllBySpec(EmployeeSpecification employeeSpec) {
		return employeeRepository.findAll(employeeSpec);
	}
	
	
	
	/**
	 * Method for getting Employee DTO objects with Entity Manager (native query)
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<EmployeeDTO> findDTO() {
		return entityManager.createNativeQuery("SELECT e.id as id, u.first_name as firstName, "
				+ "e.hire_date as hireDate "
				+ "from employees as e "
				+ "left join users as u on e.user_id = u.id")
			.unwrap(org.hibernate.query.Query.class)
			.setResultTransformer(Transformers.aliasToBean(EmployeeDTO.class)) //transformise objekat u EmployeeDTO
			.getResultList();                                                 //...uzeci u obzir aliase
	}
	
	
	/**
	 * Method for getting data by native query.
	 * @return List of EmployeeDTO objects.
	 */
	public List<EmployeeDTO> findByNativeQuery() {
		List<Object[]> objectsList = employeeRepository.findByNative();
		List<EmployeeDTO> employees = new ArrayList<>();
		
		// processing data...
		objectsList
			.stream()
			.forEach(employee -> {
				EmployeeDTO employeeDTO = new EmployeeDTO(
					(Integer) employee[0],
					(String) employee[1],
					(Date) employee[2]
				);
				employees.add(employeeDTO);
			});
		
		return employees;
	}
	
	/**
	 * Method for getting employee identifiers by native queries.
	 * @return List of identifiers (Integers)
	 */
	public List<Integer> findNativeIdentifiers() {
		return employeeRepository.findOnlyIds();
	}
	
	public Employee store(Employee employee) {
		return employeeRepository.save(employee);
	}
}
