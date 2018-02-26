package com.photo.suit.bodybuilder;

import java.lang.ref.WeakReference;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.PorterDuffXfermode;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Pair;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.choiboi.imagecroppingexample.gestures.MoveGestureDetector;
import com.choiboi.imagecroppingexample.gestures.RotateGestureDetector;



public class CropActivity extends Activity implements OnTouchListener {

	// Member fields.
	private ImageView mImg;
	private ImageView mTemplateImg;
	private int mScreenWidth;
	private int mScreenHeight;
	private CropHandler mCropHandler;
	private static ProgressDialog mProgressDialog;

	static Bitmap cropmain;
	private Matrix mMatrix = new Matrix();
	private float mScaleFactor = 0.8f;
	private float mRotationDegrees = 0.f;
	private float mFocusX = 0.f;
	private float mFocusY = 0.f;
	private int mImageHeight, mImageWidth;
	private ScaleGestureDetector mScaleDetector;
	private RotateGestureDetector mRotateDetector;
	private MoveGestureDetector mMoveDetector;
	public Context c;
	private int mTemplateWidth;
	private int mTemplateHeight;
	Bitmap resultingImage;
	static ImageView zoom;

	// Constants
	public static final int MEDIA_GALLERY = 1;
	public static final int TEMPLATE_SELECTION = 2;
	public static final int DISPLAY_IMAGE = 3;

	int to_w,to_h;
	int width1=0;
	int height1=0;
	Drawable d ;
	Bitmap b;
	FrameLayout compositeImageView,fm;
	static ImageView ib,apply;
	ImageView img,applie;
	Bitmap passed;
	DrawingView drawingView;

