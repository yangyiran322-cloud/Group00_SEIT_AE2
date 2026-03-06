package uk.ac.gla.seit.ae2.ui;

public class ApplicationController {

    private ConsoleUI ui;

    public void startup() {
        System.out.println("AE2 PTT System starting...");

        // TODO (Integration): wire repositories + services once available.
        // Example target wiring (after members merge their code):
        // RequirementRepository reqRepo = new CsvRequirementRepository("requirements.csv");
        // TeacherRepository teacherRepo = new CsvTeacherRepository("teachers.csv");
        //
        // RequirementService requirementService = new RequirementService(reqRepo);
        // StaffingService staffingService = new StaffingService(reqRepo, teacherRepo);
        // TrainingService trainingService = new TrainingService(teacherRepo);
        //
        // ui = new ConsoleUI(requirementService, staffingService, trainingService);
        // ui.run();

        // Temporary placeholder so main branch always runs:
        this.ui = new ConsoleUI();
        this.ui.run();
    }

    public void shutdown() {
        // TODO (Integration): save data before exit via repositories.
        System.out.println("AE2 PTT System shutting down...");
    }
}