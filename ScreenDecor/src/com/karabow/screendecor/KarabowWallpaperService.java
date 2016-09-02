package com.karabow.screendecor;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
//import android.graphics.Paint;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.service.wallpaper.WallpaperService;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

public class KarabowWallpaperService extends WallpaperService {
	
	@Override
	public Engine onCreateEngine(){
		return new KarabowWallpaperEngine();
	}

	private class KarabowWallpaperEngine extends Engine {
		private final Handler handler = new Handler();
		private final Runnable drawRunner = new Runnable(){
			@Override
			public void run(){
				draw();
			}
		};
		private List<MyPoint> iconPoints;
		private List<Integer> largeIcons;
		private List<Integer> smallIcons;
		private List<Integer> currentIcons = null;
		//private Paint paint = new Paint();
		private int width;
		int height;
		private boolean visible = true;
		private int maxNumber;
		private boolean touchEnabled;
		private boolean useSmallSize;
		
		public KarabowWallpaperEngine(){
			init();
			
			iconPoints = new ArrayList<MyPoint>();
			largeIcons = new ArrayList<Integer>();
			largeIcons.add(Integer.valueOf(R.drawable.wp1));
			largeIcons.add(Integer.valueOf(R.drawable.wp2));
			largeIcons.add(Integer.valueOf(R.drawable.wp3));
			largeIcons.add(Integer.valueOf(R.drawable.wp4));
			largeIcons.add(Integer.valueOf(R.drawable.wp5));
			largeIcons.add(Integer.valueOf(R.drawable.wp6));
			largeIcons.add(Integer.valueOf(R.drawable.wp7));
			largeIcons.add(Integer.valueOf(R.drawable.wp8));
			largeIcons.add(Integer.valueOf(R.drawable.wp9));
			largeIcons.add(Integer.valueOf(R.drawable.wp10));
			smallIcons = new ArrayList<Integer>();
			smallIcons.add(Integer.valueOf(R.drawable.wp1_s));
			smallIcons.add(Integer.valueOf(R.drawable.wp2_s));
			smallIcons.add(Integer.valueOf(R.drawable.wp3_s));
			smallIcons.add(Integer.valueOf(R.drawable.wp4_s));
			smallIcons.add(Integer.valueOf(R.drawable.wp5_s));
			smallIcons.add(Integer.valueOf(R.drawable.wp6_s));
			smallIcons.add(Integer.valueOf(R.drawable.wp7_s));
			smallIcons.add(Integer.valueOf(R.drawable.wp8_s));
			smallIcons.add(Integer.valueOf(R.drawable.wp9_s));
			smallIcons.add(Integer.valueOf(R.drawable.wp10_s));
//			paint.setAntiAlias(true);
//			paint.setColor(Color.WHITE);
//			paint.setStyle(Paint.Style.STROKE);
//			paint.setStrokeJoin(Paint.Join.ROUND);
//			paint.setStrokeWidth(10f);
			handler.post(drawRunner);
		}
		private void init() {
			SharedPreferences prefs = PreferenceManager
					.getDefaultSharedPreferences(KarabowWallpaperService.this);
			maxNumber = Integer.valueOf(
					prefs.getString(KarabowWallpaperService.this.getString(R.string.key_max_num_of_wall_images), "6"));
			touchEnabled = prefs.getBoolean(
					KarabowWallpaperService.this.getString(R.string.key_touch_wall), true);
			useSmallSize = prefs.getBoolean(
					KarabowWallpaperService.this.getString(R.string.key_use_small_wall_images), true);
		}
		
		private void draw() {
			SurfaceHolder holder = getSurfaceHolder();
			Canvas canvas = null;
			try {
				canvas = holder.lockCanvas();
				if (canvas != null) {
					if (iconPoints.size() >= maxNumber) {
						iconPoints.clear();
					}
					int x = (int) (width * Math.random());
					int y = (int) (height * Math.random());
					iconPoints.add(new MyPoint(x, y));
					
					drawDevices(canvas, iconPoints);
				}
			} finally {
				if (canvas != null) {
					holder.unlockCanvasAndPost(canvas);
				}
			}
			handler.removeCallbacks(drawRunner);
			if (visible) {
				handler.postDelayed(drawRunner, 5888);
			}
		}
		
		//surface view requires that all elements are drawn completely
		private void drawDevices(Canvas canvas, List<MyPoint> devices) {
			init();
			
			canvas.drawColor(Color.BLACK);
			
			if(useSmallSize){
				currentIcons = smallIcons;
			} else {
				currentIcons = largeIcons;
			}
			for (MyPoint point : devices) {
				//canvas.drawCircle(point.x, point.y, 28.8f, paint);
				
				Random rand = new Random();
				int index = rand.nextInt(currentIcons.size());
				Bitmap bitmap = BitmapFactory.decodeResource(
						KarabowWallpaperService.this.getResources(), currentIcons.get(index));
				canvas.drawBitmap(bitmap, point.x, point.y, null);
			}
		}
		
		@Override
		public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height){
			this.width = width;
			this.height = height;
			super.onSurfaceChanged(holder, format, width, height);
		}
		
		@Override
		public void onSurfaceDestroyed(SurfaceHolder holder){
			super.onSurfaceDestroyed(holder);
			this.visible = false;
			handler.removeCallbacks(drawRunner);
		}
		
		@Override
		public void onTouchEvent(MotionEvent event){
			if (touchEnabled){
				float x = event.getX();
				float y = event.getY();
				SurfaceHolder holder = getSurfaceHolder();
				Canvas canvas = null;
				try {
					canvas = holder.lockCanvas();
					if (canvas != null) {
						canvas.drawColor(Color.BLACK);
						iconPoints.clear();
						iconPoints.add(new MyPoint(x, y));
						
						drawDevices(canvas, iconPoints);
					}
				} finally {
					if (canvas != null) {
						holder.unlockCanvasAndPost(canvas);
					}
				}
				super.onTouchEvent(event);
			}
		}
		
		@Override
		public void onVisibilityChanged(boolean visible){
			this.visible = visible;
			if(visible){
				handler.post(drawRunner);
			} else {
				handler.removeCallbacks(drawRunner);
			}
		}
	}
}