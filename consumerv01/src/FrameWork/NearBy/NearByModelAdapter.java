package FrameWork.NearBy;

import java.util.ArrayList;
import java.util.List;

import ui.DisplayListItem;
import FrameWork.Entry;
import android.location.Location;

import com.nostra13.universalimageloader.core.ImageLoader;

public class NearByModelAdapter {

	public static ArrayList<DisplayListItem> Items;
	
	public static void LoadModel(List<Entry> list , Location loc) {
		int id = 0;
		Items = new ArrayList<DisplayListItem>();
		
		for (Entry item : list)
		{
			float results[] = new float[3];
			String distance= null;
			String Longitude = (String)item.attribute.get("Longitude");
			String Latitude = (String)item.attribute.get("Latitude");
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
			String imgpath =(String)item.attribute.get("LogoPath");
			if(item.attribute.get("Id")!=null)
			{
				int Id = Integer.parseInt((String)item.attribute.get("Id"));
			}
			String storeName=item.name;
			String Address=(String)item.attribute.get("Address");
			String promotion=(String)item.attribute.get("SpecialOffer");
			String info[] = {storeName ,Address ,promotion , distance};
			Items.add(new DisplayListItem(++id, imgpath, info));
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
