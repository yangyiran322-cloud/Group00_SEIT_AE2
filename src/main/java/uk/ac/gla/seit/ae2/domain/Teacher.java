package uk.ac.gla.seit.ae2.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Represents a teacher in the system.
 */
public class Teacher {

    private String teacherId;
    private String name;
    private Set<String> subjectExpertise;
    private Set<String> qualifications;
    private TeacherStatus status;
    private List<TrainingRecord> trainingHistory;

    public Teacher(String teacherId,
                   String name,
                   Set<String> subjectExpertise,
                   Set<String> qualifications,
                   TeacherStatus status) {

        this.teacherId = teacherId;
        this.name = name;
        this.subjectExpertise = subjectExpertise;
        this.qualifications = qualifications;
        this.status = status;
        this.trainingHistory = new ArrayList<>();
    }

    public String getTeacherId() {
        return teacherId;
    }

    public String getName() {
        return name;
    }

    public Set<String> getSubjectExpertise() {
        return subjectExpertise;
    }

    public Set<String> getQualifications() {
        return qualifications;
    }

    public TeacherStatus getStatus() {
        return status;
    }

    public List<TrainingRecord> getTrainingHistory() {
        return trainingHistory;
    }

    public void setStatus(TeacherStatus status) {
        this.status = status;
    }

    /**
     * Adds a training record for the teacher.
     */
    public void addTrainingRecord(TrainingRecord record) {
        trainingHistory.add(record);
    }

    /**
     * Returns true if teacher is available.
     */
    public boolean isAvailable() {
        return status == TeacherStatus.AVAILABLE;
    }

    /**
     * Checks if teacher has expertise in a subject.
     */
    public boolean matchesSubject(String subject) {
        return subjectExpertise.contains(subject);
    }

    /**
     * Checks if teacher has required qualification.
     */
    public boolean hasQualification(String requiredQualification) {
        return qualifications.contains(requiredQualification);
    }

    @Override
    public String toString() {
        return "Teacher{" +
                "teacherId='" + teacherId + '\'' +
                ", name='" + name + '\'' +
                ", subjectExpertise=" + subjectExpertise +
                ", qualifications=" + qualifications +
                ", status=" + status +
                ", trainingHistory=" + trainingHistory +
                '}';
    }
}