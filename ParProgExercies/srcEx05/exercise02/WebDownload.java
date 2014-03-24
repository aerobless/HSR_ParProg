package exercise02;

import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class WebDownload {
	ExecutorService threadPool;

	public WebDownload() {
		super();
		threadPool = Executors.newCachedThreadPool();
	}

	public ArrayList<String> downloadUrls(final String[] aLinks){
		ArrayList<Future<String>> futureList = new ArrayList<Future<String>>();
		ArrayList<String> resultSet = new ArrayList<String>();
		for(String link: aLinks){
			Future<String> future = threadPool.submit(new DownloadWorker(link));
			futureList.add(future);
		}
			try {
				for(Future<String> aFuture : futureList){
					resultSet.add(aFuture.get());
				}
			} catch (InterruptedException anEx) {
				// TODO Auto-generated catch block
				anEx.printStackTrace();
			} catch (ExecutionException anEx) {
				// TODO Auto-generated catch block
				anEx.printStackTrace();
			}
		return resultSet;
	}
	
	public void stopDownloads(){
		threadPool.shutdown();
	}
	
	class DownloadWorker implements Callable<String> {
		String link;

		public DownloadWorker(String aLink) {
			super();
			link = aLink;
		}

		/* (non-Javadoc)
		 * @see java.util.concurrent.Callable#call()
		 */
		@Override
		public String call() throws Exception {
			URL url = new URL(link);
			StringBuffer stringBuffer = new StringBuffer();
			try (Reader reader = new InputStreamReader(url.openStream())) {
				int i;
				while ((i = reader.read()) >= 0) {
					stringBuffer.append((char) i);
				}
			}
			return stringBuffer.toString();
		}	
	}
}
