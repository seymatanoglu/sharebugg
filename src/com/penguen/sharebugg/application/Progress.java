package com.penguen.sharebugg.application;
import android.os.Handler;
import android.os.Message;

public class Progress {
	public String ViewMessage = "";
	public int TotalCount = 0;
	public int CurrentCount = 0;
	public String ExMessage = "";
	private boolean HasManuallyFinished = false;
	public boolean HasFinished(){
		return this.HasManuallyFinished || (TotalCount > 0 && CurrentCount == TotalCount);
	}
	public String Percentage(){
		if(TotalCount > 0)
			return Integer.toString(CurrentCount / TotalCount * 100);
		else
			return "0";
	}
	public void Finish(){
		this.HasManuallyFinished = true;
		 if(MyApplication.ActiveForm != null && MyApplication.ActiveForm.Dialog != null)
 			MyApplication.ActiveForm.HideLoading();
	}
	public void Fail(String Message){
		this.ExMessage = Message;
		this.SetMessage(Message);
	}
	public void SetMessage(String Message){
		this.ViewMessage = Message;
		Message msg = handler.obtainMessage();
        handler.sendMessage(msg);
	}
	public void Next(String Message){
		this.SetMessage(Message);
		this.CurrentCount++;
	}
	
	public void CloseDialog(){
		if(MyApplication.ActiveForm != null)
			MyApplication.ActiveForm.HideLoading();
	}
	public void ShowLoading(){
		if(MyApplication.ActiveForm != null)
			MyApplication.ActiveForm.ShowLoading();
	}
	final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if(MyApplication.ActiveForm != null && MyApplication.ActiveForm.Dialog != null)
    			MyApplication.ActiveForm.Dialog.setMessage(ViewMessage);
        }
    };
    
	public void Reset(int TotalCount){
		this.TotalCount = TotalCount;
		this.CurrentCount = 0;
		this.ViewMessage = "Yükleniyor..";
		this.ExMessage = "";
		this.HasManuallyFinished = false;
	}
	public Progress(){
		
	}
}



