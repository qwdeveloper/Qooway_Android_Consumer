package ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import FrameWork.Entry;
import FrameWork.Login.LoginManager;
import FrameWork.NearBy.NearByItemAdapter;
import FrameWork.NearBy.NearByModelAdapter;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TabHost.TabContentFactory;
import android.widget.TabHost.TabSpec;

import com.example.consumerv01.R;

public class PageFragment extends Fragment {
		public static final String ARG_PAGE_NUMBER = "PAGE_number";
		private MainScreenActivity mainActivity;
		private View rootView;

		public PageFragment(MainScreenActivity MA) {
			mainActivity = MA;
		}

		@SuppressWarnings("deprecation")
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {

			int i = getArguments().getInt(ARG_PAGE_NUMBER);
			String menuItem = getResources().getStringArray(R.array.menu_item)[i];
			menuItem = menuItem.replace(" ", "");
			FragmentName name = FragmentName.valueOf(menuItem);

			switch (name) {
			case Login:
				rootView = inflater.inflate(R.layout.fragment_login, container,
						false);
				mainActivity.login = (EditText) rootView.findViewById(R.id.userName);
				mainActivity.password = (EditText) rootView.findViewById(R.id.password);
				mainActivity.loginButton = (Button) rootView.findViewById(R.id.logInButton);
				mainActivity.loginButton.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {

						try {
							this.login();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

					private void login() throws IOException {
						LoginManager LIM = new LoginManager(mainActivity);
						LIM.connect(mainActivity.login.getText().toString(), mainActivity.password
								.getText().toString());
					}
				});

				break;
			case NearBy:
				try {
					this.mainActivity.webApiManager.webApiGet(this.mainActivity.serverUrl
							+ '/' + this.mainActivity.APIUrl);

					rootView = inflater.inflate(R.layout.fragment_nearby,
							container, false);
					mainActivity.listViewToDisplay = (ListView) rootView
							.findViewById(R.id.listView1);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;

			case Vouchers:
				rootView = inflater.inflate(R.layout.fragment_merchant_details,
						container, false);
				mainActivity.mTabHost = (TabHost) rootView
						.findViewById(android.R.id.tabhost);
				mainActivity.mTabHost.setup();
				setupTab(inflater, container,
						new TextView(rootView.getContext()), "Check-Ins");
				setupTab(inflater, container,
						new TextView(rootView.getContext()), "Store Profile");
				setupTab(inflater, container,
						new TextView(rootView.getContext()), "Reviews");
				setupTab(inflater, container,
						new TextView(rootView.getContext()), "Menu");
				break;
			case Category:
				rootView = inflater.inflate(R.layout.fragment_search, container, false);
			default:				
				rootView = inflater.inflate(R.layout.fragment_none, container,
						false);
				break;
			}
			return rootView;
		}

		private void setupTab(final LayoutInflater inflater,
				final ViewGroup container, final View view, final String tag) {
			View tabview = createTabView(mainActivity.mTabHost.getContext(),
					tag);
			TabSpec setContent = mainActivity.mTabHost.newTabSpec(tag)
					.setIndicator(tabview);
			setContent.setContent(new TabContentFactory() {
				public View createTabContent(String tag) {
					String switchName = tag;
					switchName = switchName.replace("-", "");
					switchName = switchName.replace(" ", "");
					Merchant_Details_Pages name = Merchant_Details_Pages
							.valueOf(switchName);
					View individualTabview = view;
					switch (name) {
					case CheckIns:
						individualTabview = inflater.inflate(
								R.layout.check_ins, container, false);
						break;
					case StoreProfile:
						individualTabview = inflater.inflate(
								R.layout.store_profile, container, false);
						if (mainActivity.selectedMerchant != null) {

							final ImageButton favoriteMerchant = (ImageButton) individualTabview
									.findViewById(R.id.imageButton1);
							favoriteMerchant
									.setOnClickListener(new OnClickListener() {
										@Override
										public void onClick(View arg0) {

											favoriteMerchant
													.setImageResource(mainActivity
															.getResources()
															.getIdentifier(
																	"merchantdetails_favorite_yes",
																	"drawable",
																	mainActivity
																			.getApplicationContext()
																			.getPackageName()));
										}
									});

							final ImageButton earnQooPoints = (ImageButton) individualTabview
									.findViewById(R.id.imageButton2);
							earnQooPoints
									.setOnClickListener(new OnClickListener() {
										@Override
										public void onClick(View arg0) {

											earnQooPoints
													.setImageResource(mainActivity
															.getResources()
															.getIdentifier(
																	"merchantdetails_earnqoopoints_yes",
																	"drawable",
																	mainActivity
																			.getApplicationContext()
																			.getPackageName()));
										}
									});
							TextView merchantname = (TextView) individualTabview
									.findViewById(R.id.merchantName);
							TextView addressSmall = (TextView) individualTabview
									.findViewById(R.id.address);
							TextView addressBig = (TextView) individualTabview
									.findViewById(R.id.addressBigger);
							TextView phone = (TextView) individualTabview
									.findViewById(R.id.phoneField);

							TextView website = (TextView) individualTabview
									.findViewById(R.id.webSiteField);
							// TextView features = (TextView) individualTabview
							// .findViewById(R.id.description);
							// TextView hours = (TextView) individualTabview
							// .findViewById(R.id.hours);

							LinearLayout extraInfo = (LinearLayout) individualTabview
									.findViewById(R.id.extraInfo);
							String hoursOfOperation = "";
							for (int i = 1; i < 7; i++) {
								String openclosother = "OpenClosOther";
								String key = openclosother + i;
								String value = (String) mainActivity.selectedMerchant.attribute
										.get(key);
								if (value != null) {
									if (i != 1 && !value.equals(""))
										hoursOfOperation += "\n";
									hoursOfOperation += value;
								}
							}
							TextView hoursHeader = null;
							TextView hoursTextView = null;
							if (!hoursOfOperation.equals("")) {
								hoursHeader = new TextView(
										mainActivity.mTabHost.getContext());
								setTextView(hoursHeader,
										"Hours Of Operation: ", 16,
										Typeface.BOLD);
								hoursTextView = new TextView(
										mainActivity.mTabHost.getContext());
								setTextView(hoursTextView, hoursOfOperation,
										14, Typeface.NORMAL);
								extraInfo.addView(hoursHeader);
								extraInfo.addView(hoursTextView);
							}
							String BiggerAddress = (String) mainActivity.selectedMerchant.attribute
									.get("City")
									+ ", "
									+ (String) mainActivity.selectedMerchant.attribute
											.get("Province")
									+ ", "
									+ (String) mainActivity.selectedMerchant.attribute
											.get("PostalCode");
							String Phone = "("
									+ (String) mainActivity.selectedMerchant.attribute
											.get("AreaCode")
									+ ")"
									+ (String) mainActivity.selectedMerchant.attribute
											.get("Phone");

							LinearLayout featuresInfo = new LinearLayout(
									mainActivity);
							featuresInfo.setOrientation(LinearLayout.VERTICAL);
							String s_features = (String) mainActivity.selectedMerchant.attribute
									.get("OtherInfo");
							TextView featuresHeader = null;
							TextView featuresTextView = null;
							if (s_features != null && s_features != "") {
								featuresHeader = new TextView(
										mainActivity.mTabHost.getContext());
								setTextView(featuresHeader,
										"Store Description/Features: ", 16,
										Typeface.BOLD);
								featuresTextView = new TextView(
										mainActivity.mTabHost.getContext());

								setTextView(featuresTextView,
										Html.toHtml(Html.fromHtml(s_features)),
										14, Typeface.NORMAL);
								extraInfo.addView(featuresHeader);
								extraInfo.addView(featuresTextView);
							}

							LinearLayout additionalInfo = new LinearLayout(
									mainActivity);
							additionalInfo
									.setOrientation(LinearLayout.VERTICAL);
							Boolean containsAdditionalInformation = containsInformation(mainActivity.selectedMerchant);
							if (containsAdditionalInformation) {
								TextView additionalHeader = new TextView(
										mainActivity.mTabHost.getContext());

								setTextView(additionalHeader,
										"Additional Information: ", 16,
										Typeface.BOLD);
								additionalInfo.addView(additionalHeader);
								String MerchantType = (String) mainActivity.selectedMerchant.attribute
										.get("MerchantType");
								String RestaurantMerchantDetails[] = {
										"PayOption", "Parking", "WebCuisines",
										"WebMealTypes", "CanTakeout",
										"CanDelivery" };
								String RetailMerchantDetails[] = { "PayOption",
										"Parking", "WebCategories" };
								ArrayList<String> listNames = new ArrayList<String>();
								listNames.add("WebCuisines");
								listNames.add("WebMealTypes");
								listNames.add("WebCategories");
								String PayOption = (String) mainActivity.selectedMerchant.attribute
										.get("PayOption");
								String Parking = (String) mainActivity.selectedMerchant.attribute
										.get("Parking");

								if (MerchantType.equals("R")) {
									setUpAdditionInfo(
											RestaurantMerchantDetails,
											additionalInfo, listNames);
								} else if (MerchantType.equals("T")) {
									setUpAdditionInfo(RetailMerchantDetails,
											additionalInfo, listNames);
								}
								extraInfo.addView(additionalInfo);
							}

							merchantname.setText(mainActivity.selectedMerchant.name);
							addressSmall
									.setText((String) mainActivity.selectedMerchant.attribute
											.get("Address"));
							addressBig.setText(BiggerAddress);
							phone.setText(Phone);

							website.setText((String) mainActivity.selectedMerchant.attribute
									.get("Website"));
							int PriceLegend = Integer
									.parseInt((String) mainActivity.selectedMerchant.attribute
											.get("PriceLegend"));
							int Score = Integer
									.parseInt((String) mainActivity.selectedMerchant.attribute
											.get("Score"));
							setUpPriceLegend(PriceLegend, inflater, container,
									individualTabview);
							setUpScore(Score, inflater, container,
									individualTabview);
						}
						break;
					case Reviews:
						individualTabview = inflater.inflate(R.layout.reviews,
								container, false);
						break;
					case Menu:
						individualTabview = inflater.inflate(R.layout.menu,
								container, false);
						break;
					default:
					}
					return individualTabview;
				}
			});
			mainActivity.mTabHost.addTab(setContent);
		}

		private Boolean containsInformation(Entry entry) {
			String InformationToCheck[] = { "PayOption", "Parking",
					"WebCuisines", "WebMealTypes", "CanTakeout", "CanDelivery",
					"WebCategories" };
			for (String item : InformationToCheck) {
				if (entry.attribute.containsKey(item)
						|| entry.arrayAttribute.containsKey(item)) {
					return true;
				}
			}
			return false;
		}

		private void setTextView(TextView TV, String Text, int fontSize, int tf) {

			TV.setTypeface(null, tf);
			TV.setTextSize(fontSize);
			TV.setLayoutParams(new ViewGroup.LayoutParams(
					ViewGroup.LayoutParams.WRAP_CONTENT,
					ViewGroup.LayoutParams.WRAP_CONTENT));
			TV.setPadding(0, 0, 0, 5);
			TV.setText(Text);
		}

		private void setUpAdditionInfo(String intoTitle[],
				LinearLayout additionalInfo, ArrayList<String> listNames) {
			LinearLayout infoToadd = null;
			for (String item : intoTitle) {
				infoToadd = new LinearLayout(mainActivity.mTabHost.getContext());
				infoToadd.setOrientation(LinearLayout.HORIZONTAL);
				TextView titleTag = new TextView(
						mainActivity.mTabHost.getContext());
				if (listNames.contains(item)) {
					if (mainActivity.selectedMerchant.arrayAttribute.containsKey(item)) {
						titleTag.setTypeface(null, Typeface.BOLD);
						titleTag.setText(item + ":" + " ");
						TextView infoTag = new TextView(
								mainActivity.mTabHost.getContext());
						ArrayList<String> infoListToAdd = (ArrayList<String>) mainActivity.selectedMerchant.arrayAttribute
								.get(item);
						infoToadd.addView(titleTag);
						boolean secondOrLater = false;
						for (String infoItem : infoListToAdd) {
							if (secondOrLater) {
								infoTag.setText(infoTag.getText() + "," + " "
										+ infoItem + " ");
							} else {
								infoTag.setText(infoTag.getText() + infoItem
										+ " ");
							}

							secondOrLater = true;
						}
						infoToadd.addView(infoTag);
						additionalInfo.addView(infoToadd);
					}
				} else {
					TextView infoTag = new TextView(
							mainActivity.mTabHost.getContext());
					String value = (String) mainActivity.selectedMerchant.attribute
							.get(item);
					if (value != null && value != "") {
						titleTag.setTypeface(null, Typeface.BOLD);
						titleTag.setText(item + ":" + " ");
						if (value.equals("Y"))
							value = "Yes";
						else if (value.equals("N"))
							value = "No";
						infoTag.setText(value);
						infoToadd.addView(titleTag);
						infoToadd.addView(infoTag);
						additionalInfo.addView(infoToadd);
					}
				}
			}

		}

		private void setUpPriceLegend(int priceLegend,
				final LayoutInflater inflater, final ViewGroup container,
				final View view) {
			switch (priceLegend) {
			case 2:
				break;
			case 3:
				setPicture(view, R.id.imageView6,
						"merchantdetails_money_green_middle");
				break;
			case 4:
				setPicture(view, R.id.imageView6,
						"merchantdetails_money_green_middle");
				setPicture(view, R.id.imageView7,
						"merchantdetails_money_green_middle");
				break;
			case 5:
				setPicture(view, R.id.imageView6,
						"merchantdetails_money_green_middle");
				setPicture(view, R.id.imageView7,
						"merchantdetails_money_green_middle");
				setPicture(view, R.id.imageView7,
						"merchantdetails_money_green_right");
				break;
			default:
				break;
			}
		}

		private void setUpScore(int score, final LayoutInflater inflater,
				final ViewGroup container, final View view) {
			switch (score) {
			case 0:
				break;
			case 1:
				setPicture(view, R.id.imageView9,
						"merchantdetails_starrating_left_halfstar");
				break;
			case 2:
				setPicture(view, R.id.imageView9,
						"merchantdetails_starrating_yellow_left");

				break;
			case 3:
				setPicture(view, R.id.imageView9,
						"merchantdetails_starrating_yellow_left");
				setPicture(view, R.id.imageView10,
						"merchantdetails_starrating_halfstar");
				break;
			case 4:

				setPicture(view, R.id.imageView9,
						"merchantdetails_starrating_yellow_left");
				setPicture(view, R.id.imageView10,
						"merchantdetails_starrating_yellow_middle");

				break;
			case 5:

				setPicture(view, R.id.imageView9,
						"merchantdetails_starrating_yellow_left");
				setPicture(view, R.id.imageView10,
						"merchantdetails_starrating_yellow_middle");
				setPicture(view, R.id.imageView11,
						"merchantdetails_starrating_halfstar");

			case 6:
				setPicture(view, R.id.imageView9,
						"merchantdetails_starrating_yellow_left");
				setPicture(view, R.id.imageView10,
						"merchantdetails_starrating_yellow_middle");
				setPicture(view, R.id.imageView11,
						"merchantdetails_starrating_yellow_middle");

				break;
			case 7:
				setPicture(view, R.id.imageView9,
						"merchantdetails_starrating_yellow_left");
				setPicture(view, R.id.imageView10,
						"merchantdetails_starrating_yellow_middle");
				setPicture(view, R.id.imageView11,
						"merchantdetails_starrating_yellow_middle");
				setPicture(view, R.id.imageView12,
						"merchantdetails_starrating_halfstar");
				break;
			case 8:

				setPicture(view, R.id.imageView9,
						"merchantdetails_starrating_yellow_left");
				setPicture(view, R.id.imageView10,
						"merchantdetails_starrating_yellow_middle");
				setPicture(view, R.id.imageView11,
						"merchantdetails_starrating_yellow_middle");
				setPicture(view, R.id.imageView12,
						"merchantdetails_starrating_yellow_middle");

				break;
			case 9:

				setPicture(view, R.id.imageView9,
						"merchantdetails_starrating_yellow_left");
				setPicture(view, R.id.imageView10,
						"merchantdetails_starrating_yellow_middle");
				setPicture(view, R.id.imageView11,
						"merchantdetails_starrating_yellow_middle");
				setPicture(view, R.id.imageView12,
						"merchantdetails_starrating_yellow_middle");
				setPicture(view, R.id.imageView13,
						"merchantdetails_starrating_right_halfstar");
				break;
			case 10:

				setPicture(view, R.id.imageView9,
						"merchantdetails_starrating_yellow_left");
				setPicture(view, R.id.imageView10,
						"merchantdetails_starrating_yellow_middle");
				setPicture(view, R.id.imageView11,
						"merchantdetails_starrating_yellow_middle");
				setPicture(view, R.id.imageView12,
						"merchantdetails_starrating_yellow_middle");
				setPicture(view, R.id.imageView13,
						"merchantdetails_starrating_yellow_right");

				break;
			default:
				break;
			}
		}

		private void setPicture(View parent, int id, String picture) {
			ImageView IV = (ImageView) parent.findViewById(id);
			IV.setImageResource(this.getResources().getIdentifier(picture,
					"drawable",
					mainActivity.getApplicationContext().getPackageName()));
		}

		private static View createTabView(final Context context,
				final String text) {
			View view = LayoutInflater.from(context).inflate(R.layout.tabs_bg,
					null);
			TextView tv = (TextView) view.findViewById(R.id.tabsText);
			tv.setText(text);
			return view;
		}

		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			super.onActivityCreated(savedInstanceState);
			int j = getArguments().getInt(ARG_PAGE_NUMBER);
			String menuItem = getResources().getStringArray(R.array.menu_item)[j];
			menuItem = menuItem.replace(" ", "");
			FragmentName name = FragmentName.valueOf(menuItem);
			switch (name) {
			case NearBy:
				mainActivity.mCurrentLocation = mainActivity.mLocationClient.getLastLocation();
				NearByModelAdapter.LoadModel(mainActivity.listToDisplay, mainActivity.mCurrentLocation);
				String[] ids = new String[NearByModelAdapter.Items.size()];
				for (int i = 0; i < ids.length; i++) {
					ids[i] = Integer.toString(i + 1);
				}
				NearByItemAdapter Adapter = new NearByItemAdapter(
						getActivity(), R.layout.merchant_list_item, ids);
				mainActivity.listViewToDisplay.setAdapter(Adapter);
				mainActivity.listViewToDisplay
						.setOnItemClickListener(new OnItemClickListener() {
							@Override
							public void onItemClick(AdapterView<?> parent,
									View view, int position, long id) {
								Toast.makeText(
										mainActivity.getApplicationContext(),
										"Click ListItem Number " + position,
										Toast.LENGTH_LONG).show();
								mainActivity.selectedMerchant = mainActivity.listToDisplay.get(position);
								mainActivity.changeFragment(7);
							}
						});
				break;
			case Login:
				break;
			default:
				break;
			}
			if (mainActivity.progress != null)
				mainActivity.progress.cancel();
		}
		public enum Merchant_Details_Pages {

			CheckIns, StoreProfile, Reviews, Menu

		}

		public enum FragmentName {

			Login, Home, Search, Category, NearBy, AllDeals, CheckIn, Vouchers, Stamps, Qoopons,

		}
	}

	
