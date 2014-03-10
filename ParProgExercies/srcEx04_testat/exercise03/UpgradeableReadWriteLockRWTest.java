package exercise03;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class UpgradeableReadWriteLockRWTest {
	private ReadWriteLock rwLock = new ReentrantReadWriteLock(true);
	// TODO: Implement

	public void readLock() throws InterruptedException {
		rwLock.readLock().lock();
		// TODO:
		
	}

	public void readUnlock() {
		rwLock.readLock().unlock();
		// TODO:
	}

	public void upgradeableReadLock() throws InterruptedException {
		rwLock.writeLock().lock();
		// TODO:
	}

	public void upgradeableReadUnlock() {
		rwLock.writeLock().unlock();
		// TODO:
	}

	public void writeLock() throws InterruptedException {
		rwLock.writeLock().lock();
		// TODO:
	}

	public void writeUnlock() {
		rwLock.writeLock().unlock();
		// TODO:
	}
}
