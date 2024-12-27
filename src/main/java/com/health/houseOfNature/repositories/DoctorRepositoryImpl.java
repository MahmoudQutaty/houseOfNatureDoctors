package com.health.houseOfNature.repositories;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.health.houseOfNature.models.Doctor;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Repository
public class DoctorRepositoryImpl implements DoctorRepositoryCustom{
	
	@PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public List<Doctor> findDoctorsBySimilarSkills(String skill) {
        String query = "SELECT DISTINCT d FROM Doctor d JOIN d.skills s WHERE LOWER(s.name) LIKE LOWER(CONCAT('%', :skill, '%'))";
        return entityManager.createQuery(query, Doctor.class)
                            .setParameter("skill", skill)
                            .getResultList();
    }

	

}
