package uk.ac.gla.seit.ae2.service;

import uk.ac.gla.seit.ae2.domain.Teacher;
import uk.ac.gla.seit.ae2.domain.TeacherStatus;
import uk.ac.gla.seit.ae2.domain.TeachingRequirement;
import uk.ac.gla.seit.ae2.repository.RequirementRepository;
import uk.ac.gla.seit.ae2.repository.TeacherRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class StaffingService {

    private final TeacherRepository teacherRepository;
    private final RequirementRepository requirementRepository;

    public StaffingService(TeacherRepository teacherRepository,
                           RequirementRepository requirementRepository) {
        this.teacherRepository = Objects.requireNonNull(teacherRepository);
        this.requirementRepository = Objects.requireNonNull(requirementRepository);
    }

    /**
     * US3: Search teachers by subject AND qualification, available only.
     */
    public List<Teacher> searchAvailableTeachers(String subject, String qualification) {
        requireText(subject, "subject");
        requireText(qualification, "qualification");

        List<Teacher> result = new ArrayList<>();

        for (Teacher teacher : teacherRepository.findAll()) {
            if (teacher.isAvailable()
                    && teacher.matchesSubject(subject.trim())
                    && teacher.hasQualification(qualification.trim())) {
                result.add(teacher);
            }
        }

        return result;
    }

    /**
     * US4: Assign teacher to requirement.
     */
    public boolean assignTeacher(String requirementId, String teacherId) {
        requireText(requirementId, "requirementId");
        requireText(teacherId, "teacherId");

        TeachingRequirement req = requirementRepository.findById(requirementId.trim());
        if (req == null) {
            throw new IllegalArgumentException("Requirement not found: " + requirementId);
        }

        Teacher teacher = teacherRepository.findById(teacherId.trim());
        if (teacher == null) {
            throw new IllegalArgumentException("Teacher not found: " + teacherId);
        }

        if (!teacher.isAvailable()) {
            return false;
        }

        if (!teacher.matchesSubject(req.getSubject())) {
            return false;
        }

        if (!teacher.hasQualification(req.getRequiredQualification())) {
            return false;
        }

        req.markAssigned(teacherId.trim());
        if (!requirementRepository.update(req)) {
            throw new IllegalStateException("Failed to update requirement");
        }

        teacher.setStatus(TeacherStatus.ASSIGNED);
        if (!teacherRepository.update(teacher)) {
            throw new IllegalStateException("Failed to update teacher");
        }

        return true;
    }

    private static void requireText(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Missing mandatory field: " + fieldName);
        }
    }
}