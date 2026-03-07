package uk.ac.gla.seit.ae2.service;

import uk.ac.gla.seit.ae2.repository.TeacherRepository;

public class TrainingService {
    private final TeacherRepository teacherRepository;

    public TrainingService(TeacherRepository teacherRepository) {
        this.teacherRepository = teacherRepository;
    }
}