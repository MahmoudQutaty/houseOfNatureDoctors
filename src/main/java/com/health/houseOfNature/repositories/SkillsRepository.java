package com.health.houseOfNature.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.health.houseOfNature.models.Skills;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SkillsRepository extends JpaRepository<Skills, Long>{
    boolean existsByName(String name);
    Optional<Skills> findByName(String name);

    @Query("SELECT s.name FROM Skills s WHERE s.name IN :skills")
    List<String> findSkillNamesByNames(@Param("skills") List<String> skills);

    List<Skills> findAll();

    @Query("SELECT s.name FROM Skills s")  // Custom query to fetch all skill names
    List<String> findAllNames();  // Returns a list of skill names


    // You could add more methods if needed, for example to find by name

}
