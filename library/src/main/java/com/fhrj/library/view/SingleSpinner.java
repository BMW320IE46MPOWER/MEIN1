package com.fhrj.library.view;

import com.fhrj.library.R;
import com.fhrj.library.data.widget.Option;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
/***
 * 自定义Spinner
 * @author ZhangGuoHao
 * @date 2016年4月7日 下午8:22:02
 */
public class SingleSpinner extends android.widget.Spinner {

	private String mKey;
	
	public SingleSpinner(Context context) {
		this(context,null);
	}
	
	public SingleSpinner(Context context, AttributeSet attrs) {
		this(context, attrs,android.R.attr.spinnerStyle);
	}
	
	public SingleSpinner(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		//获取自定义属性和默认值
		TypedArray mTypedArray = context.obtainStyledAttributes(attrs,R.styleable.TextView);
		mKey = mTypedArray.getString(R.styleable.TextView_key);
		mTypedArray.recycle();
	}

	public String getKey(){
		return mKey;
	}
	
	public void setKey(String key){
		this.mKey = key;
	}
	
	/**
	 * 获取选中的选项
	 */
	public Option getSelectedItem(){
		return (Option)super.getSelectedItem();
	}
	
	/**
	 * 获取选中的选项索引
	 */
	public int getSelectedIndex(){
		return super.getSelectedItemPosition();
	}
	
	/**
	 * 获取选中的选项编码value
	 */
	public String getSelectedValue(){
		return getSelectedItem().getValue();
	}
	
	/**
	 * 获取选中的选项编码显示文本
	 */
	public String getSelectedLabel(){
		return getSelectedItem().getLabel();
	}
	
}
