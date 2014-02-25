import java.util.ArrayList;
import java.util.List;
import java.util.Random;

//Test for part 1 of Exercise 3
class BoundedBuffer<T> {
	int boundCapacity=0;
	ArrayList<T> bufferList = new ArrayList<T>();
	
	// TODO: Implement bounded buffer as monitor
	public BoundedBuffer(int capacity) {
		boundCapacity = capacity;

	}

	public synchronized void put(T item) {
		// TODO: wait until non-full, then add item in last position
		while (boundCapacity <= bufferList.size()){
			try {
				wait();
			} catch (InterruptedException anEx) {
				// TODO Auto-generated catch block
				anEx.printStackTrace();
			}
		}
		bufferList.add(item);
		notifyAll();
	}

	public synchronized T get() {
		// TODO: wait until non-empty, then remove and return item from first position
		while (bufferList.size()<1){
			try {
				wait();
			} catch (InterruptedException anEx) {
				// TODO Auto-generated catch block
				anEx.printStackTrace();
			}
		}
		T returnObject = bufferList.remove(0);
		notifyAll();
		return returnObject;
	}
}

//Test for part

class Producer extends Thread {
	private final BoundedBuffer<Long> buffer;
	private final int nofItems;

	public Producer(BoundedBuffer<Long> buffer, int nofItems) {
		this.buffer = buffer;
		this.nofItems = nofItems;
	}

	public void run() {
		Random random = new Random();
		for (int i = 0; i < nofItems; i++) {
			buffer.put(random.nextLong());
		}
		System.out.println("Producer finished " + getName());
	}
}

class Consumer extends Thread {
	private final BoundedBuffer<Long> buffer;
	private final int nofItems;

	public Consumer(BoundedBuffer<Long> buffer, int nofItems) {
		this.buffer = buffer;
		this.nofItems = nofItems;
	}

	public void run() {
		for (int i = 0; i < nofItems; i++) {
			buffer.get();
		}
		System.out.println("Consumer finished " + getName());
	}
}

public class Simulation {
	private static final int NOF_PRODUCERS = 1;
	private static final int NOF_CONSUMERS = 10;
	private static final int BUFFER_CAPACITY = 1;
	// TotalElements must be a multiple of ElementsPerProducer and ElementsPerConsumer
	//private static final int TOTAL_ELEMENTS = 1000000; 
	private static final int ELEMENTS_PER_PRODUCER = 1000000;//TOTAL_ELEMENTS / NOF_PRODUCERS;
	private static final int ELEMENTS_PER_CONSUMER = 100000;//TOTAL_ELEMENTS / NOF_CONSUMERS;

	public static void main(String[] args) throws InterruptedException {
		List<Thread> threads = new ArrayList<Thread>();
		BoundedBuffer<Long> buffer = new BoundedBuffer<Long>(BUFFER_CAPACITY);
		long startTime = System.currentTimeMillis();
		for (int i = 0; i < NOF_PRODUCERS; i++) {
			threads.add(new Producer(buffer, ELEMENTS_PER_PRODUCER));
		}
		for (int i = 0; i < NOF_CONSUMERS; i++) {
			threads.add(new Consumer(buffer, ELEMENTS_PER_CONSUMER));
		}
		for (Thread thread: threads) {
			thread.start();
		}
		for (Thread thread: threads) {
			thread.join();
		}
		long stopTime = System.currentTimeMillis();
		System.out.println("Producer-consumer simulation finished");
		System.out.println("Total time: " + (stopTime - startTime) + " ms");
	}
}
