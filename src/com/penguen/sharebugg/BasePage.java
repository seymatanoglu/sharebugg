package com.penguen.sharebugg;

import com.penguen.sharebugg.application.MyApplication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class BasePage extends Activity{

	public ProgressDialog Dialog;
	protected boolean NeedsProgressBar=true;
	protected boolean NeedsLoading=true;
	protected String SubmitText = "Gšnderiliyor..";
	protected LinearLayout container, header, list;
	protected RelativeLayout footer;
	protected ImageButton InfoButton, BuggCodesButton, NewBuggButton, BuggListButton, SettingsButton;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		MyApplication.ActiveForm = this;
		MyApplication.CurrentContext = this.getApplicationContext();
		this.DrawPage();
		this.BindPage();
	}
	
	protected void DrawPage() {
		setContentView(R.layout.basepage);	
		this.container = (LinearLayout)this.findViewById(R.id.container);
		
		this.header = (LinearLayout)this.findViewById(R.id.header);
		
		this.footer = (RelativeLayout)this.findViewById(R.id.footer);
		
		this.InfoButton = (ImageButton)this.findViewById(R.id.info);
		this.InfoButton.setOnClickListener(new OnClickListener() {    
			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(), SplashScreen1.class);
				startActivityForResult(intent, 0);
				finish();
			}
		});
		this.BuggCodesButton = (ImageButton)this.findViewById(R.id.buggcodes);
		this.BuggCodesButton.setOnClickListener(new OnClickListener() {    
			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(), BuggCodesPage.class);
				startActivity(intent);
				finish();
			}
		});
		this.NewBuggButton = (ImageButton)this.findViewById(R.id.newbugg);
		this.NewBuggButton.setOnClickListener(new OnClickListener() {    
			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(), NewBuggPage.class);
				startActivity(intent);
				finish();
			}
		});
		this.BuggListButton = (ImageButton)this.findViewById(R.id.bugglist);
		this.BuggListButton.setOnClickListener(new OnClickListener() {    
			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(), BuggListPage.class);
				startActivity(intent);
				finish();
			}
		});
		this.SettingsButton = (ImageButton)this.findViewById(R.id.settings);
		this.SettingsButton.setOnClickListener(new OnClickListener() {    
			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(), SettingsPage.class);
				startActivity(intent);
				finish();
			}
		});
	}
	

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {

	    View v = getCurrentFocus();
	    boolean ret = super.dispatchTouchEvent(event);

	    if (v instanceof EditText) {
	        View w = getCurrentFocus();
	        int scrcoords[] = new int[2];
	        w.getLocationOnScreen(scrcoords);
	        float x = event.getRawX() + w.getLeft() - scrcoords[0];
	        float y = event.getRawY() + w.getTop() - scrcoords[1];

	        //Log.d("Activity", "Touch event "+event.getRawX()+","+event.getRawY()+" "+x+","+y+" rect "+w.getLeft()+","+w.getTop()+","+w.getRight()+","+w.getBottom()+" coords "+scrcoords[0]+","+scrcoords[1]);
	        if (event.getAction() == MotionEvent.ACTION_UP && (x < w.getLeft() || x >= w.getRight() || y < w.getTop() || y > w.getBottom()) ) { 
	            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
	            imm.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);
	            //this.ShowFooter();
	        }
	    }
	return ret;
	}

	private void BindPage()  {
		if (isOnline()) {
			if (this.NeedsLoading) {
				if (this.NeedsProgressBar) {
					MyApplication.CurrentProgress.Reset(0);
					MyApplication.CurrentProgress.SetMessage("Loading..");
					MyApplication.CurrentProgress.ShowLoading();
				}
				new BindPageInThread<Object, Object, Object>().execute();
			}
			else {
				this.Bind();
			}
		}
	}
	
	protected void Bind()  {

	}
	
	private class BindPageInThread<X, Y, Z> extends AsyncTask<X, Y, Z>{
		@Override
		protected Z doInBackground(X... params) { 
			Bind();
			return null;
		}	
		@Override
		protected void onPostExecute(Z z){
			OnAfterBind();
		} 
	}
	protected void OnAfterBind() {
		MyApplication.CurrentProgress.Finish();
	}

	protected void Submit() {
		if (Validate()) {
			this.ShowLoading();
			new OnSubmitThread<Object, Object, Object>().execute();
		}		
	}
	
	private class OnSubmitThread<X, Y, Z> extends AsyncTask<X, Y, Z>{
		@Override
		protected Z doInBackground(X... params) {
			OnSubmit();
			return null;
		}	
		@Override
		protected void onPostExecute(Z z){
			if (isOnline()) {
				OnAfterSubmit();
			}
			else
				HideLoading();
		} 
	}
	
	protected void OnSubmit() {
		
	}
	
	protected boolean isOnline() {
	    ConnectivityManager cm =  (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    if(cm.getActiveNetworkInfo() ==null) {
	    	 MyApplication.ShowErrorAlertDialog("Connection Error", "Cannot establish connection.");
	    	return false;
	    }
	    return true;
	}
	
	protected void OnAfterSubmit() {
		this.HideLoading();
	}
	
	protected boolean Validate() {
		return true;
	}
	
	protected void OnAfterRebind() {
		MyApplication.CurrentProgress.Finish();
	}
	
	private class RebindPageInThread<X, Y, Z> extends AsyncTask<X, Y, Z>{
		@Override
		protected Z doInBackground(X... params) {
			Bind();	
			return null;
		}	
		@Override
		protected void onPostExecute(Z z){
			OnAfterRebind();
		} 
	}
	
	protected void RebindPage()  {
		if (this.NeedsLoading) {
			if (this.NeedsProgressBar) {
				MyApplication.CurrentProgress.Reset(0);
				MyApplication.CurrentProgress.SetMessage("YŸkleniyor..");
				MyApplication.CurrentProgress.ShowLoading();
			}
			new RebindPageInThread<Object, Object, Object>().execute();
		}
		else
			this.Bind();
	}
	
	public void ShowLoading(){
		if(Dialog == null){
			Dialog = ProgressDialog.show(this, "", this.SubmitText);
			if(MyApplication.CurrentProgress.TotalCount > 0){
				Dialog.setMax(MyApplication.CurrentProgress.TotalCount);	
			}
			Dialog.show();
		}
	}
	public void HideLoading(){
		Dialog.dismiss();
		Dialog = null;
	}
	
//	protected LinearLayout AddTextCell(String Value, boolean bold, int forecolor){	
//		return this.AddTextCell(Value, bold, forecolor, 12);
//	}
//	
//	protected LinearLayout AddTextCell(String Value, boolean bold, int forecolor, int TextSize){
//		return this.AddTextCell(Value, bold, forecolor, TextSize, 20,15);
//	}
//	
//	protected LinearLayout AddTextCell(String Value, boolean bold, int forecolor, int PaddingLeft, int PaddingTop){
//		return this.AddTextCell(Value, bold, forecolor,  12, PaddingLeft,PaddingTop);
//	}
//	
//	protected LinearLayout AddTextCell(String Value, boolean bold, int forecolor, int TextSize, int PaddingLeft, int PaddingTop){
//		return this.AddTextCell(Value, bold, forecolor, 12, PaddingLeft,PaddingTop, 0, 0);
//	}
	
	protected LinearLayout AddTextCell(String Value, boolean bold, int forecolor, int TextSize, int PaddingLeft, int PaddingTop, int PaddingRight, int PaddingBottom){
		LayoutParams params= (new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,   LayoutParams.WRAP_CONTENT, 1f));
		return this.AddTextCell(Value, params, bold, forecolor, TextSize, PaddingLeft,PaddingTop, PaddingRight, PaddingBottom);
	}
	
	
	protected LinearLayout AddTextCell(String Value, LayoutParams params, boolean bold, int forecolor, int TextSize, int PaddingLeft, int PaddingTop, int PaddingRight, int PaddingBottom){
		LinearLayout Cell = new LinearLayout(this);
		//Cell.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,   LayoutParams.WRAP_CONTENT, 1f));
		Cell.setLayoutParams(params);
		
		TextView ColumnHeader = new TextView(this);
		ColumnHeader.setText(Value);
		ColumnHeader.setPadding(PaddingLeft, PaddingTop,PaddingRight, PaddingBottom);
		ColumnHeader.setTextColor(forecolor);
		ColumnHeader.setTextSize(TextSize);
		ColumnHeader.setGravity(Gravity.LEFT);
		if (bold) { ColumnHeader.setTypeface(null,Typeface.BOLD); }
		Cell.addView(ColumnHeader);
		return Cell;
	}
	
	protected LinearLayout AddImage(Drawable drawable, int PaddingLeft, int PaddingTop, int PaddingRight, int PaddingBottom){
		LinearLayout Cell = new LinearLayout(this);
		Cell.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,   LayoutParams.WRAP_CONTENT));
		Cell.setPadding(PaddingLeft, PaddingTop,PaddingRight, PaddingBottom);
		
		ImageView Image = new ImageView(this);
		Image.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,   LayoutParams.WRAP_CONTENT));
		Image.setBackgroundDrawable(drawable);
		Cell.addView(Image);
		return Cell;
	}
	
//	protected void Kill(){
//		Intent intent = new Intent();
//        setResult(KILL_YOURSELF, intent);
//		this.finish();
//	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data); 
    	MyApplication.ActiveForm = this;
		MyApplication.CurrentContext = this.getApplicationContext();
	}
}
