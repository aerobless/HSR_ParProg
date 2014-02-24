class BankAccount {
	private int balance = 0;

	public synchronized void deposit(int amount) throws InterruptedException {
		balance += amount;
	}

	public synchronized boolean withdraw(int amount) throws InterruptedException {
		if (amount <= balance) {
			balance -= amount;
			return true;
		} else {
			return false;
		}
	}

	public int getBalance() {
		return balance;
	}
}

class BankCustomer extends Thread {
	private final static int NOF_TRANSACTIONS = 10000000;
	private final BankAccount account;
	
	public BankCustomer(BankAccount account) {
		this.account = account;
	}
	
	@Override
	public void run() {
		for (int k = 0; k < NOF_TRANSACTIONS; k++) {
			try {
				account.deposit(100);
			} catch (InterruptedException anEx) {
				// TODO Auto-generated catch block
				anEx.printStackTrace();
			}
			try {
				account.withdraw(100);
			} catch (InterruptedException anEx) {
				// TODO Auto-generated catch block
				anEx.printStackTrace();
			}
		}
	}
}

public class BankTest1 {
	private final static int NOF_CUSTOMERS = 10;

	public static void main(String[] args) {
		BankAccount account = new BankAccount();
		for (int i = 0; i < NOF_CUSTOMERS; i++) {
			new BankCustomer(account).start();
		}
	}
}
