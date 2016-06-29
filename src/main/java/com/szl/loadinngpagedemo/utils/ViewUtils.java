package com.szl.loadinngpagedemo.utils;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

public class ViewUtils {
	public static void removeParent(View v){
		//  先找到爹 在通过爹去移除孩子
		ViewParent parent = v.getParent();
		//所有的控件 都有爹  爹一般情况下 就是ViewGoup
		if(parent instanceof ViewGroup){
			ViewGroup group=(ViewGroup) parent;
			group.removeView(v);
		}
	}
}
