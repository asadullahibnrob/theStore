package com.asad.thestore;

import java.io.InputStream;
import java.util.ArrayList;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class StoreAdapter extends ArrayAdapter<Models> {
     ArrayList<Models> list;
     LayoutInflater inflater;
     int Resource;
     int targetWidth = 700;
     int targetHeight = 400;
     ViewHolder holder;
     BitmapFactory.Options options = new BitmapFactory.Options();
	
	
	
	
	public StoreAdapter(Context context, int resource,
			ArrayList<Models> objects) {
		super(context, resource, objects);
		
		inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		Resource = resource;
		list = objects;
				
	}




	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		if (v == null){
			holder = new ViewHolder();
			v = inflater.inflate(Resource, null);
			holder.name  = (TextView)v.findViewById(R.id.gameName);
			holder.imageview = (ImageView)v.findViewById(R.id.ivImage);
			
			v.setTag(holder);
			}else{
		holder = (ViewHolder) v.getTag();}
		
		new DownloadImageTask(holder.imageview).execute(list.get(position).getImage());
		holder.name.setText(list.get(position).getName());

		
		return v;
	}
	
	
	
	
	static class ViewHolder{
		public TextView name;
		public ImageView imageview;
		public TextView uuid;
		public TextView tags;
		public TextView version;
	}
	
	
	
	
	private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
		ImageView bmImage;

		public DownloadImageTask(ImageView bmImage) {
			this.bmImage = bmImage;
		}

		protected Bitmap doInBackground(String... urls) {
			String urldisplay = urls[0];
			Bitmap mIcon11 = null;
			try {
				InputStream in = new java.net.URL(urldisplay).openStream();
				mIcon11 = BitmapFactory.decodeStream(in);
			} catch (Exception e) {
				Log.e("Error", e.getMessage());
				e.printStackTrace();
			}
			return mIcon11;
		}
	
		protected void onPostExecute(Bitmap result) {
			bmImage.setImageBitmap(result);
			
			
		}
	
}}
