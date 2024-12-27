package com.health.houseOfNature.repositories;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;

import com.health.houseOfNature.models.Doctor;

public interface DoctorRepository extends JpaRepository<Doctor, Long>, DoctorRepositoryCustom {

    Doctor getByEmail(String name);

    //@Query("SELECT DISTINCT d FROM Doctor d JOIN d.skills s WHERE s.name IN (:skills)")
    //List<Doctor> findDoctorsBySkills(@Param("skills") List<String> skills);

    List<Doctor> findBySkills_NameIn(List<String> skills);

}