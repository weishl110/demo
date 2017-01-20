package com.wei.demo.pregress;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public class ParamsCreator {
	private Context context;
    private int screenWidth;//屏幕宽度
    private int screenHeight;//屏幕高度
    private int densityDpi;//像素密度
    public ParamsCreator(Context context){
    	this.context = context;
    	WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    	screenWidth = wm.getDefaultDisplay().getWidth();
    	screenHeight = wm.getDefaultDisplay().getHeight();
    	DisplayMetrics metric = new DisplayMetrics();
    	wm.getDefaultDisplay().getMetrics(metric);
    	densityDpi = metric.densityDpi;
    }
    
	/**
	 * 获得默认文字大小
	 */
	public int getDefaultTextSize(){
    	if(screenWidth >= 1400){//1440
    		return 50;
    	}
    	if(screenWidth >= 1000){//1080
    		if(densityDpi >=480)
        		return 48;
        	if(densityDpi >= 320)
        		return 48;
        	return 48;
    	}
    	if(screenWidth >= 700){//720
        	if(densityDpi >= 320)
        		return 32;
        	if(densityDpi >= 240)
        		return 32;
        	if(densityDpi >= 160)
        		return 32;
        	return 32;
    	}
    	if(screenWidth >= 500){//540
        	if(densityDpi >= 320)
        		return 28;
        	if(densityDpi >= 240)
        		return 28;
        	if(densityDpi >= 160)
        		return 28;
        	return 28;
    	}
    	return 28;
	}
	/**
	 * 获得内圆半径
	 */
	public int getDefaultRadiusForCircle(){
    	if(screenWidth >= 1400){//1440
    		return 50;
    	}
    	if(screenWidth >= 1000){//1080
    		if(densityDpi >=480)
        		return 48;
        	if(densityDpi >= 320)
        		return 48;
        	return 48;
    	}
    	if(screenWidth >= 700){//720
        	if(densityDpi >= 320)
        		return 40;
        	if(densityDpi >= 240)
        		return 40;
        	if(densityDpi >= 160)
        		return 40;
        	return 40;
    	}
    	if(screenWidth >= 500){//540
        	if(densityDpi >= 320)
        		return 34;
        	if(densityDpi >= 240)
        		return 34;
        	if(densityDpi >= 160)
        		return 34;
        	return 34;
    	}
    	return 34;
	}
	/**
	 * 获得圆环宽度
	 */
	public int getDefaultStrokeWidthForCircle(){
    	if(screenWidth >= 1400){//1440
    		return 9;
    	}
    	if(screenWidth >= 1000){//1080
    		if(densityDpi >=480)
        		return 8;
        	if(densityDpi >= 320)
        		return 8;
        	return 8;
    	}
    	if(screenWidth >= 700){//720
        	if(densityDpi >= 320)
        		return 5;
        	if(densityDpi >= 240)
        		return 5;
        	if(densityDpi >= 160)
        		return 5;
        	return 5;
    	}
    	if(screenWidth >= 500){//540
        	if(densityDpi >= 320)
        		return 4;
        	if(densityDpi >= 240)
        		return 4;
        	if(densityDpi >= 160)
        		return 4;
        	return 4;
    	}
    	return 4;
	}
}
