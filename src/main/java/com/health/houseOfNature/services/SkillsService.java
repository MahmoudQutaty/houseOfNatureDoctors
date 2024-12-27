package com.health.houseOfNature.services;

import com.health.houseOfNature.models.Skills;
import com.health.houseOfNature.repositories.SkillsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SkillsService {

    private final SkillsRepository skillsRepository;

    @Autowired
    public SkillsService(SkillsRepository skillsRepository) {
        this.skillsRepository = skillsRepository;
    }

    @Transactional
    public void addSkillIfNotExists(String skillName) {
        if (!skillsRepository.existsByName(skillName)) {
            Skills skill = new Skills(skillName);
            skillsRepository.save(skill);
        }
    }
}
