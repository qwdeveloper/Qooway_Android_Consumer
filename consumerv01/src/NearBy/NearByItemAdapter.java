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
		distancePosition.setText("Wait for GPS");

		Boolean imageRecieved = false;
		String baseUri= "http://online.profitek.com/Qooway/StorePictures/";
		if(item.ThumbNail.equals(""))
		{
			imageView.setBackgroundResource(context.getResources().getIdentifier(
					"temp_logo", "drawable", context.getPackageName()));
		}
		else{
			
				String imageUri = baseUri +item.ThumbNail;
		ImageLoader IM = ImageLoader.getInstance();
		IM.displayImage(imageUri, imageView); 
		}

/*		String imageFile = NearByModelAdapter.GetbyId(id).IconFile;

		textView.setText(NearByModelAdapter.GetbyId(id).Name);
		// get input stream
		InputStream ims = null;
		try {
			ims = context.getAssets().open(imageFile);
		} catch (IOException e) {
			e.printStackTrace();
		}

*/

		return rowView;

	}
	

}


