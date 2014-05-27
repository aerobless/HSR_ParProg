package exercise01;
import java.util.Date;


public class Account {
	// TODO: replace monitor synchronization with software transactions
	
	private int balance = 0;
	private Date lastUpdate = new Date();
	private boolean isClosed = false;
	
	public synchronized void withdraw(int amount) {
		if (isClosed) {
			throw new RuntimeException("Closed account");
		}
		while (balance < amount) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		balance -= amount;
		lastUpdate = new Date();
	}

	public synchronized void deposit(int amount) {
		if (isClosed) {
			throw new RuntimeException("Closed account");
		}
		balance += amount;
		lastUpdate = new Date();
		notifyAll();
	}

	public synchronized void setClosed(boolean isClosed) {
		this.isClosed = isClosed;
	}

	public synchronized int getBalance() {
		return balance;
	}

	public synchronized Date getLastUpdate() {
		return lastUpdate;
	}

	public static void transfer(Account from, Account to, int amount) {
		// TODO: make transfer atomic
		from.withdraw(amount);
		to.deposit(amount);
	}
}
