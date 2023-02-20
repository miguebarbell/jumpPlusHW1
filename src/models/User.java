package models;

import menu.Colors;

import java.util.ArrayList;
import java.util.List;

public class User {
	private final String name;
	private final String address;
	private Long contactNumber;
	private final String id;
	private final String password;
	private Double balance;
	private List<Transaction> transactions = new ArrayList<>();

	public User(String name,
	            String address,
	            Long contactNumber,
	            String id,
	            String password,
	            Double balance
	) {
		this.name = name;
		this.address = address;
		this.contactNumber = contactNumber;
		this.id = id;
		this.password = password;
		if (balance < 0) {
			transactions.add(new Transaction("withdraw", balance));
		} else if (balance > 0) {
			transactions.add(new Transaction("deposit", balance));
		}
		this.balance = balance;
	}


	public boolean checkPassword(String pass) {
		return password.equals(pass);
	}


	public String getId() {
		return id;
	}

	public Double withdraw(Double amount) {
		this.balance -= amount;
		transactions.add(new Transaction("withdraw", amount));
		return this.balance;
	}

	public Double deposit(Double amount) {
		this.balance += amount;
		transactions.add(new Transaction("deposit", amount));
		return this.balance;
	}

	public void recentTransactions() {
		transactions.stream()
		            .limit(5)
		            .forEach(transaction -> System.out.println(transaction.toString()));
	}

	public void showInformation() {
		System.out.printf("""
			\n%s
				ID: %s
				Name: %s
				Address: %s
				Contact Number: %s
				Balance: %s
				%s""", Colors.YELLOW, id, name, address, contactNumber, balance, Colors.RESET);
	}


}
