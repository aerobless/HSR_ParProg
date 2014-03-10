package exercise01;

import java.util.concurrent.CountDownLatch;


public class BrokenCyclicLatch {
	private static final int NOF_ROUNDS = 10; 
	private static final int NOF_THREADS = 10;
	
	private static CountDownLatch latch = new CountDownLatch(NOF_THREADS);
	
	private static void multiRounds(int threadId) throws InterruptedException {
		System.out.println("Thread: "+threadId+" beginning ticking");
		for (int round = 0; round < NOF_ROUNDS; round++) {
			System.out.println("Thread: "+threadId+" waiting on latch");
			latch.countDown();
			latch.await();
			System.out.println("Thread: "+threadId+" go");
			/**
			 * Ex1 - Theory:
			 * Thread 0 triggers the creation of a new latch. If thread 0 manages to
			 * overtake a thread it locks that thread out. However everyone waits on that
			 * Thread for the next round, so we end up with a deadlock.
			 */
			if (threadId == 0) { 
				latch = new CountDownLatch(NOF_THREADS); // new latch for new round
			}
			System.out.println("Round " + round + " thread " + threadId);
		}
	}
	
	public static void main(String[] args) {
		System.out.println("Start:");
		for (int i = 0; i < NOF_THREADS; i++) {
			int threadId = i;
			System.out.println("Setting up thread: "+i);
			new Thread(() -> {
				try {
					multiRounds(threadId);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}).start();
		}
	}
}
