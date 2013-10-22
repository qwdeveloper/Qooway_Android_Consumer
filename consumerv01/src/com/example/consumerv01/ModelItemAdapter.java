package com.example.consumerv01;

import java.util.ArrayList;

public class ModelItemAdapter {

    public static ArrayList<Item> Items;

    public static void LoadModel(String[] names , String files[]) {

        Items = new ArrayList<Item>();
        for(int i = 0; i< names.length ;i++)
        {       	
        Items.add(new Item(i+1, files[i], names[i]));
        }
    }

    public static Item GetbyId(int id){

        for(Item item : Items) {
            if (item.Id == id) {
                return item;
            }
        }
        return null;
    }
  

}