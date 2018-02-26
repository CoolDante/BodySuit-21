package com.photo.suit.bodybuilder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainActivity extends Activity {
	Button b;
	int he = 300, wi = 300, to, le;
	float ro;
	int mov = 0;
	private static File APP_FILE_PATH = new File(Environment
			.getExternalStorageDirectory().getPath() + "/Body Suit/");

	int cool = 0;
	String filename;
	int position1;
	ToggleButton switchbody;
	RelativeLayout parent, takephoto;
	float startX, startY, startEventX, startEventY, diffX, diffY, m2;
	float m1, wid, high;
	float distx, disty, dist0, distLast, distCurrent;
	final int IDEL = 0;
	final int TOUCH = 1;
	final int PINCH = 2;
	float dw;
	float dh;
	Bitmap backg;
	LinearLayout linearfix, linearmain;
	ImageView body, photobfix, changefix;
	OnTouchListener ot, otShadow;
	CustomGridAdapter adapter;
	GridView facegridview, facegridviews;
	Integer[] bodysticker = { R.drawable.q, R.drawable.w, R.drawable.e,
			R.drawable.r, R.drawable.t, R.drawable.y, R.drawable.z,
			R.drawable.x, R.drawable.a, R.drawable.s, R.drawable.d,
			R.drawable.f, R.drawable.g, R.drawable.as, R.drawable.ad,
			R.drawable.aw, R.drawable.aq, R.drawable.ae, R.drawable.ar };
	RelativeLayout mainimage, imageholder, photolinear;
	int touchState = IDEL;
	RelativeLayout.LayoutParams objectparam, objectparams;
	ImageView photo, photomove;
	private int RESULT_LOAD_IMAGE = 101;
	@SuppressWarnings("unused")
	private Bitmap bmp;
	private LayoutParams rl;
	private RelativeLayout global;
	private LayoutParams globalparam;
	private RelativeLayout holder;
	Bitmap savevbmp;
	File f;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.mainactivity);
		photomove = (ImageView) findViewById(R.id.photomove);
		facegridview = (GridView) findViewById(R.id.facegridview);

		globalparam = new LayoutParams(200, 200);
		photobfix = (ImageView) findViewById(R.id.photobfix);
		b = (Button) findViewById(R.id.gallery);
		switchbody = (ToggleButton) findViewById(R.id.switchbody);

		linearmain = (LinearLayout) findViewById(R.id.linearmain);
		photolinear = (RelativeLayout) findViewById(R.id.photolinear);
		takephoto = (RelativeLayout) findViewById(R.id.takephoto);

		LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		imageholder = (RelativeLayout) findViewById(R.id.imageholder);
		mainimage = (RelativeLayout) vi.inflate(R.layout.addpic, null);
		int aa = CropActivity.cropmain.getWidth();
		int bb = CropActivity.cropmain.getHeight();

		objectparam = new RelativeLayout.LayoutParams(aa, bb);

		objectparam.leftMargin = 0;
		objectparam.topMargin = 0;
		objectparam.bottomMargin = 10000;
		objectparam.rightMargin = 10000;
		mainimage.setLayoutParams(objectparam);

		photo = (ImageView) mainimage.findViewById(R.id.photo);

		Drawable d = new BitmapDrawable(getResources(), CropActivity.cropmain);
		photo.setBackground(d);
		imageholder.addView(mainimage);
		le = imageholder.getWidth() / 2;
		to = imageholder.getHeight() / 2;
		onClick(photomove);

		switchbody.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					linearmain.setVisibility(View.VISIBLE);
					photolinear.setVisibility(View.INVISIBLE);
					otShadow = ot;
					addbody(position1);
				}
				else {

					otShadow = null;
					addbody(position1);
				}
			}
		});

		facegridview
		.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent1, View view,
					int position, long id) {

				cool = 0;
				if (imageholder.getChildCount() > 2)
					imageholder.removeViewAt(2);
				position1 = position;

				addbody(position);

				facegridview.setVisibility(View.INVISIBLE);
				findViewById(R.id.photobfix).setVisibility(View.GONE);
			}
		});

		ot = new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub

				LayoutInflater li = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				parent = (RelativeLayout) li.inflate(R.layout.addbody, null);
				float x11 = 0, y11 = 0;
				float x21 = 0, y21 = 0;
				int pointerCount = event.getPointerCount();
				for (int i = 0; i < pointerCount; i++) {

					int id = event.getPointerId(i);

					if (id == 0) {
						x11 = event.getX(i);
						y11 = event.getY(i);
					}

					else if (id == 1) {
						x21 = event.getX(i);
						y21 = event.getY(i);
					}

					switch (event.getAction() & MotionEvent.ACTION_MASK) {
					case MotionEvent.ACTION_POINTER_DOWN:

						if (i == 1 && pointerCount != 1) {
							m1 = (y21 - y11) / (x21 - x11);

						}
						touchState = PINCH;
						distx = event.getX(0) - event.getX(1);
						disty = event.getY(0) - event.getY(1);
						dist0 = (float) Math
								.sqrt(distx * distx + disty * disty);
						distLast = dist0;
						break;

					case MotionEvent.ACTION_POINTER_UP:
						if (touchState == PINCH) {

							wid = globalparam.width;
							high = globalparam.height;
						}
						touchState = TOUCH;
						break;

					case MotionEvent.ACTION_DOWN:

						holder = global = (RelativeLayout) v.getParent();
						globalparam = (RelativeLayout.LayoutParams) global
								.getLayoutParams();

						touchState = TOUCH;

						startX = globalparam.leftMargin;
						startY = globalparam.topMargin;
						wid = globalparam.width;
						high = globalparam.height;
						startEventX = event.getRawX();
						startEventY = event.getRawY();
						diffX = diffY = 0;
						break;
					case MotionEvent.ACTION_MOVE:

						if (pointerCount == 1) {
							if (id == 0) {
								diffX = event.getRawX() - startEventX;
								diffY = event.getRawY() - startEventY;

								globalparam.leftMargin = (int) (startX + diffX);
								globalparam.topMargin = (int) (startY + diffY);
								le = globalparam.leftMargin;
								to = globalparam.topMargin;
								global.setLayoutParams(globalparam);

							}
						} else if (i == 1 && pointerCount != 1) {
							m2 = (y21 - y11) / (x21 - x11);
							float deg = (float) Math.toDegrees(Math
									.atan((m2 - m1) / (1 + m2 * m1)));
							float de = global.getRotation();
							global.setRotation(de + deg);
							ro=de+deg;
						}
						if (touchState == PINCH) {
							{
								distx = event.getX(0) - event.getX(1);
								disty = event.getY(0) - event.getY(1);

								distCurrent = (float) Math.sqrt(distx * distx
										+ disty * disty);
								float d = (distCurrent - distLast);
								globalparam.width = (int) (wid + d
										* v.getWidth() / v.getHeight());
								globalparam.height = (int) (high + d);

								wi = globalparam.width;
								he = globalparam.height;

								global.setLayoutParams(globalparam);
							}
						}

						break;
					case MotionEvent.ACTION_UP:

						startX = globalparam.leftMargin;
						startY = globalparam.topMargin;
						touchState = IDEL;

						break;
					}

				}
				holder.invalidate();
				return false;

			}
		};

	}

	@SuppressLint("NewApi")
	protected void addbody(int position) {
		// TODO Auto-generated method stub
		LayoutInflater li = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		parent = (RelativeLayout) li.inflate(R.layout.addbody, null);
		ImageView body = (ImageView) parent.findViewById(R.id.body);
		body.setOnTouchListener(otShadow);


		rl = new RelativeLayout.LayoutParams(wi, he);
		rl.leftMargin = le;
		rl.topMargin = to;

		parent.setLayoutParams(rl);
		parent.setRotation(ro);
		body.setBackgroundResource(bodysticker[position]);
		if (imageholder.getChildCount() > 1) {
			imageholder.removeViewAt(1);
		}
		imageholder.addView(parent);

		global = parent;
		globalparam = (RelativeLayout.LayoutParams) global.getLayoutParams();
		holder = global;

	}

	@SuppressLint("NewApi")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		BitmapFactory.Options BitmapFactoryOptionsbfo = new BitmapFactory.Options();

		BitmapFactoryOptionsbfo.inPreferredConfig = Bitmap.Config.RGB_565;
		if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK
				&& null != data) {
			Uri selectedImage = data.getData();
			try {
				InputStream in = getContentResolver().openInputStream(
						selectedImage);
				backg = BitmapFactory.decodeStream(in, null,
						BitmapFactoryOptionsbfo);
			} catch (FileNotFoundException e) {

				e.printStackTrace();
			}

			if (backg == null) {
				Toast.makeText(getApplicationContext(),
						"Image could not be loaded..", Toast.LENGTH_LONG)
						.show();
				return;
			}

			Drawable d = new BitmapDrawable(getResources(), backg);
			findViewById(R.id.imageholder).setBackground(d);
		}

	}

	@SuppressLint("NewApi")
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.gallery:
			opengallery();
			break;
		case R.id.photomove:
			Log.e("", "photo move click msg");
			if (mov == 0) {
				photo.setOnTouchListener(new OnTouchListener() {

					@SuppressLint("NewApi")
					@Override
					public boolean onTouch(View v, MotionEvent event) {
						float x11 = 0, y11 = 0;
						float x21 = 0, y21 = 0;
						int pointerCount = event.getPointerCount();
						for (int i = 0; i < pointerCount; i++) {

							int id = event.getPointerId(i);

							if (id == 0) {
								x11 = event.getX(i);
								y11 = event.getY(i);
							}

							else if (id == 1) {
								x21 = event.getX(i);
								y21 = event.getY(i);
							}

							switch (event.getAction() & MotionEvent.ACTION_MASK) {

							case MotionEvent.ACTION_POINTER_DOWN:

								if (i == 1 && pointerCount != 1) {
									m1 = (y21 - y11) / (x21 - x11);

								}
								touchState = PINCH;
								distx = event.getX(0) - event.getX(1);
								disty = event.getY(0) - event.getY(1);
								dist0 = (float) Math.sqrt(distx * distx + disty
										* disty);
								distLast = dist0;

								break;

							case MotionEvent.ACTION_POINTER_UP:
								if (touchState == PINCH) {

									wid = objectparam.width;
									high = objectparam.height;
								}
								touchState = TOUCH;
								break;

							case MotionEvent.ACTION_DOWN:
								touchState = TOUCH;
								startX = objectparam.leftMargin;
								startY = objectparam.topMargin;
								wid = objectparam.width;
								high = objectparam.height;

								startEventX = event.getRawX();
								startEventY = event.getRawY();
								diffX = diffY = 0;
								break;
							case MotionEvent.ACTION_MOVE:

								if (pointerCount == 1) {
									if (id == 0) {
										diffX = event.getRawX() - startEventX;
										diffY = event.getRawY() - startEventY;
										objectparam.leftMargin = (int) (startX + diffX);

										objectparam.topMargin = (int) (startY + diffY);

										mainimage.setLayoutParams(objectparam);
									}

								} else if (i == 1 && pointerCount != 1) {

									m2 = (y21 - y11) / (x21 - x11);
									float deg = (float) Math.toDegrees(Math
											.atan((m2 - m1) / (1 + m2 * m1)));
									float de = mainimage.getRotation();
									mainimage.setRotation(de + deg);
								}

								if (touchState == PINCH) {
									{
										distx = event.getX(0) - event.getX(1);
										disty = event.getY(0) - event.getY(1);

										distCurrent = (float) Math.sqrt(distx
												* distx + disty * disty);
										float d = (distCurrent - distLast);
										objectparam.width = (int) (wid + d
												* v.getWidth() / v.getHeight());
										objectparam.height = (int) (high + d);

										mainimage.setLayoutParams(objectparam);
									}
								}

								break;

							case MotionEvent.ACTION_UP:

								Log.e("", "msg dw and dh" + dw + "      " + dh);
								startX = objectparam.leftMargin;
								startY = objectparam.topMargin;
								dw = startX;
								dh = startY;
								if (dw < 0) {
									dw = -dw;
								}
								if (dh < 0) {
									dh = -dh;
								}
								touchState = IDEL;
								break;
							}
						}
						mov = 1;
						photomove.setVisibility(View.GONE);
						imageholder.invalidate();
						return false;
					}
				});
			}
			break;
		case R.id.body:
			// facegridview.startAnimation(animBUp);
			facegridview.setVisibility(View.VISIBLE);
			adapter = new CustomGridAdapter(getApplicationContext(),
					bodysticker);
			facegridview.setAdapter(adapter);
			break;
		case R.id.save:
			savevbmp = loadBitmapFromView(takephoto);

			saveBitmap(savevbmp);
			break;
		case R.id.share:
			savevbmp = loadBitmapFromView(takephoto);
			saveBitmap(savevbmp);
			Intent share = new Intent(Intent.ACTION_SEND);
			share.setType("image/jpg");

			Uri uri = Uri.fromFile(f);
			share.putExtra(Intent.EXTRA_STREAM, uri);

			share.putExtra(android.content.Intent.EXTRA_EMAIL,
					new String[] { "" });
			share.putExtra(android.content.Intent.EXTRA_SUBJECT,
					"See This Cool Android App");
			share.putExtra(android.content.Intent.EXTRA_TEXT,
					"Hi,\n I found this really cool app on android market BODY SUIT ");

			startActivity(Intent.createChooser(share, "Share Via"));

			break;
		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if (facegridview.getVisibility() == View.VISIBLE) {
			facegridview.setVisibility(View.GONE);
		} else
			super.onBackPressed();
	}

	private void saveBitmap(Bitmap bmp) {
		// TODO Auto-generated method stub

		try {
			if (!APP_FILE_PATH.exists()) {
				APP_FILE_PATH.mkdirs();
			}
			Calendar cal = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			filename = sdf.format(cal.getTime());
			f = new File(APP_FILE_PATH, filename + ".jpg");
			final FileOutputStream out = new FileOutputStream(f);
			bmp.compress(Bitmap.CompressFormat.JPEG, 100, out);
			out.flush();
			out.close();
			Toast.makeText(getApplicationContext(), "Your file is saved ",
					Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	private Bitmap loadBitmapFromView(View v) {
		// TODO Auto-generated method stub
		Bitmap b = Bitmap.createBitmap(v.getWidth(), v.getHeight(),
				Bitmap.Config.RGB_565);
		Canvas c = new Canvas(b);
		v.draw(c);
		return b;

	}

	private void opengallery() {
		// TODO Auto-generated method stub
		Intent photoPickerIntent = new Intent(Intent.ACTION_PICK,
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		startActivityForResult(photoPickerIntent, RESULT_LOAD_IMAGE);
	}
	public void changebackground(View v){
		Intent photoPickerIntent = new Intent(Intent.ACTION_PICK,
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		startActivityForResult(photoPickerIntent, RESULT_LOAD_IMAGE);
	}
	
}
