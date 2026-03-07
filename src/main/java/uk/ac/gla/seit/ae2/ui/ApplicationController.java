package uk.ac.gla.seit.ae2.ui;

import uk.ac.gla.seit.ae2.repository.RequirementRepository;
import uk.ac.gla.seit.ae2.repository.TeacherRepository;
import uk.ac.gla.seit.ae2.service.RequirementService;
import uk.ac.gla.seit.ae2.service.StaffingService;
import uk.ac.gla.seit.ae2.service.TrainingService;

public class ApplicationController {

    // Repositories (interfaces)
    private RequirementRepository requirementRepository;
    private TeacherRepository teacherRepository;

    // Services
    private RequirementService requirementService;
    private StaffingService staffingService;
    private TrainingService trainingService;

    // UI
    private ConsoleUI ui;

    public void startup() {
        System.out.println("AE2 PTT System starting...");

        // ===== Integration wiring (replace TODO classes once E/D deliver implementations) =====
        // TODO (E): instantiate repository implementations here, e.g.
        // requirementRepository = new CsvRequirementRepository("requirements.csv");
        // teacherRepository = new CsvTeacherRepository("teachers.csv");
        //
        // TODO (D): instantiate services here, once repositories are ready:
        // requirementService = new RequirementService(requirementRepository);
        // staffingService = new StaffingService(requirementRepository, teacherRepository);
        // trainingService = new TrainingService(teacherRepository);
        //
        // TODO (B): pass services into ConsoleUI:
        // ui = new ConsoleUI(requirementService, staffingService, trainingService);

        // Temporary: keep main runnable even before implementations exist
        ui = new ConsoleUI();
        ui.run();
    }

    public void shutdown() {
        System.out.println("AE2 PTT System shutting down...");

        // TODO (E): persist data on exit
        // if (teacherRepository != null) teacherRepository.saveAll();
        // if (requirementRepository != null) requirementRepository.saveAll();
    }
}