	static FrameLayout main;
	protected void init() {

		height1= main.getHeight();
		width1 = main.getWidth();

	} 
	Path    mPath;
	float x,y ,diffx,diffy;
	ImageView image;
	Bitmap map;
	Button drawing;
	OnTouchListener om;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_crop);

		// mSelectedVersion = getIntent().getExtras().getInt(MainActivity.CROP_VERSION_SELECTED_KEY, -1);

		zoom=(ImageView)findViewById(R.id.zoom);
		mImg = (ImageView) findViewById(R.id.cp_img);
		mTemplateImg = (ImageView) findViewById(R.id.cp_face_template);
		mImg.setOnTouchListener(this);

		main=(FrameLayout) findViewById(R.id.main);


		// Get screen size in pixels.
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		mScreenHeight = metrics.heightPixels;
		mScreenWidth = metrics.widthPixels;

		Bitmap faceTemplate = BitmapFactory.decodeResource(getResources(), R.drawable.face_oval);
		mTemplateWidth = faceTemplate.getWidth();
		mTemplateHeight = faceTemplate.getHeight();

		// Set template image accordingly to device screen size.
		if (mScreenWidth == 320 && mScreenHeight == 480) {
			mTemplateWidth = 218;
			mTemplateHeight = 300;
			faceTemplate = Bitmap.createScaledBitmap(faceTemplate, mTemplateWidth, mTemplateHeight, true);
			mTemplateImg.setImageBitmap(faceTemplate);
		}

		mImg.setImageBitmap(StartActivity.bmp);
		mImageHeight = StartActivity.bmp.getHeight();
		mImageWidth = StartActivity.bmp.getWidth();

		// View is scaled by matrix, so scale initially
		mMatrix.postScale(mScaleFactor, mScaleFactor);
		mImg.setImageMatrix(mMatrix);

		// Setup Gesture Detectors
		mScaleDetector = new ScaleGestureDetector(getApplicationContext(), new ScaleListener());
		mRotateDetector = new RotateGestureDetector(getApplicationContext(), new RotateListener());
		mMoveDetector = new MoveGestureDetector(getApplicationContext(), new MoveListener());

		// Instantiate Thread Handler.
		mCropHandler = new CropHandler(this);
	}

	public void onCropImageButton(View v) {
		// Create progress dialog and display it.
		//findViewById(R.id.imageButton1).setVisibility(View.VISIBLE);
		mProgressDialog = new ProgressDialog(v.getContext());
		mProgressDialog.setCancelable(false);
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgressDialog.setMessage("Cropping Image\nPlease Wait.....");
		mProgressDialog.show();

		// Setting values so that we can retrive the image from 
		// ImageView multiple times.
		mImg.buildDrawingCache(true);
		mImg.setDrawingCacheEnabled(true);
		mTemplateImg.buildDrawingCache(true);
		mTemplateImg.setDrawingCacheEnabled(true);

		// Create new thread to crop.
		new Thread(new Runnable() {
			@Override
			public void run() {
				// Crop image using the correct template size.
				Bitmap croppedImg = null;
				/* if (mScreenWidth == 320 && mScreenHeight == 480)*/ {
					{
						croppedImg = ImageProcess.cropImageVer2(mImg.getDrawingCache(true), mTemplateImg.getDrawingCache(true), mTemplateWidth, mTemplateHeight);
						cropmain=croppedImg;
					}
				} /*else {
                    {
                        croppedImg = ImageProcess.cropImageVer2(mImg.getDrawingCache(true), mTemplateImg.getDrawingCache(true), mTemplateWidth, mTemplateHeight);
                        cropmain=croppedImg;
                    }
                }*/
				mImg.setDrawingCacheEnabled(false);
				mTemplateImg.setDrawingCacheEnabled(false);

				// Send a message to the Handler indicating the Thread has finished.
				mCropHandler.obtainMessage(DISPLAY_IMAGE, -1, -1, croppedImg).sendToTarget();
			}
		}).start();
	}

	public void onChangeTemplateButton(View v) {

		Intent intent = new Intent(this, TemplateSelectDialog.class);
		startActivityForResult(intent, TEMPLATE_SELECTION);
	}
	public void onChangeImageButton(View v) {
		//cropmain=resultingImage;
		Intent i = new Intent(getApplicationContext(), MainActivity.class);
		startActivity(i);
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		//  findViewById(R.id.imageButton1).setVisibility(View.GONE);
		if (resultCode == RESULT_OK) {
			if (requestCode == TEMPLATE_SELECTION) {
				int pos = data.getExtras().getInt(TemplateSelectDialog.POSITION);
				Bitmap templateImg = null;

				// Change template according to what the user has selected.
				switch(pos) {
				case 0:
					templateImg = BitmapFactory.decodeResource(getResources(), R.drawable.face_oblong);
					break;
				case 1:
					templateImg = BitmapFactory.decodeResource(getResources(), R.drawable.face_oval);
					break;
				case 2:
					templateImg = BitmapFactory.decodeResource(getResources(), R.drawable.face_round);
					break;
				case 3:
					templateImg = BitmapFactory.decodeResource(getResources(), R.drawable.face_square);
					break;
				case 4:
					templateImg = BitmapFactory.decodeResource(getResources(), R.drawable.face_triangular);
					break;
				}

				mTemplateWidth = templateImg.getWidth();
				mTemplateHeight = templateImg.getHeight();

				// Resize template if necessary.
				if (mScreenWidth == 320 && mScreenHeight == 480) {
					mTemplateWidth = 218;
					mTemplateHeight = 300;
					templateImg = Bitmap.createScaledBitmap(templateImg, mTemplateWidth, mTemplateHeight, true);
				}
				mTemplateImg.setImageBitmap(templateImg);
			}
		}
	}

	private class CropHandler extends Handler {
		WeakReference<CropActivity> mThisCA;

		CropHandler(CropActivity ca) {
			mThisCA = new WeakReference<CropActivity>(ca);
		}

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			CropActivity ca = mThisCA.get();
			if (msg.what == DISPLAY_IMAGE) {
				mProgressDialog.dismiss();
				Bitmap cropImg = (Bitmap) msg.obj;

				// Setup an AlertDialog to display cropped image.
				AlertDialog.Builder builder = new AlertDialog.Builder(ca);
				builder.setTitle("Final Cropped Image");
				builder.setIcon(new BitmapDrawable(ca.getResources(), cropImg));
				builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						if(cropmain!=null)
						{
							/*CropActivity cp=new CropActivity();
                          cp.onChangeImageButton();*/

							dialog.cancel();
							Intent i = new Intent(getApplicationContext(), MainActivity.class);
							startActivity(i);
						}
					}
				});
				AlertDialog dialog = builder.create();
				dialog.show();
			}
		}
	}


	public boolean onTouch(View v, MotionEvent event) {
		mScaleDetector.onTouchEvent(event);
		mRotateDetector.onTouchEvent(event);
		mMoveDetector.onTouchEvent(event);

		float scaledImageCenterX = (mImageWidth * mScaleFactor) / 2;
		float scaledImageCenterY = (mImageHeight * mScaleFactor) / 2;

		mMatrix.reset();
		mMatrix.postScale(mScaleFactor, mScaleFactor);
		mMatrix.postRotate(mRotationDegrees, scaledImageCenterX, scaledImageCenterY);
		mMatrix.postTranslate(mFocusX - scaledImageCenterX, mFocusY - scaledImageCenterY);

		ImageView view = (ImageView) v;
		view.setImageMatrix(mMatrix);
		return true;
	}

	private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
		@Override
		public boolean onScale(ScaleGestureDetector detector) {
			mScaleFactor *= detector.getScaleFactor();
			mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 10.0f));

			return true;
		}
	}

	private class RotateListener extends RotateGestureDetector.SimpleOnRotateGestureListener {
		@Override
		public boolean onRotate(RotateGestureDetector detector) {
			mRotationDegrees -= detector.getRotationDegreesDelta();
			return true;
		}
	}

	private class MoveListener extends MoveGestureDetector.SimpleOnMoveGestureListener {
		@Override
		public boolean onMove(MoveGestureDetector detector) {
			PointF d = detector.getFocusDelta();
			mFocusX += d.x;
			mFocusY += d.y;

			return true;
		}
	}

	public void Draw()
	{

		init();


		d =new BitmapDrawable(getResources(),loadBitmapFromView(main) );

		if(d.getIntrinsicHeight()>d.getIntrinsicWidth())
		{
			FrameLayout.LayoutParams l1=new FrameLayout.LayoutParams(height1*d.getIntrinsicWidth()/d.getIntrinsicHeight(),height1);
			l1.gravity=Gravity.CENTER;
			mImg.setLayoutParams(l1);
			width1=height1*d.getIntrinsicWidth()/d.getIntrinsicHeight();
			to_w=height1*d.getIntrinsicWidth()/d.getIntrinsicHeight();
			to_h=height1;

		}

		else
		{
			FrameLayout.LayoutParams l1=new FrameLayout.LayoutParams(width1,width1*d.getIntrinsicHeight()/d.getIntrinsicWidth()) ;
			l1.gravity=Gravity.CENTER;
			mImg.setLayoutParams(l1);
			height1=width1*d.getIntrinsicHeight()/d.getIntrinsicWidth();
			to_w=width1;
			to_h=width1*d.getIntrinsicHeight()/d.getIntrinsicWidth();
		}
		drawingView = new DrawingView(getApplicationContext(),to_w,to_h);
		compositeImageView = (FrameLayout) findViewById(R.id.main);
		ib = (ImageView) findViewById(R.id.imageButton2);


		compositeImageView.removeView(drawingView);
		compositeImageView.addView(drawingView);
		ib.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				
			
				//Container1.bit = loadBitmapFromView(findViewById(R.id.fm));
				//Bitmap bitmap2 = Container1.bit;
				DrawingView.m_Paint.setColor(Color.TRANSPARENT);
				drawingView.invalidate();
				Bitmap bitmap2 = loadBitmapFromView(main);
				
				
				Bitmap resultingImage = Bitmap.createBitmap(bitmap2.getWidth(),
						bitmap2.getHeight(), bitmap2.getConfig());

				Canvas canvas = new Canvas(resultingImage);

				Paint paint = new Paint();
				paint.setAntiAlias(true);

				for (Pair<Path, Paint> p : Container1.paths) {

					canvas.drawPath(p.first, paint);

				}


				paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
				canvas.drawBitmap(bitmap2,0,0, paint);

				Matrix matrix = new Matrix();
				matrix.postScale(1f, 1f);

				if(Container1.xs<0)
				{
					Container1.xs=0;
				}

				if(Container1.ys<0)
				{
					Container1.ys=0;
				}
				if(Container1.yl>resultingImage.getHeight())
				{
					Container1.yl=resultingImage.getHeight();
				}

				if(Container1.xl>resultingImage.getWidth())
				{
					Container1.xl=resultingImage.getWidth();
				}
				if(Container1.xl-Container1.xs>5){
					resultingImage = Bitmap.createBitmap(resultingImage, Container1.xs, Container1.ys,Container1.xl-Container1.xs,Container1.yl-Container1.ys, matrix, true);
					Container1.passed=resultingImage;
					//Container1.clip=true;

					cropmain=resultingImage;
					compositeImageView.removeView(drawingView);
					Intent i = new Intent(getApplicationContext(), MainActivity.class);
					startActivity(i);
					findViewById(R.id.imageButton2).setEnabled(false);
					findViewById(R.id.cropin).setVisibility(View.GONE);
					findViewById(R.id.cropimage).setVisibility(View.VISIBLE);
				}
				else 
					Toast.makeText(getApplicationContext(), "CROP BIGGER",
							Toast.LENGTH_LONG).show();
				
				
			}
		});
	}
	public static Bitmap loadBitmapFromView(View v) {

		Bitmap b = Bitmap.createBitmap(v.getWidth(), v.getHeight(),
				Bitmap.Config.ARGB_8888);
		Canvas c = new Canvas(b);
		v.draw(c);
		return b;

	}
	public void clickonowncrop(View v){
		findViewById(R.id.cropin).setVisibility(View.VISIBLE);
		findViewById(R.id.cropimage).setVisibility(View.GONE);
		findViewById(R.id.cropout).setVisibility(View.GONE);

		findViewById(R.id.imageButton2).setEnabled(false);
		Toast.makeText(getApplicationContext(), "CROP IMAGE",
				Toast.LENGTH_LONG).show();
		Draw();

	}
	public void clickondocrop(View v){
		//	findViewById(R.id.imageButton1).setVisibility(View.GONE);
		findViewById(R.id.cp_face_template).setVisibility(View.VISIBLE);
		findViewById(R.id.cropout).setVisibility(View.VISIBLE);
		findViewById(R.id.cropin).setVisibility(View.GONE);
		findViewById(R.id.cropimage).setVisibility(View.GONE);
		findViewById(R.id.fcp_info_text).setVisibility(View.VISIBLE);
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		findViewById(R.id.fcp_info_text).setVisibility(View.INVISIBLE);
		//	findViewById(R.id.imageButton1).setVisibility(View.GONE);
		findViewById(R.id.cp_face_template).setVisibility(View.GONE);
		if(findViewById(R.id.cropin).getVisibility()==(View.VISIBLE)){
			findViewById(R.id.cropin).setVisibility(View.GONE);
			findViewById(R.id.cropimage).setVisibility(View.VISIBLE);
		}else if(findViewById(R.id.cropout).getVisibility()==(View.VISIBLE)){
			findViewById(R.id.cropimage).setVisibility(View.VISIBLE);
			findViewById(R.id.cropout).setVisibility(View.GONE);
		}else if(findViewById(R.id.cropimage).getVisibility()==(View.VISIBLE)){
			super.onBackPressed();}
	}
}
