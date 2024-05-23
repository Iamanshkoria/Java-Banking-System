import java.io.*;
import java.util.*;

class Customer {
    String name;
    String address;
    String contactInfo;
    double balance;
    String password;

    public Customer(String name, String address, String contactInfo, double initialDeposit, String password) {
        this.name = name;
        this.address = address;
        this.contactInfo = contactInfo;
        this.balance = initialDeposit;
        this.password = password;
    }

    public boolean authenticate(String password) {
        return this.password.equals(password);
    }
}

class BankingManagementSystem {
    private Map<Integer, Customer> accounts;
    private Map<Integer, String> loggedInUsers;
    private Map<String, String> adminCredentials;
    private Scanner scanner;

    public BankingManagementSystem() {
        this.accounts = new HashMap<>();
        this.loggedInUsers = new HashMap<>();
        this.adminCredentials = new HashMap<>();
        adminCredentials.put("Ansh", "58");
        adminCredentials.put("Jatin", "50");
        adminCredentials.put("Ravi", "57");
        adminCredentials.put("Pratap", "43");
        this.scanner = new Scanner(System.in);
        loadAccountsFromFile("acc.txt");
    }

    public boolean adminLogin(String username, String password) {
        if (adminCredentials.containsKey(username) && adminCredentials.get(username).equals(password)) {
            System.out.println("Admin login successful");
            return true;
        } else {
            System.out.println("Invalid admin credentials");
            return false;
        }
    }

