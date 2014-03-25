// Parallel Programming Course, L. Bläser
using System;
using System.Collections.Concurrent;
using System.Linq;
using System.Threading.Tasks;

namespace ParallelPrimeCheck {
  public class PrimeCheck {
    public static bool[] SequentialCheckPrimes(int[] numbers) {
      var result = new bool[numbers.Length];
      for (int i = 0; i < numbers.Length; i++) {
        result[i] = _IsPrime(numbers[i]);
      }
      return result;
    }

    // TODO: Implement parallel prime check with parallel loop and range partitioner
    public static bool[] ParallelForCheckPrimesPartitioned(int[] numbers) {
        bool[] result = new bool[numbers.Length];
        Parallel.ForEach(Partitioner.Create(0, numbers.Length), (range, _) =>
        {
              for (int i = range.Item1; i < range.Item2; i++) {
                 result[i] = _IsPrime(numbers[i]);
              }
        });
        return result;
        }

        // TODO: Implement parallel prime check with parallel for loop (no range partitioner)
        public static bool[] ParallelForCheckPrimesUnpartitioned(int[] numbers) {
            bool[] result = new bool[numbers.Length];
            Parallel.For(0, numbers.Length, (i) =>
            {
                result[i] = _IsPrime(numbers[i]);
            });
            return result;
        }

        // TODO: Implement parallel prime check as parallel LINQ
        public static bool[] ParallelLinqCheckPrimes(int[] numbers) {
            var result =
            from number in numbers.AsParallel().AsOrdered()
            select _IsPrime(number);
            return result.ToArray();
        }
    
    private static bool _IsPrime(int number) {
      for (int i = 2; i * i <= number; i++) {
        if (number % i == 0) {
          return false;
        }
      }
      return true;
    }
  }
}
