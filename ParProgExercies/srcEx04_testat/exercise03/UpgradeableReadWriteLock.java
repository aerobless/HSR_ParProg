package exercise03;

import java.util.concurrent.Semaphore;

public class UpgradeableReadWriteLock {
	Semaphore mutex = new Semaphore(1, true);
	// TODO: Implement

	public void readLock() throws InterruptedException {
		mutex.acquire();
		// TODO:
		
	}

	public void readUnlock() {
		mutex.release();
		// TODO:
	}

	public void upgradeableReadLock() throws InterruptedException {
		mutex.acquire();
		// TODO:
	}

	public void upgradeableReadUnlock() {
		mutex.release();
		// TODO:
	}

	public void writeLock() throws InterruptedException {
		mutex.acquire();
		// TODO:
	}

	public void writeUnlock() {
		mutex.release();
		// TODO:
	}
}
