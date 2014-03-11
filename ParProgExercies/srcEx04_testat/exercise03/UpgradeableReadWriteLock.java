package exercise03;

import java.util.concurrent.Semaphore;

public class UpgradeableReadWriteLock {
	Semaphore writeLock = new Semaphore(1, false);
	Semaphore readLock = new Semaphore(1, false);
	//Semaphore upgradeLock = new Semaphore(1, false);

	public synchronized void readLock() throws InterruptedException {
		while(writeLock.availablePermits()!=1){
			wait();
		}
			if(readLock.availablePermits()>0){
				readLock.acquire();
			}
	}

	public synchronized void readUnlock() {
		if(readLock.availablePermits()==0){
			readLock.release();
			notifyAll();
		}
	}

	public void upgradeableReadLock() throws InterruptedException {

	}

	public void upgradeableReadUnlock() {

	}

	public synchronized void writeLock() throws InterruptedException {
		while(readLock.availablePermits()==0 || writeLock.availablePermits()==0){
			wait();
		}
		writeLock.acquire();
	}

	public synchronized void writeUnlock() {
			writeLock.release();	
			notifyAll();
	}
}

