package com.penguen.sharebugg;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class SplashScreen4 extends BasePage {
	private ImageButton NextButton;
	 
	protected void DrawPage() {
		this.NeedsProgressBar=false;
		setContentView(R.layout.splashscreen4);	  
		this.NextButton = (ImageButton)this.findViewById(R.id.nextbutton4);
		this.NextButton.setOnClickListener(new OnClickListener() {    
			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(), BuggListPage.class);
				startActivityForResult(intent, 0);
				finish();
			}
		});
	}
}
