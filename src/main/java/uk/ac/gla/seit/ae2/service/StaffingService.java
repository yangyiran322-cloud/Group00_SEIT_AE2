package uk.ac.gla.seit.ae2.service;

import uk.ac.gla.seit.ae2.repository.RequirementRepository;
import uk.ac.gla.seit.ae2.repository.TeacherRepository;

public class StaffingService {
    private final RequirementRepository requirementRepository;
    private final TeacherRepository teacherRepository;

    public StaffingService(RequirementRepository requirementRepository, TeacherRepository teacherRepository) {
        this.requirementRepository = requirementRepository;
        this.teacherRepository = teacherRepository;
    }
}