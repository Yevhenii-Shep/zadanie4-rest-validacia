package sk.ukf.vd.service;

import sk.ukf.vd.entity.Employee;

import java.util.List;

public interface EmployeeService {

	List<Employee> findAll();

	Employee findById(Integer id);

	Employee save(Employee employee);

	void deleteById(Integer id);
}
