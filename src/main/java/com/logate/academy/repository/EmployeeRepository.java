package com.logate.academy.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.logate.academy.domains.Employee;
import com.logate.academy.web.dto.EmployeeDTO;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer>,
JpaSpecificationExecutor<Employee>{
	
	// dva primjera sa @Query-ima...
	@Query(value = "select new com.logate.academy.web.dto.EmployeeDTO(e.id, u.firstName, e.hireDate) "
			+ "from Employee e "
			+ "left join e.user u")
	List<EmployeeDTO> findShort();
	
	@Query(value = "select new com.logate.academy.web.dto.EmployeeDTO(e.id, u.firstName, e.hireDate)"
			+ " from Employee e"
			+ " left join e.user u "
			+ " where e.id = :id and u.firstName = :firstName")
	EmployeeDTO findOnlyDTO(@Param("id") Integer id, @Param("firstName") String firstName);

	//	@Query(value = "select e from Employee e")
	//	List<Employee> findByQuery();
	
	
	// Primjer gotovih JP Expression Language metoda...
	//	List<Employee> findAllByHireDateAndJobDescription(Date hireDate, String jobDescription);
	
	List<Employee> findAllByIdAndJobDescription(Integer id, String jobDescription);
	
	List<Employee> findByJobDescription(String jobDescription);
	//or:  List<Employee> findAllByJobDescription(String jobDescription);
	
	
	// some native queries...
	@Query(value = "select e.id, u.first_name, e.hire_date "
			+ "from employees e "
			+ "left join users as u on e.user_id = u.id", nativeQuery = true)
	List<Object[]> findByNative();
	
	
	@Query(value = "select id from employees", nativeQuery = true)
	List<Integer> findOnlyIds();
	
	
	
	
	
	
	
	
}
