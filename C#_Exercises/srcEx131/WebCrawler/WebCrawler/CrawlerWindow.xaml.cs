using System;
using System.Reactive.Concurrency;
using System.Reactive.Linq;
using System.Reactive.Subjects;
using System.Windows;

namespace WebCrawler {
  public partial class CrawlerWindow : Window {
    private WebClient webClient = new WebClient();
    private ReplaySubject<Uri> addresses = new ReplaySubject<Uri>();

    public CrawlerWindow() {
      InitializeComponent();
      DefineWorkflow();
    }

    private void DefineWorkflow() {
        // TODO: Define Rx workflow
        // TODO: Display each result with resultListView.Items.Add()

        (
            from uri in addresses
            from link in webClient.LinksInPage(uri)
            where webClient.IsWebLink(link)
            select link
        ).Distinct().ObserveOnDispatcher()
        .Subscribe((uri) => resultListView.Items.Add(uri));


    }
    private void startButton_Click(object sender, RoutedEventArgs e) {
        // TODO: Push into Rx workflow
        resultListView.Items.Clear();
        addresses.OnNext(new Uri(addressTextBox.Text));
    }
  }
}
