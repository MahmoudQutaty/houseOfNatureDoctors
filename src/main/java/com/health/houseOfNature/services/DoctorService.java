package com.health.houseOfNature.services;

import java.util.*;
import java.util.stream.Collectors;

import com.health.houseOfNature.models.Licence;
import com.health.houseOfNature.models.Skills;
import com.health.houseOfNature.repositories.LicenceRepository;
import com.health.houseOfNature.repositories.SkillsRepository;
import com.health.houseOfNature.utils.FuzzyMatchingUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.health.houseOfNature.models.Doctor;
import com.health.houseOfNature.repositories.DoctorRepository;

@Service
public class DoctorService {
	
    private final DoctorRepository doctorRepository;
    private final SkillsRepository skillsRepository;
    private final LicenceRepository licenceRepository;

    public Optional<Doctor> findById(Long id) {
        return doctorRepository.findById(id);
    }

    public Doctor save(Doctor doctor) {
        return doctorRepository.save(doctor);
    }


    @Autowired
    public DoctorService(DoctorRepository doctorRepository, SkillsRepository skillsRepository, LicenceRepository licenceRepository) {
        this.doctorRepository = doctorRepository;
        this.skillsRepository = skillsRepository;
        this.licenceRepository = licenceRepository;

    }

    @Transactional
    public Doctor saveDoctorWithSkillsAndLicence(Doctor doctor, Set<Skills> skillNames, Set<Licence> liceneNames) {
        Set<Skills> persistentSkills = new HashSet<>();
        for (Skills skillName : skillNames) {
            // Check if the skill already exists
            Skills skill = skillsRepository.findByName(skillName.getName())
                    .orElseGet(() -> skillsRepository.save(new Skills(skillName.getName()))); // Save if not exists
            persistentSkills.add(skill);
        }

        Set<Licence> persistentLicence = new HashSet<>();
        for (Licence licenceName : liceneNames) {
            // Check if the licence already exists
            Licence licence = licenceRepository.findByName(licenceName.getName())
                    .orElseGet(()-> licenceRepository.save(new Licence(licenceName.getName())));
            persistentLicence.add(licence);
        }

        // Associate the persistent skills with the doctor
        doctor.setSkills(persistentSkills);
        doctor.setLicences(persistentLicence);
        return doctorRepository.save(doctor);// Persist the doctor
    }


    /*public List<Doctor> searchDoctorsBySkills(String skills) {
        List<String> skillList = Arrays.asList(skills.split("\\s+and\\s+"));
        List<Doctor> allDoctors = doctorRepository.findDoctorsBySkills(skillList);

        // Sort doctors by the number of matched skills
        return allDoctors.stream()
                .sorted((d1, d2) -> {
                    long d1Matches = d1.getSkills().stream()
                            .filter(skill -> skillList.contains(skill.getName()))
                            .count();
                    long d2Matches = d2.getSkills().stream()
                            .filter(skill -> skillList.contains(skill.getName()))
                            .count();
                    return Long.compare(d2Matches, d1Matches);
                })
                .collect(Collectors.toList());
    }*/

    /*public List<Doctor> searchDoctorsBySkills(List<String> skills) {
        System.out.println("Skills to query: " + skills); // Debugging step
        return doctorRepository.findDoctorsBySkills(skills);
    }*/

    public List<Doctor> findDoctorsBySkills(List<String> searchSkills) {
        // Fetch all skill names from the database
        List<String> allSkills = skillsRepository.findAllNames();  // Fetch just the names of all skills

        List<Doctor> matchingDoctors = new ArrayList<>();

        // Iterate through all doctors and check how many skills match the search skills
        for (Doctor doctor : doctorRepository.findAll()) {
            int matchingSkillsCount = 0;

            // Iterate through the skills of each doctor
            for (Skills doctorSkill : doctor.getSkills()) {
                for (String searchSkill : searchSkills) {
                    // Use fuzzy matching utility to get the closest skill
                    String closestMatch = FuzzyMatchingUtil.findImprovedClosestSkill(searchSkill, allSkills);

                    // Check if the closest match is valid (i.e., the input matches a skill within the threshold)
                    if (closestMatch != null && closestMatch.equalsIgnoreCase(doctorSkill.getName())) {
                        matchingSkillsCount++;
                    }
                }
            }

            // If the doctor has matching skills, add them to the result list
            if (matchingSkillsCount > 0) {
                matchingDoctors.add(doctor);
            }
        }

        return matchingDoctors;
    }
}