    public void createAccount() {
        try {
            System.out.println("Enter account number:");
            int accountNumber = scanner.nextInt();
            scanner.nextLine();
            if (!accounts.containsKey(accountNumber)) {
                System.out.println("Enter name:");
                String name = scanner.nextLine();
                System.out.println("Enter address:");
                String address = scanner.nextLine();
                System.out.println("Enter contact info:");
                String contactInfo = scanner.nextLine();
                System.out.println("Enter initial deposit amount:");
                double initialDeposit = scanner.nextDouble();
                scanner.nextLine();
                System.out.println("Set password:");
                String password = scanner.nextLine();
                Customer newCustomer = new Customer(name, address, contactInfo, initialDeposit, password);
                accounts.put(accountNumber, newCustomer);
                System.out.println("Account created successfully for " + name);
            } else {
                System.out.println("Account already exists with account number " + accountNumber);
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input format");
            scanner.nextLine();
        }
    }

    public int login() {
        System.out.println("Enter account number:");
        int accountNumber = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Enter password:");
        String password = scanner.nextLine();
        if (accounts.containsKey(accountNumber)) {
            Customer customer = accounts.get(accountNumber);
            if (customer.authenticate(password)) {
                loggedInUsers.put(accountNumber, customer.name);
                System.out.println("Login successful for " + customer.name);
                return accountNumber;
            }
        }
        System.out.println("Invalid account number or password");
        return 0;
    }

    public void deposit(int loggedInAccount) {
        if (loggedInAccount != 0) {
            Customer customer = accounts.get(loggedInAccount);
            System.out.println("Enter deposit amount:");
            double amount = scanner.nextDouble();
            scanner.nextLine();
            if (amount > 0) {
                customer.balance += amount;
                System.out.println("Deposit of $" + amount + " successful for " + customer.name);
            } else {
                System.out.println("Invalid deposit amount");
            }
        } else {
            System.out.println("User not logged in");
        }
    }

    public void withdraw(int loggedInAccount) {
        if (loggedInAccount != 0) {
            Customer customer = accounts.get(loggedInAccount);
            System.out.println("Enter withdrawal amount:");
            double amount = scanner.nextDouble();
            scanner.nextLine();
            if (amount > 0 && customer.balance >= amount) {
                customer.balance -= amount;
                System.out.println("Withdrawal of $" + amount + " successful for " + customer.name);
            } else {
                System.out.println("Invalid withdrawal amount or insufficient funds in account " + loggedInAccount);
            }
        } else {
            System.out.println("User not logged in");
        }
    }

    public void transfer(int senderAccountNumber) {
        if (loggedInUsers.containsKey(senderAccountNumber)) {
            Customer sender = accounts.get(senderAccountNumber);
            System.out.println("Enter recipient account number:");
            int recipientAccountNumber = scanner.nextInt();
            scanner.nextLine();
            if (accounts.containsKey(recipientAccountNumber)) {
                Customer recipient = accounts.get(recipientAccountNumber);
                System.out.println("Enter transfer amount:");
                double amount = scanner.nextDouble();
                scanner.nextLine();
                if (amount > 0 && sender.balance >= amount) {
                    sender.balance -= amount;
                    recipient.balance += amount;
                    System.out.println("$" + amount + " transferred successfully from "
                            + sender.name + " to " + recipient.name);
                } else {
                    System.out.println("Invalid transfer amount or insufficient funds in account " + senderAccountNumber);
                }
            } else {
                System.out.println("Recipient account not found");
            }
        } else {
            System.out.println("User not logged in");
        }
    }

    public void checkBalance(int accountNumber) {
        if (loggedInUsers.containsKey(accountNumber)) {
            Customer customer = accounts.get(accountNumber);
            System.out.println("Account balance for " + customer.name + ": $" + customer.balance);
        } else {
            System.out.println("User not logged in");
        }
    }

    public void displayAccountInfo(int accountNumber) {
        if (loggedInUsers.containsKey(accountNumber)) {
            Customer customer = accounts.get(accountNumber);
            System.out.println("Account Information for " + customer.name + ":");
            System.out.println("Account Number: " + accountNumber);
            System.out.println("Name: " + customer.name);
            System.out.println("Address: " + customer.address);
            System.out.println("Contact Info: " + customer.contactInfo);
            System.out.println("Balance: $" + customer.balance);
        } else {
            System.out.println("User not logged in");
        }
    }

    public void editAccount(int accountNumber, boolean isAdmin) {
        if (!isAdmin && accounts.containsKey(accountNumber) && loggedInUsers.containsKey(accountNumber)) {
            Customer customer = accounts.get(accountNumber);
            System.out.println("Enter new name (leave blank to keep unchanged):");
            String newName = scanner.nextLine();
            if (!newName.isEmpty()) {
                customer.name = newName;
            }
            System.out.println("Enter new address (leave blank to keep unchanged):");
            String newAddress = scanner.nextLine();
            if (!newAddress.isEmpty()) {
                customer.address = newAddress;
            }
            System.out.println("Enter new contact info (leave blank to keep unchanged):");
            String newContactInfo = scanner.nextLine();
            if (!newContactInfo.isEmpty()) {
                customer.contactInfo = newContactInfo;
            }
            System.out.println("Account information updated successfully");
        } else {
            System.out.println("Insufficient permissions or account not found");
        }
    }

    public void deleteAccount(int accountNumber, boolean isAdmin) {
        if (isAdmin || (accounts.containsKey(accountNumber) && loggedInUsers.containsKey(accountNumber))) {
            accounts.remove(accountNumber);
            loggedInUsers.remove(accountNumber);
            System.out.println("Account deleted successfully");
        } else {
            System.out.println("Insufficient permissions or account not found");
        }
    }

    public void saveAccountsToFile(String fileName) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            for (Map.Entry<Integer, Customer> entry : accounts.entrySet()) {
                int accountNumber = entry.getKey();
                Customer customer = entry.getValue();
                writer.println("Account Number: " + accountNumber);
                writer.println("Name: " + customer.name);
                writer.println("Address: " + customer.address);
                writer.println("Contact Info: " + customer.contactInfo);
                writer.println("Balance: " + customer.balance);
                writer.println("Password: " + customer.password);
                writer.println();
            }
            System.out.println("Accounts data saved successfully to " + fileName);
        } catch (IOException e) {
            System.out.println("Error saving accounts data: " + e.getMessage());
        }
    }

    public void loadAccountsFromFile(String fileName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Account Number: ")) {
                    int accountNumber = Integer.parseInt(line.substring("Account Number: ".length()));
                    String name = reader.readLine().substring("Name: ".length());
                    String address = reader.readLine().substring("Address: ".length());
                    String contactInfo = reader.readLine().substring("Contact Info: ".length());
                    double balance = Double.parseDouble(reader.readLine().substring("Balance: ".length()));
                    String password = reader.readLine().substring("Password: ".length());
                    reader.readLine();
                    Customer customer = new Customer(name, address, contactInfo, balance, password);
                    accounts.put(accountNumber, customer);
                }
            }
            System.out.println("Accounts data loaded successfully from " + fileName);
        } catch (IOException e) {
            System.out.println("Error loading accounts data: " + e.getMessage());
        }
    }

    public void displayAllAccounts() {
        for (Map.Entry<Integer, Customer> entry : accounts.entrySet()) {
            int accountNumber = entry.getKey();
            Customer customer = entry.getValue();
            System.out.println("Account Number: " + accountNumber);
            System.out.println("Name: " + customer.name);
            System.out.println("Address: " + customer.address);
            System.out.println("Contact Info: " + customer.contactInfo);
            System.out.println("Balance: $" + customer.balance);
            System.out.println("Password: " + customer.password);
            System.out.println();
        }
    }

    public void searchAccount() {
        System.out.println("Enter account number to search:");
        int accountNumber = scanner.nextInt();
        scanner.nextLine();
        if (accounts.containsKey(accountNumber)) {
            Customer customer = accounts.get(accountNumber);
            System.out.println("Account Information for " + customer.name + ":");
            System.out.println("Account Number: " + accountNumber);
            System.out.println("Name: " + customer.name);
            System.out.println("Address: " + customer.address);
            System.out.println("Contact Info: " + customer.contactInfo);
            System.out.println("Balance: $" + customer.balance);
        } else {
            System.out.println("Account not found");
        }
    }

    public void logout(int accountNumber) {
        if (loggedInUsers.containsKey(accountNumber)) {
            loggedInUsers.remove(accountNumber);
            System.out.println("Logged out successfully");
        } else {
            System.out.println("User not logged in");
        }
    }

    public void changePassword(int accountNumber) {
        if (loggedInUsers.containsKey(accountNumber)) {
            Customer customer = accounts.get(accountNumber);
            System.out.println("Enter current password:");
            String currentPassword = scanner.nextLine();
            if (customer.authenticate(currentPassword)) {
                System.out.println("Enter new password:");
                String newPassword = scanner.nextLine();
                customer.password = newPassword;
                System.out.println("Password changed successfully");
            } else {
                System.out.println("Incorrect current password");
            }
        } else {
            System.out.println("User not logged in");
        }
    }

    public void blockAccount(int accountNumber) {
        if (adminCredentials.containsKey(loggedInUsers.get(accountNumber))) {
		accounts.remove(accountNumber);
		loggedInUsers.remove(accountNumber);

            System.out.println("Account " + accountNumber + " blocked");
        } else {
            System.out.println("Insufficient permissions or account not found");
        }
    }
}

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        BankingManagementSystem bankingSystem = new BankingManagementSystem();
        boolean exit = false;

        while (!exit) {
            System.out.println("╔═════════════════════════════════╗");
            System.out.println("║    Welcome to the Banking       ║");
            System.out.println("║     Management System           ║");
            System.out.println("╠═════════════════════════════════╣");
            System.out.println("║ 1. Admin Login                  ║");
            System.out.println("║ 2. User Login                   ║");
            System.out.println("║ 3. Create Account               ║");
            System.out.println("║ 4. Exit                         ║");
            System.out.println("╚═════════════════════════════════╝");
            System.out.println("Enter your choice:");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.println("Enter admin username:");
                    String adminUsername = scanner.nextLine();
                    System.out.println("Enter admin password:");
                    String adminPassword = scanner.nextLine();
                    if (bankingSystem.adminLogin(adminUsername, adminPassword)) {
                        boolean adminExit = false;
                        while (!adminExit) {
                            System.out.println("╔════════════════════════════════╗");
                            System.out.println("║          Admin Menu            ║");
                            System.out.println("╠════════════════════════════════╣");
                            System.out.println("║ 1. Display All Accounts        ║");
                            System.out.println("║ 2. Search Account              ║");
                            System.out.println("║ 3. Delete Account              ║");
                            System.out.println("║ 4. Logout                      ║");
                            System.out.println("╚════════════════════════════════╝");
                            System.out.println("Enter your choice:");
                            int adminChoice = scanner.nextInt();
                            scanner.nextLine();

                            switch (adminChoice) {
                                case 1:
                                    bankingSystem.displayAllAccounts();
                                    break;
                                case 2:
                                    bankingSystem.searchAccount();
                                    break;
                                case 3:
                                    System.out.println("Enter account number to delete:");
                                    int accountNumberToDelete = scanner.nextInt();
                                    scanner.nextLine();
                                    bankingSystem.deleteAccount(accountNumberToDelete, true);
                                    break;
                                case 4:
                                    adminExit = true;
                                    break;
                                default:
                                    System.out.println("Invalid choice");
                            }
                        }
                    }
                    break;
                case 2:
                    int loggedInAccount = bankingSystem.login();
                    if (loggedInAccount != 0) {
                        boolean userExit = false;
                        while (!userExit) {
                            System.out.println("╔════════════════════════════════╗");
                            System.out.println("║           User Menu            ║");
                            System.out.println("╠════════════════════════════════╣");
                            System.out.println("║ 1. Deposit                     ║");
                            System.out.println("║ 2. Withdraw                    ║");
                            System.out.println("║ 3. Transfer                    ║");
                            System.out.println("║ 4. Check Balance               ║");
                            System.out.println("║ 5. Display Account Info        ║");
                            System.out.println("║ 6. Edit Account                ║");
                            System.out.println("║ 7. Delete Account              ║");
                            System.out.println("║ 8. Logout                      ║");
                            System.out.println("║ 9. Change Password             ║");
                            System.out.println("╚════════════════════════════════╝");
                            System.out.println("Enter your choice:");
                            int userChoice = scanner.nextInt();
                            scanner.nextLine();

                            switch (userChoice) {
                                case 1:
                                    bankingSystem.deposit(loggedInAccount);
                                    break;
                                case 2:
                                    bankingSystem.withdraw(loggedInAccount);
                                    break;
                                case 3:
                                    bankingSystem.transfer(loggedInAccount);
                                    break;
                                case 4:
                                    bankingSystem.checkBalance(loggedInAccount);
                                    break;
                                case 5:
                                    bankingSystem.displayAccountInfo(loggedInAccount);
                                    break;
                                case 6:
                                    bankingSystem.editAccount(loggedInAccount, false);
                                    break;
                                case 7:
                                    bankingSystem.deleteAccount(loggedInAccount, false);
                                    userExit = true;
                                    break;
                                case 8:
                                    bankingSystem.logout(loggedInAccount);
                                    userExit = true;
                                    break;
                                case 9:
                                    bankingSystem.changePassword(loggedInAccount);
                                    break;
                                default:
                                    System.out.println("Invalid choice");
                            }
                        }
                    }
                    break;
                case 3:
                    bankingSystem.createAccount();
                    break;
                case 4:
                    exit = true;
                    bankingSystem.saveAccountsToFile("acc.txt");
                    break;
                default:
                    System.out.println("Invalid choice");
            }
        }

        scanner.close();
    }
}

