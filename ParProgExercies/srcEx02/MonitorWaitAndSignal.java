import java.util.ArrayList;

//Unfinished Exercise 4

class Room<T> {
	int capacity = 5;
	ArrayList<T> roomArray = new ArrayList<T>(capacity);
	
	public synchronized void enter(T person){
		while (capacity <= roomArray.size()){
			try {
				wait();
			} catch (InterruptedException anEx) {
				// TODO Auto-generated catch block
				anEx.printStackTrace();
			}
		}
		roomArray.add(person);
		notify();
	}
	
	public synchronized void exit(){
		while (roomArray.isEmpty()){
			try {
				wait();
			} catch (InterruptedException anEx) {
				// TODO Auto-generated catch block
				anEx.printStackTrace();
			}
		}
		roomArray.remove(0);
		notify();	
	}
}

class Person extends Thread {

	public void run() {
		System.out.println("Room entered " + Thread.currentThread().getName());
		try {
			Thread.sleep(1000);
		} catch (InterruptedException anEx) {
			// TODO Auto-generated catch block
			anEx.printStackTrace();
		}
		System.out.println("Room exited " + Thread.currentThread().getName());
	}
}



public class MonitorWaitAndSignal {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Room<Person> test = new Room<Person>();
		Person loler = new Person();
		test.enter(loler);
	}
}
