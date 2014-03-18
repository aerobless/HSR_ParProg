package exercise01;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class QuickSortSample {
	private static final int NOF_ELEMENTS = 10_000_000;
	
	public static void main(String[] args) {
		int[] numberArray = createRandomArray(NOF_ELEMENTS);
		//Setting up Thread-pool before string to measure:
		ForkJoinPool threadPool = new ForkJoinPool();

		int[] sortedArrayResult = threadPool.invoke(new QuickSortWorker(numberArray));
		
		
		
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
	
	static class QuickSortWorker extends RecursiveTask<int[]>{
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
		
		public <T> int[] concatenate (int[] aLeftResult, int[] aRightResult) {
		    int aLen = aLeftResult.length;
		    int bLen = aRightResult.length;
		    int[] C = (int[]) Array.newInstance(aLeftResult.getClass().getComponentType(), aLen+bLen);
		    System.arraycopy(aLeftResult, 0, C, 0, aLen);
		    System.arraycopy(aRightResult, 0, C, aLen, bLen);

		    return C;
		}

		/* (non-Javadoc)
		 * @see java.util.concurrent.RecursiveTask#compute()
		 */
		@Override
		protected int[] compute() {
			int[] combinedResult = null;
			if(arrayToBeSorted.length>THRESHOLD){
				int[] leftArray = Arrays.copyOfRange(arrayToBeSorted, 0, arrayToBeSorted.length/2);
				int[] rightArray = Arrays.copyOfRange(arrayToBeSorted, arrayToBeSorted.length/2, arrayToBeSorted.length);
				QuickSortWorker left = new QuickSortWorker(leftArray);
				QuickSortWorker right = new QuickSortWorker(rightArray);
				invokeAll(left, right);
				int[] leftResult;
				try {
					leftResult = left.get();
					int[] rightResult = right.get();
					if(leftResult[leftResult.length-1]>rightResult[rightResult.length-1]){
						combinedResult = concatenate(leftResult,rightResult);
					}
					else
					{
						combinedResult = concatenate(rightResult,leftResult);
					}
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
				combinedResult = arrayToBeSorted;
			}
			return combinedResult;
		}
	}
}
