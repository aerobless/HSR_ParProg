package exercise03;

import java.util.concurrent.Semaphore;


public class UpgradeableReadWriteLock {
	private Semaphore read = new Semaphore(0);
	private Semaphore write = new Semaphore(0);
	private Semaphore writeRequest = new Semaphore(0);
	

	public synchronized void readLock() throws InterruptedException {
		while(write.availablePermits() > 0 || writeRequest.availablePermits() > 0){
			wait();
		}
		read.acquire();
	}

	public synchronized void readUnlock() {
		read.release();
		notifyAll();
	}

	public void upgradeableReadLock() throws InterruptedException {

	}

	public void upgradeableReadUnlock() {

	}

	public synchronized void writeLock() throws InterruptedException {
		writeRequest.acquire();
		
		while(read.availablePermits() > 0 || write.availablePermits() > 0){
			wait();
		}
		writeRequest.release();
		write.acquire();

	}

	public synchronized void writeUnlock() {
		write.release();
		notifyAll();
	}
}
