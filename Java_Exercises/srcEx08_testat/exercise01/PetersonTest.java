package exercise01;

public class PetersonTest {
  private static int counter;
  
  public static void main(String[] args) throws InterruptedException {
    final int NofRounds = 10000000;
    final PetersonMutex mutex = new PetersonMutex();
    counter = 0;
    
    Thread t0 = new Thread() {
      public void run() {
        for (int i = 0; i < NofRounds; i++) {
        	mutex.thread0Lock();
        	counter++;
        	mutex.thread0Unlock();
        }
      }
    };
    Thread t1 = new Thread() {
      public void run() {
        for (int i = 0; i < NofRounds; i++) {
        	mutex.thread1Lock();
        	counter--;
        	mutex.thread1Unlock();
        }
      }
    };
    t0.start(); 
    t1.start();
    t0.join(); 
    t1.join();
    if (counter != 0) {
      throw new AssertionError("Wrong synchronization");
    }
    System.out.println("Completed");
  }
}

