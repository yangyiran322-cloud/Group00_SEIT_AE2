package uk.ac.gla.seit.ae2.service;

import uk.ac.gla.seit.ae2.repository.RequirementRepository;

public class RequirementService {
    private final RequirementRepository requirementRepository;

    public RequirementService(RequirementRepository requirementRepository) {
        this.requirementRepository = requirementRepository;
    }
}