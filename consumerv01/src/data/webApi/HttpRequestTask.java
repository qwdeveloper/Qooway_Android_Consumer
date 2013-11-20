package data.webApi;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.xmlpull.v1.XmlPullParserException;

import com.example.consumerv01.R;

import ui.MainScreenActivity;
import FrameWork.Entry;
import FrameWork.FeedParser;
import android.os.AsyncTask;
import android.util.Base64;

public class HttpRequestTask extends AsyncTask<String, Void, List<Entry>> {

	private MainScreenActivity activity;

	public HttpRequestTask(MainScreenActivity activity) {
		this.activity = activity;
	}

	@SuppressWarnings("finally")
	@Override
	protected List<Entry> doInBackground(String... urls) {

		// params comes from the execute() call: params[0] is the url.
		List<Entry> result = null;
		try {
			result = loadXml(urls[0]);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	// onPostExecute displays the results of the AsyncTask.
	@Override
	protected void onPostExecute(List<Entry> result) {
		activity.listToDisplay = result;
		if (activity.progress != null) {
			activity.progress.cancel();
		}
	}

	@SuppressWarnings("unchecked")
	private List<Entry> loadXml(String urlString)
			throws XmlPullParserException, IOException {
		InputStream stream = null;
		List<Entry> entries = null;
		FeedParser parser = new FeedParser();

		try {
			stream = downloadUrl(urlString);
			entries = parser.parse(stream, activity);
			// Makes sure that the InputStream is closed after the app is
			// finished using it.
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if (stream != null) {
			stream.close();
		}

		return entries;

	}

	private InputStream downloadUrl(String urlString) throws IOException,
			KeyManagementException, NoSuchAlgorithmException,
			KeyStoreException, CertificateException {
		String userName = "alan@qooway.com";
		String passWord = "abc123";
		HttpURLConnection conn = null;
		if (urlString.startsWith("https")) {
			try {
				conn = (HttpURLConnection) httpsConnection(urlString);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			URL url = new URL(urlString);
			conn = (HttpURLConnection) url.openConnection();
		}
		conn.setReadTimeout(10000 /* milliseconds */);
		conn.setConnectTimeout(15000 /* milliseconds */);
		conn.setRequestMethod("GET");
		conn.setDoInput(true);
		conn.setRequestProperty("Accept", "text/xml");
		conn.setRequestProperty(
				"Authorization",
				"Basic "
						+ Base64.encodeToString(
								((userName + ":" + passWord).getBytes()),
								Base64.NO_WRAP));

		// Starts the query
		conn.connect();
		return conn.getInputStream();
	}

	private HttpURLConnection httpsConnection(String urlString)
			throws NoSuchAlgorithmException, KeyManagementException,
			KeyStoreException, IOException, CertificateException {

		// Get an instance of the Bouncy Castle KeyStore format
		KeyStore trusted = KeyStore.getInstance("BKS");
		// Get the raw resource, which contains the keystore with
		// your trusted certificates (root and any intermediate certs)
		InputStream in = activity.getApplicationContext().getResources()
				.openRawResource(R.raw.mykeystore);
		try {
			// Initialize the keystore with the provided trusted
			// certificates
			// Also provide the password of the keystore
			trusted.load(in, "mysecret".toCharArray());
		} finally {
			in.close();
		}
		String algorithm = TrustManagerFactory.getDefaultAlgorithm();
		TrustManagerFactory tmf = TrustManagerFactory.getInstance(algorithm);
		tmf.init(trusted);

		SSLContext context = SSLContext.getInstance("TLS");
		context.init(null, tmf.getTrustManagers(), null);

		URL url = new URL("urlString");
		HttpsURLConnection urlConnection = (HttpsURLConnection) url
				.openConnection();
		urlConnection.setSSLSocketFactory(context.getSocketFactory());
		return urlConnection;
	}
}