public class Menu {
	private boolean isLogedIn = false;
	static public void welcomeMenu() {
		System.out.println("""
						    +---------------------------+
						    | DOLLARSBANK Welcomes You! |
						    +---------------------------+
						    1. Create New Account
						    2. Login
						    3. Exit.
						    
						    \u001B[32mEnter Choice (1,2 or 3) :
				""");
	}


}
