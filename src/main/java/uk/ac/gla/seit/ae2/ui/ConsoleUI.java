package uk.ac.gla.seit.ae2.ui;

import uk.ac.gla.seit.ae2.domain.Teacher;
import uk.ac.gla.seit.ae2.domain.TeachingRequirement;
import uk.ac.gla.seit.ae2.service.RequirementService;
import uk.ac.gla.seit.ae2.service.StaffingService;
import uk.ac.gla.seit.ae2.service.TrainingService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ConsoleUI {

    private final Scanner scanner = new Scanner(System.in);

    // Services
    private final RequirementService requirementService;
    private final StaffingService staffingService;
    private final TrainingService trainingService;

    public ConsoleUI() {
        this(null, null, null);
    }

    public ConsoleUI(RequirementService requirementService,
                     StaffingService staffingService,
                     TrainingService trainingService) {
        this.requirementService = requirementService;
        this.staffingService = staffingService;
        this.trainingService = trainingService;
    }

    public void run() {
        boolean running = true;
        System.out.println("==================================================");
        System.out.println("  Welcome to Part-Time Teacher Management System  ");
        System.out.println("==================================================");

        while (running) {
            showMenu();
            String choice = scanner.nextLine().trim();

            try {
                switch (choice) {
                    case "1": handleSubmitRequirement(); break; // US1
                    case "2": handleViewRequirements(); break;  // US2
                    case "3": handleSearchTeachers(); break;    // US3
                    case "4": handleAssignTeacher(); break;     // US4
                    case "5": handleRecordTraining(); break;    // US5
                    case "6":
                        System.out.println("Exiting and saving data... Goodbye!");
                        running = false; // US6
                        break;
                    default:
                        System.out.println("Invalid choice. Please enter a number between 1 and 6.");
                }
            } catch (Exception e) {
                System.out.println("Operation Failed: " + e.getMessage());
            }
            System.out.println();
        }
    }

    private void showMenu() {
        System.out.println("Please select an option:");
        System.out.println("1. Submit teaching requirement");
        System.out.println("2. View all teaching requirements");
        System.out.println("3. Search available teachers");
        System.out.println("4. Assign teacher to requirement");
        System.out.println("5. Record teacher training");
        System.out.println("6. Save & Exit");
        System.out.print("> ");
    }

    // ==========================================
    // US1: Submit Requirement
    // ==========================================
    private void handleSubmitRequirement() {
        System.out.println("\n--- Submit Teaching Requirement ---");

        System.out.print("Enter Module Code (e.g., SEIT): ");
        String moduleCode = scanner.nextLine().trim();
        System.out.print("Enter Subject: ");
        String subject = scanner.nextLine().trim();
        System.out.print("Enter Term (e.g., 2026S): ");
        String term = scanner.nextLine().trim();
        System.out.print("Enter Required Qualification: ");
        String qual = scanner.nextLine().trim();
        int hours = readIntSafely("Enter Hours per Week: ");

        // TODO: Waiting for D
        // requirementService.submitRequirement(moduleCode, subject, term, qual, hours);

        System.out.println("[UI Test] Requirement input collected successfully! (Waiting for D's Service to save)");
    }

    // ==========================================
    // US2: View Requirements
    // ==========================================
    private void handleViewRequirements() {
        System.out.println("\n========================================================================================================================");
        System.out.println("                                              ALL TEACHING REQUIREMENTS                                                 ");
        System.out.println("========================================================================================================================");

        try {
            // TODO: Waiting for D
            // List<TeachingRequirement> requirements = requirementService.listAllRequirements();
            List<TeachingRequirement> requirements = new ArrayList<>(); // 临时空列表，保证不报错

            if (requirements.isEmpty()) {
                System.out.println("No teaching requirements found. (Or waiting for D's Service to return data)");
                System.out.println("========================================================================================================================\n");
                return;
            }

            System.out.printf("%-8s | %-12s | %-15s | %-10s | %-20s | %-8s | %-20s | %-12s%n",
                    "Req ID", "Module Code", "Subject", "Term", "Qualification", "Hours", "Status", "Assigned To");
            System.out.println("------------------------------------------------------------------------------------------------------------------------");

            for (TeachingRequirement req : requirements) {
                String statusDisplay = req.isPending() ? "PENDING" : "ASSIGNED";
                String assignedTo = (req.getAssignedTeacherId() != null) ? req.getAssignedTeacherId() : "N/A";
                System.out.printf("%-8s | %-12s | %-15s | %-10s | %-20s | %-8d | %-20s | %-12s%n",
                        req.getRequirementId(), req.getModuleCode(), req.getSubject(),
                        req.getTerm(), req.getRequiredQualification(), req.getHours(),
                        statusDisplay, assignedTo);
            }
        } catch (Exception e) { System.out.println("Load failed: " + e.getMessage()); }
        System.out.println("========================================================================================================================\n");
    }

    // ==========================================
    // US3: Search Teachers
    // ==========================================
    private void handleSearchTeachers() {
        System.out.println("\n--- Search Available Teachers ---");

        System.out.print("Enter Subject required: ");
        String subject = scanner.nextLine().trim();
        System.out.print("Enter Qualification required: ");
        String qualification = scanner.nextLine().trim();

        try {
            // TODO: Waiting for D
            // List<Teacher> teachers = staffingService.searchAvailableTeachers(subject, qualification);
            List<Teacher> teachers = new ArrayList<>(); // 临时空列表，保证不报错

            if (teachers.isEmpty()) {
                System.out.println("No suitable candidate exists. (Or waiting for D's Service to return data)");
                return;
            }

            System.out.println("\n=====================================================================================================");
            System.out.println("                                    MATCHING AVAILABLE TEACHERS                                      ");
            System.out.println("=====================================================================================================");
            System.out.printf("%-10s | %-15s | %-30s | %-30s%n", "Teacher ID", "Name", "Subject Expertise", "Qualifications");
            System.out.println("-----------------------------------------------------------------------------------------------------");

            for (Teacher teacher : teachers) {
                String subjectsStr = String.join(", ", teacher.getSubjectExpertise());
                String qualsStr = String.join(", ", teacher.getQualifications());
                System.out.printf("%-10s | %-15s | %-30s | %-30s%n",
                        teacher.getTeacherId(), teacher.getName(), subjectsStr, qualsStr);
            }
            System.out.println("=====================================================================================================\n");
        } catch (Exception e) { System.out.println("Search failed: " + e.getMessage()); }
    }

    // ==========================================
    // US4: Assign Teacher
    // ==========================================
    private void handleAssignTeacher() {
        System.out.println("\n--- Assign Teacher to Requirement ---");

        System.out.print("Enter Requirement ID: ");
        String reqId = scanner.nextLine().trim();
        System.out.print("Enter Teacher ID to assign: ");
        String teacherId = scanner.nextLine().trim();

        try {
            // TODO: Waiting for D
            // staffingService.assignTeacher(reqId, teacherId);
            System.out.println("[UI Test] Success! Teacher assigned! (Waiting for D's Service to process)");
        } catch (Exception e) {
            System.out.println("Assignment Failed: " + e.getMessage());
        }
    }

    // ==========================================
    // US5: Record Training
    // ==========================================
    private void handleRecordTraining() {
        System.out.println("\n--- Record Teacher Training ---");

        System.out.print("Enter Teacher ID: ");
        String teacherId = scanner.nextLine().trim();
        System.out.print("Enter Training Name: ");
        String trainingName = scanner.nextLine().trim();
        LocalDate date = readDateSafely("Enter Training Date (YYYY-MM-DD): ");
        System.out.print("Enter Description/Notes: ");
        String description = scanner.nextLine().trim();

        try {
            // TODO: Waiting for D
            // trainingService.recordTraining(teacherId, trainingName, date, description);
            System.out.println("[UI Test] Training record added! (Waiting for D's Service to process)");
        } catch (Exception e) {
            System.out.println("Failed to record training: " + e.getMessage());
        }
    }

    // ==========================================
    // method: get safe
    // ==========================================
    private int readIntSafely(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                int value = Integer.parseInt(input);
                if (value <= 0) {
                    System.out.println("Please enter a positive number.");
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid integer.");
            }
        }
    }

    private LocalDate readDateSafely(String prompt) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                return LocalDate.parse(input, formatter);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please strictly use YYYY-MM-DD (e.g., 2026-03-15).");
            }
        }
    }
}