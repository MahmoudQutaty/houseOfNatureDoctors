package com.health.houseOfNature.repositories;

import java.util.List;

import com.health.houseOfNature.models.Doctor;

public interface DoctorRepositoryCustom {
	
    List<Doctor> findDoctorsBySimilarSkills(String skill);

}
