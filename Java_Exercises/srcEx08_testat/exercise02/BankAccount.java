package exercise02;

public class BankAccount {
	private int balance = 0;

	public synchronized void deposit(int amount) {
		balance += amount;
	}

	public synchronized boolean withdraw(int amount) {
		if (amount <= this.balance) {
			balance -= amount;
			return true;
		} else {
			return false;
		}
	}

	public synchronized int getBalance() {
		return balance;
	}
}
