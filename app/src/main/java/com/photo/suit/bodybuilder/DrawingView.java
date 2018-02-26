package com.photo.suit.bodybuilder;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.util.Pair;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.FrameLayout;

public class DrawingView extends View implements OnTouchListener {
	private Canvas m_Canvas;

	private Path m_Path,path;
	Bitmap zo;
	AlertDialog alertDialog;
	static Paint m_Paint;
	

	ArrayList<Pair<Path, Paint>> paths = new ArrayList<Pair<Path, Paint>>();
	
	private float mX, mY;
	private float he, man;

	private static final float TOUCH_TOLERANCE = 0;

	public static boolean isEraserActive = false;

	@SuppressLint("NewApi")
	public DrawingView(Context context,int w,int h) {
		
		super(context);

		setFocusable(true);
		setFocusableInTouchMode(true);
	    this.setOnTouchListener(this);
		setDrawingCacheEnabled(true);
		onCanvasInitialization();
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(w, h);
		params.gravity=Gravity.CENTER;
		setLayoutParams(params);
		
	}

	public void onCanvasInitialization() {
		float y = 1.0F;
		m_Paint = new Paint();

		m_Paint.setColor(Color.WHITE);
		m_Paint.setAntiAlias(true);
		m_Paint.setDither(true);
		m_Paint.setStrokeWidth(5);
		m_Paint.setStyle(android.graphics.Paint.Style.STROKE);
		m_Paint.setStrokeJoin(android.graphics.Paint.Join.ROUND);
		m_Paint.setStrokeCap(android.graphics.Paint.Cap.ROUND);
		m_Paint.setPathEffect(new DashPathEffect(new float[] {10,10}, 0));

		
		m_Canvas = new Canvas();

		m_Path = new Path();
		path=new Path();
		
		//newPaint = new Paint(m_Paint);
		paths.add(new Pair<Path, Paint>(m_Path, m_Paint));
		
	

		invalidate();
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
	}

	public boolean onTouch(View arg0, MotionEvent event) {
		float x = event.getX();
		float y = event.getY();

		int cx = 0,cy = 0;
		int x1=(int) x;
		int y1=(int) y;
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			Container1.draw_path.clear();
			touch_start(x, y);
			Container1.xs= (int) x1;
			Container1.xl=(int) x1;
			Container1.ys=(int) y1;
			Container1.yl=(int) y1;
			
		CropActivity.zoom.setVisibility(View.VISIBLE);
			invalidate();
			break;
		case MotionEvent.ACTION_MOVE:
			touch_move(x, y);

			if(x1<Container1.xs)
				Container1.xs=x1;
			
			if(x1>Container1.xl)
				Container1.xl=x1;
			
			if(y1<Container1.ys)
				Container1.ys=y1;
			
			if(y1>Container1.yl)
				Container1.yl=y1;
			invalidate();
			
	show(x,y);
			
			break;
		case MotionEvent.ACTION_UP:
			touch_up();
             invalidate();
             CropActivity.ib.setEnabled(true);
             zo=null;
            
           
		
		CropActivity.zoom.setVisibility(View.GONE);
			
			break;
		}
		return true;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		//Log.e("coloo", newPaint.getColor()+"");
		for (Pair<Path, Paint> p : paths) {
			canvas.drawPath(p.first, p.second);
				
		}
	
	}

	private void touch_start(float x, float y) {

		if (isEraserActive) {
			m_Paint.setColor(Color.WHITE);
			m_Paint.setStrokeWidth(6);
			//newPaint = new Paint(m_Paint); // Clones the mPaint object
			paths.add(new Pair<Path, Paint>(m_Path, m_Paint));
		
			

		} else {
			paths.removeAll(paths);

			//newPaint = new Paint(m_Paint);
			paths.add(new Pair<Path, Paint>(m_Path, m_Paint));
			Container1.draw_path.add(new Pair<Path, Paint>(m_Path, m_Paint));
			
		}

		m_Path.reset();
		path.reset();
		m_Path.moveTo(x, y);
		path.moveTo(0, 0);
		he=x;
		man=y;
		mX = x;
		mY = y;
	

	}

	private void touch_move(float x, float y) {
		
		float dx = Math.abs(x - mX);
		float dy = Math.abs(y - mY);
		if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
			m_Path.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
			path.quadTo(mX-he, mY-man, (x-he + mX-he) / 2, (y-man + mY-man) / 2);
			mX = x;
			mY = y;
			Container1.draw_path.add(new Pair<Path, Paint>(m_Path, m_Paint));
		}
		
		
	}

	private void touch_up() {

		Container1.paths = Container1.draw_path;
		m_Path.lineTo(mX, mY);
		m_Canvas.drawPath(m_Path, m_Paint);
		m_Path = new Path();
		paths.add(new Pair<Path, Paint>(m_Path, m_Paint));
	
	}

	public static Bitmap loadBitmapFromView(View v) {

		Bitmap b = Bitmap.createBitmap(v.getWidth(), v.getHeight(),
				Bitmap.Config.RGB_565);
		Canvas c = new Canvas(b);
		v.draw(c);
		return b;

	}
	public void show(float x,float y){
		int x1,y1;
		if(x+100>CropActivity.main.getWidth()){
			x=CropActivity.main.getWidth()-100;
		}
		if(y+100>CropActivity.main.getHeight()){
			y=CropActivity.main.getHeight()-100;
		}
		x1=(int) (x-100);
		y1=(int) (y-100);
		if((x1-100)<0){
			x1=0;
		}
		if((y1-100)<0){
			y1=0;
		}
		zo=loadBitmapFromView(CropActivity.main);
		zo=Bitmap.createBitmap(zo, x1, y1, 200, 200);
		Bitmap circleBitmap = Bitmap.createBitmap(zo.getWidth(), zo.getHeight(), Bitmap.Config.ARGB_8888);

		BitmapShader shader = new BitmapShader (zo,  TileMode.CLAMP, TileMode.CLAMP);
		Paint paint = new Paint();
		        paint.setShader(shader);
		paint.setAntiAlias(true);
		Canvas c = new Canvas(circleBitmap);
		c.drawCircle(zo.getWidth()/2, zo.getHeight()/2, zo.getWidth()/2, paint);

		
		CropActivity.zoom.setImageBitmap(circleBitmap);
	}


}
