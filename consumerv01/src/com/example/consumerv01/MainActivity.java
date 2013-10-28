package com.example.consumerv01;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Locale;

import org.xmlpull.v1.XmlPullParserException;

import com.example.consumerv01.R;
import com.example.consumerv01.Drawer.DrawerItemAdapter;
import com.example.consumerv01.Drawer.DrawerModelAdapter;
import com.example.consumerv01.R.color;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import NearBy.NearByItemAdapter;
import NearBy.NearByModelAdapter;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;
import android.widget.TextView;

public class MainActivity extends Activity {
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;

	private CharSequence mDrawerTitle;
	private CharSequence mTitle;
	private String[] mPlanetTitles;
	private String serverUrl = "http://192.168.1.96:50364";
	private String APIUrl = "api/merchant";
	private String stringUrl = serverUrl + "/" + APIUrl;
	//private String stringUrl = "http://weather.yahooapis.com/forecastrss?w=Canada";
	private TextView displayText;
	private static List<Entry> listToDisplay ;
	private Activity currentActvity;
	private static ListView listViewToDisplay ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		// getActionBar().setCustomView(R.layout.action_bar);

		mTitle = mDrawerTitle = getTitle();
		mPlanetTitles = getResources().getStringArray(R.array.menu_item);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);
		// mDrawerList.setBackgroundColor(color.Red);
		// mDrawerLayout
		mDrawerLayout.setDrawerShadow(color.DarkGray, GravityCompat.START);
		// set up the drawer's list view with items and click listener
		String[] mMenuItem = getResources().getStringArray(R.array.menu_item);
		String[] mMenuItemFiles = getResources().getStringArray(
				R.array.menu_item_image_name);

		DrawerModelAdapter.LoadModel(mMenuItem, mMenuItemFiles);
		String[] ids = new String[DrawerModelAdapter.Items.size()];
		for (int i = 0; i < ids.length; i++) {

			ids[i] = Integer.toString(i + 1);
		}

		DrawerItemAdapter adapter = new DrawerItemAdapter(this,
				R.layout.drawer_list_item, ids);
		mDrawerList.setAdapter(adapter);

		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

		// enable ActionBar app icon to behave as action to toggle nav drawer
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		// ActionBarDrawerToggle ties together the the proper interactions
		// between the sliding drawer and the action bar app icon
		mDrawerToggle = new ActionBarDrawerToggle(this, /* host Activity */
		mDrawerLayout, /* DrawerLayout object */
		R.drawable.menu_main, /* nav drawer image to replace 'Up' caret */
		R.string.drawer_open, /* "open drawer" description for accessibility */
		R.string.drawer_close /* "close drawer" description for accessibility */
		) {
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(mTitle);
				invalidateOptionsMenu(); // creates call to
											// onPrepareOptionsMenu()
			}

			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(mDrawerTitle);
				invalidateOptionsMenu(); // creates call to
											// onPrepareOptionsMenu()
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		if (savedInstanceState == null) {
			selectItem(0);

		}

		currentActvity = this;
		// code for testing HTTP / XML fetch
		ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()) {
			new DownloadWebpageTask().execute(stringUrl);
		} else {
			displayText.setText("No network connection available.");
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		SearchView searchView = (SearchView) menu.findItem(R.id.search)
				.getActionView();
		searchView.setQueryHint(getString(R.string.query_hint));
		int searchPlateId = searchView.getContext().getResources()
				.getIdentifier("android:id/search_plate", null, null);
		// Getting the 'search_plate' LinearLayout.
		View searchPlate = searchView.findViewById(searchPlateId);
		searchPlate.setBackgroundResource(R.drawable.searchbar);
		return super.onCreateOptionsMenu(menu);
	}

	/* Called whenever we call invalidateOptionsMenu() */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// If the nav drawer is open, hide action items related to the content
		// view
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
		menu.findItem(R.id.search).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// The action bar home/up action should open or close the drawer.
		// ActionBarDrawerToggle will take care of this.
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// Handle action buttons
		switch (item.getItemId()) {
		case R.id.search:
			// create intent to perform web search for this planet
			Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
			intent.putExtra(SearchManager.QUERY, getActionBar().getTitle());
			// catch event that there's no activity to handle intent
			if (intent.resolveActivity(getPackageManager()) != null) {
				startActivity(intent);
			} else {
				Toast.makeText(this, R.string.app_not_available,
						Toast.LENGTH_LONG).show();
			}
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/* The click listner for ListView in the navigation drawer */
	private class DrawerItemClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			selectItem(position);
		}
	}

	private void selectItem(int position) {
		// update the main content by replacing fragments
		Fragment fragment = new PageFragment();
		Bundle args = new Bundle();
		args.putInt(PageFragment.ARG_PLANET_NUMBER, position);
		fragment.setArguments(args);

		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction()
				.replace(R.id.content_frame, fragment).commit();

		// update selected item and title, then close the drawer
		mDrawerList.setItemChecked(position, true);
		setTitle(mPlanetTitles[position]);
		mDrawerLayout.closeDrawer(mDrawerList);
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	/**
	 * Fragment that appears in the "content_frame", shows a planet
	 */
	private class DownloadWebpageTask extends
			AsyncTask<String, Void, List<Entry>> {

		@SuppressWarnings("finally")
		@Override
		protected List<Entry> doInBackground(String... urls) {

			// params comes from the execute() call: params[0] is the url.
			List<Entry> result=null;
			try {
				result= loadXml(urls[0]);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;
		}

		// onPostExecute displays the results of the AsyncTask.
		@Override
		protected void onPostExecute(List<Entry> result) {
			listToDisplay = result;

		}

		@SuppressWarnings("unchecked")
		private List<Entry> loadXml(String urlString)
				throws XmlPullParserException, IOException {
			InputStream stream = null;
			List<Entry> entries = null;
			FeedParser parser = new FeedParser();

			try {
				stream = downloadUrl(urlString);			
				entries = parser.parse(stream, currentActvity);
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

		private InputStream downloadUrl(String urlString) throws IOException {
		    URL url = new URL(urlString);
		    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		    conn.setReadTimeout(10000 /* milliseconds */);
		    conn.setConnectTimeout(15000 /* milliseconds */);
		    conn.setRequestMethod("GET");
		    conn.setDoInput(true);
		    conn.setRequestProperty("Accept", "text/xml");
		    // Starts the query
		    conn.connect();
		    
		    return conn.getInputStream();
		}

	}

	/**
	 * Fragment that appears in the "content_frame", shows a planet
	 */
	public static class PageFragment extends Fragment {
		public static final String ARG_PLANET_NUMBER = "planet_number";

		public PageFragment() {
			// Empty constructor required for fragment subclasses
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {

			int i = getArguments().getInt(ARG_PLANET_NUMBER);
			String menuItem = getResources().getStringArray(R.array.menu_item)[i];
			menuItem = menuItem.replace(" ", "");
			FragmentName name = FragmentName.valueOf(menuItem);
            View rootView ;
            switch (name) {
				case Login:
					rootView = inflater.inflate(R.layout.fragment_login, container,false);
					break;
				case NearBy:
					rootView = inflater.inflate(R.layout.fragment_nearby, container,false);
					listViewToDisplay = (ListView) rootView.findViewById(R.id.listView1); 
					break;
				default:
					rootView = inflater.inflate(R.layout.fragment_none, container,false);
					break;
            }
            return rootView;

		}   
            @Override
            public void onActivityCreated(Bundle savedInstanceState) {
                super.onActivityCreated(savedInstanceState);
                int j = getArguments().getInt(ARG_PLANET_NUMBER);
                String menuItem = getResources().getStringArray(R.array.menu_item)[j];
                if(menuItem.equals("Near By"))
                {
            		NearByModelAdapter.LoadModel(listToDisplay);
            		String[] ids = new String[NearByModelAdapter.Items.size()];
            		for (int i = 0; i < ids.length; i++) {

            			ids[i] = Integer.toString(i + 1);
            		}
                    NearByItemAdapter Adapter = new NearByItemAdapter(getActivity(), R.layout.merchant_list_item, ids);
                    listViewToDisplay.setAdapter(Adapter);
 //                   listViewToDisplay.setOnItemClickListener(new NearByItemClickListener());

                }
                            }

	}

	public enum FragmentName {

		Login, Home, Search, Category, NearBy, AllDeals, CheckIn, Vouchers, Stamps, Qoopons,

	}
}