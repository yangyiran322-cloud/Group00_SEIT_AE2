package uk.ac.gla.seit.ae2.service;

import uk.ac.gla.seit.ae2.domain.Teacher;
import uk.ac.gla.seit.ae2.domain.TrainingRecord;
import uk.ac.gla.seit.ae2.repository.TeacherRepository;

import java.time.LocalDate;
import java.util.Objects;

public class TrainingService {

    private final TeacherRepository teacherRepository;

    public TrainingService(TeacherRepository teacherRepository) {
        this.teacherRepository = Objects.requireNonNull(teacherRepository);
    }

    /**
     * US5: Record teacher training.
     */
    public boolean recordTraining(String teacherId,
                                  String trainingName,
                                  LocalDate trainingDate,
                                  String description) {
        requireText(teacherId, "teacherId");
        requireText(trainingName, "trainingName");
        Objects.requireNonNull(trainingDate, "trainingDate must not be null");
        requireText(description, "description");

        Teacher teacher = teacherRepository.findById(teacherId.trim());
        if (teacher == null) {
            throw new IllegalArgumentException("Teacher not found: " + teacherId);
        }

        TrainingRecord record = new TrainingRecord(
                trainingName.trim(),
                trainingDate,
                description.trim()
        );

        teacher.addTrainingRecord(record);
        return teacherRepository.update(teacher);
    }

    private static void requireText(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Missing mandatory field: " + fieldName);
        }
    }
}