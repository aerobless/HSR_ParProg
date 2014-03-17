package exercise02;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class TestDownload {
	private static final String[] links = new String[] {
			"http://www.google.com", 
			"http://www.bing.com",
			"http://www.yahoo.com", 
			"http://www.microsoft.com",
			"http://www.oracle.com" 
	};

	public static void main(String[] args) throws IOException, InterruptedException, ExecutionException {
		long startTime = System.currentTimeMillis();
		WebDownload downloader = new WebDownload();
		for (int i = 0; i < links.length; i++) {
			String link = links[i];
			String result = downloader.downloadUrl(link);
			System.out.println(String.format("%s downloaded (%d characters)", link, result.length()));
		}
		long endTime = System.currentTimeMillis();
		System.out.println(String.format("total time: %d ms", endTime - startTime));
	}
}
