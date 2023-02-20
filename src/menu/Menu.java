package menu;

import helper.Validators;
import models.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Menu {
	static List<User> userData = new ArrayList<>();
	static Scanner scanner = new Scanner(System.in);
	private static User loggedUser;

	static void transferFunds(String userId, String amount) {
		Double validatedAmount = Validators.validateAmount(amount);
		Optional<User> userFound = userData.stream().filter(user -> user.getId().equals(userId)).findFirst();
		if (validatedAmount <= 0) {
			System.out.println(Colors.RED + "amount must be greater than zero" + Colors.RED);
		} else if (userFound.isPresent()) {
			userFound.get().deposit(validatedAmount);
			loggedUser.withdraw(validatedAmount);
			System.out.println("Transferred " + validatedAmount + " to " + userId);
		}
	}

	static void loggingMenu() {
		String userId;
		String password;
		System.out.printf("""
    
				%s+---------------------+
				| Enter Login Details |
				+---------------------+
				
			 %s""", Colors.BLUE, Colors.RESET);
		System.out.printf("%sUser Id:%s\n", Colors.RESET, Colors.PURPLE);
		userId = scanner.nextLine().trim();
		System.out.printf("%sPassword:%s\n", Colors.RESET, Colors.BLACK);
		password = scanner.nextLine();
		User userFound = userData.stream()
		                         .filter(user1 -> user1.getId().equals(userId))
		                         .findFirst()
		                         .filter(user1 -> user1.checkPassword(password))
		                         .get();
		if (userFound != null) {
			loggedUser = userFound;
			loggedMenu();
		} else {
			System.out.println("Bad Credentials");
		}
	}

	static public void welcomeMenu() {

		userData.add(new User("miguel", "usa", 16039331308L, "1", "pass", 100.0));
		userData.add(new User("angel", "usa", 16039331308L, "2", "pass", 2000.0));


		String option;
		try {
			do {
				System.out.printf("""
      
						%s+---------------------------+
						| DOLLARSBANK Welcomes You! |
						+---------------------------+%s
						1. Create New Account
						2. Login
						3. Exit.

						%sEnter Choice (1,2 or 3) :
						%s""", Colors.BLUE, Colors.RESET, Colors.GREEN, Colors.RESET);
				option = scanner.nextLine();
				switch (option) {
					case "2" -> loggingMenu();
					case "1" -> createUserMenu();
					default -> System.out.println(Colors.RED + "Not a valid option" + Colors.RESET);
				}
			} while (!option.equals("3"));
			System.out.println("Bye!");
		} catch (NumberFormatException e) {
			System.out.println("Invalid choice");
		}
	}

	static void createUserMenu() {
		try {
			System.out.printf(
					"""
       
							%s+-------------------------------+
							| Enter Details For New Account |
							+-------------------------------+
							
							%s""", Colors.BLUE, Colors.RESET);
			String name;
			do {
				System.out.printf("Customer Name: \n%s", Colors.YELLOW);
				name = scanner.nextLine();
			} while (name.isBlank());
			String address;
			do {
				System.out.printf("%sCustomer Address: \n%s", Colors.RESET, Colors.YELLOW);
				address = scanner.nextLine();
			} while (address.isBlank());
			long contactNumber;
			do {
				System.out.printf("%sCustomer Contact Number: \n%s", Colors.RESET, Colors.YELLOW);
				contactNumber = Validators.validateContactNumber(scanner.nextLine());
			} while (contactNumber == 0);
			String userId;
			do {
				System.out.printf("%sUser Id: \n%s", Colors.RESET, Colors.YELLOW);
				userId = scanner.nextLine();
			} while (userId.isBlank());
			String password;
			do {
				System.out.printf("%sPassword: 8 Characters With Lower, Upper & Special characters \n%s", Colors.RESET,
						Colors.YELLOW);
				password = scanner.nextLine();
			} while (!Validators.validatePassword(password));
			Double amount;
			System.out.printf("%sInitial Deposit Amount: \n%s", Colors.RESET, Colors.YELLOW);
			amount = Validators.validateAmount(scanner.nextLine());
			User newUser = new User(name, address, contactNumber, userId, password, amount);
			userData.add(newUser);
		} catch (Exception e) {
			System.out.println("Error");
		}
	}


	public static void loggedMenu() {
		try {
			String option;
			do {
				System.out.printf("""
      
						%s+---------------------+
						| WELCOME Customer!!! |
						+---------------------+
						
					 %s""", Colors.BLUE, Colors.RESET);
				System.out.println("1. Deposit Amount");
				System.out.println("2. Withdraw Amount");
				System.out.println("3. Funds Transfer");
				System.out.println("4. View 5 Recent Transactions");
				System.out.println("5. Display Customer Information");
				System.out.println("6. Sign Out");
				option = scanner.nextLine();
				switch (option) {
					case "1":
						System.out.println("Amount to deposit");
						Double deposit =
						loggedUser.deposit(Validators.validateAmount(scanner.nextLine()));
						System.out.println("You new balance is " + deposit);
						break;
					case "2":
						System.out.println("Amount to withdraw");
						Double withdraw =
								loggedUser.withdraw(Double.parseDouble(String.valueOf(Validators.validateAmount(scanner.nextLine()))));
						System.out.println("You new balance is " + withdraw);
						break;
					case "3":
						System.out.println("Id of the destination account");
						String destinationAccountId = scanner.nextLine();
						System.out.println("Amount to Transfer");
						String amountToTransfer = scanner.nextLine();
						transferFunds(destinationAccountId, amountToTransfer);
						break;
					case "4":
						loggedUser.recentTransactions();
						break;
					case "5":
						loggedUser.showInformation();
						break;
					case "6":
						System.out.println("Signing out");
						loggedUser = null;
						break;
					default:
						System.out.println("Not a valid option");
				}
			} while (!option.equals("6"));
		} catch (Exception e) {

		}
	}
}
