package uk.ac.gla.seit.ae2.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import uk.ac.gla.seit.ae2.domain.Teacher;
import uk.ac.gla.seit.ae2.domain.TeacherStatus;
import uk.ac.gla.seit.ae2.domain.TrainingRecord;
import uk.ac.gla.seit.ae2.repository.CsvTeacherRepository;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class TrainingServiceTest {

    @TempDir
    Path tempDir;

    @Test
    void recordTraining_addsTrainingRecordSuccessfully() {
        Path teacherCsv = tempDir.resolve("teachers.csv");

        CsvTeacherRepository teacherRepo = new CsvTeacherRepository(teacherCsv.toString());
        TrainingService service = new TrainingService(teacherRepo);

        Teacher teacher = new Teacher(
                "T1",
                "Alice",
                Set.of("Java"),
                Set.of("PGCert"),
                TeacherStatus.AVAILABLE
        );

        teacherRepo.add(teacher);

        boolean recorded = service.recordTraining(
                "T1",
                "Safeguarding",
                LocalDate.of(2026, 3, 13),
                "Completed basic safeguarding training"
        );

        assertTrue(recorded);

        Teacher updatedTeacher = teacherRepo.findById("T1");
        assertNotNull(updatedTeacher);

        List<TrainingRecord> history = updatedTeacher.getTrainingHistory();
        assertEquals(1, history.size());

        TrainingRecord record = history.get(0);
        assertEquals("Safeguarding", record.getTrainingName());
        assertEquals(LocalDate.of(2026, 3, 13), record.getTrainingDate());
        assertEquals("Completed basic safeguarding training", record.getDescription());
    }

    @Test
    void recordTraining_failsWhenTeacherNotFound() {
        Path teacherCsv = tempDir.resolve("teachers.csv");

        CsvTeacherRepository teacherRepo = new CsvTeacherRepository(teacherCsv.toString());
        TrainingService service = new TrainingService(teacherRepo);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                service.recordTraining(
                        "T999",
                        "Safeguarding",
                        LocalDate.of(2026, 3, 13),
                        "Completed basic safeguarding training"
                )
        );

        assertTrue(ex.getMessage().contains("Teacher not found"));
    }
}