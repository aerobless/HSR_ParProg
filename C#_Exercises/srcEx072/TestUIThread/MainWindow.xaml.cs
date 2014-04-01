// Course "Efficient concurrent programming in .NET", Luc Bläser

using System;
using System.Windows;
using System.Threading;
using System.Collections.Concurrent;
using System.Threading.Tasks;
using System.ComponentModel;

namespace TestUIThread {
  public partial class MainWindow : Window {
    BackgroundWorker bw = new BackgroundWorker();

    public MainWindow() {
      InitializeComponent();
      numberTextBox.Text = "20000000000000003";

      bw.WorkerReportsProgress = true;
      bw.WorkerSupportsCancellation = true;
      bw.DoWork += new DoWorkEventHandler(bw_DoWork);
     // bw.ProgressChanged += new ProgressChangedEventHandler(bw_ProgressChanged);
     // bw.RunWorkerCompleted += new RunWorkerCompletedEventHandler(bw_RunWorkerCompleted);
    }

    private void bw_DoWork(object sender, DoWorkEventArgs e)
    {
        BackgroundWorker worker = sender as BackgroundWorker;
        int partitions = 100;
        long number = 20000000000000003;
        bool singleResult = false;
        long[] splitter = new long[partitions];
        for (int i2 = 0; i2 < partitions; i2++)
        {
            if (i2 == 0)
            {
                splitter[i2] = number / partitions;
            }
            else
            {
                splitter[i2] = splitter[i2 - 1] + (number / partitions);
            }

            for (int i = 1; (i <= partitions); i++)
            {
                if ((worker.CancellationPending == true))
                {
                    e.Cancel = true;
                    break;
                }
                else
                {
                    //todo: 
                    _ParallelForCheckPrimesPartitioned(splitter[i]);
                    worker.ReportProgress((i * 10));
                }
            }
        }
    }

    private void startCalculationButton_Click(object sender, RoutedEventArgs e) {
      calculationResultLabel.Content = "(computing)";
      long number;
      if (bw.IsBusy != true)
      {
          bw.RunWorkerAsync();
      if (long.TryParse(numberTextBox.Text, out number))
      {
          new Thread(() =>
          {
              String result = "";
              {
                  if (_ParallelForCheckPrimesPartitioned(number))
                  {
                      result = "Prime";
                  }
                  else
                  {
                      result = "No prime";
                  }
              }

              Dispatcher.BeginInvoke(new ThreadStart(() =>
              {
                  calculationResultLabel.Content = result;
              }));
          }).Start();
      }
      }
    }

    public static bool _ParallelForCheckPrimesPartitioned(long number)
    {
        int partitions = 2;
        bool singleResult = false;
        bool[] result = new bool[partitions];
        long[] splitter = new long[partitions];
        for (int i = 0; i < partitions; i++)
        {
            if(i==0){
             splitter[i] = number/partitions;
            }
            else{
             splitter[i] = splitter[i-1]+(number/partitions);
          }
        }
        Parallel.ForEach(Partitioner.Create(0, splitter.Length), (range, _) =>
            {
                for (int i = range.Item1; i < range.Item2; i++)
                {
                    Console.WriteLine("Status " + i);
                    if (i == 0)
                    {
                        result[i] = _IsPrime(2, splitter[i], number);
                        if (result[i] == true) {
                            singleResult = true;
                        }
                    }
                    else
                    {
                        result[i] = _IsPrime(splitter[i-1], splitter[i], number);
                    }
                }
            });
        return singleResult;
    }

    private static bool _IsPrime(long start, long end, long number) {
        for (long i = start; i <= Math.Sqrt(end); i++)
        {
            if (number % i == 0)
            {
          return false;
        }
      }
      return true;
    }
  }
}
