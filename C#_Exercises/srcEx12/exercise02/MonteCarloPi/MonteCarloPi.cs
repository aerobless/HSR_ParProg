using System;
using MPI;
using System.Diagnostics;

namespace MonteCarloPi {
  public class MonteCarloPi {
    private const long Trials = 10000000000L;

    public static void Main(string[] args) {
        using (new MPI.Environment(ref args))
        {
            int rank = Communicator.world.Rank;
            int size = Communicator.world.Size;

            var watch = Stopwatch.StartNew();
            long hits = CountHits(Trials / size);
            long totalHits = Communicator.world.Reduce(hits, (a, b) => a + b, 0);

            if (rank == 0)
            {
                double pi = 4 * ((double)totalHits / Trials);

                Console.WriteLine("Pi approximation {0} versus exact {1}", pi, Math.PI);
                Console.WriteLine("Total time: {0} ms", watch.ElapsedMilliseconds);
            }
        }
    }

    private static long CountHits(long trials) {
      long hits = 0;
      Random random = new Random();
      for (long i = 0; i < trials; i++) {
        double x = random.NextDouble();
        double y = random.NextDouble();
        if (x * x + y * y <= 1) { // identical to Math.Sqrt(x * x + y * y) <= 1
          hits++;
        }
      }
      return hits;
    }
  }
}
