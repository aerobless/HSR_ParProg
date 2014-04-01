// Course "Efficient concurrent programming in .NET", Luc Bläser
using System;
using System.Windows;
using System.ComponentModel;

namespace TestUIThread
{
    public partial class MainWindow : Window
    {
        private BackgroundWorker bw = new BackgroundWorker();

        public MainWindow()
        {
            InitializeComponent();
            numberTextBox.Text = "20000000000000003";

            bw.WorkerReportsProgress = true;
            bw.WorkerSupportsCancellation = true;
            bw.DoWork += new DoWorkEventHandler(bw_DoWork);
            bw.ProgressChanged += new ProgressChangedEventHandler(bw_ProgressChanged);
            bw.RunWorkerCompleted += new RunWorkerCompletedEventHandler(bw_RunWorkerCompleted);
        }

        private void bw_RunWorkerCompleted(object sender, RunWorkerCompletedEventArgs e)
        {
            if ((e.Cancelled == true))
            {
                this.calculationResultLabel.Content = "Canceled!";
            }

            else if (!(e.Error == null))
            {
                this.calculationResultLabel.Content = ("Error: " + e.Error.Message);
            }

            else
            {
                this.calculationResultLabel.Content = "Done! "+e.Result;
            }
            startCalculationButton.Content = "Start";
        }

        private void bw_ProgressChanged(object sender, ProgressChangedEventArgs e)
        {
            this.calculationResultLabel.Content = (e.ProgressPercentage.ToString() + "%");
        }

        private void bw_DoWork(object sender, DoWorkEventArgs e)
        {
            BackgroundWorker worker = sender as BackgroundWorker;
                bool isPrime = true;
                long number = (long)e.Argument;
                long[] partitions = _PartitionNumber(number, 100);
                for (int i = 0; i < partitions.Length-1; i++)
                {
                     if ((worker.CancellationPending == true))
                         {
                            e.Cancel = true;
                            break;
                        }
                     else
                     {
                        //Slow down
                        System.Threading.Thread.Sleep(100);
                        if (!_IsPrime(partitions[i], partitions[i+1], number))
                        {
                            isPrime = false;
                        }
                        worker.ReportProgress((i));
                     }
                }
                if (isPrime)
                {
                    Console.WriteLine("Prime");
                    e.Result = "Prime";
                }
                else
                {
                    Console.WriteLine("NOT Prime");
                    e.Result = "NOT Prime";
                }
        }

        private void startCalculationButton_Click(object sender, RoutedEventArgs e)
        {
            long number;
            if (long.TryParse(numberTextBox.Text, out number))
            if (bw.IsBusy != true)
            {
                startCalculationButton.Content = "Cancel";
                bw.RunWorkerAsync(number);
            }
            else
            {
                startCalculationButton.Content = "Start";
                bw.CancelAsync();
            }
        }

        private long[] _PartitionNumber(long number, int partitionCount)
        {
            long[] partitions = new long[partitionCount];
            for (int i = 0; i < partitionCount; i++)
            {
                if (i == 0)
                {
                    partitions[i] = 2;
                }
                else if (i == partitionCount-1)
                {
                    partitions[i] = number-1;
                    Console.WriteLine("YAY "+partitions[i]);
                }
                else 
                {
                    partitions[i] = partitions[i - 1] + (number / partitionCount);
                }
            }
           return partitions;
        }

        private bool _IsPrime(long start, long end, long number)
        {
            for (long i = start; i <= Math.Sqrt(end); i++)
            {
                if (number % i == 0)
                {
                    Console.WriteLine(start + " " + end + " " + number);
                    return false;
                }
            }
            return true;
        }
    }
}

