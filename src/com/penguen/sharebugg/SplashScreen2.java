package com.penguen.sharebugg;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class SplashScreen2 extends BasePage {
	private ImageButton NextButton;
	 
	protected void DrawPage() {
		this.NeedsProgressBar=false;
		setContentView(R.layout.splashscreen2);	  
		this.NextButton = (ImageButton)this.findViewById(R.id.nextbutton2);
		this.NextButton.setOnClickListener(new OnClickListener() {    
			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(), SplashScreen3.class);
				startActivityForResult(intent, 0);
				finish();
			}
		});
	}
}
