package uk.ac.gla.seit.ae2.app;

import uk.ac.gla.seit.ae2.repository.CsvRequirementRepository;
import uk.ac.gla.seit.ae2.repository.CsvTeacherRepository;
import uk.ac.gla.seit.ae2.service.RequirementService;
import uk.ac.gla.seit.ae2.service.StaffingService;
import uk.ac.gla.seit.ae2.service.TrainingService;
import uk.ac.gla.seit.ae2.ui.ConsoleUI;

public class Main {
    public static void main(String[] args) {
        System.out.println("AE2 PTT System starting...");

        CsvRequirementRepository reqRepo = new CsvRequirementRepository("requirements.csv");
        CsvTeacherRepository teacherRepo = new CsvTeacherRepository("teachers.csv");

        reqRepo.loadAll();
        teacherRepo.loadAll();

        RequirementService reqService = new RequirementService(reqRepo);
        StaffingService staffService = new StaffingService(teacherRepo, reqRepo);
        TrainingService trainService = new TrainingService(teacherRepo);

        ConsoleUI ui = new ConsoleUI(reqService, staffService, trainService);

        ui.run();

        System.out.println("Saving data to CSV files...");
        reqRepo.saveAll();
        teacherRepo.saveAll();1
        System.out.println("Data saved successfully. Goodbye!");
    }
}