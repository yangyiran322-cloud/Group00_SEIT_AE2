package uk.ac.gla.seit.ae2.service;

import uk.ac.gla.seit.ae2.domain.RequirementStatus;
import uk.ac.gla.seit.ae2.domain.TeachingRequirement;
import uk.ac.gla.seit.ae2.repository.RequirementRepository;

import java.util.List;
import java.util.Objects;

public class RequirementService {

    private final RequirementRepository requirementRepository;

    public RequirementService(RequirementRepository requirementRepository) {
        this.requirementRepository = Objects.requireNonNull(requirementRepository);
    }

    /**
     * US1: Submit teaching requirement.
     */
    public String submitRequirement(String moduleCode,
                                    String subject,
                                    String term,
                                    String requiredQualification,
                                    int hours) {
        requireText(moduleCode, "moduleCode");
        requireText(subject, "subject");
        requireText(term, "term");
        requireText(requiredQualification, "requiredQualification");

        if (hours <= 0) {
            throw new IllegalArgumentException("hours must be > 0");
        }

        String requirementId = generateNextRequirementId();

        TeachingRequirement req = new TeachingRequirement(
                requirementId,
                moduleCode.trim(),
                subject.trim(),
                term.trim(),
                requiredQualification.trim(),
                hours
        );

        return requirementRepository.add(req);
    }

    /**
     * US2: View all requirements.
     */
    public List<TeachingRequirement> listAllRequirements() {
        return requirementRepository.findAll();
    }

    /**
     * Helper: list pending requirements.
     */
    public List<TeachingRequirement> listPendingRequirements() {
        return requirementRepository.findByStatus(RequirementStatus.PENDING_ASSIGNMENT);
    }

    private String generateNextRequirementId() {
        List<TeachingRequirement> all = requirementRepository.findAll();
        int max = 0;

        for (TeachingRequirement req : all) {
            String id = req.getRequirementId();
            if (id != null && id.startsWith("R")) {
                try {
                    int num = Integer.parseInt(id.substring(1));
                    if (num > max) {
                        max = num;
                    }
                } catch (NumberFormatException ignored) {
                }
            }
        }

        return String.format("R%03d", max + 1);
    }

    private static void requireText(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Missing mandatory field: " + fieldName);
        }
    }
}