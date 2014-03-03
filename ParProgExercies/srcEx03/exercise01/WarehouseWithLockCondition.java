package exercise01;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class WarehouseWithLockCondition implements Warehouse {
	Lock monitor;
	Condition notFull;
	Condition notEmpty;
	int wares = 0;
	int totalCapacity;
  
	public WarehouseWithLockCondition(int capacity, boolean fair) {
		monitor = new ReentrantLock(fair);
		notFull = monitor.newCondition();
		notEmpty = monitor.newCondition();
		totalCapacity = capacity;
	}
  
	@Override
	public void put(int amount) throws InterruptedException {
		monitor.lock();
		try {
			while (wares+amount > totalCapacity){ notFull.await();}
			wares = wares+amount;
			notEmpty.signal();
		} finally {
			monitor.unlock();
		}
	}

	@Override
	public void get(int amount) throws InterruptedException {
		monitor.lock();
		try {
			//bug-fixed: 0 is okay too, otherwise we'd get stuck!
			while(wares-amount < 0) { notEmpty.await();}
			wares = wares-amount;
			notFull.signal();
		} finally { monitor.unlock();}
	}
}
