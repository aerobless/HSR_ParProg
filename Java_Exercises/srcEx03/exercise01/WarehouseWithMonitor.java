package exercise01;

public class WarehouseWithMonitor implements Warehouse {
	int totalWareCapacity = 0;
	int wareCapacity = 0;
  
  public WarehouseWithMonitor(int capacity) {
	  totalWareCapacity = capacity;
  }
  
  @Override
  public synchronized void put(int amount) throws InterruptedException {
	  while(totalWareCapacity<(amount+wareCapacity)){ wait(); }
	  wareCapacity = wareCapacity+amount;
	  notifyAll();
  }

  @Override
  public synchronized void get(int amount) throws InterruptedException {
	  while(wareCapacity<(amount)){ wait(); }
	  wareCapacity = wareCapacity-amount;
	  notifyAll();
  }
}
