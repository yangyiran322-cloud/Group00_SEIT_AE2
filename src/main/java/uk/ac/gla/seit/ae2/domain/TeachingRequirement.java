package uk.ac.gla.seit.ae2.domain;

/**
 * Represents a teaching requirement in the system.
 */
public class TeachingRequirement {

    private String requirementId;
    private String moduleCode;
    private String subject;
    private String term;
    private String requiredQualification;
    private int hours;
    private RequirementStatus status;
    private String assignedTeacherId;

    public TeachingRequirement(String requirementId,
                               String moduleCode,
                               String subject,
                               String term,
                               String requiredQualification,
                               int hours) {

        this.requirementId = requirementId;
        this.moduleCode = moduleCode;
        this.subject = subject;
        this.term = term;
        this.requiredQualification = requiredQualification;
        this.hours = hours;
        this.status = RequirementStatus.PENDING_ASSIGNMENT;
    }

    public String getRequirementId() {
        return requirementId;
    }

    public String getModuleCode() {
        return moduleCode;
    }

    public String getSubject() {
        return subject;
    }

    public String getTerm() {
        return term;
    }

    public String getRequiredQualification() {
        return requiredQualification;
    }

    public int getHours() {
        return hours;
    }

    public RequirementStatus getStatus() {
        return status;
    }

    public String getAssignedTeacherId() {
        return assignedTeacherId;
    }

    /**
     * Marks the requirement as assigned to a teacher.
     */
    public void markAssigned(String teacherId) {
        this.assignedTeacherId = teacherId;
        this.status = RequirementStatus.ASSIGNED;
    }

    /**
     * Returns true if requirement still waiting assignment.
     */
    public boolean isPending() {
        return status == RequirementStatus.PENDING_ASSIGNMENT;
    }

    @Override
    public String toString() {
        return "TeachingRequirement{" +
                "requirementId='" + requirementId + '\'' +
                ", moduleCode='" + moduleCode + '\'' +
                ", subject='" + subject + '\'' +
                ", term='" + term + '\'' +
                ", requiredQualification='" + requiredQualification + '\'' +
                ", hours=" + hours +
                ", status=" + status +
                ", assignedTeacherId='" + assignedTeacherId + '\'' +
                '}';
    }
}