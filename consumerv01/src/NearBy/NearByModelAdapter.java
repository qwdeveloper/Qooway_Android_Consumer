package NearBy;

import java.util.ArrayList;
import java.util.List;

import com.example.consumerv01.DisplayListItem;
import com.example.consumerv01.Entry;
import com.nostra13.universalimageloader.core.ImageLoader;

public class NearByModelAdapter {

	public static ArrayList<DisplayListItem> Items;

	public static void LoadModel(List<Entry> list) {
		

		Items = new ArrayList<DisplayListItem>();
		for (Entry item : list)
		{
			String imgpath = "";
			if(item.info.containsKey("StorePicturePath"))
				imgpath =(String)item.info.get("StorePicturePath");
			int Id = Integer.parseInt((String)item.info.get("Id"));
			String storeName=item.name;
			String Address=(String)item.info.get("Address");
			String promotion="Regular normal promotion for now";
			String info[] = {storeName ,Address ,promotion};
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
