package exercise01;
import java.util.HashMap;
import java.util.Map;


public class Bank {
	// TODO: replace monitor synchronization with software transactions
	
	private final Map<String, Account> accounts = new HashMap<>();

	public synchronized Account openAccount(String name) {
		if (getAccount(name) != null) {
			throw new RuntimeException("Account already exists");
		}
		Account account = new Account();
		accounts.put(name, account);
		return account;
	}

	public synchronized int nofAccounts() {
		return accounts.size();
	}
	
	public synchronized Account getAccount(String name) {
		return accounts.get(name);
	}
}
