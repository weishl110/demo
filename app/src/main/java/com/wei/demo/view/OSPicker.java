package com.wei.demo.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.graphics.Paint.FontMetrics;
import android.os.Handler;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * OSPicker View
 * @author hjq
 *
 */
public class OSPicker extends View{
	
	/**
	 * Paint To DrawText
	 */
	private TextPaint paint;
	
	/**
	 * Max Text Size In Pixels
	 */
	private float max_textSize;
	
	/**
	 * Min Text Size In Pixels
	 */
	private float min_textSize;
	
	/**
	 * The Center Offset
	 */
	private float dis_to_center;
	
	/**
	 * The Pre Touch Y
	 */
	private float last_touchY;
	
	/**
	 * view width
	 */
	private int pick_w;
	
	/**
	 * view height
	 */
	private int pick_h;
	
	/**
	 * The OptionsItems To Show
	 */
	private ArrayList<String> items;
	
	/**
	 * The Count Of Items To Show At Once
	 */
	private int visible_count;
	
	/**
	 * The Center Post
	 */
	private int centerPos;
	
	private Timer timer;
	
	/**
	 * Listener Operation Should In UIThread
	 */
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			if(listener != null)
				listener.onItemSelect(items.get(centerPos));
		};
	};
	private OnItemSelectListener listener;
	
	public OSPicker(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	public OSPicker(Context context) {
		this(context, null);
	}
	
	protected void init(){
		paint = new TextPaint();
		paint.setTextAlign(Align.CENTER);
		
		visible_count = 7;
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		pick_w = getMeasuredWidth();
		pick_h = getMeasuredHeight();
		max_textSize = pick_h / visible_count;
		min_textSize = max_textSize / 3;
		paint.setTextSize(max_textSize);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if(items!=null)
			drawCenterItem(canvas);
	}
	
	/**
	 * Draw The Center OptionsItem
	 * @param canvas
	 */
	protected void drawCenterItem(Canvas canvas){
		float curr_textSize = getCurrTextSize(dis_to_center);
		float curr_skewX = getCurrSkewX(dis_to_center);
		paint.setTextSize(curr_textSize);
		paint.setTextSkewX(curr_skewX);
		paint.setColor(Color.parseColor("#282828"));
		FontMetrics fm = paint.getFontMetrics();
		
		float x = pick_w / 2;
		float y = pick_h / 2 + dis_to_center - (fm.top + fm.bottom)/2;
		Log.e("OSPicker", "MaxSize -> "+max_textSize+" CurrSize -> "+curr_textSize +" y -> "+y);
		canvas.drawText(items.get(centerPos), x, y, paint);
		
		drawOtherItems(canvas);
	}
	
	/**
	 * Draw Other OptionsItem
	 * @param canvas
	 */
	protected void drawOtherItems(Canvas canvas){
		
		float x = pick_w / 2;
		paint.setColor(Color.parseColor("#787878"));
		//Draw The Items Below The Center
		for(int i = (centerPos-1) ; i > Math.max((centerPos-visible_count/2-1),0) ; i--){
			float distance = dis_to_center + (max_textSize) * (centerPos - i);
			float curr_textSize = getCurrTextSize(distance);
			float curr_skewX = getCurrSkewX(distance);
			paint.setTextSize(curr_textSize);
			paint.setTextSkewX(curr_skewX);
			FontMetrics fm = paint.getFontMetrics();
			float y = pick_h / 2 + distance - (fm.top+fm.bottom)/2;
			canvas.drawText(items.get(i), x, y, paint);
			
		}
		
		//Draw The Items Above The Center
		for(int i = (centerPos + 1) ; i < Math.min((centerPos+visible_count/2+1),items.size()) ; i ++ ){
			float distance = dis_to_center - (i - centerPos) * max_textSize;
			float curr_textSize = getCurrTextSize(distance);
			float curr_skewX = getCurrSkewX(distance);
			paint.setTextSize(curr_textSize);
			paint.setTextSkewX(curr_skewX);
			FontMetrics fm = paint.getFontMetrics();
			float y = pick_h / 2 + distance - (fm.top + fm.bottom)/2;
			canvas.drawText(items.get(i), x, y, paint);
			
		}
		
	}
	
	/**
	 * Set The Options
	 * @param data
	 */
	public void setData(List<String> data){
		this.items = new ArrayList<String>(data);
		this.centerPos = this.items.size()/2;
		postInvalidate();
	}
	
	/**
	 * Set The Visible Nums
	 * @param nums
	 */
	public void setVisibleNums(int nums){
		this.visible_count = nums;
		postInvalidate();
	}
	
	/**
	 * Set The Default Selected Value
	 * @param itemValue
	 */
	public void setDefault(String itemValue){
		int index = items.indexOf(itemValue);
		if(index >= 0){
			setDefault(index);
		}
	}
	
	/**
	 * Set The Default Selected Index
	 * @param index
	 */
	public void setDefault(int index){
		if(index > centerPos){
			for(int i = 0 ; i < (index-centerPos) ; i++)
				moveTailToHead();
		}else {
			for(int i = 0 ; i < (centerPos - index) ; i++)
				moveHeadToTail();
		}
		
		postInvalidate();
	}

	/**
	 * Listener Do When The Item Selected
	 * @param listener
	 */
	public void setSelectedListener(OnItemSelectListener listener){
		this.listener = listener;
	}
	
	/**
	 * Get The TextSize By The Distance To The CenterY
	 * @param distance
	 * @return
	 */
	private float getCurrTextSize(float distance){
		float all_dis = visible_count / 2 * max_textSize;
		float curr_size = max_textSize - Math.abs(distance / all_dis) * (max_textSize - min_textSize);
		return curr_size;
	}
	
	/**
	 * Get The SkewX By The Distance To The CenterY
	 * @param distance
	 * @return
	 */
	private float getCurrSkewX(float distance){
		float all_dis = visible_count / 2 * max_textSize;
		float curr_skewx = distance / all_dis * 0.35f;
		Log.d("getCurrSkewX", "curr_skewx -> "+curr_skewx);
		return curr_skewx;
	}
	
	private void moveTailToHead(){
		String tailItem  = items.get(items.size()-1);
		items.remove(items.size()-1);
		items.add(0, tailItem);
	}
	
	private void moveHeadToTail(){
		String headItem = items.get(0);
		items.remove(0);
		items.add(headItem);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			if(timer!=null){
				timer.cancel();
				timer = null;
			}
			last_touchY = event.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			onTouchMoved(event);
			break;
		case MotionEvent.ACTION_UP:
			onTouchUp();
			break;
		default:
			break;
		}
		postInvalidate();
		return true;
	}
	
	private void onTouchMoved(MotionEvent event){
		float y = event.getY();
		dis_to_center = dis_to_center + y - last_touchY;
		last_touchY = y;
		scrollMoved();
	}
	
	private void scrollMoved(){
		if(dis_to_center >= ((float)1 / 2 * max_textSize)){
			//向下滑动
			moveHeadToTail();
			dis_to_center -= min_textSize * 2.97f;
		}else if(dis_to_center <= -((float)1 / 2 * max_textSize)){
			//向上滑动
			moveTailToHead();
			dis_to_center += min_textSize * 2.97f;
		}
	}
	
	private void onTouchUp(){
		if(Math.abs(dis_to_center) > 0){
			ScrollTask task = new ScrollTask();
			timer = new Timer();
			timer.schedule(task, 0 ,10);
		}else{
			handler.obtainMessage().sendToTarget();
		}
	}
	
	/**
	 * Scroll Timer Task
	 * @author hjq
	 *
	 */
	private class ScrollTask extends TimerTask{
		@Override
		public void run() {
			if(Math.abs(dis_to_center) < 2){
				dis_to_center = 0; 
				timer.cancel();
				timer = null;
				handler.obtainMessage().sendToTarget();
			}else{
				dis_to_center -= dis_to_center/(Math.abs(dis_to_center)) * 2;
			}
			scrollMoved();
			OSPicker.this.postInvalidate();
		}
	}
	
	public interface OnItemSelectListener{
		public void onItemSelect(String value);
	}
	
}
