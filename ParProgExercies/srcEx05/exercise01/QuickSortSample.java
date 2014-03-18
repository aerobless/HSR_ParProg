package exercise01;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class QuickSortSample {
	private static final int NOF_ELEMENTS = 10_000_000;
	
	public static void main(String[] args) {
		int[] numberArray = createRandomArray(NOF_ELEMENTS);
		//Setting up Thread-pool before string to measure:
		ExecutorService threadPool = Executors.newFixedThreadPool(2);
		Future<int[]> future1;
		Future<int[]> future2;
		
		int[] firstHalf = Arrays.copyOfRange(numberArray, 0, numberArray.length/2);
		int[] secondHalf = Arrays.copyOfRange(numberArray, numberArray.length/2, numberArray.length);
		
		future1 = threadPool.submit(new QuickSortWorker(firstHalf));
		future2 = threadPool.submit(new QuickSortWorker(secondHalf));
		
		try {
			int[] result1 = future1.get();
			int[] result2 = future2.get();
			checkSorted(result1);
			checkSorted(result2);
			System.out.println("good");
		} catch (InterruptedException anEx) {
			// TODO Auto-generated catch block
			anEx.printStackTrace();
		} catch (ExecutionException anEx) {
			// TODO Auto-generated catch block
			anEx.printStackTrace();
		} 
		
		threadPool.shutdown();
		
		long startTime = System.currentTimeMillis();
		
		
		long stopTime = System.currentTimeMillis();
		System.out.println("Total time: " + (stopTime - startTime) + " ms");
		checkSorted(numberArray);
	}
	
	private static int[] createRandomArray(int length) {
		Random random = new Random(4711);
		int[] numberArray = new int[length];
		for (int i = 0; i < length; i++) {
			numberArray[i] = random.nextInt();
		}
		return numberArray;
	}

	private static void checkSorted(int[] numberArray) {
		for (int i = 0; i < numberArray.length - 1; i++) {
			if (numberArray[i] > numberArray[i + 1]) {
				throw new RuntimeException("Not sorted");
			}
		}
	}
	
	static class QuickSortWorker implements Callable<int[]>{
		int[] arrayToBeSorted;

		/**
		 * Creates a new instance of this class.
		 *
		 * @param aArrayToBeSorted
		 */
		public QuickSortWorker(int[] aArrayToBeSorted) {
			super();
			arrayToBeSorted = aArrayToBeSorted;
		}

		// sorts the partition between array[left] and array[right]
		private void quickSort(int[] array, int left, int right) {
			int i = left, j = right;
			long m = array[(left + right) / 2];
			do {
				while (array[i] < m) {
					i++;
				}
				while (array[j] > m) {
					j--;
				}
				if (i <= j) {
					int t = array[i];
					array[i] = array[j];
					array[j] = t;
					i++;
					j--;
				}
			} while (i <= j);
			if (j > left) {
				quickSort(array, left, j);
			}
			if (i < right) {
				quickSort(array, i, right);
			}
		}

		/* (non-Javadoc)
		 * @see java.util.concurrent.Callable#call()
		 */
		@Override
		public int[] call() throws Exception {
			quickSort(arrayToBeSorted, 0, arrayToBeSorted.length - 1);
			return arrayToBeSorted;
		}
	}
}
