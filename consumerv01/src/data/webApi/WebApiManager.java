package data.webApi;

import java.util.concurrent.ExecutionException;

import ui.MainScreenActivity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class WebApiManager {
	
	private MainScreenActivity activity ;
	private String serverAddress;
	
	public WebApiManager(MainScreenActivity msa , String address)
	{
		activity= msa;
		serverAddress = address;
	}
	
	public void webApiPost(String url) throws InterruptedException,
			ExecutionException {
		ConnectivityManager connMgr = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()) {
			HttpRequestTask task = new HttpRequestTask(activity);
			activity.listToDisplay = task.execute(url).get();
		} else {
			activity.displayText.setText("No network connection available.");
		}
	}

	public void webApiGet(String url) throws InterruptedException,
			ExecutionException {
		ConnectivityManager connMgr = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()) {
			HttpPostTask task = new HttpPostTask(activity);
			activity.listToDisplay = task.execute(url).get();
		} else {
			activity.displayText.setText("No network connection available.");
		}
	}

}
