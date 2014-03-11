package exercise03;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class UpgradeableReadWriteLock {
	Semaphore upgrader = new Semaphore(1, false);
	Semaphore writeLock = new Semaphore(1, false);
	ArrayList<Semaphore> readerList = new ArrayList<Semaphore>();

	public synchronized void readLock() throws InterruptedException {
		System.out.println("Read lock");
		while(writeLock.availablePermits()!=1){
			wait();
		}
		Semaphore reader = new Semaphore(1, false);
		reader.acquire();
		readerList.add(reader);
	}

	public synchronized void readUnlock() {
		System.out.println("Read Unlock");
		Semaphore reader = readerList.remove(readerList.size()-1);
		reader.release();
		notifyAll();
	}

	//LOCK
	public synchronized void upgradeableReadLock() throws InterruptedException {
		System.out.println("upgradeableReadLock");
		readLock();
		upgrader.acquire();
	}

	//UNLOCK
	public void upgradeableReadUnlock() {
		System.out.println("upgradeableReadUnlock");
		upgrader.release();
		readUnlock();
	}

	public synchronized void writeLock() throws InterruptedException {
		System.out.println("writeLock");
		if(upgrader.availablePermits()==0){
			//TODO: release read
			while(readerList.size()>0 || writeLock.availablePermits()==0){
				wait();
			}
			writeLock.acquire();
		}
		else{
			while(readerList.size()>0 || writeLock.availablePermits()==0){
				wait();
			}
			writeLock.acquire();
		}
	}

	public synchronized void writeUnlock() throws InterruptedException {
		System.out.println("writeUnlock");
		if(upgrader.availablePermits()==0){
			//TODO: acquire read
			writeLock.release();	
			readLock();
			notifyAll();
		}
		else{
			writeLock.release();	
			notifyAll();
		}
	}
}

