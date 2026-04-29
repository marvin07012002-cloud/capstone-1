package com.pluralsight;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Scanner;

public class AccountingApp {

    public static void main(String[] args) {

        createFileHeader();// Created a File Header to organize transactions
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("""
                    Home Screen
                    D) Add Deposit
                    P) Make a payment
                    L) Ledger
                    X) Exit""");

            String choice = scanner.nextLine().trim().toUpperCase();

            switch (choice) {

                case "D":
                    addTransaction(scanner, true);
                    break;
                case "P":
                    addTransaction(scanner, false);
                    break;
                case "L":
                    showLedgerScreen(scanner);
                    break;
                case "X":
                    return;
                default:
                    System.err.println("Invalid Choice");

            }

        }
    }

    //Here are all the methods for the program

    private static void showLedgerScreen(Scanner scanner) {

        while (true) {
            System.out.println("""
                    Ledger
                    A) All
                    D) Deposits
                    P) Payments
                    R) Reports
                    H) Home
                    """);

            String choice = scanner.nextLine().trim().toUpperCase();

            switch (choice) {
                case "A":
                    displayTransactions("ALL");
                    break;
                case "D":
                    displayTransactions("DEPOSITS");
                    break;
                case "P":
                    displayTransactions("PAYMENTS");
                    break;
                case "R":
                    showReportScreen(scanner);
                    break;
                case "H":
                    return;
                default:
                    System.err.println("Invalid choice");
            }
        }
    }

    private static void showReportScreen(Scanner scanner) {

        while (true) {
            System.out.println("""
                    Reports
                    1) Month To Date
                    2) Previous Month
                    3) Year to Date
                    4) Previous Year
                    5) Search by Vendor
                    0) Back
                    """);

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    displayMonthToDate();
                    break;
                case "2":
                    displayPreviousMonth();
                    break;
                case "3":
                    displayYearToDate();
                    break;
                case "4":
                    displayPreviousYear();
                    break;
                case "5":
                    break;
                case "0":
                    return;
                default:
                    System.err.println("Invalid choice");

            }
        }
    }

    private static void displayPreviousMonth() {
        ArrayList<Transaction> transactionList = loadTransactions();
        LocalDate today = LocalDate.now();
        int previusMonth = today.getMonthValue() - 1;

        for (Transaction currentTransaction : transactionList) {
            LocalDate transactionMonth = currentTransaction.getDate();
            int currentMonth = transactionMonth.getMonthValue() - 1;

            if (currentMonth == previusMonth) {
                System.out.println(currentTransaction.getDate() + " " + currentTransaction.getTime() + " " +currentTransaction.getDescription());

            }


        }
    }

    private static void displayMonthToDate() {
        ArrayList<Transaction> transactionList = loadTransactions();
        LocalDate today = LocalDate.now();
        int currentYear = today.getYear();
        int currentMonth = today.getMonthValue();

        for (Transaction currentTransaction : transactionList) {

            LocalDate transactionDate = currentTransaction.getDate();
            int transactionYear = transactionDate.getYear();
            int transactionMonth = transactionDate.getMonthValue();

            if (transactionYear == currentYear && transactionMonth == currentMonth) {
                System.out.println(currentTransaction.getDate() + " " + currentTransaction.getTime() + " " + currentTransaction.getDescription());
            }
        }

    }

    private static void displayYearToDate() {
        ArrayList<Transaction> transactionList = loadTransactions();
        LocalDate today = LocalDate.now();
        int currentYear = today.getYear();

        for (Transaction currentTransaction : transactionList) {
            LocalDate transactionDate = currentTransaction.getDate();
            int transactionYear = transactionDate.getYear();

            if (transactionYear == currentYear) {
                System.out.println(currentTransaction.getDate() + " " + currentTransaction.getTime() + " " + currentTransaction.getDescription());
            }
        }

    }

    private static void displayPreviousYear() {
        ArrayList<Transaction> transactionList = loadTransactions();
        LocalDate today = LocalDate.now();
        int currentYear = today.getYear() - 1;

        for (Transaction currentTransaction : transactionList) {
            LocalDate transactionDate = currentTransaction.getDate();
            int transactionYear = transactionDate.getYear();

            if (transactionYear == currentYear) {
                System.out.println(currentTransaction.getDate() + " " + currentTransaction.getTime() + " " + currentTransaction.getDescription());
            }
        }

    }

    public static ArrayList<Transaction> loadTransactions() {
        ArrayList<Transaction> transactionList = new ArrayList<>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/transactions.csv"));

            String line;
            LocalDate today = LocalDate.now();

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("date|time|description|vendor|amount")) {
                    continue;
                }

                String[] parts = line.split("\\|");

                LocalDate date = LocalDate.parse(parts[0]);
                LocalTime time = LocalTime.parse(parts[1]);
                String description = parts[2];
                String vendor = parts[3];
                double amount = Double.parseDouble(parts[4]);

                Transaction transaction = new Transaction(date, time, description, vendor, amount);
                transactionList.add(transaction);
            }
        } catch (FileNotFoundException e) {
            System.err.println("Could not find file");
        } catch (IOException e) {
            System.err.println("Problem reading file");
        }

        return transactionList;
    }

    private static void displayReport(String reportType, String vendorSearch) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/transactions.csv"));

            String line;
            LocalDate today = LocalDate.now();

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("date|time|description|vendor|amount")) {
                    continue;
                }

                String[] parts = line.split("\\|");

                LocalDate date = LocalDate.parse(parts[0]);
                String time = parts[1];
                String description = parts[2];
                String vendor = parts[3];
                double amount = Double.parseDouble(parts[4]);

                boolean shouldDisplay = switch (reportType) {
                    case "MONTH_TO_DATE" -> date.getMonth() == today.getMonth()
                            && date.getYear() == today.getYear();
                    case "PREVIOUS_MONTH" -> {
                        LocalDate previousMonth = today.minusMonths(1);
                        yield date.getMonth() == previousMonth.getMonth()
                                && date.getYear() == previousMonth.getYear();
                    }
                    case "YEAR_TO_DATE" -> date.getYear() == today.getYear();
                    case "PREVIOUS_YEAR" -> date.getYear() == today.minusYears(1).getYear();
                    case "VENDOR" -> vendor.equalsIgnoreCase(vendorSearch);
                    default -> false;
                };

                if (shouldDisplay) {
                    System.out.printf("%s | %s | %s |%s | %.2f%n ", date, time, description, vendor, amount);
                }
            }
            reader.close();
        } catch (IOException e) {
            System.err.println("Could not read transaction file");
        }


    }

    private static void displayTransactions(String filter) {
        ArrayList<Transaction> transactionList = loadTransactions();
        for (Transaction currentTransaction : transactionList) {
            System.out.println(currentTransaction.csvString());
        }
    }

    private static void addTransaction(Scanner scanner, boolean isDeposit) {

        String description = requiredInput(scanner, "Description: ");
        String vendor = requiredInput(scanner, "Vendor: ");

        double amount;

        while (true) {
            System.out.println("Amount: ");
            String amountInput = scanner.nextLine().trim();

            try {
                amount = Double.parseDouble(amountInput);

                if (amount * 100 != Math.round(amount * 100)) {
                    System.err.println("Only 2 decimal places allowed.");
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                System.err.println("Please enter a valid number, like 25.50");
            }

        }


        if (isDeposit) {
            amount = Math.abs(amount);
        } else {
            amount = -Math.abs(amount);
        }

        LocalDate date;
        LocalTime time;
        while (true) {
            try {
                System.out.println("Enter date (yyyy-MM-dd) or press Enter for today: ");
                String input = scanner.nextLine().trim();
                System.out.println("Enter time (HH:mm:ss) or press enter for current time");
                String timeInput = scanner.nextLine().trim();

                if (timeInput.isEmpty()) {
                    time = LocalTime.now().withNano(0);
                } else {
                    time = LocalTime.parse(timeInput);
                }

                if (input.isEmpty()) {
                    date = LocalDate.now();
                } else {
                    date = LocalDate.parse(input);
                }
                break;

            } catch (Exception e) {
                System.err.println("Invalid date or time. Use yyyy-MM-dd and HH:mm:ss.");
            }
        }

        Transaction transaction = new Transaction(date, time, description, vendor, amount);

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/resources/transactions.csv", true));

            writer.write(transaction.csvString());

            writer.newLine();

            writer.close();

            System.out.println("Transaction saved!");


        } catch (IOException e) {

            System.err.println("There was a problem saving the transaction.");
        }

    }

    // requiredInput is a method for defensing coding and avoid empty spaces
    private static String requiredInput(Scanner scanner, String prompt) {
        while (true) {
            System.out.println(prompt);
            String input = scanner.nextLine().trim();

            if (!input.isEmpty()) {
                return input;
            }
            System.err.println(prompt + "cannot be blank.");
        }
    }

    // this for the header in the transactions file
    private static void createFileHeader() {
        try {
            File file = new File("src/main/resources/transactions.csv");

            if (!file.exists() || file.length() == 0) {

                BufferedWriter writer = new BufferedWriter(new FileWriter(file));

                writer.write("date|time|description|vendor|amount");
                writer.newLine();
                writer.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}




