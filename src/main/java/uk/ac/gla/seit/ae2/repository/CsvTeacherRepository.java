package uk.ac.gla.seit.ae2.repository;

import uk.ac.gla.seit.ae2.domain.Teacher;
import uk.ac.gla.seit.ae2.domain.TeacherStatus;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class CsvTeacherRepository implements TeacherRepository {

    private static final String HEADER = "teacherId,name,subjectExpertise,qualifications,status";

    private final Path filePath;
    private final List<Teacher> teachers = new ArrayList<>();

    public CsvTeacherRepository(String fileName) {
        this.filePath = Paths.get(fileName);
    }

    @Override
    public String add(Teacher teacher) {
        if (teacher == null) {
            return null;
        }

        if (findById(teacher.getTeacherId()) != null) {
            return null;
        }

        teachers.add(teacher);
        return teacher.getTeacherId();
    }

    @Override
    public Teacher findById(String id) {
        for (Teacher teacher : teachers) {
            if (teacher.getTeacherId().equals(id)) {
                return teacher;
            }
        }
        return null;
    }

    @Override
    public List<Teacher> findAll() {
        return new ArrayList<>(teachers);
    }

    @Override
    public List<Teacher> searchBySubjectOrQualification(String keyword) {
        List<Teacher> result = new ArrayList<>();
        if (keyword == null || keyword.isBlank()) {
            return result;
        }

        String normalized = keyword.trim().toLowerCase();

        for (Teacher teacher : teachers) {
            boolean subjectMatch = false;
            for (Object subject : teacher.getSubjectExpertise()) {
                if (subject != null && subject.toString().toLowerCase().contains(normalized)) {
                    subjectMatch = true;
                    break;
                }
            }

            boolean qualificationMatch = false;
            for (Object qualification : teacher.getQualifications()) {
                if (qualification != null && qualification.toString().toLowerCase().contains(normalized)) {
                    qualificationMatch = true;
                    break;
                }
            }

            if (subjectMatch || qualificationMatch) {
                result.add(teacher);
            }
        }

        return result;
    }

    @Override
    public boolean update(Teacher updatedTeacher) {
        if (updatedTeacher == null) {
            return false;
        }

        for (int i = 0; i < teachers.size(); i++) {
            Teacher existing = teachers.get(i);
            if (existing.getTeacherId().equals(updatedTeacher.getTeacherId())) {
                teachers.set(i, updatedTeacher);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean saveAll() {
        List<String> bodyLines = new ArrayList<>();
        for (Teacher teacher : teachers) {
            bodyLines.add(toCsvLine(teacher));
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
        teachers.clear();

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
                teachers.add(fromCsvLine(line));
            }
            return true;
        } catch (RuntimeException e) {
            teachers.clear();
            return false;
        }
    }

    private String toCsvLine(Teacher teacher) {
        return String.join(",",
                teacher.getTeacherId(),
                teacher.getName(),
                CsvUtil.joinSemicolon(toStringSet(teacher.getSubjectExpertise())),
                CsvUtil.joinSemicolon(toStringSet(teacher.getQualifications())),
                teacher.getStatus().name()
        );
    }

    private Teacher fromCsvLine(String line) {
        String[] parts = line.split(",", -1);
        if (parts.length != 5) {
            throw new IllegalArgumentException("Invalid teacher CSV line: " + line);
        }

        String teacherId = parts[0].trim();
        String name = parts[1].trim();
        Set<String> subjectExpertise = new LinkedHashSet<>(CsvUtil.splitSemicolon(parts[2]));
        Set<String> qualifications = new LinkedHashSet<>(CsvUtil.splitSemicolon(parts[3]));
        TeacherStatus status = TeacherStatus.valueOf(parts[4].trim());

        return new Teacher(teacherId, name, subjectExpertise, qualifications, status);
    }

    private Set<String> toStringSet(Set values) {
        Set<String> result = new LinkedHashSet<>();
        if (values == null) {
            return result;
        }

        for (Object value : values) {
            if (value != null) {
                result.add(value.toString());
            }
        }
        return result;
    }
}