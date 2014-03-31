using System;
using System.Net.Http;
using System.Threading;
using System.Threading.Tasks;

namespace Pitfall2 {
  class Downloader {
    public async Task DownloadAsync() {
      Console.WriteLine("BEFORE " + Thread.CurrentThread.ManagedThreadId);
      HttpClient client = new HttpClient();
      Task<string> task = client.GetStringAsync("http://msdn.microsoft.com");
      string result = await task;
      Console.WriteLine("AFTER " + Thread.CurrentThread.ManagedThreadId);
    }
  }
}
