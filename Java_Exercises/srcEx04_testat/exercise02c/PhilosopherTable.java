package exercise02c;

import java.util.Observable;
import java.util.concurrent.Semaphore;

class PhiloState {
	public static final int thinking = 0;
	public static final int hungry = 1;
	public static final int eating = 2;
}

class Philosopher extends Thread {
	private PhilosopherTable table;
	private int philoState = PhiloState.thinking;
	private int id;

	public Philosopher(PhilosopherTable table, int id) {
		this.id = id;
		this.table = table;
	}

	public int getPhiloState() {
		return philoState;
	}

	public long getId() {
		return id;
	}

	private void think() throws InterruptedException {
		philoState = PhiloState.thinking;
		table.notifyStateChange(this);
		sleep((int) (Math.random() * 1500));
	}

	private void eat() throws InterruptedException {
		philoState = PhiloState.eating;
		table.notifyStateChange(this);
		sleep((int) (Math.random() * 1000));
	}

	/**
	 * The forks are numbered 1 through 5 because of the semaphore array.
	 * Now each philosopher will always pick up the fork with the lower number
	 * first. After that he'll try to take the fork with the higher number.
	 * So in the worst case, if 4 philosophers try to pick up the lower numbered
	 * forks at the same time, the highest numbered fork will remain on the table.
	 * So the fifth philosopher can't pick up a fork and we cannot achieve a
	 * dead-lock state.
	 *
	 * @throws InterruptedException
	 */
	private void takeForks() throws InterruptedException {
		philoState = PhiloState.hungry;
		table.notifyStateChange(this);
		// try to get the forks
		if(table.leftForkNumber(id)>table.rightForkNumber(id)){
			table.acquireFork(table.rightForkNumber(id));
			sleep(500);
			table.acquireFork(table.leftForkNumber(id));
		}
		else{
			table.acquireFork(table.leftForkNumber(id));
			sleep(500);
			table.acquireFork(table.rightForkNumber(id));
		}
	}

	private void putForks() {
		table.releaseFork(table.leftForkNumber(id));
		table.releaseFork(table.rightForkNumber(id));
	}

	public void run() {
		yield();
		while (true) {
			try {
				think();
				System.out.println("trying to take forks");
				takeForks();
				eat();
				putForks();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}

class PhilosopherTable extends Observable {
	private int nofPhilosophers;
	private Semaphore[] forks;
	private Philosopher[] philosophers;

	public PhilosopherTable(int nofPhilosophers) {
		System.out.println("creating table ...");
		this.nofPhilosophers = nofPhilosophers;
		forks = new Semaphore[nofPhilosophers];
		for (int i = 0; i < nofPhilosophers; i++) {
			forks[i] = new Semaphore(1);
		}
		philosophers = new Philosopher[nofPhilosophers];
		for (int i = 0; i < nofPhilosophers; i++) {
			philosophers[i] = new Philosopher(this, i);
		}
	}

	public void acquireFork(int forkNumber) throws InterruptedException {
		forks[forkNumber].acquire();
	}

	public void releaseFork(int forkNumber) {
		forks[forkNumber].release();
	}

	public int leftForkNumber(int philosopherId) {
		return philosopherId;
	}

	public int rightForkNumber(int philosopherId) {
		return (philosopherId + 1) % nofPhilosophers;
	}

	public void notifyStateChange(Philosopher sender) {
		setChanged();
		notifyObservers(sender);
	}

	public void start() {
		notifyStateChange(null);
		for (int i = nofPhilosophers - 1; i >= 0; i--) {
			philosophers[i].setPriority(Thread.MIN_PRIORITY);
			philosophers[i].start();
		}
	}

	public Philosopher getPhilo(int i) {
		return philosophers[i];
	}
}
