package uk.ac.gla.seit.ae2.app;
import uk.ac.gla.seit.ae2.repository.CsvRequirementRepository;
import uk.ac.gla.seit.ae2.domain.TeachingRequirement;

import uk.ac.gla.seit.ae2.repository.CsvTeacherRepository;
import uk.ac.gla.seit.ae2.domain.Teacher;

import java.util.List;


public class RepositoryTest {

    public static void main(String[] args) {

        CsvTeacherRepository repo =
                new CsvTeacherRepository("data/teachers.csv");

        boolean loaded = repo.loadAll();

        if (!loaded) {
            System.out.println("Failed to load teachers.");
            return;
        }

        List<Teacher> teachers = repo.findAll();

        System.out.println("Loaded teachers:");

        for (Teacher t : teachers) {
            System.out.println(
                    t.getTeacherId() + " - " + t.getName()
            );
        }
        System.out.println("\nLoaded requirements:");

        CsvRequirementRepository reqRepo =
                new CsvRequirementRepository("data/requirements.csv");

        boolean reqLoaded = reqRepo.loadAll();

        if (!reqLoaded) {
            System.out.println("Failed to load requirements.");
            return;
        }

        for (TeachingRequirement r : reqRepo.findAll()) {
            System.out.println(
                    r.getRequirementId() + " - " +
                            r.getModuleCode() + " - " +
                            r.getSubject()
            );
        }
        }
    }
