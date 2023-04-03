package helper;

import models.User;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class PersistenceManager {
	static String fileName = "account";


	public static boolean loadData(List<User> userData) {
		return false;
	}

	public static boolean updateFile(List<User> userData) {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
			// clear the file
			// add the header
			writer.write("id,name,contact_number,address,balance,password\n");
			for (User user : userData) {
				writer.write("%s, %s, %s, %s, %s, %s, [%s]\n".formatted(
						user.getId(),
						user.getName(),
						user.getContactNumber(),
						user.getAddress(),
						user.getBalance(),
						user.getPassword()
//						user.getTransactions().forEach(transaction -> {
//							return  "%s".formatted(transaction.getDate());
//						});
				));
			}
			return true;
		} catch (IOException e) {
			return false;
		}
	}


}
