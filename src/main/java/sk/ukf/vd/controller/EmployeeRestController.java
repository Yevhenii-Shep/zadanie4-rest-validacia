package sk.ukf.vd.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sk.ukf.vd.entity.Employee;
import sk.ukf.vd.response.ResponseFactory;
import sk.ukf.vd.response.ResponseWrapper;
import sk.ukf.vd.service.EmployeeService;

import java.util.List;

@RestController
@RequestMapping("/api")
public class EmployeeRestController {

	private final EmployeeService employeeService;
	private final ResponseFactory response;

	public EmployeeRestController(EmployeeService employeeService, ResponseFactory response) {
		this.employeeService = employeeService;
		this.response = response;
	}

	@GetMapping("/employees")
	public ResponseEntity<ResponseWrapper<List<Employee>>> getAllEmployees() {
		return response.ok("Zamestnanci úspešne načítaní", employeeService.findAll());
	}

	@GetMapping("/employees/{id}")
	public ResponseEntity<ResponseWrapper<Employee>> getEmployeeById(@PathVariable Integer id) {
		return response.ok("Zamestnanec úspešne načítaný", employeeService.findById(id));
	}

	@PostMapping("/employees")
	public ResponseEntity<ResponseWrapper<Employee>> createEmployee(@Valid @RequestBody Employee employee) {
		employee.setId(null);
		Employee createdEmployee = employeeService.save(employee);
		return response.created("Zamestnanec úspešne vytvorený", createdEmployee);
	}

	@PutMapping("/employees/{id}")
	public ResponseEntity<ResponseWrapper<Employee>> updateEmployee(
			@PathVariable Integer id, @Valid @RequestBody Employee employee) {

		employeeService.findById(id);

		employee.setId(id);
		Employee updatedEmployee = employeeService.save(employee);
		return response.ok("Zamestnanec úspešne aktualizovaný", updatedEmployee);
	}

	@DeleteMapping("/employees/{id}")
	public ResponseEntity<ResponseWrapper<Void>> deleteEmployee(@PathVariable Integer id) {
		employeeService.deleteById(id);
		return response.ok("Zamestnanec úspešne zmazaný");
	}
}
