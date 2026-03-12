package uk.ac.gla.seit.ae2.repository;

import uk.ac.gla.seit.ae2.domain.RequirementStatus;
import uk.ac.gla.seit.ae2.domain.TeachingRequirement;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class CsvRequirementRepository implements RequirementRepository {

    private static final String HEADER =
            "requirementId,moduleCode,subject,term,requiredQualification,hours,status,assignedTeacherId";

    private final Path filePath;
    private final List<TeachingRequirement> requirements = new ArrayList<>();

    public CsvRequirementRepository(String fileName) {
        this.filePath = Paths.get(fileName);
    }

    @Override
    public String add(TeachingRequirement requirement) {
        if (requirement == null) {
            return null;
        }

        if (findById(requirement.getRequirementId()) != null) {
            return null;
        }

        requirements.add(requirement);
        return requirement.getRequirementId();
    }

    @Override
    public TeachingRequirement findById(String id) {
        for (TeachingRequirement requirement : requirements) {
            if (requirement.getRequirementId().equals(id)) {
                return requirement;
            }
        }
        return null;
    }

    @Override
    public List<TeachingRequirement> findAll() {
        return new ArrayList<>(requirements);
    }

    @Override
    public List<TeachingRequirement> findByStatus(RequirementStatus status) {
        List<TeachingRequirement> result = new ArrayList<>();
        for (TeachingRequirement requirement : requirements) {
            if (requirement.getStatus() == status) {
                result.add(requirement);
            }
        }
        return result;
    }

    @Override
    public boolean update(TeachingRequirement updatedRequirement) {
        if (updatedRequirement == null) {
            return false;
        }

        for (int i = 0; i < requirements.size(); i++) {
            TeachingRequirement existing = requirements.get(i);
            if (existing.getRequirementId().equals(updatedRequirement.getRequirementId())) {
                requirements.set(i, updatedRequirement);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean saveAll() {
        List<String> bodyLines = new ArrayList<>();
        for (TeachingRequirement requirement : requirements) {
            bodyLines.add(toCsvLine(requirement));
        }

        List<String> lines = CsvUtil.withHeader(HEADER, bodyLines);

        try {
            CsvUtil.writeAllLinesAtomically(filePath, lines);
            return true;
        } catch (RuntimeException e) {
            return false;
        }
    }

    @Override
    public boolean loadAll() {
        requirements.clear();

        try {
            List<String> lines = CsvUtil.readAllLines(filePath);
            if (lines.isEmpty()) {
                return true;
            }

            List<String> bodyLines = CsvUtil.removeHeader(lines);
            for (String line : bodyLines) {
                if (line == null || line.isBlank()) {
                    continue;
                }
                requirements.add(fromCsvLine(line));
            }
            return true;
        } catch (RuntimeException e) {
            requirements.clear();
            return false;
        }
    }

    private String toCsvLine(TeachingRequirement requirement) {
        String assignedTeacherId = requirement.getAssignedTeacherId() == null
                ? ""
                : requirement.getAssignedTeacherId();

        return String.join(",",
                requirement.getRequirementId(),
                requirement.getModuleCode(),
                requirement.getSubject(),
                requirement.getTerm(),
                requirement.getRequiredQualification(),
                String.valueOf(requirement.getHours()),
                requirement.getStatus().name(),
                assignedTeacherId
        );
    }

    private TeachingRequirement fromCsvLine(String line) {
        String[] parts = line.split(",", -1);
        if (parts.length < 7 ) {
            throw new IllegalArgumentException("Invalid requirement CSV line: " + line);
        }

        String requirementId = parts[0].trim();
        String moduleCode = parts[1].trim();
        String subject = parts[2].trim();
        String term = parts[3].trim();
        String requiredQualification = parts[4].trim();
        int hours = Integer.parseInt(parts[5].trim());
        RequirementStatus status = RequirementStatus.valueOf(parts[6].trim());
        String assignedTeacherId = parts[7].trim();

        TeachingRequirement requirement = new TeachingRequirement(
                requirementId,
                moduleCode,
                subject,
                term,
                requiredQualification,
                hours
        );

        if (status == RequirementStatus.ASSIGNED && !assignedTeacherId.isEmpty()) {
            requirement.markAssigned(assignedTeacherId);
        }

        return requirement;
    }
}