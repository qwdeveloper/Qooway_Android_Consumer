package NearBy;

import java.util.ArrayList;

import com.example.consumerv01.DisplayListItem;

public class NearByModelAdapter {

	public static ArrayList<DisplayListItem> Items;

	public static void LoadModel(String picture[], ArrayList<String[]> info) {

		Items = new ArrayList<DisplayListItem>();
		for (int i = 0; i < info.size(); i++) {
			Items.add(new DisplayListItem(i + 1, picture[i], info.get(i)));
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
