package exercise01;

import java.util.concurrent.Semaphore;

public class WarehouseWithSemaphore implements Warehouse {
	/* When acquiring we take resources from upperLimit. So in effect this shows
	how much space there's left in our warehouse */
	Semaphore upperLimit;
	/* When releasing we add resources to the lowerLimit. So in effect this shows
	how many items we have in our warehouse */
	Semaphore lowerLimit;
	/* The mutex semaphor is used to make sure that we can only run one operation
	on the actual inventory, in our case the int "wares". This replaces the
	while() loop we had in the JavaMonitor example. */
	Semaphore mutex;
	int wares = 0;
  
  public WarehouseWithSemaphore(int capacity, boolean fair) {
	  upperLimit = new Semaphore(capacity, fair);
	  lowerLimit = new Semaphore(0, fair);
	  mutex = new Semaphore(1, fair);
  }
  
  @Override
  public void put(int amount) throws InterruptedException {
	  //Note2Self: We have to acquire an amount instead of just one permit!
	  upperLimit.acquire(amount);
	  mutex.acquire();
	  wares = wares+amount;
	  mutex.release();
	  //Note2Self: We have to release an amount instead of just one permit!
	  lowerLimit.release(amount);
	  System.out.println("put: "+wares);
  }

  @Override
  public void get(int amount) throws InterruptedException {
	  //Note2Self: We have to release an amount instead of just one permit!
	  lowerLimit.acquire(amount);
	  mutex.acquire();
	  wares = wares-amount;
	  mutex.release();
	  //Note2Self: We have to acquire an amount instead of just one permit!
	  upperLimit.release(amount);
	  System.out.println("get: "+wares);
  }
}
