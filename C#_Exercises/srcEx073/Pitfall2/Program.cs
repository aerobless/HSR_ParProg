using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Http;
using System.Text;
using System.Threading;
using System.Threading.Tasks;

namespace Pitfall2 {
  class Program {
    static void Main(string[] args) {
      new Downloader().DownloadAsync().Wait();
    }
  }
}
