package uk.ac.gla.seit.ae2.domain;

public class TrainingRecord {
    private final String id;
    private final String teacherId;
    private final String requirementId;
    private final String date;   // e.g., "2026-03-10"
    private final String notes;

    public TrainingRecord(String id, String teacherId, String requirementId, String date, String notes) {
        this.id = id;
        this.teacherId = teacherId;
        this.requirementId = requirementId;
        this.date = date;
        this.notes = notes;
    }

    public String getId() {
        return id;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public String getRequirementId() {
        return requirementId;
    }

    public String getDate() {
        return date;
    }

    public String getNotes() {
        return notes;
    }
}