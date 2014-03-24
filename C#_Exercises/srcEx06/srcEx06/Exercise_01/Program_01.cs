// Parallel Programming Course, L. Bläser
using System;
using System.Diagnostics;

namespace srcEx06
{
	public class Program_01
	{
		public void run ()
		{
			// A has dimension N x K, B has K x M, C has N x M
			const int N = 1000;
			const int M = 10000;
			const int K = 100;
			var random = new Random (4711);
			var matrixA = _CreateRandomMatrix (random, N, K);
			var matrixB = _CreateRandomMatrix (random, K, M);
			var watch = Stopwatch.StartNew ();
			var matrixC = ParallelMatrixMultiplication.Multiply (matrixA, matrixB);
			Console.WriteLine ("Total computing time: {0} ms", watch.ElapsedMilliseconds);
			_CheckCorrectness (matrixA, matrixB, matrixC);
		}

		private static long[,] _CreateRandomMatrix (Random random, long len1, long len2)
		{
			var matrix = new long[len1, len2];
			for (int i = 0; i < len1; i++) {
				for (int j = 0; j < len2; j++) {
					matrix [i, j] = random.Next () * random.Next ();
				}
			}
			return matrix;
		}

		private static void _CheckCorrectness (long[,] matrixA, long[,] matrixB, long[,] matrixC)
		{
			for (int i = 0; i < matrixC.GetLength(0); i++) {
				for (int j = 0; j < matrixC.GetLength(1); j++) {
					long sum = 0;
					for (int k = 0; k < matrixA.GetLength(1); k++) {
						sum += matrixA [i, k] * matrixB [k, j];
					}
					if (sum != matrixC [i, j]) {
						throw new Exception ("Incorrect matrix multiplication");
					}
				}
			}
		}
	}
}

