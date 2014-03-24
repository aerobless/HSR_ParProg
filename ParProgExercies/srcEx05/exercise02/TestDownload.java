package exercise02;

import java.io.IOException;
import java.util.ArrayList;
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
		ArrayList<String> results = downloader.downloadUrls(links);
		for(String result : results){
			System.out.println(String.format("%s downloaded (%d characters)","xy", result.length()));
		}
		long endTime = System.currentTimeMillis();
		System.out.println(String.format("total time: %d ms", endTime - startTime));
	}
}
