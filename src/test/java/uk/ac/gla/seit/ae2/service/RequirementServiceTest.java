package uk.ac.gla.seit.ae2.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import uk.ac.gla.seit.ae2.domain.TeachingRequirement;
import uk.ac.gla.seit.ae2.repository.CsvRequirementRepository;

import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RequirementServiceTest {

    @TempDir
    Path tempDir;

    @Test
    void submitRequirement_successfullyAddsPendingRequirement() {
        Path csv = tempDir.resolve("requirements.csv");

        CsvRequirementRepository repo = new CsvRequirementRepository(csv.toString());
        RequirementService service = new RequirementService(repo);

        String requirementId = service.submitRequirement(
                "CS101",
                "Java",
                "Term1",
                "PGCert",
                20
        );

        assertEquals("R001", requirementId);

        List<TeachingRequirement> all = repo.findAll();
        assertEquals(1, all.size());

        TeachingRequirement req = repo.findById("R001");
        assertNotNull(req);
        assertEquals("CS101", req.getModuleCode());
        assertEquals("Java", req.getSubject());
        assertEquals("Term1", req.getTerm());
        assertEquals("PGCert", req.getRequiredQualification());
        assertEquals(20, req.getHours());
        assertTrue(req.isPending());
        assertNull(req.getAssignedTeacherId());
    }

    @Test
    void submitRequirement_rejectsBlankModuleCode() {
        Path csv = tempDir.resolve("requirements.csv");

        CsvRequirementRepository repo = new CsvRequirementRepository(csv.toString());
        RequirementService service = new RequirementService(repo);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                service.submitRequirement(
                        "   ",
                        "Java",
                        "Term1",
                        "PGCert",
                        20
                )
        );

        assertTrue(ex.getMessage().contains("moduleCode"));
    }

    @Test
    void submitRequirement_rejectsBlankSubject() {
        Path csv = tempDir.resolve("requirements.csv");

        CsvRequirementRepository repo = new CsvRequirementRepository(csv.toString());
        RequirementService service = new RequirementService(repo);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                service.submitRequirement(
                        "CS101",
                        "   ",
                        "Term1",
                        "PGCert",
                        20
                )
        );

        assertTrue(ex.getMessage().contains("subject"));
    }

    @Test
    void submitRequirement_rejectsNonPositiveHours() {
        Path csv = tempDir.resolve("requirements.csv");

        CsvRequirementRepository repo = new CsvRequirementRepository(csv.toString());
        RequirementService service = new RequirementService(repo);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                service.submitRequirement(
                        "CS101",
                        "Java",
                        "Term1",
                        "PGCert",
                        0
                )
        );

        assertTrue(ex.getMessage().contains("hours"));
    }

    @Test
    void listPendingRequirements_returnsOnlyPendingItems() {
        Path csv = tempDir.resolve("requirements.csv");

        CsvRequirementRepository repo = new CsvRequirementRepository(csv.toString());
        RequirementService service = new RequirementService(repo);

        TeachingRequirement r1 = new TeachingRequirement(
                "R001", "CS101", "Java", "Term1", "PGCert", 20
        );

        TeachingRequirement r2 = new TeachingRequirement(
                "R002", "CS102", "Database", "Term2", "PhD", 30
        );
        r2.markAssigned("T1");

        repo.add(r1);
        repo.add(r2);

        List<TeachingRequirement> pending = service.listPendingRequirements();

        assertEquals(1, pending.size());
        assertEquals("R001", pending.get(0).getRequirementId());
        assertTrue(pending.get(0).isPending());
    }
}