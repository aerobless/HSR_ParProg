package exercise01;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class MultiThreadedSlowSort {
	private static final int NOF_ELEMENTS = 10_000_000;
	
	public static void main(String[] args) {
		int[] numberArray = createRandomArray(NOF_ELEMENTS);
		//Setting up Thread-pool before string to measure:
		ForkJoinPool threadPool = new ForkJoinPool();

		long startTime = System.currentTimeMillis();		
		int[] sortedArrayResult = threadPool.invoke(new QuickSortWorker(numberArray));
		long stopTime = System.currentTimeMillis();
		System.out.println("Total time: " + (stopTime - startTime) + " ms");
		checkSorted(sortedArrayResult);
		/*for(int i = 0; i<sortedArrayResult.length; i++){
			System.out.println(sortedArrayResult[i]);
		}*/
		threadPool.shutdown();

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
	
	static class QuickSortWorker extends RecursiveTask<int[]>{ //RecursiveAction?!
		private static final long serialVersionUID = 3772193021134281020L;
		private static final long THRESHOLD = 1000;
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
		private int[] quickSort(int[] array, int left, int right) {
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
			return array;
		}

		private static int[] join(int[] input1, int[] input2)
		{
			int[] output = new int[input1.length + input2.length];

		    System.arraycopy(input1, 0, output, 0, input1.length);
		    System.arraycopy(input2, 0, output, input1.length, input2.length);

		    return output;
		}
		
		/* (non-Javadoc)
		 * @see java.util.concurrent.RecursiveTask#compute()
		 */
		@Override
		protected int[] compute() {
			int[] returnedArray = null;
			if(arrayToBeSorted.length>THRESHOLD){
				int[] leftArray = Arrays.copyOfRange(arrayToBeSorted, 0, arrayToBeSorted.length/2);
				int[] rightArray = Arrays.copyOfRange(arrayToBeSorted, arrayToBeSorted.length/2, arrayToBeSorted.length);
				QuickSortWorker left = new QuickSortWorker(leftArray);
				QuickSortWorker right = new QuickSortWorker(rightArray);
				invokeAll(left, right);
				try {
					int[] leftResult = left.get();
					int[] rightResult = right.get();
					returnedArray = join(leftResult, rightResult);
					quickSort(returnedArray, 0, returnedArray.length-1);
				} catch (InterruptedException anEx) {
					// TODO Auto-generated catch block
					anEx.printStackTrace();
				} catch (ExecutionException anEx) {
					// TODO Auto-generated catch block
					anEx.printStackTrace();
				}
			}
			else{
				quickSort(arrayToBeSorted, 0, arrayToBeSorted.length - 1);
				returnedArray = arrayToBeSorted;
			}
			return returnedArray;
		}
	}
}
