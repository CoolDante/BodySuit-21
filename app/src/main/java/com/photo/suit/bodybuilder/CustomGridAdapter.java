package com.photo.suit.bodybuilder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
	public class CustomGridAdapter extends BaseAdapter{
		private Context mContext;
		//   private final String[] web;
		private final Integer[] Imageid;
		public CustomGridAdapter(Context c,Integer[] Imageid ) {
			mContext = c;
			this.Imageid = Imageid;
			//  this.web = web;
		}
		@Override
		public int getCount() {
			return Imageid.length;
		}
		@Override
		public Object getItem(int position) {
			return null;
		}
		@Override
		public long getItemId(int position) {
			return 0;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View grid;

			if (convertView == null) {
				LayoutInflater inflater = (LayoutInflater) mContext
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				//grid = new View(mContext);
				grid = inflater.inflate(R.layout.singlegrid, null);


			} else {
				grid = (View) convertView;
			}
			ImageView imageView = (ImageView)grid.findViewById(R.id.grid_image);
			imageView.setImageResource(Imageid[position]);

			imageView.setBackgroundColor(000000);
			imageView.setScaleType(ImageView.ScaleType.FIT_XY);

			return grid;
		}
	}

