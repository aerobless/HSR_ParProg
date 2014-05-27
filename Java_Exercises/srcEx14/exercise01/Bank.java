package exercise01;
import java.util.HashMap;
import java.util.Map;

import scala.concurrent.stm.Ref;
import scala.concurrent.stm.japi.STM;


public class Bank {
	// TODO: replace monitor synchronization with software transactions
	private Ref.View<Map<String, Account>> accounts = STM.newRef(new HashMap<>());

	public Account openAccount(String name) {
		return STM.atomic(()->{
			if (getAccount(name) != null) {
				throw new RuntimeException("Account already exists");
			}
			Account account = new Account();
			accounts.get().put(name, account);
			return account;
		});
	}

	public synchronized int nofAccounts() {
		return accounts.get().size();
	}
	
	public synchronized Account getAccount(String name) {
		return accounts.get().get(name);
	}
}
