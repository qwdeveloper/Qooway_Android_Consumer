package NearBy;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;

import com.example.consumerv01.DisplayListItem;
import com.example.consumerv01.R;
import com.example.consumerv01.R.id;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

public class NearByItemAdapter extends ArrayAdapter<String>{

	private final Context context;
	private final String[] Ids;
	private final int rowResourceId;

	
	public NearByItemAdapter(Context context, int textViewResourceId,
			String[] objects) {
		super(context, textViewResourceId, objects);
		this.context = context;
		this.Ids = objects;
		this.rowResourceId = textViewResourceId;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View rowView = inflater.inflate(rowResourceId, parent, false);
		ImageView imageView = (ImageView) rowView.findViewById(R.id.list_image);
		TextView namePosition = (TextView) rowView.findViewById(R.id.shopName);
		TextView addressPosition = (TextView) rowView.findViewById(R.id.address);
		TextView promotionPosition = (TextView) rowView.findViewById(R.id.promotion);
		TextView distancePosition = (TextView) rowView.findViewById(R.id.distance);
        int id = Integer.parseInt(Ids[position]);

        DisplayListItem item = NearByModelAdapter.GetbyId(id);
		namePosition.setText(item.Info[0]);
		addressPosition.setText(item.Info[1]);
		promotionPosition.setText(item.Info[2]);
		distancePosition.setText(item.Info[3]);

		Boolean imageRecieved = false;
		DisplayImageOptions options = new DisplayImageOptions.Builder()
        .cacheInMemory(true) 
        .cacheOnDisc(true)
        .build();
		String baseUri= "http://192.168.1.55";
		int o =123;
		if(addressPosition.getText().equals("1788 West Broadway"))
			o = 12334;
		if(item.ThumbNail=="")
		{
			imageView.setBackgroundResource(context.getResources().getIdentifier(
					"temp_logo", "drawable", context.getPackageName()));
		}
		else{
			
		String imageUri = baseUri +item.ThumbNail;
		ImageLoader IM = ImageLoader.getInstance();
		IM.displayImage(imageUri, imageView, options); 
		}


		return rowView;

	}
	

}


