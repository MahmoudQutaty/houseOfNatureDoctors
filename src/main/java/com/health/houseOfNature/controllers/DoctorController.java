package com.health.houseOfNature.controllers;

import java.util.*;
import java.util.stream.Collectors;

import com.health.houseOfNature.services.WebSocketNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.health.houseOfNature.models.Department;
import com.health.houseOfNature.models.Doctor;
import com.health.houseOfNature.models.Licence;
import com.health.houseOfNature.models.Skills;
import com.health.houseOfNature.repositories.DepartmentRepository;
import com.health.houseOfNature.repositories.DoctorRepository;
import com.health.houseOfNature.repositories.LicenceRepository;
import com.health.houseOfNature.repositories.SkillsRepository;
import com.health.houseOfNature.services.DoctorService;

@RestController
@RequestMapping("/doctor")
public class DoctorController {

	@Autowired
	private SkillsController skillsController;

	@Autowired
	private DoctorRepository doctorRepository;
	
	@Autowired 
	private SkillsRepository skillsRepository;
	
	@Autowired
	private LicenceRepository licenceRepository;
	
	@Autowired
	private DoctorService doctorService;
	@Autowired
	private DepartmentRepository departmentRepository;

	@Autowired
	private WebSocketNotificationService notificationService;


	@PostMapping("/{departmentId}")
	public ResponseEntity<?> createDoctor(@RequestBody Doctor doctorRequest, @PathVariable Long departmentId) {
		try {
			Doctor doctor = new Doctor();
			doctor.setName(doctorRequest.getName());
			doctor.setEmail(doctorRequest.getEmail());
			doctor.setInfo(doctorRequest.getInfo());
			doctor.setAddress(doctorRequest.getAddress());
			doctor.setPhone(doctorRequest.getPhone());
			departmentRepository.findById(departmentId).ifPresent(doctor::setDepartment);

			doctorService.saveDoctorWithSkillsAndLicence(doctor, doctorRequest.getSkills(), doctorRequest.getLicences());

			notificationService.broadcastUpdate(doctor); // Optional WebSocket notification
			return ResponseEntity.status(HttpStatus.CREATED).body(doctor);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
		}
	}


	@GetMapping("/search")
	public ResponseEntity<?> searchDoctors(@RequestParam("skills") String skills) {
		try {
			// Normalize input: split by comma, 'and', or dash
			List<String> normalizedSkills = Arrays.stream(skills.split("\\s*(,|and|-)\\s*"))
					.map(String::trim)
					.filter(s -> !s.isEmpty())
					.collect(Collectors.toList());

			System.out.println("Normalized Skills: " + normalizedSkills); // Debug

			List<Doctor> doctors = doctorService.findDoctorsBySkills(normalizedSkills);
			return ResponseEntity.ok(doctors);

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(500).body("Error: " + e.getMessage());
		}
	}

	@GetMapping("")
	public ResponseEntity<?> getAllDoctors(){
		List<Doctor> doctors = doctorRepository.findAll();
		return ResponseEntity.ok(doctors);
	}
	
	/*@GetMapping("/{id}")
	public ResponseEntity<?> getOneDoctor(@PathVariable Long id){
		Doctor doctor = doctorRepository.findById(id).orElse(null);
		if(doctor != null) {
			return ResponseEntity.ok(doctor);
		}else {
			return ResponseEntity.notFound().build();
		}
	}*/

	@GetMapping("/{id}")
	public ResponseEntity<Doctor> getDoctor(@PathVariable Long id) {
		Doctor doctor = doctorRepository.findById(id).orElse(null);
		if (doctor == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(doctor);
	}
	
    @PutMapping("/{departmentId}/{doctorId}")
    public void updateDoctor(@PathVariable Long departmentId, @PathVariable Long doctorId, @RequestBody Doctor updatedDoctor) {
        // Find the existing doctor by ID
        Optional<Doctor> existingDoctorOptional = doctorService.findById(doctorId);


        Doctor existingDoctor = existingDoctorOptional.get();

        // Update fields
        existingDoctor.setName(updatedDoctor.getName());
        existingDoctor.setInfo(updatedDoctor.getInfo());
        existingDoctor.setAddress(updatedDoctor.getAddress());
        existingDoctor.setPhone(updatedDoctor.getPhone());
		existingDoctor.setEmail(updatedDoctor.getEmail());
		departmentRepository.findById(departmentId).ifPresent(existingDoctor::setDepartment);
		doctorService.saveDoctorWithSkillsAndLicence(existingDoctor, updatedDoctor.getSkills(),updatedDoctor.getLicences());
		notificationService.broadcastUpdate(existingDoctor);

	}
	 @PutMapping("/{doctorId}/skills")
	    public ResponseEntity<Doctor> updateDoctorSkills(
	        @PathVariable Long doctorId,
	        @RequestBody Set<Long> skillIds) {
	        
	        // Find the doctor by ID
	        Optional<Doctor> optionalDoctor = doctorRepository.findById(doctorId);
	        if (!optionalDoctor.isPresent()) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	        }

	        Doctor doctor = optionalDoctor.get();

	        // Retrieve the skills by their IDs
	        Set<Skills> skills = new HashSet<>(skillsRepository.findAllById(skillIds));

	        // Update the doctor's skills
	        doctor.setSkills(skills);
	        Doctor updatedDoctor = doctorRepository.save(doctor);

	        return ResponseEntity.ok(updatedDoctor);
	    }
	 
	 @PutMapping("/{doctorId}/licences")
	    public ResponseEntity<Doctor> updateDoctorLicences(
	        @PathVariable Long doctorId,
	        @RequestBody Set<Long> licenceIds) {
	        
	        // Find the doctor by ID
	        Optional<Doctor> optionalDoctor = doctorRepository.findById(doctorId);
	        if (!optionalDoctor.isPresent()) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	        }

	        Doctor doctor = optionalDoctor.get();

	        // Retrieve the licenses by their IDs
	        Set<Licence> licenses = new HashSet<>(licenceRepository.findAllById(licenceIds));

	        // Update the doctor's licenses
	        doctor.setLicences(licenses);
	        Doctor updatedDoctor = doctorRepository.save(doctor);

	        return ResponseEntity.ok(updatedDoctor);
	    }

	@DeleteMapping("{id}")
	public ResponseEntity<?> deleteDoctor(@PathVariable Long id) {
		Optional<Doctor> doctorOptional = doctorRepository.findById(id);
		if (!doctorOptional.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Doctor not found");
		}

		try {
			doctorRepository.deleteById(id);

			// Broadcast deletion event
			notificationService.broadcastDeletion(id);

			return ResponseEntity.ok("Doctor deleted successfully");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting doctor: " + e.getMessage());
		}
	}


}
