package com.photo.suit.bodybuilder;

import java.util.ArrayList;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.util.Pair;

public class Container1 {
	

	public static ArrayList<Pair<Path, Paint>> draw_path = new ArrayList<Pair<Path, Paint>>();
	public static ArrayList<Pair<Path, Paint>> paths = new ArrayList<Pair<Path, Paint>>();
	public static ArrayList<Pair<Path, Paint>> paths1 = new ArrayList<Pair<Path, Paint>>();
	
	public static Bitmap [] backgrounds=null;
	public static Bitmap [] stickers=null;
	public static Bitmap [] templates=null;

    public static int xs;
	public static int xl;
	public static int ys;
	public static int yl;
	
	public static Drawable pic1;
	public static Bitmap bit;
	public static Bitmap passed;
	public static boolean edit;
	public static boolean text;
	public static CharSequence texts;
	public static boolean clip;
	public static Bitmap crop_to_edit;
	public static Bitmap crop_to_clip=CropActivity.loadBitmapFromView(CropActivity.main);
	public static ArrayList<Pair<Path,Paint>> path=new ArrayList<Pair<Path,Paint>>();
	
	
	/*
	public static Typeface tf;
	public static int color=Color.BLACK;
	public static boolean background;*/

   
}
