package helper;

import models.Transaction;
import models.User;

import java.io.*;
import java.time.LocalDateTime;
import java.util.List;

public class PersistenceManager {
	static String accountFileName = "accounts";
	static String transactionFileName = "transactions";


	public static boolean loadData(List<User> userData) {
		boolean loadAccountData = false;
		boolean loadTransactionData = false;
		try (BufferedReader reader = new BufferedReader(new FileReader(accountFileName))) {
			reader.lines().skip(1).forEach(line -> {
				String[] splitedLine = line.split(",");
				User newUser = new User(
						splitedLine[1],
						splitedLine[3],
						Long.parseLong(splitedLine[2]),
						splitedLine[0],
						splitedLine[5],
						Double.parseDouble(splitedLine[4]));
				userData.add(newUser);
			});
			loadAccountData = true;
		} catch (IOException e) {
			return false;
		}


		try (BufferedReader reader = new BufferedReader(new FileReader(transactionFileName))) {
			reader.lines().skip(1).forEach(line -> {
				String[] splitedLine = line.split(",");
				Integer userId = Integer.parseInt(splitedLine[0]);
				Transaction transaction = new Transaction(
						splitedLine[1],
						Double.parseDouble(splitedLine[2])
				);
				transaction.setDate(LocalDateTime.parse(splitedLine[3]));
				userData.stream().map(user -> {
					if (user.getId().equals(userId)) {
						user.addTransaction(transaction);
					}
					return user;
				});
			});
			loadTransactionData = true;
		} catch (IOException e) {
			return false;
		}
		return loadAccountData && loadTransactionData;
	}

	public static boolean updateAccount(List<User> userData) {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(accountFileName))) {
			writer.write("id,name,contact_number,address,balance,password\n");
			for (User user : userData) {
				writer.write("%s,%s,%s,%s,%s,%s\n".formatted(user.getId(), user.getName(), user.getContactNumber(), user.getAddress(), user.getBalance(), user.getPassword()));
			}
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	public static boolean updateTransactions(List<User> userData) {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(transactionFileName))) {
			writer.write("user_id,type,amount,date\n");
			for (User user : userData) {
				List<Transaction> transactions = user.getTransactions();
				for (Transaction transaction : transactions) {
					writer.write("%s,%s,%s,%s\n".formatted(user.getId(), transaction.getType(), transaction.getAmount(), transaction.getDate()));
				}
			}
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	public static boolean updateTransactions(String userId, Transaction transaction) {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(transactionFileName, true))) {
			writer.write("%s,%s,%s,%s\n".formatted(userId, transaction.getType(), transaction.getAmount(),
					transaction.getDate()));
		} catch (IOException e) {
			System.out.println("Could not write transaction to file");
			return false;
		}
		return true;
	}

	public static boolean updateAccounts(User user) {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(accountFileName, true))) {
			writer.write("%s,%s,%s,%s,%s,%s\n".formatted(user.getId(), user.getName(), user.getContactNumber(), user.getAddress(), user.getBalance(), user.getPassword()));
		} catch (IOException e) {
			System.out.println("Could not write transaction to file");
			return false;
		}
		user.getTransactions().forEach(transaction -> updateTransactions(user.getId(),transaction));
		return true;
	}
}
