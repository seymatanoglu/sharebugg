package com.penguen.sharebugg;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class SplashScreen3 extends BasePage {
	private ImageButton NextButton;
	 
	protected void DrawPage() {
		this.NeedsProgressBar=false;
		setContentView(R.layout.splashscreen3);	  
		this.NextButton = (ImageButton)this.findViewById(R.id.nextbutton3);
		this.NextButton.setOnClickListener(new OnClickListener() {    
			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(), SplashScreen4.class);
				startActivityForResult(intent, 0);
				finish();
			}
		});
	}
}
