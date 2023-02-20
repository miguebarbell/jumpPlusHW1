package models;

import menu.Colors;

import java.time.LocalDateTime;

public class Transaction {
	String type;
	Double amount;
	LocalDateTime date;

	public Transaction(String type, Double amount) {
		this.type = type;
		this.amount = amount;
		this.date = LocalDateTime.now();
	}

	String datePrettified(LocalDateTime date) {
		return date.getMonth() + "-" + date.getDayOfMonth() + "-" + date.getYear();
	}


	@Override
	public String toString() {
		if (type.equals("deposit")) {
			return Colors.GREEN + datePrettified(date) + " " + this.type + " " + this.amount + Colors.RESET;
		} else {
			return Colors.RED + datePrettified(date) + " " + this.type + " " + this.amount + Colors.RESET;
		}
	}
}
