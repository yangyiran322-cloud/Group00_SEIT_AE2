package uk.ac.gla.seit.ae2.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import uk.ac.gla.seit.ae2.domain.Teacher;
import uk.ac.gla.seit.ae2.domain.TeacherStatus;
import uk.ac.gla.seit.ae2.domain.TeachingRequirement;
import uk.ac.gla.seit.ae2.repository.CsvRequirementRepository;
import uk.ac.gla.seit.ae2.repository.CsvTeacherRepository;

import java.nio.file.Path;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class StaffingServiceTest {

    @TempDir
    Path tempDir;

    @Test
    void searchAvailableTeachers_returnsOnlyAvailableAndMatchingTeachers() {
        Path teacherCsv = tempDir.resolve("teachers.csv");
        Path requirementCsv = tempDir.resolve("requirements.csv");

        CsvTeacherRepository teacherRepo = new CsvTeacherRepository(teacherCsv.toString());
        CsvRequirementRepository requirementRepo = new CsvRequirementRepository(requirementCsv.toString());
        StaffingService service = new StaffingService(teacherRepo, requirementRepo);

        Teacher t1 = new Teacher(
                "T1",
                "Alice",
                Set.of("Java", "Database"),
                Set.of("PGCert", "PhD"),
                TeacherStatus.AVAILABLE
        );

        Teacher t2 = new Teacher(
                "T2",
                "Bob",
                Set.of("Java"),
                Set.of("PGCert"),
                TeacherStatus.ASSIGNED
        );

        Teacher t3 = new Teacher(
                "T3",
                "Carol",
                Set.of("Networks"),
                Set.of("PGCert"),
                TeacherStatus.AVAILABLE
        );

        Teacher t4 = new Teacher(
                "T4",
                "David",
                Set.of("Java"),
                Set.of("MSc"),
                TeacherStatus.AVAILABLE
        );

        teacherRepo.add(t1);
        teacherRepo.add(t2);
        teacherRepo.add(t3);
        teacherRepo.add(t4);

        List<Teacher> result = service.searchAvailableTeachers("Java", "PGCert");

        assertEquals(1, result.size());
        assertEquals("T1", result.get(0).getTeacherId());
        assertEquals("Alice", result.get(0).getName());
    }

    @Test
    void assignTeacher_successfullyAssignsTeacherAndUpdatesStatuses() {
        Path teacherCsv = tempDir.resolve("teachers.csv");
        Path requirementCsv = tempDir.resolve("requirements.csv");

        CsvTeacherRepository teacherRepo = new CsvTeacherRepository(teacherCsv.toString());
        CsvRequirementRepository requirementRepo = new CsvRequirementRepository(requirementCsv.toString());
        StaffingService service = new StaffingService(teacherRepo, requirementRepo);

        TeachingRequirement req = new TeachingRequirement(
                "R001", "CS101", "Java", "Term1", "PGCert", 20
        );

        Teacher teacher = new Teacher(
                "T1",
                "Alice",
                Set.of("Java"),
                Set.of("PGCert"),
                TeacherStatus.AVAILABLE
        );

        requirementRepo.add(req);
        teacherRepo.add(teacher);

        boolean assigned = service.assignTeacher("R001", "T1");

        assertTrue(assigned);

        TeachingRequirement updatedReq = requirementRepo.findById("R001");
        Teacher updatedTeacher = teacherRepo.findById("T1");

        assertNotNull(updatedReq);
        assertNotNull(updatedTeacher);
        assertEquals("T1", updatedReq.getAssignedTeacherId());
        assertFalse(updatedReq.isPending());
        assertEquals(TeacherStatus.ASSIGNED, updatedTeacher.getStatus());
    }

    @Test
    void assignTeacher_failsWhenRequirementNotFound() {
        Path teacherCsv = tempDir.resolve("teachers.csv");
        Path requirementCsv = tempDir.resolve("requirements.csv");

        CsvTeacherRepository teacherRepo = new CsvTeacherRepository(teacherCsv.toString());
        CsvRequirementRepository requirementRepo = new CsvRequirementRepository(requirementCsv.toString());
        StaffingService service = new StaffingService(teacherRepo, requirementRepo);

        Teacher teacher = new Teacher(
                "T1",
                "Alice",
                Set.of("Java"),
                Set.of("PGCert"),
                TeacherStatus.AVAILABLE
        );

        teacherRepo.add(teacher);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                service.assignTeacher("R999", "T1")
        );

        assertTrue(ex.getMessage().contains("Requirement not found"));
    }

    @Test
    void assignTeacher_failsWhenTeacherNotFound() {
        Path teacherCsv = tempDir.resolve("teachers.csv");
        Path requirementCsv = tempDir.resolve("requirements.csv");

        CsvTeacherRepository teacherRepo = new CsvTeacherRepository(teacherCsv.toString());
        CsvRequirementRepository requirementRepo = new CsvRequirementRepository(requirementCsv.toString());
        StaffingService service = new StaffingService(teacherRepo, requirementRepo);

        TeachingRequirement req = new TeachingRequirement(
                "R001", "CS101", "Java", "Term1", "PGCert", 20
        );

        requirementRepo.add(req);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                service.assignTeacher("R001", "T999")
        );

        assertTrue(ex.getMessage().contains("Teacher not found"));
    }

    @Test
    void assignTeacher_failsWhenTeacherNotAvailable() {
        Path teacherCsv = tempDir.resolve("teachers.csv");
        Path requirementCsv = tempDir.resolve("requirements.csv");

        CsvTeacherRepository teacherRepo = new CsvTeacherRepository(teacherCsv.toString());
        CsvRequirementRepository requirementRepo = new CsvRequirementRepository(requirementCsv.toString());
        StaffingService service = new StaffingService(teacherRepo, requirementRepo);

        TeachingRequirement req = new TeachingRequirement(
                "R001", "CS101", "Java", "Term1", "PGCert", 20
        );

        Teacher teacher = new Teacher(
                "T1",
                "Alice",
                Set.of("Java"),
                Set.of("PGCert"),
                TeacherStatus.ASSIGNED
        );

        requirementRepo.add(req);
        teacherRepo.add(teacher);

        boolean assigned = service.assignTeacher("R001", "T1");

        assertFalse(assigned);

        TeachingRequirement unchangedReq = requirementRepo.findById("R001");
        Teacher unchangedTeacher = teacherRepo.findById("T1");

        assertNotNull(unchangedReq);
        assertNotNull(unchangedTeacher);
        assertTrue(unchangedReq.isPending());
        assertNull(unchangedReq.getAssignedTeacherId());
        assertEquals(TeacherStatus.ASSIGNED, unchangedTeacher.getStatus());
    }

    @Test
    void assignTeacher_failsWhenQualificationDoesNotMatch() {
        Path teacherCsv = tempDir.resolve("teachers.csv");
        Path requirementCsv = tempDir.resolve("requirements.csv");

        CsvTeacherRepository teacherRepo = new CsvTeacherRepository(teacherCsv.toString());
        CsvRequirementRepository requirementRepo = new CsvRequirementRepository(requirementCsv.toString());
        StaffingService service = new StaffingService(teacherRepo, requirementRepo);

        TeachingRequirement req = new TeachingRequirement(
                "R001", "CS101", "Java", "Term1", "PGCert", 20
        );

        Teacher teacher = new Teacher(
                "T1",
                "Alice",
                Set.of("Java"),
                Set.of("MSc"),
                TeacherStatus.AVAILABLE
        );

        requirementRepo.add(req);
        teacherRepo.add(teacher);

        boolean assigned = service.assignTeacher("R001", "T1");

        assertFalse(assigned);

        TeachingRequirement unchangedReq = requirementRepo.findById("R001");
        Teacher unchangedTeacher = teacherRepo.findById("T1");

        assertNotNull(unchangedReq);
        assertNotNull(unchangedTeacher);
        assertTrue(unchangedReq.isPending());
        assertNull(unchangedReq.getAssignedTeacherId());
        assertEquals(TeacherStatus.AVAILABLE, unchangedTeacher.getStatus());
    }
}