using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Net.Http;
using System.Text.RegularExpressions;

namespace WebCrawler {
  class WebClient {
    private HttpClient client;
    private Regex hrefRegex;

    public WebClient() {
      client = new HttpClient();
      client.DefaultRequestHeaders.Add("user-agent", "HSR exercise");
      hrefRegex = new Regex("<a\\s+href=(?:\"(?<link1>[^\"]+)\"|'(?<link2>[^']+)').*?>(.*?)</a>", RegexOptions.IgnoreCase | RegexOptions.Compiled);
    }

    public bool IsWebLink(Uri uri) {
      return uri.Scheme == Uri.UriSchemeHttp || uri.Scheme == Uri.UriSchemeHttps;
    }

    public IEnumerable<Uri> LinksInPage(Uri webPageUri) {
      string html = string.Empty;
      try {
        html = client.GetStringAsync(webPageUri).Result;
      } catch (AggregateException) {
        Debug.WriteLine(string.Format("Error while fetching {0}", webPageUri));
      }
      return ParseLinks(webPageUri, html);
    }

    private IEnumerable<Uri> ParseLinks(Uri baseUri, string html) {
      foreach (Match match in hrefRegex.Matches(html)) {
        var link = match.Groups["link1"].Value;
        if (string.IsNullOrEmpty(link)) {
          link = match.Groups["link2"].Value;
        }
        Uri uri = MakeUri(baseUri, link);
        if (uri != null) {
          yield return uri;
        }
      }
    }



    private Uri MakeUri(Uri baseUri, string link) {
      try {
        if (!Uri.IsWellFormedUriString(link, UriKind.Absolute)) {
          return new Uri(baseUri, link);
        } else {
          return new Uri(link);
        }
      } catch (UriFormatException) {
        Debug.WriteLine(string.Format("Invalid url {0} in site {1}", link, baseUri));
        return null;
      }
     }
  }
}
