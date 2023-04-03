package models;

import helper.Colors;

public class CustomException extends RuntimeException {
	public CustomException(String message) {
		System.out.printf("%s" + message + "\n%s", Colors.RED, Colors.RESET);
	}
}
