import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class BankAccount2 {
	private int balance = 0;

	public synchronized void deposit(int amount) {
		balance += amount;
	}

	public synchronized boolean withdraw(int amount, long timeOutMillis){
		if (amount <= balance) {
			balance -= amount;
			return true;
		}
		while(amount > balance){
			try {
				wait(timeOutMillis);
			} catch (InterruptedException anEx) {
				// TODO Auto-generated catch block
				anEx.printStackTrace();
			}
		}
		if (amount <= balance) {
			balance -= amount;
			return true;
		} else {
			System.out.println("withdrawl false");
			return false;
		}
	}

	public synchronized int getBalance() {
		return balance;
	}
}

class BankCreditCustomer extends Thread {
	private static final int NOF_TRANSACTIONS = 100;
	private final BankAccount2 account;
	private final int amount;

	public BankCreditCustomer(BankAccount2 account, int amount) {
		this.account = account;
		this.amount = amount;
	}
	
	@Override
	public void run() {
		for (int i = 0; i < NOF_TRANSACTIONS; i++) {
			account.withdraw(amount, 100);
			System.out.println("Use credit " + amount + " by " + Thread.currentThread().getName());
			account.deposit(amount);
		}
	}
}

public class BankTest2 {
	private final static int NOF_CUSTOMERS = 10;
	private final static int START_BUDGET = 1000;

	public static void main(String[] args) throws InterruptedException {
		BankAccount2 account = new BankAccount2();
		List<BankCreditCustomer> customers = new ArrayList<>();
		Random random = new Random(4711);
		for (int i = 0; i < NOF_CUSTOMERS; i++) {
			customers.add(new BankCreditCustomer(account, random.nextInt(1000)));
		}
		for (BankCreditCustomer customer: customers) {
			customer.start();
		}
		account.deposit(START_BUDGET);
		for (BankCreditCustomer customer: customers) {
			customer.join();
		}
	}
}
