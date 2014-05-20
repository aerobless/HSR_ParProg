using MPI;
using System;

namespace HelloCluster {
  class Program {
    static void Main(string[] args) {
      using (new MPI.Environment(ref args)) {
        Console.WriteLine("Hello cluster from process {0} on {1}", Communicator.world.Rank, System.Environment.MachineName);
      }
    }
  }
}
