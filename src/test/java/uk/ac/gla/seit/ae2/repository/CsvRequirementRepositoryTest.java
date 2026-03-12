package uk.ac.gla.seit.ae2.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import uk.ac.gla.seit.ae2.domain.TeachingRequirement;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class CsvRequirementRepositoryTest {

    @TempDir
    Path tempDir;

    @Test
    void loadAll_readsExistingCsvFile() throws IOException {
        Path csv = tempDir.resolve("requirements.csv");

        Files.writeString(csv,
                "requirementId,moduleCode,subject,term,requiredQualification,hours,status,assignedTeacherId\n" +
                "R1,CS101,Java,Term1,PGCert,20,PENDING_ASSIGNMENT,\n" +
                "R2,CS102,Database,Term2,PhD,30,ASSIGNED,T1\n");

        CsvRequirementRepository repo = new CsvRequirementRepository(csv.toString());

        boolean loaded = repo.loadAll();

        assertTrue(loaded);
        assertEquals(2, repo.findAll().size());

        TeachingRequirement r1 = repo.findById("R1");
        TeachingRequirement r2 = repo.findById("R2");

        assertNotNull(r1);
        assertNotNull(r2);

        assertEquals("CS101", r1.getModuleCode());
        assertTrue(r1.isPending());

        assertEquals("Database", r2.getSubject());
        assertEquals("T1", r2.getAssignedTeacherId());
        assertFalse(r2.isPending());
    }

    @Test
    void saveAll_thenReload_keepsDataConsistent() {
        Path csv = tempDir.resolve("requirements.csv");

        CsvRequirementRepository repo = new CsvRequirementRepository(csv.toString());

        TeachingRequirement r1 = new TeachingRequirement(
                "R1", "CS101", "Java", "Term1", "PGCert", 20
        );

        TeachingRequirement r2 = new TeachingRequirement(
                "R2", "CS102", "Database", "Term2", "PhD", 30
        );
        r2.markAssigned("T1");

        assertEquals("R1", repo.add(r1));
        assertEquals("R2", repo.add(r2));
        assertTrue(repo.saveAll());

        CsvRequirementRepository reloadedRepo = new CsvRequirementRepository(csv.toString());
        assertTrue(reloadedRepo.loadAll());

        assertEquals(2, reloadedRepo.findAll().size());

        TeachingRequirement loadedR1 = reloadedRepo.findById("R1");
        TeachingRequirement loadedR2 = reloadedRepo.findById("R2");

        assertNotNull(loadedR1);
        assertNotNull(loadedR2);
        assertTrue(loadedR1.isPending());
        assertEquals("T1", loadedR2.getAssignedTeacherId());
        assertFalse(loadedR2.isPending());
    }

    @Test
    void loadAll_invalidCsv_returnsFalseAndLeavesRepositoryEmpty() throws IOException {
        Path csv = tempDir.resolve("requirements.csv");

        Files.writeString(csv,
                "requirementId,moduleCode,subject,term,requiredQualification,hours,status,assignedTeacherId\n" +
                "bad,line\n");

        CsvRequirementRepository repo = new CsvRequirementRepository(csv.toString());

        boolean loaded = repo.loadAll();

        assertFalse(loaded);
        assertTrue(repo.findAll().isEmpty());
    }
}