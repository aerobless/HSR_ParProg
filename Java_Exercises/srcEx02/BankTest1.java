import java.util.ArrayList;

class BankAccount {
	private int balance = 0;

	public synchronized void deposit(int amount) throws InterruptedException {
			balance += amount;
			notifyAll();
	}

	public synchronized void withdraw(int amount) throws InterruptedException {
		while (amount > balance) {
		      wait();
		}
		balance -= amount;
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
		int balanceBeforeTransactions = account.getBalance();
		ArrayList<BankCustomer> customerList = new ArrayList<BankCustomer>();
		for (int i = 0; i < NOF_CUSTOMERS; i++) {
			BankCustomer bankMensch = new BankCustomer(account);
			bankMensch.start();
			customerList.add(bankMensch);
		}
		for (int i=0; i<customerList.size(); i++){
			try {
				customerList.get(i).join();
			} catch (InterruptedException anEx) {
				// TODO Auto-generated catch block
				anEx.printStackTrace();
			}
		}
		int balanceAfterTransactions = account.getBalance();
		System.out.println("Balance before Transactions:"+balanceBeforeTransactions);
		System.out.println("Balance after Transactions:"+balanceAfterTransactions);
		System.out.println("Error through Race-Conditions:"+(balanceBeforeTransactions-balanceAfterTransactions));
	}
}
