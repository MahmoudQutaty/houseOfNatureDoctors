package com.health.houseOfNature.repositories;

import com.health.houseOfNature.models.Skills;
import org.springframework.data.jpa.repository.JpaRepository;

import com.health.houseOfNature.models.Licence;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LicenceRepository extends JpaRepository<Licence, Long>{

    boolean existsByName(String name);
    Optional<Licence> findByName(String name);

}
