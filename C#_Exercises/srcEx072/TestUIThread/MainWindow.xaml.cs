// Course "Efficient concurrent programming in .NET", Luc Bläser

using System;
using System.Windows;

namespace TestUIThread {
  public partial class MainWindow : Window {
    public MainWindow() {
      InitializeComponent();
      numberTextBox.Text = "20000000000000003";
    }

    private void startCalculationButton_Click(object sender, RoutedEventArgs e) {
      calculationResultLabel.Content = "(computing)";
      long number;
      if (long.TryParse(numberTextBox.Text, out number)) {
        if (_IsPrime(number)) {
          calculationResultLabel.Content = "Prime";
        } else {
          calculationResultLabel.Content = "No prime";
        }
      }
    }

    private bool _IsPrime(long number) {
      for (long i = 2; i <= Math.Sqrt(number); i++) {
        if (number % i == 0) {
          return false;
        }
      }
      return true;
    }
  }
}
