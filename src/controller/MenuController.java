package controller;

import helper.PersistenceManager;
import helper.Validators;
import models.CustomException;
import models.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import static helper.Prompt.*;

public class MenuController {
	static List<User> userData = new ArrayList<>();
	static Scanner scanner = new Scanner(System.in);
	private static User loggedUser;

	static void transferFunds(String userId, String amount) {
		Double validatedAmount = Validators.validateAmount(amount);
		Optional<User> userFound = userData.stream()
		                                   .filter(user -> user.getId().equals(userId))
		                                   .findFirst();

		if (validatedAmount <= 0) {
			promptError("amount must be greater than zero");
		} else if (userFound.isPresent()) {
			userFound.get().deposit(validatedAmount);
			loggedUser.withdraw(validatedAmount);
			promptFeedback("Transferred " + validatedAmount + " to " + userId);
		} else {
			promptError("Target user not found.");
		}
	}

	static void loggingMenu() {
		String userId;
		String password;
		promptHeader("""
				    
				+---------------------+
				| Enter Login Details |
				+---------------------+
				""");
		prompt("User Id:");
		userId = scanner.nextLine().trim();
		prompt("Password:");
		password = scanner.nextLine();
		Optional<User> userFound = userData.stream()
		                                   .filter(user1 -> user1.getId().equals(userId))
		                                   .findFirst()
		                                   .filter(user1 -> user1.checkPassword(password));
		if (userFound.isPresent()) {
			loggedUser = userFound.get();
			loggedMenu();
		} else {
			promptError("Bad Credentials");
		}
	}

	static public void welcomeMenu() {
		if (PersistenceManager.loadData(userData)) {
			System.out.println("loading data from files");
		} else {
			System.out.println("creating new users, not loading data from files");
			userData.add(new User("miguel", "usa", 16039331308L, "1", "pass", 100.0));
			userData.add(new User("angel", "usa", 16039331308L, "2", "pass", 2000.0));
			PersistenceManager.updateAccount(userData);
			PersistenceManager.updateTransactions(userData);
		}
		String option;
		try {
			do {
				promptHeader(
						"""
								      
								+---------------------------+
								| DOLLARSBANK Welcomes You! |
								+---------------------------+
								""");
				System.out.println("""
						1. Create New Account
						2. Login
						3. Exit.
						""");
				promptOptions("Enter Choice (1,2 or 3)");
				option = scanner.nextLine();
				switch (option) {
					case "2" -> loggingMenu();
					case "1" -> createUserMenu();
					default -> promptError("Not a valid option");
				}
			} while (!option.equals("3"));
			promptFeedback("Bye!");
		} catch (NumberFormatException e) {
			promptError("Invalid choice");
		}
	}

	static void createUserMenu() {
		try {
			promptHeader(
					"""
							       
							+-------------------------------+
							| Enter Details For New Account |
							+-------------------------------+
							""");
			String name;
			do {
				prompt("Customer Name: ");
				name = scanner.nextLine();
				try {
					if (name.isBlank()) {
						throw new CustomException("Name cannot be blank");
					}
				} catch (RuntimeException e) {
					System.out.println("Try again");
				}
			} while (name.isBlank());
			String address;
			do {
				prompt("Customer Address: ");
				address = scanner.nextLine();
			} while (address.isBlank());
			long contactNumber;
			do {
				prompt("Customer Contact Number: ");
				contactNumber = Validators.validateContactNumber(scanner.nextLine());
			} while (contactNumber == 0);
			String userId;
			List<String> ids = userData.stream()
			                           .map(User::getId)
			                           .toList();
			do {
				prompt("User Id: ");
				userId = scanner.nextLine();
				try {
					if (ids.contains(userId)) {
						throw new CustomException("User ID already exists");
					} else if (null == userId || userId.equals("") || userId.isBlank()) {
						throw new CustomException("User ID cannot be empty");
					}
				} catch (RuntimeException e) {
					System.out.println("Try again with different user id");
				}
			} while (null == userId || userId.isEmpty() || ids.contains(userId));
			String password;
			do {
				prompt("Password: 8 Characters With Lower, Upper & Special characters ");
				password = scanner.nextLine();
				try {
					if (!Validators.validatePassword(password)) {
						throw new CustomException("Password doesn't meet the security requirements");
					}
				} catch (RuntimeException e) {
					System.out.println("try again please");
				}
			} while (!Validators.validatePassword(password));
			Double amount;
			prompt("Initial Deposit Amount: ");
			amount = Validators.validateAmount(scanner.nextLine());
			User newUser = new User(name, address, contactNumber, userId, password, amount);
			userData.add(newUser);
			PersistenceManager.updateAccounts(newUser);
		} catch (Exception e) {
			promptError("Error");
		}
	}


	public static void loggedMenu() {
		try {
			String option;
			do {
				promptHeader("""
						      
						+---------------------+
						| WELCOME Customer!!! |
						+---------------------+
						""");
				System.out.println("""
						1. Deposit Amount
						2. Withdraw Amount
						3. Funds Transfer
						4. View 5 Recent Transactions
						5. Display Customer Information
						6. Sign Out
						""");
				option = scanner.nextLine();
				switch (option) {
					case "1" -> {
						prompt("Amount to deposit");
						Double deposit =
								loggedUser.deposit(Validators.validateAmount(scanner.nextLine()));
						promptFeedback("You new balance is " + deposit);
					}
					case "2" -> {
						prompt("Amount to withdraw");
						Double withdraw =
								loggedUser.withdraw(Double.parseDouble(String.valueOf(Validators.validateAmount(scanner.nextLine()))));
						promptFeedback("You new balance is " + withdraw);
					}
					case "3" -> {
						prompt("Id of the destination account");
						String destinationAccountId = scanner.nextLine();
						prompt("Amount to Transfer");
						String amountToTransfer = scanner.nextLine();
						transferFunds(destinationAccountId, amountToTransfer);
					}
					case "4" -> {
						prompt("Last 5 transactions");
						loggedUser.recentTransactions();
					}
					case "5" -> loggedUser.showInformation();
					case "6" -> {
						promptFeedback("Signing out");
						loggedUser = null;
					}
					default -> promptError("Not a valid option");
				}
			} while (!option.equals("6"));
		} catch (Exception e) {

		}
	}
}
