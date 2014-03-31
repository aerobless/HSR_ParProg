using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Http;
using System.Windows;
using System.Windows.Controls;

namespace Demo2_AsyncDownloadGUI {
  public partial class MainWindow : Window {
    public MainWindow() {
      InitializeComponent();
    }

    private void addButton_Click(object sender, RoutedEventArgs e) {
      urlListBox.Items.Add(urlEntryTextBox.Text);
    }

    private async void downloadButton_Click(object sender, RoutedEventArgs e) {
       HttpClient client = new HttpClient();
       foreach (string url in _UrlCollection) {
          string data = await client.GetStringAsync(url);
          outputTextBox.Text += string.Format("{0} downloaded: {1} bytes", url, data.Length) + Environment.NewLine;
       }
    }

    private IEnumerable<string> _UrlCollection {
      get { return urlListBox.Items.Cast<string>(); }
    }
  }
}
