package exercise01;

public class PetersonMutex {
	/*
	 * 1a: The CPU or JVM can re-order the operations and then
	 * they're no always called in the same order, which leads to a
	 * "Wrong synchronisation".
	 */
	
	/* 
	 * 1b: The variables have to be volatile, so that changes get
	 * propagated to memory and  there is no re-ordering by the CPU/JVM etc.
	 */
	private volatile boolean state0 = false;
	private volatile boolean state1 = false;
	private volatile int turn = 0;

	// acquire lock by thread 0
	public void thread0Lock() {
		state0 = true;
		turn = 1;
		while (turn == 1 && state1);
	}

	// release lock by thread 0
	public void thread0Unlock() {
		state0 = false;
	}

	// acquire lock by thread 1
	public void thread1Lock() {
		state1 = true;
		turn = 0;
		while (turn == 0 && state0);
	}

	// release lock by thread 1
	public void thread1Unlock() {
		state1 = false;
	}
}
