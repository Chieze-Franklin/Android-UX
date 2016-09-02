package com.karabow.screendecor;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import android.animation.TimeAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.Build;
import android.preference.PreferenceManager;
import android.service.dreams.DreamService;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class KarabowDreamService extends DreamService {

	private List<Integer> largeIcons;
	private List<Integer> smallIcons;
	private List<Integer> currentIcons = null;
	
	private int maxNumber, imageSpeed;
	private boolean touchEnabled;
	private boolean useSmallSize;
	
	private void init() {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		maxNumber = Integer.valueOf(
				prefs.getString(this.getString(R.string.key_max_num_of_screen_images), "6"));
		imageSpeed = Integer.valueOf(
				prefs.getString(this.getString(R.string.key_screen_image_speed), "10"));
		touchEnabled = prefs.getBoolean(this.getString(R.string.key_touch_screen), true);
		useSmallSize = prefs.getBoolean(this.getString(R.string.key_use_small_screen_images), true);
		PowerConnectionReceiver.minimumBatteryLevel = Integer.valueOf(
				prefs.getString(this.getString(R.string.key_min_battery_level), "10"));
	}
	
	@Override
	public void onAttachedToWindow() {
		super.onAttachedToWindow();
		
		//hide system UI
		//this.setFullscreen(true);
		
		//reduce screen brightness
		this.setScreenBright(false);
		
		//icons
		largeIcons = new ArrayList<Integer>();
		largeIcons.add(Integer.valueOf(R.drawable.ss1));
		largeIcons.add(Integer.valueOf(R.drawable.ss2));
		largeIcons.add(Integer.valueOf(R.drawable.ss3));
		largeIcons.add(Integer.valueOf(R.drawable.ss4));
		largeIcons.add(Integer.valueOf(R.drawable.ss5));
		largeIcons.add(Integer.valueOf(R.drawable.ss6));
		largeIcons.add(Integer.valueOf(R.drawable.ss7));
		largeIcons.add(Integer.valueOf(R.drawable.ss8));
		largeIcons.add(Integer.valueOf(R.drawable.ss9));
		largeIcons.add(Integer.valueOf(R.drawable.ss10));
		smallIcons = new ArrayList<Integer>();
		smallIcons.add(Integer.valueOf(R.drawable.ss1_s));
		smallIcons.add(Integer.valueOf(R.drawable.ss2_s));
		smallIcons.add(Integer.valueOf(R.drawable.ss3_s));
		smallIcons.add(Integer.valueOf(R.drawable.ss4_s));
		smallIcons.add(Integer.valueOf(R.drawable.ss5_s));
		smallIcons.add(Integer.valueOf(R.drawable.ss6_s));
		smallIcons.add(Integer.valueOf(R.drawable.ss7_s));
		smallIcons.add(Integer.valueOf(R.drawable.ss8_s));
		smallIcons.add(Integer.valueOf(R.drawable.ss9_s));
		smallIcons.add(Integer.valueOf(R.drawable.ss10_s));
	}
	@Override
	public void onDetachedFromWindow() {
		//tear down anything u built in onAttachedToWindow()
		
		super.onDetachedFromWindow();
	}
	@Override
	public void onDreamingStarted() {
		super.onDreamingStarted();
		
		init();
		
		//determine if users can/cannot touch
		this.setInteractive(touchEnabled);
		
		if(useSmallSize){
			currentIcons = smallIcons;
		} else {
			currentIcons = largeIcons;
		}
		
		//our content view will take care of its animated children
		final Bouncer bouncer = new Bouncer(this);
		bouncer.setLayoutParams(new ViewGroup.LayoutParams
				(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		bouncer.setSpeed(imageSpeed * 10); //pixels/sec
		//add some image views that will be bounced around
		for(int i = 0; i < maxNumber; i++) {
			final FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams
					(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
			final ImageView image = new ImageView(this);
			
			Random rand = new Random();
			int index = rand.nextInt(currentIcons.size());
			image.setImageResource(currentIcons.get(index));
			
			//image.setBackgroundColor(0xFF004000);
			bouncer.addView(image, lp);
		}
		
		//now add a pause/resume button for API level > 18
		@SuppressWarnings("deprecation")
		int sdkVersion = Integer.parseInt(Build.VERSION.SDK);
		if(touchEnabled && sdkVersion >= Build.VERSION_CODES.KITKAT) {
			final Button btnPauseResume = new Button(this);
			PowerConnectionReceiver.btnPauseResume = btnPauseResume;
			bouncer.setLayoutParams(new ViewGroup.LayoutParams
					(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
			btnPauseResume.setGravity(Gravity.BOTTOM | Gravity.CENTER);
			btnPauseResume.setBackgroundColor(Color.TRANSPARENT);//(Color.argb(100, 100, 100, 100));
			if (bouncer.isAnimationPaused())
				btnPauseResume.setText(R.string.resume_anim);
			else
				btnPauseResume.setText(R.string.pause_anim);
			btnPauseResume.setTextColor(Color.WHITE);
			btnPauseResume.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View view) {
					if (bouncer.isAnimationPaused()) {
						bouncer.resumeAnimation();
						btnPauseResume.setText(R.string.pause_anim);
					}
					else {
						bouncer.pauseAnimation();
						btnPauseResume.setText(R.string.resume_anim);
					}
				}
			});
			bouncer.addView(btnPauseResume);
		}
		
		this.setContentView(bouncer);
	}
	@Override
	public void onDreamingStopped() {
		//stop ur animations
		
		super.onDreamingStopped();
	}
	
	public class Bouncer extends FrameLayout implements TimeAnimator.TimeListener {
		private float maxSpeed;
		private final TimeAnimator animator;
		private int width, height;
		
		public Bouncer(Context context) {
			this(context, null);
		}
		public Bouncer(Context context, AttributeSet attrs) {
			this(context, attrs, 0);
		}
		public Bouncer(Context context, AttributeSet attrs, int flags) {
			super(context, attrs, flags);
			
			animator = new TimeAnimator();
			animator.setTimeListener(this);
			
			PowerConnectionReceiver.bouncer = this;
		}
		
		@TargetApi(Build.VERSION_CODES.KITKAT)
		public boolean isAnimationPaused() {
			return animator.isPaused();
		}
		@TargetApi(Build.VERSION_CODES.KITKAT)
		public void pauseAnimation() {
			animator.pause();
		}
		@TargetApi(Build.VERSION_CODES.KITKAT)
		public void resumeAnimation() {
			animator.resume();
		}
		
		/*
		 * start the bouncing as soon as we're on screen
		 * */
		@Override
		public void onAttachedToWindow() {
			super.onAttachedToWindow();
			
			animator.start();
		}
		/*
		 * stop animations when the view hierarchy is torn down
		 * */
		@Override
		public void onDetachedFromWindow() {
			animator.cancel();
			
			super.onDetachedFromWindow();
		}
		
		/*
		 * whenever view is added, place it randomly
		 * */
		@Override
		public void addView(View v, ViewGroup.LayoutParams lp) {
			super.addView(v, lp);
			setupView(v);
		}
		/*
		 * reposition all children when the container size changes
		 * */
		@Override
		protected void onSizeChanged(int w, int h, int oldw, int oldh) {
			super.onSizeChanged(w, h, oldw, oldh);
			width = w;
			height = h;
			for(int i = 0; i < this.getChildCount(); i++) {
				setupView(this.getChildAt(i));
			}
		}
		/*
		 * bouncing view setup: random placement, random velocity
		 * */
		private void setupView(View v) {
			//deal with ONLY image views
			if(!(v instanceof ImageView)) return;
			
			final PointF p = new PointF();
			final float a = (float) (Math.random() * 360);
			p.x = maxSpeed * (float)(Math.cos(a));
			p.y = maxSpeed * (float)(Math.sin(a));
			v.setTag(p);
			v.setX((float) (Math.random() * (width - v.getWidth())));
			v.setY((float) (Math.random() * (height - v.getHeight())));
		}
		private int MAX_TIME_TO_CHANGE = 5000;
		private int CURR_TIME_TO_CHANGE = 0;
		/*
		 * every TimeAnimator frame, nudge each bouncing view along
		 * */
		public void onTimeUpdate(TimeAnimator animation, long elapsed, long dt_ms) {
			final float dt = dt_ms / 1000f; //seconds
			for (int i=0; i<this.getChildCount(); i++) {
				final View view = this.getChildAt(i);
				//deal with ONLY image views
				if(!(view instanceof ImageView)) continue;
				
				final PointF v = (PointF)view.getTag();
				
				//see if u should change image
				CURR_TIME_TO_CHANGE++;
				if (CURR_TIME_TO_CHANGE >= MAX_TIME_TO_CHANGE){
					CURR_TIME_TO_CHANGE = 0;
					Random rand = new Random();
					int index = rand.nextInt(currentIcons.size());
					((ImageView)view).setImageResource(currentIcons.get(index));
				}
				
				//step view for velocity * time
				view.setX(view.getX() + v.x * dt);
				view.setY(view.getY() + v.y * dt);
				
				//handle reflections
				final float l = view.getX();
				final float t = view.getY();
				final float r = l + view.getWidth();
				final float b = t + view.getHeight();
				boolean flipX = false, flipY = false;
				if (r > width) {
					view.setX(view.getX() - 2 * (r - width));
					flipX = true;
				} else if (l < 0) {
					view.setX(-l);
					flipX = true;
				}
				if (b > height) {
					view.setY(view.getY() - 2 * (b - height));
					flipY = true;
				} else if (t < 0) {
					view.setY(-t);
					flipY = true;
				}
				if (flipX) v.x *= -1;
				if (flipY) v.y *= -1;
			}
		}
		
		public void setSpeed(float s) {
			maxSpeed = s;
		}
	}
}