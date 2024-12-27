package com.health.houseOfNature.services;

import com.health.houseOfNature.models.Licence;
import com.health.houseOfNature.models.Skills;
import com.health.houseOfNature.repositories.LicenceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LicenceService {
    private final LicenceRepository licenceRepository;

    @Autowired
    public LicenceService(LicenceRepository licenceRepository) {
        this.licenceRepository = licenceRepository;
    }

    @Transactional
    public void addLicenceIfNotExists(String licenceName) {
        if (!licenceRepository.existsByName(licenceName)) {
            Licence licence = new Licence(licenceName);
            licenceRepository.save(licence);
        }
    }

}
