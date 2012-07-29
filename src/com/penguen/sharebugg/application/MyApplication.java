package com.penguen.sharebugg.application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.penguen.sharebugg.BasePage;

import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class MyApplication extends Application {
	public static clsUser CurrentUser;
	public static Progress CurrentProgress;
	public static Context CurrentContext;
	public static BasePage ActiveForm;
	public static String WarningResponse ="";
	public static String AuthenticationKey ="";
	public static final String PREFS_NAME = "MyPrefsFile";
	public static final String PREF_AuthKey = "AuthenticationKey";
	private static final String ServiceURL = "http://ec2-23-21-169-206.compute-1.amazonaws.com/";
	private static final String Login = "users/sign_in.json";
    private static final String AuthToken = "?auth_token=";
	private static final String Cheepins = "latest_cheepins.json";
	private static final String Cheepars = "cheepars.json";
	private static final String SystemDate = "now.json";
	public static boolean UserAuthorized;

	public static clsUser getCurrentUser() {
		return CurrentUser;
	}
	public static void setCurrentUser(clsUser currentUser) {
		CurrentUser = currentUser;
	}
	
	public void onCreate() {
		super.onCreate();
		CurrentContext = getApplicationContext();
		CurrentProgress = new Progress();
		UserAuthorized=false;
		LoadAuthenticationKey();
		if (AuthorizeUser()) {
			UserAuthorized =true;
			//this.GetSystemDate();
		}
	}
	
	public  void LoadAuthenticationKey () {
		if (AuthenticationKey ==null || AuthenticationKey.equals("") ) {
			SharedPreferences pref = getSharedPreferences(MyApplication.PREFS_NAME,MODE_PRIVATE);   
			AuthenticationKey= pref.getString(MyApplication.PREF_AuthKey, null);
		}
	}
	
	public boolean AuthorizeUser() {
		if (AuthenticationKey ==null || AuthenticationKey.equals("")) {
			HttpClient client = new DefaultHttpClient();
	        HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000); //Timeout Limit
	        HttpResponse response;
	        JSONObject json = new JSONObject();
	        try{
	        	//String DeviceID = Secure.getString(MyApplication.CurrentContext.getContentResolver(),  Secure.ANDROID_ID); 
	            HttpPost post = new HttpPost(ServiceURL + Login);
	            json.put("password", "123456");
	            json.put("email", "huseyinozcan@gmail.com");
	            
	            JSONObject jsonUser = new JSONObject();
	            jsonUser.put("user", json);
	            StringEntity se = new StringEntity(jsonUser.toString());  
	            
	            post.setEntity(se);
	            post.setHeader("Accept", "application/json");
	            post.setHeader("Content-type", "application/json");
	            response = client.execute(post);
	            if(response!=null){
	            	InputStream instream = response.getEntity().getContent();
	            	String result= convertStreamToString(instream);
	            	JSONObject jsonResult=new JSONObject(result);
	            	AuthenticationKey = jsonResult.getString("auth_token");
	            	 getSharedPreferences(MyApplication.PREFS_NAME,MODE_PRIVATE)
	 	            .edit()
	 	            .putString(MyApplication.PREF_AuthKey,AuthenticationKey)
	 	            .commit();
	            	instream.close();
	            	return true;
	            }
	        }
	        catch(Exception e){
	            e.printStackTrace();
	        }    
	        CurrentUser = null;
	        return false;
		}
		CurrentUser = new clsUser();
		CurrentUser.UserName = AuthenticationKey;
		return true;
	}
	
	
	
	public static Bitmap loadBitmap(String url) {
		Bitmap bitmap = null;
		try{
		    URL ulrn = new URL(url);
		    HttpURLConnection con = (HttpURLConnection)ulrn.openConnection();
		    InputStream is = con.getInputStream();
		    bitmap=  BitmapFactory.decodeStream(is);
		    }
			catch(Exception e){
				
			}
	    return bitmap;
	}
	
	public static void ShowErrorAlertDialog( String Title, String Message) {
		AlertDialog alertDialog = new AlertDialog.Builder(MyApplication.ActiveForm).create();
		alertDialog.setTitle(Title);
		alertDialog.setMessage(Message);
		alertDialog.setButton("Tamam", new DialogInterface.OnClickListener() {
		   public void onClick(DialogInterface dialog, int which) {
			  dialog.cancel();
		   }
		});
		alertDialog.show();
	}
	
	private static String convertStreamToString(InputStream is) {
        /*
         * To convert the InputStream to String we use the BufferedReader.readLine()
         * method. We iterate until the BufferedReader return null which means
         * there's no more data to read. Each line will appended to a StringBuilder
         * and returned as String.
         */
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
 
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
	private  static String URLEncode(String Value) {
		String NewValue=Value;
		NewValue= Value.replace("þ", "s").replace("Þ", "S");
		NewValue= NewValue.replace("ý", "i").replace("Ý", "I");
		NewValue= NewValue.replace("ö", "o").replace("Ö", "O");
		NewValue= NewValue.replace("ü", "u").replace("Ü", "U");
		NewValue= NewValue.replace("ð", "g").replace("Ð", "G");
		NewValue= NewValue.replace(" ", "%20");
		NewValue= NewValue.replace("ç", "c").replace("Ç", "C");
		return NewValue;
	}
	
	private boolean isNetworkAvailable() {
	    ConnectivityManager connectivityManager    = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null;
	}
	
	private static void ResetResponse (){
		MyApplication.WarningResponse="";
	}
}

