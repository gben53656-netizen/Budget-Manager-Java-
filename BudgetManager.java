import java.util.ArrayList;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;

class Expense {
    String category;
    double amount;
    String date;
    String note;

    Expense(String category, double amount, String date, String note) {
        this.category = category;
        this.amount = amount;
        this.date = date;
        this.note = note;
    }

    public String toString() {
        return date + " | " + category + " | $" + amount + " | " + note;
    }
}

public class BudgetManager {
    static ArrayList<Expense> expenses = new ArrayList<>();
    static double monthlyBudget = 500.0;
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        loadFromFile(); // load old data if exists
        System.out.println("--- Personal Budget Manager ---");

        while (true) {
            System.out.println("\n1. Add Expense");
            System.out.println("2. View All Expenses");
            System.out.println("3. View Summary by Category");
            System.out.println("4. Check Budget Status");
            System.out.println("5. Set Monthly Budget");
            System.out.println("6. Save & Exit");
            System.out.print("Choose option: ");

            int choice = sc.nextInt();
            sc.nextLine(); // fix for nextLine bug

            if (choice == 1) addExpense();
            else if (choice == 2) viewAll();
            else if (choice == 3) viewByCategory();
            else if (choice == 4) checkBudget();
            else if (choice == 5) setBudget();
            else if (choice == 6) {
                saveToFile();
                System.out.println("Data saved to expenses.txt. Bye!");
                break;
            } else {
                System.out.println("Invalid choice!");
            }
        }
    }

    static void addExpense() {
        System.out.print("Enter Category (Food, Rent, Transport, Shopping, Other): ");
        String cat = sc.nextLine();
        System.out.print("Enter Amount: ");
        double amt = sc.nextDouble();
        sc.nextLine();
        System.out.print("Enter Date (YYYY-MM-DD): ");
        String date = sc.nextLine();
        System.out.print("Enter Note: ");
        String note = sc.nextLine();

        expenses.add(new Expense(cat, amt, date, note));
        System.out.println("Expense added!");
        checkBudget();
    }

    static void viewAll() {
        if (expenses.isEmpty()) {
            System.out.println("No expenses yet.");
            return;
        }
        for (Expense e : expenses) {
            System.out.println(e);
        }
    }

    static void viewByCategory() {
        System.out.println("\n--- Spending by Category ---");
        double food = 0, rent = 0, transport = 0, shopping = 0, other = 0;
        for (Expense e : expenses) {
            if (e.category.equalsIgnoreCase("Food")) food += e.amount;
            else if (e.category.equalsIgnoreCase("Rent")) rent += e.amount;
            else if (e.category.equalsIgnoreCase("Transport")) transport += e.amount;
            else if (e.category.equalsIgnoreCase("Shopping")) shopping += e.amount;
            else other += e.amount;
        }
        System.out.println("Food: $" + food);
        System.out.println("Rent: $" + rent);
        System.out.println("Transport: $" + transport);
        System.out.println("Shopping: $" + shopping);
        System.out.println("Other: $" + other);
        System.out.println("TOTAL: $" + (food+rent+transport+shopping+other));
    }

    static void checkBudget() {
        double total = 0;
        for (Expense e : expenses) total += e.amount;

        System.out.println("\nBudget: $" + monthlyBudget + " | Spent: $" + total);
        if (total > monthlyBudget) {
            System.out.println("ALERT: You are OVER budget by $" + (total - monthlyBudget));
        } else {
            System.out.println("Remaining: $" + (monthlyBudget - total));
        }
    }

    static void setBudget() {
        System.out.print("Enter new Monthly Budget: ");
        monthlyBudget = sc.nextDouble();
        System.out.println("Budget updated to $" + monthlyBudget);
    }

    static void saveToFile() {
        try {
            FileWriter writer = new FileWriter("expenses.txt");
            writer.write(monthlyBudget + "\n");
            for (Expense e : expenses) {
                writer.write(e.category + "," + e.amount + "," + e.date + "," + e.note + "\n");
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("Error saving file.");
        }
    }

    static void loadFromFile() {
        try {
            File file = new File("expenses.txt");
            if (!file.exists()) return;
            Scanner fileScanner = new Scanner(file);
            if (fileScanner.hasNextLine()) {
                monthlyBudget = Double.parseDouble(fileScanner.nextLine());
            }
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] parts = line.split(",", 4);
                if (parts.length == 4) {
                    expenses.add(new Expense(parts[0], Double.parseDouble(parts[1]), parts[2], parts[3]));
                }
            }
            fileScanner.close();
        } catch (Exception e) {
            System.out.println("No previous data found.");
        }
    }
}