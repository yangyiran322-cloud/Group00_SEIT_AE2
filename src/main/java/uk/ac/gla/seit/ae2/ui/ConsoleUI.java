package uk.ac.gla.seit.ae2.ui;

import uk.ac.gla.seit.ae2.service.RequirementService;
import uk.ac.gla.seit.ae2.service.StaffingService;
import uk.ac.gla.seit.ae2.service.TrainingService;

import java.util.Scanner;

public class ConsoleUI {

    private final Scanner scanner = new Scanner(System.in);

    // Services (nullable for now to keep main runnable)
    private final RequirementService requirementService;
    private final StaffingService staffingService;
    private final TrainingService trainingService;

    /** Placeholder constructor (keeps main runnable before services exist). */
    public ConsoleUI() {
        this(null, null, null);
    }

    /** Preferred constructor once services are implemented. */
    public ConsoleUI(RequirementService requirementService,
                     StaffingService staffingService,
                     TrainingService trainingService) {
        this.requirementService = requirementService;
        this.staffingService = staffingService;
        this.trainingService = trainingService;
    }

    public void run() {
        System.out.println("Console UI ready.");
        System.out.println("Commands: help, exit");

        while (true) {
            System.out.print("> ");
            String cmd = scanner.nextLine().trim().toLowerCase();

            if ("exit".equals(cmd)) {
                System.out.println("Exiting UI...");
                break;
            } else if ("help".equals(cmd)) {
                System.out.println("Available commands: help, exit");
            } else {
                System.out.println("Unknown command: " + cmd);
            }
        }
    }
}