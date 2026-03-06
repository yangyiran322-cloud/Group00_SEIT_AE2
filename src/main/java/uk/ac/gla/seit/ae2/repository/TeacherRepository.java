package uk.ac.gla.seit.ae2.repository;

import uk.ac.gla.seit.ae2.domain.Teacher;

import java.util.List;

public interface TeacherRepository {
    String add(Teacher teacher);
    Teacher findById(String id);
    List<Teacher> findAll();
    List<Teacher> searchBySubjectOrQualification(String keyword);
    boolean update(Teacher teacher);
    boolean saveAll();
    boolean loadAll();
}