package com.photo.suit.bodybuilder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

public class StartActivity extends Activity {
	private static final int RESULT_LOAD_IMAGE = 101;
	private static final int CAMERA_REQUEST = 202;
	Uri imgUri;
	static Bitmap bmp;
	Button rate,more,exit;
	Dialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.startactivity);
	}

	
	
	public void galImg(View v) {
		Intent photoPickerIntent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		startActivityForResult(photoPickerIntent, RESULT_LOAD_IMAGE);
	}


	@SuppressLint({ "SimpleDateFormat", "SdCardPath" })
	public void camImg(View v) {
		 Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		 (new File("/sdcard/Pictures/")).mkdirs();
		 SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy-hhmmss");
		 
		 imgUri = Uri.fromFile(new File("/sdcard/Pictures/img"+sdf.format(new Date())+".jpg"));
		 cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);
         startActivityForResult(cameraIntent, CAMERA_REQUEST);  
       
 			
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		super.onActivityResult(requestCode, resultCode, data);
		BitmapFactory.Options BitmapFactoryOptionsbfo = new BitmapFactory.Options();

		BitmapFactoryOptionsbfo.inPreferredConfig = Bitmap.Config.RGB_565;
		if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK
				&& null != data) {
			Uri selectedImage = data.getData();
			try {
				InputStream in = getContentResolver().openInputStream(selectedImage);
				bmp = BitmapFactory.decodeStream(in,null,BitmapFactoryOptionsbfo);
			} catch (FileNotFoundException e) {
				
				e.printStackTrace();
			}

			if(bmp == null){
				Toast.makeText(getApplicationContext(), "Image could not be loaded..", Toast.LENGTH_LONG).show();
				return;
			}
			 Intent i = new Intent(this,CropActivity.class);
			//i.putExtra("Image", bmp);
			 StaticBmp.startBmp = bmp;
			startActivity(i);
		}
		else if(requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
			 bmp = BitmapFactory.decodeFile(imgUri.getPath(), BitmapFactoryOptionsbfo);
			 if(bmp == null){
					Toast.makeText(getApplicationContext(), "Image could not be loaded..", Toast.LENGTH_LONG).show();
					return;
				}
			 Intent i = new Intent(this,CropActivity.class);
//			 i.putExtra("Image", bmp);
			 StaticBmp.startBmp = bmp;
			 startActivity(i);
		}
		else{
			Toast.makeText(getApplicationContext(), "Image could not be loaded..", Toast.LENGTH_LONG).show();
		}
	}
	@Override
	public void onBackPressed() {
	    dialogFunction();
		dialog.show();
	}
	public void dialogFunction() {
		dialog = new Dialog(this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		dialog.setContentView(R.layout.dialog);
		dialoginit();
		rate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent rate = null;
				rate = new Intent(Intent.ACTION_VIEW,Uri.parse("https://play.google.com/store/apps/details?id=com.photo.suit.bodybuilder"));
				startActivity(rate);
			}
		});
		more.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Toast.makeText(getApplicationContext(), "Thanks for valueble feedback..!!", Toast.LENGTH_SHORT).show();
				StartActivity.this.finish();
			}
		});

		exit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				StartActivity.this.finish();
				dialog.dismiss();
			}
		});

	}

	public void dialoginit() {
		rate = (Button) dialog.findViewById(R.id.rate);
		more = (Button) dialog.findViewById(R.id.more);
		exit = (Button) dialog.findViewById(R.id.exit);
	}
}
