package NearBy;

import java.util.ArrayList;
import java.util.List;

import android.location.Location;

import com.example.consumerv01.DisplayListItem;
import com.example.consumerv01.Entry;
import com.nostra13.universalimageloader.core.ImageLoader;

public class NearByModelAdapter {

	public static ArrayList<DisplayListItem> Items;

	public static void LoadModel(List<Entry> list , Location loc) {
		
		Items = new ArrayList<DisplayListItem>();
		
		for (Entry item : list)
		{
			float results[] = new float[3];
			String distance= null;
			String Longitude = (String)item.info.get("Longitude");
			String Latitude = (String)item.info.get("Latitude");
			if(!Longitude.equals("")&&!Latitude.equals(""))
			{
				double venueLongitude = Double.parseDouble(Longitude);
				double venueLatitude = Double.parseDouble(Latitude);
				Location.distanceBetween(loc.getLatitude(), loc.getLongitude(), venueLatitude, venueLongitude , results);
				results[0] = (Math.round(results[0]/100))/10;
				if(results[0]>1000)
					results[0] = Math.round(results[0]);
				distance = String.valueOf(results[0]) + "KM";
			}
			else
			{
				distance= "N/A";
			}
			String imgpath =(String)item.info.get("LogoPath");
			int Id = Integer.parseInt((String)item.info.get("Id"));
			String storeName=item.name;
			String Address=(String)item.info.get("Address");
			String promotion=(String)item.info.get("SpecialOffer");
			String info[] = {storeName ,Address ,promotion , distance};
			Items.add(new DisplayListItem(Id, imgpath, info));
		}

	}

	public static DisplayListItem GetbyId(int id) {

		for (DisplayListItem item : Items) {
			if (item.Id == id) {
				return item;
			}
		}
		return null;
	}
	
	

}
