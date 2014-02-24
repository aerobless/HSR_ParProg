import java.util.Scanner;

public class ConsoleTicker {

	/* Excercise 1A */
	static class SubclassExample extends Thread {
		char tickerCharacter;
		int timer;

		public SubclassExample(char tickerCharacter, int timer) {
			super();
			this.tickerCharacter = tickerCharacter;
			this.timer = timer;
		}

		@Override
		public void run() {
			try {
				periodTicker(tickerCharacter, timer);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
	/*Excercise 1B */
	static class RunnableExample implements Runnable {
		char tickerCharacter;
		int timer;
		
		public RunnableExample(char tickerCharacter, int timer) {
			super();
			this.tickerCharacter = tickerCharacter;
			this.timer = timer;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				periodTicker(tickerCharacter, timer);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

	/*Exercise 1C */
	static void anonymousInnerClassExample (final char tickerCharacter, final int timer){
		new Thread() {
			@Override
			public void run() {
				try {
					periodTicker(tickerCharacter, timer);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}.start();
	}
	
	
	
	private static void periodTicker(char sign, int intervallMillis)
			throws InterruptedException {
		while (true) {
			System.out.print(sign);
			Thread.sleep(intervallMillis);
		}
	}

	public static void main(String[] args) throws InterruptedException {
		Thread mySubclassExampleThread = new SubclassExample('A', 5);
		mySubclassExampleThread.setDaemon(true);
		mySubclassExampleThread.start();
		
		Thread myRunnableExampleThread = new Thread(new RunnableExample('B', 7));
		myRunnableExampleThread.setDaemon(true);
		myRunnableExampleThread.start();

		String s = "run";
		while (s.equals("run")) {
			Scanner in = new Scanner(System.in);
			s = in.next();
			in.close();
		}
		System.out.println("continuing in main");
		
		//anonymousInnerClassExample('C', 1);
		
		//periodTicker('.', 10);
		// TODO: Concurrent periodTicker('*', 20);
	}
}
