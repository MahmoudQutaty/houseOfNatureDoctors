package com.health.houseOfNature.controllers;

import java.util.List;
import java.util.Set;

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

import com.health.houseOfNature.models.Skills;
import com.health.houseOfNature.repositories.SkillsRepository;

@RestController
@RequestMapping("/skills")
public class SkillsController {
	
	@Autowired
	private SkillsRepository skillsRepository;
	private final SkillsService skillsService;

	@Autowired
	public SkillsController(SkillsService skillsService) {
		this.skillsService = skillsService;
	}


	@PostMapping("")
	public String addSkill(String skillName) {
		skillsService.addSkillIfNotExists(skillName);
		return "Skill added successfully (if it didn't already exist).";
	}
	
	@GetMapping("")
	public ResponseEntity<?> getAllSkillss(){
		List<Skills> Skills = skillsRepository.findAll();
		return ResponseEntity.ok(Skills);
	}

	@GetMapping("/numOfSkills")
	public int totalNumberOfSkills(){
		List<Skills> skills = skillsRepository.findAll();
		return skills.size();
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> getOneDoctor(@PathVariable Long id){
		Skills Skills = skillsRepository.findById(id).orElse(null);
		if(Skills != null) {
			return ResponseEntity.ok(Skills);
		}else {
			return ResponseEntity.notFound().build();
		}
	}
	
	
	@DeleteMapping("{id}")
	public ResponseEntity<?> deleteSkills(@PathVariable Long id){
		Skills Skills = skillsRepository.findById(id).orElse(null);
		if(Skills != null)
		{
			skillsRepository.deleteById(id);
			return ResponseEntity.ok("Deleted Successfully");
		}else {
			return ResponseEntity.notFound().build();
		}
		
	}


}
