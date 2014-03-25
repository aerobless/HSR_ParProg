// Parallel Programming Course, L. Bläser
using System;
using System.Diagnostics;
using System.Linq;

namespace ParallelPrimeCheck {
  public class Program {
    public static void Main() {
      const int N = 500000000;
      var numbers = _CreateRandomNumberArray(N);
      _Test(PrimeCheck.SequentialCheckPrimes, numbers);
      _Test(PrimeCheck.ParallelForCheckPrimesUnpartitioned, numbers);
      _Test(PrimeCheck.ParallelForCheckPrimesPartitioned, numbers);
      _Test(PrimeCheck.ParallelLinqCheckPrimes, numbers);
    }

    private delegate bool[] _ArrayPrimeChecker(int[] numbers); 

    private static void _Test(_ArrayPrimeChecker checker, int[] numbers) {
      var watch = Stopwatch.StartNew();
      var result = checker(numbers);
      Console.WriteLine("{0} total time: {1} ms", checker.Method.Name, watch.ElapsedMilliseconds);
      Console.WriteLine("{0} primes found", (from x in result where x select x).Count());
    }

    private static int[] _CreateRandomNumberArray(int length) {
      var random = new Random(4711);
      var array = new int[length];
      for (int i = 0; i < length; i++) {
        array[i] = random.Next(128);
      }
      return array;
    }
  }
}
