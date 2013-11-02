package Login;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import org.xmlpull.v1.XmlPullParserException;
import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.widget.Toast;
import com.example.consumerv01.FeedParser;

public class LoginManager {

	private HttpURLConnection loginConnection = null;
	private Boolean result = null;
	private String baseUrl = "http://192.168.1.96:50364/api/customer?";
	private String usernameparam = "username=";
	private String passwordparam = "password=";
	private String userName ;
	private String stringUrl = null;
	private Activity activity = null;

	public LoginManager(Activity Activity) {
		activity = Activity;
	}

	public void connect(String username, String password) throws IOException {
		userName = username;
		ConnectivityManager connMgr = (ConnectivityManager) this.activity
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()) {
			new DownloadWebpageTask().execute(baseUrl + usernameparam
					+ username + "&"+ passwordparam + password);
		} else {

		}
	
	}

	public Boolean result() {
		return result;
	}

	private class DownloadWebpageTask extends
			AsyncTask<String, Void, String> {

		@SuppressWarnings("finally")
		@Override
		protected String doInBackground(String... urls) {

			// params comes from the execute() call: params[0] is the url.
			String result = null;
			try {
				result = loadXml(urls[0]);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;
		}

		// onPostExecute displays the results of the AsyncTask.
		@Override
		protected void onPostExecute(String result) {
			String text = "";
			Toast toast =null;
			int duration = Toast.LENGTH_SHORT;
			if (result.equals("OK"))
			{
				text ="Welcome " + userName;
			}
			else
			{
				text ="User name and password not match";
			}
			toast = Toast.makeText(activity, text, duration);
			toast.show();
			return;
		}

		@SuppressWarnings("unchecked")
		private String loadXml(String urlString)
				throws XmlPullParserException, IOException {
			String response = "";
			try {
				response = downloadUrl(urlString);

				// Makes sure that the InputStream is closed after the app is
				// finished using it.
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			return response;

		}

		private String downloadUrl(String urlString)
				throws IOException {
			URL url = new URL(urlString);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(10000 /* milliseconds */);
			conn.setConnectTimeout(15000 /* milliseconds */);
			conn.setRequestMethod("POST");
			conn.setDoInput(true);
			// Starts the query
			conn.connect();
			return conn.getResponseMessage();
		}

	}
}
