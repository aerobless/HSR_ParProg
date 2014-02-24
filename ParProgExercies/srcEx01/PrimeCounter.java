import java.util.ArrayList;

public class PrimeCounter {
  private static boolean isPrime(long number) {
    for (long factor = 2; factor * factor <= number; factor++) {
      if (number % factor == 0) { 
        return false; 
      }
    }
    return true;
  }
  
  private static long countPrimes(long start, long end) {
    long count = 0;
    for (long number = start; number < end; number++) {
      if (isPrime(number)) { 
        count++; 
      }
    }
    return count;
  }
  
  private static final long START = 1_000_000L;
  private static final long END = 10_000_000L;
  
  
  static class PrimeThread extends Thread{
	  long starter;
	  long ender; 
	  public long count;
	
	public long getCount() {
		return count;
	}

	public PrimeThread(long aStarter, long aEnder) {
		super();
		starter = aStarter;
		ender = aEnder;
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		// TODO Auto-generated method stub
		count = countPrimes(starter, ender);
	}}
  
  public static void main(String[] args) {
    long startTime = System.currentTimeMillis(); 
    
    long totalCount = 0;
    int threadCount = 20;
    long endStep = (END-START)/threadCount;

	long start = 1_000_000L;
	long end = start+endStep;
    
    ArrayList<PrimeThread> threadList = new ArrayList<PrimeThread>();
    
    for (int i= 0; i < threadCount; i++) {
    	//System.out.println("start:"+start);
    	//System.out.println("end:"+end);
        PrimeThread primer = new PrimeThread(start, end);
        threadList.add(primer);
        primer.start();
        start = end;
        end = end+endStep;
    }
    
    for (int i = 0; i<threadList.size(); i++) {
    	try {
			threadList.get(i).join();
		} catch (InterruptedException anEx) {
			// TODO Auto-generated catch block
			anEx.printStackTrace();
		}
    	totalCount = totalCount+threadList.get(i).count;
    }
    
    long endTime = System.currentTimeMillis();    
    System.out.println("#Primes: " + totalCount + " Time: " + (endTime - startTime) + " ms");
  }
}
