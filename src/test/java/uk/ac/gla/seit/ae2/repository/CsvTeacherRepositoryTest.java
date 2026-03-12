package uk.ac.gla.seit.ae2.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import uk.ac.gla.seit.ae2.domain.Teacher;
import uk.ac.gla.seit.ae2.domain.TeacherStatus;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CsvTeacherRepositoryTest {

    @TempDir
    Path tempDir;

    @Test
    void loadAll_readsExistingCsvFile() throws IOException {
        Path csv = tempDir.resolve("teachers.csv");

        Files.writeString(csv,
                "teacherId,name,subjectExpertise,qualifications,status\n" +
                "T1,Alice,Java;Database,PGCert;PhD,AVAILABLE\n" +
                "T2,Bob,Networks,MSc,ASSIGNED\n");

        CsvTeacherRepository repo = new CsvTeacherRepository(csv.toString());

        boolean loaded = repo.loadAll();

        assertTrue(loaded);
        assertEquals(2, repo.findAll().size());

        Teacher t1 = repo.findById("T1");
        Teacher t2 = repo.findById("T2");

        assertNotNull(t1);
        assertNotNull(t2);

        assertEquals("Alice", t1.getName());
        assertTrue(t1.getSubjectExpertise().contains("Java"));
        assertTrue(t1.getSubjectExpertise().contains("Database"));
        assertTrue(t1.getQualifications().contains("PGCert"));
        assertTrue(t1.getQualifications().contains("PhD"));
        assertEquals(TeacherStatus.AVAILABLE, t1.getStatus());

        assertEquals("Bob", t2.getName());
        assertTrue(t2.getSubjectExpertise().contains("Networks"));
        assertTrue(t2.getQualifications().contains("MSc"));
        assertEquals(TeacherStatus.ASSIGNED, t2.getStatus());
    }

    @Test
    void saveAll_thenReload_keepsDataConsistent() {
        Path csv = tempDir.resolve("teachers.csv");

        CsvTeacherRepository repo = new CsvTeacherRepository(csv.toString());

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
                Set.of("Networks"),
                Set.of("MSc"),
                TeacherStatus.ASSIGNED
        );

        assertEquals("T1", repo.add(t1));
        assertEquals("T2", repo.add(t2));
        assertTrue(repo.saveAll());

        CsvTeacherRepository reloadedRepo = new CsvTeacherRepository(csv.toString());
        assertTrue(reloadedRepo.loadAll());

        assertEquals(2, reloadedRepo.findAll().size());

        Teacher loadedT1 = reloadedRepo.findById("T1");
        Teacher loadedT2 = reloadedRepo.findById("T2");

        assertNotNull(loadedT1);
        assertNotNull(loadedT2);

        assertEquals("Alice", loadedT1.getName());
        assertTrue(loadedT1.getSubjectExpertise().contains("Java"));
        assertTrue(loadedT1.getQualifications().contains("PhD"));
        assertEquals(TeacherStatus.AVAILABLE, loadedT1.getStatus());

        assertEquals("Bob", loadedT2.getName());
        assertTrue(loadedT2.getSubjectExpertise().contains("Networks"));
        assertTrue(loadedT2.getQualifications().contains("MSc"));
        assertEquals(TeacherStatus.ASSIGNED, loadedT2.getStatus());
    }

    @Test
    void loadAll_invalidCsv_returnsFalseAndLeavesRepositoryEmpty() throws IOException {
        Path csv = tempDir.resolve("teachers.csv");

        Files.writeString(csv,
                "teacherId,name,subjectExpertise,qualifications,status\n" +
                "bad,line\n");

        CsvTeacherRepository repo = new CsvTeacherRepository(csv.toString());

        boolean loaded = repo.loadAll();

        assertFalse(loaded);
        assertTrue(repo.findAll().isEmpty());
    }
}