package com.penguen.sharebugg;

import com.penguen.sharebugg.application.MyApplication;

import android.content.Intent;

public class SplashScreen extends BasePage {
	 
		protected void DrawPage() {
			this.NeedsProgressBar=false;
			setContentView(R.layout.splashscreen0);	  
		}
		
		protected void Bind() {
			try {
				Thread.sleep(4000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		protected void OnAfterBind() {

			Intent intent = new Intent(MyApplication.CurrentContext, SplashScreen1.class);
			startActivityForResult(intent, 0);
			super.OnAfterBind();
			this.finish();
		}
}
