package ui.drawer;

import java.util.ArrayList;

public class DrawerModelAdapter {

    public static ArrayList<DrawerItem> Items;

    public static void LoadModel(String[] names , String files[]) {

        Items = new ArrayList<DrawerItem>();
        for(int i = 0; i< names.length ;i++)
        {       	
        Items.add(new DrawerItem(i+1, files[i], names[i]));
        }
    }

    public static DrawerItem GetbyId(int id){

        for(DrawerItem item : Items) {
            if (item.Id == id) {
                return item;
            }
        }
        return null;
    }
}