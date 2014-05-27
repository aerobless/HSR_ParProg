package exercise01;
import java.util.Date;

import scala.concurrent.stm.Ref;
import scala.concurrent.stm.japi.STM;


public class Account {
	private Ref.View<Integer> balance = STM.newRef(0);
	private Ref.View<Date> lastUpdate = STM.newRef(new Date());
	private Ref.View<Boolean> isClosed = STM.newRef(false);
	
	public void withdraw(int amount) {
		STM.atomic(() -> {
			if (isClosed.get()) {
				throw new RuntimeException("Closed account");
			}
			if(balance.get()<amount){
				STM.retry();
			}
			while (balance.get() < amount) {
				try {
					wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			balance.set(balance.get()-amount);
			lastUpdate.set(new Date());	
		});
	}

	public void deposit(int amount) {
		STM.atomic(()-> {
			if (isClosed.get()) {
				System.out.println("dd");
				throw new RuntimeException("Closed account");
			}
			balance.set(balance.get() + amount);
			lastUpdate.set(new Date());
		});
	}

	public void setClosed(boolean isClosed) {
		STM.atomic(()-> {
			this.isClosed.set(isClosed);
		});
	}

	public int getBalance() {
		return balance.get();
	}

	public Date getLastUpdate() {
		return lastUpdate.get();
	}

	public static void transfer(Account from, Account to, int amount) {
		STM.atomic(() -> {
			from.withdraw(amount);
			to.deposit(amount);
		});
	}
}
