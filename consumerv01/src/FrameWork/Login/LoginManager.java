package FrameWork.Login;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManagerFactory;

import org.xmlpull.v1.XmlPullParserException;

import FrameWork.FeedParser;
import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Base64;
import android.widget.Toast;

import com.example.consumerv01.R;

public class LoginManager {

	private HttpURLConnection loginConnection = null;
	private Boolean result = null;
	private String baseUrl = "https://192.168.1.96:44300/api/customer?";
	// private String usernameparam = "username=";
	// private String passwordparam = "password=";

	private String stringUrl = null;
	private Activity activity = null;

	public LoginManager(Activity Activity) {
		activity = Activity;
	}

	public void connect(String username, String password) throws IOException {
		ConnectivityManager connMgr = (ConnectivityManager) this.activity
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()) {
			String inputs[] = {baseUrl, username,password};

			new DownloadWebpageTask().execute(inputs
			// + usernameparam
			// + username + "&"+ passwordparam + password
					);
		} else {

		}

	}

	public Boolean result() {
		return result;
	}

	private class DownloadWebpageTask extends AsyncTask<String, Void, String> {
		private String userName;
		private String passWord;
		@SuppressWarnings("finally")
		@Override
		protected String doInBackground(String... urls) {

			// params comes from the execute() call: params[0] is the url.
			String result = null;
			try {
				userName=urls[1];
				passWord=urls[2];
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
			Toast toast = null;
			int duration = Toast.LENGTH_SHORT;
			if (result.equals("OK")) {
				text = "Welcome " + userName;
			} else {
				text = "User name and password not match";
			}
			toast = Toast.makeText(activity, text, duration);
			toast.show();
			return;
		}

		@SuppressWarnings("unchecked")
		private String loadXml(String urlString) throws XmlPullParserException,
				IOException {
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

		private String downloadUrl(String urlString) throws IOException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException, CertificateException {

			HostnameVerifier hostnameVerifier = new HostnameVerifier() {
			    @Override
			    public boolean verify(String hostname, SSLSession session) {
			        HostnameVerifier hv =
			            HttpsURLConnection.getDefaultHostnameVerifier();
			        return hv.verify("localhost", session);
			    }

			};
			URL url = new URL(urlString);
			HttpsURLConnection conn = null;
			// Get an instance of the Bouncy Castle KeyStore format
			KeyStore trusted = KeyStore.getInstance("BKS");
			// Get the raw resource, which contains the keystore with
			// your trusted certificates (root and any intermediate certs)
			InputStream in = activity.getApplicationContext()
					.getResources().openRawResource(R.raw.mykeystore);
			try {
				// Initialize the keystore with the provided trusted
				// certificates
				// Also provide the password of the keystore
				trusted.load(in, "my_password".toCharArray());
			} finally {
				in.close();
			}
			String algorithm = TrustManagerFactory.getDefaultAlgorithm();
			TrustManagerFactory tmf = TrustManagerFactory
					.getInstance(algorithm);
			tmf.init(trusted);

			SSLContext context = SSLContext.getInstance("TLS");
			context.init(null, tmf.getTrustManagers(), null);

			conn = (HttpsURLConnection) url.openConnection();
			conn.setHostnameVerifier(hostnameVerifier);
			conn.setSSLSocketFactory(context.getSocketFactory());
			conn.setReadTimeout(10000 /* milliseconds */);
			conn.setConnectTimeout(15000 /* milliseconds */);
			conn.setRequestMethod("GET");
			conn.setDoInput(true);
			conn.setRequestProperty("Accept", "text/xml");
			conn.setRequestProperty("Authorization", "Basic " + Base64.encodeToString(((this.userName+":"+this.passWord).getBytes()), Base64.NO_WRAP));
			// Starts the query
			conn.connect();
			return conn.getResponseMessage();
		}

	}
}
