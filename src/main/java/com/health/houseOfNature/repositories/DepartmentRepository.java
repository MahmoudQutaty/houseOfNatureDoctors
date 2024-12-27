package com.health.houseOfNature.repositories;

import org.hibernate.Hibernate;
import org.springframework.data.jpa.repository.JpaRepository;

import com.health.houseOfNature.models.Department;

import java.util.Optional;

public interface DepartmentRepository extends JpaRepository<Department, Long>{


    Optional<Department> findByName(String name);
}
