package uk.ac.gla.seit.ae2.domain;

import java.time.LocalDate;

/**
 * Represents a training record linked to a teacher.
 */
public class TrainingRecord {

    private String trainingName;
    private LocalDate trainingDate;
    private String description;

    public TrainingRecord(String trainingName, LocalDate trainingDate, String description) {
        this.trainingName = trainingName;
        this.trainingDate = trainingDate;
        this.description = description;
    }

    public String getTrainingName() {
        return trainingName;
    }

    public LocalDate getTrainingDate() {
        return trainingDate;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "TrainingRecord{" +
                "trainingName='" + trainingName + '\'' +
                ", trainingDate=" + trainingDate +
                ", description='" + description + '\'' +
                '}';
    }
}