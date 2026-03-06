package uk.ac.gla.seit.ae2.ui;

import java.util.Scanner;

public class ConsoleUI {

    private final Scanner scanner = new Scanner(System.in);

    /** Minimal console loop so the program can run end-to-end. */
    public void run() {
        System.out.println("Console UI ready (placeholder).");
        System.out.println("Commands: help, exit");

        while (true) {
            System.out.print("> ");
            String cmd = scanner.nextLine().trim().toLowerCase();

            if (cmd.equals("exit")) {
                System.out.println("Exiting UI...");
                break;
            } else if (cmd.equals("help")) {
                System.out.println("Available commands: help, exit");
            } else {
                System.out.println("Unknown command: " + cmd);
            }
        }
    }
}