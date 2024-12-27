package com.health.houseOfNature.controllers;

import java.util.List;

import com.health.houseOfNature.repositories.SkillsRepository;
import com.health.houseOfNature.services.LicenceService;
import com.health.houseOfNature.services.SkillsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.health.houseOfNature.models.Licence;
import com.health.houseOfNature.repositories.LicenceRepository;

@RestController
@RequestMapping("/licence")
public class LicenceController {

	@Autowired
	private LicenceRepository licenceRepository;
	private final LicenceService licenceService;

	@Autowired
	public LicenceController(LicenceService licenceService) {
		this.licenceService = licenceService;
	}



	@PostMapping("")
	public ResponseEntity<?> addLicence(String licenceName){
		licenceService.addLicenceIfNotExists(licenceName);
		return ResponseEntity.ok("License added successfully (if it didn't already exist).");
	}
	
	@GetMapping("")
	public ResponseEntity<?> getAllLicences(){
		List<Licence> Licences = licenceRepository.findAll();
		return ResponseEntity.ok(Licences);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> getOneDoctor(@PathVariable Long id){
		Licence Licence = licenceRepository.findById(id).orElse(null);
		if(Licence != null) {
			return ResponseEntity.ok(Licence);
		}else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@DeleteMapping("{id}")
	public ResponseEntity<?> deleteLicence(@PathVariable Long id){
		Licence licence = licenceRepository.findById(id).orElse(null);
		if(licence != null)
		{
			licenceRepository.deleteById(id);
			return ResponseEntity.ok("Deleted Successfully");
		}else {
			return ResponseEntity.notFound().build();
		}
		
	}

}
