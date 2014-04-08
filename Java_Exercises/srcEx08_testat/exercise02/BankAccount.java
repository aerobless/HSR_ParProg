package exercise02;

import java.util.concurrent.atomic.AtomicInteger;

public class BankAccount {
	private AtomicInteger balance = new AtomicInteger();

	public void deposit(int amount) {
		balance.addAndGet(amount);
	}

	public boolean withdraw(int amount) {
		int sealedBalance = balance.get();
		while(!(balance.compareAndSet(sealedBalance, sealedBalance-amount))){
			sealedBalance = balance.get();
			if(sealedBalance<=amount){
				return false;
			}
		}
		return true;
	}

	public int getBalance() {
		return balance.get();
	}
}
