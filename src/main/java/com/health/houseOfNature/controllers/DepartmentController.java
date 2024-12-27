package com.health.houseOfNature.controllers;

import java.util.List;

import com.health.houseOfNature.models.Skills;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.health.houseOfNature.models.Department;
import com.health.houseOfNature.repositories.DepartmentRepository;

@RestController
@RequestMapping("/department")
public class DepartmentController {

	@Autowired
	private DepartmentRepository departmentRepository;
	
	@PostMapping("")
	public ResponseEntity<?> addDepartment(@RequestBody Department department){
		Department newDepartment = departmentRepository.save(department);
		return ResponseEntity.ok(newDepartment);
	}
	
	@GetMapping("")
	public ResponseEntity<?> getAllDepartments(){
		List<Department> departments = departmentRepository.findAll();
		return ResponseEntity.ok(departments);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> getOneDepartment(@PathVariable Long id){
		Department department = departmentRepository.findById(id).orElse(null);
		if(department != null) {
			return ResponseEntity.ok(department);
		}else {
			return ResponseEntity.notFound().build();
		}
	}

	@GetMapping("/id-by-name")
	public ResponseEntity<Long> getDepartmentIdByName(@RequestParam String name) {
		return departmentRepository.findByName(name)
				.map(department -> ResponseEntity.ok(department.getId()))
				.orElse(ResponseEntity.notFound().build());
	}

	@GetMapping("/numOfDepartments")
	public int totalNumberOfDepartments(){
		List<Department> departments = departmentRepository.findAll();
		return departments.size();
	}
	
	@PutMapping("{id}")
	public ResponseEntity<?> updateDepartment(@RequestBody Department department, @PathVariable Long id){
		Department oldDepartment = departmentRepository.findById(id).orElse(null);
		
		if(oldDepartment != null) {
			Department updatedDepartment = department;
			updatedDepartment.setDoctors(oldDepartment.getDoctors());
			departmentRepository.save(updatedDepartment);
			return ResponseEntity.ok(updatedDepartment);
		}else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@DeleteMapping("{id}")
	public ResponseEntity<?> deleteDepartment(@PathVariable Long id){
		Department department = departmentRepository.findById(id).orElse(null);
		if(department != null)
		{
			departmentRepository.deleteById(id);
			return ResponseEntity.ok("Deleted Successfully");
		}else {
			return ResponseEntity.notFound().build();
		}
		
	}

}
