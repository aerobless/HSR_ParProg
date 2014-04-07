using System;
using System.Threading;

namespace Peterson {
  class Program {
    static void Main() {
      const int NofRounds = 100000;
      PetersonMutex mutex = new PetersonMutex();
      int counter = 0;
    
      Thread t0 = new Thread(() => {
          for (int i = 0; i < NofRounds; i++) {
            mutex.Thread0Lock();
            counter++;
            mutex.Thread0Unlock();
          }
      });
      Thread t1 = new Thread(() => {
        for (int i = 0; i < NofRounds; i++) {
          mutex.Thread1Lock();
          counter--;
          mutex.Thread1Unlock();
        }
      });
      t0.Start(); 
      t1.Start();
      t0.Join(); 
      t1.Join();
      if (counter != 0) {
        throw new Exception("Wrong synchronization");
      }
      Console.WriteLine("Completed");
    }
  }
}
