package com.penguen.sharebugg.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.penguen.sharebugg.application.Brand;

public class ReadBrandsTask extends AsyncTask<String, List<Brand>, List<Brand>> {
	 
	private ProgressDialog dialog;
	protected Context applicationContext;
	 
	protected void onPreExecute() {
	}
	
	protected void onPostExecute(List<String> posts) {
	}

	protected List<Brand> doInBackground(String... params) {
		
		StringBuilder url = new StringBuilder(params[0]);
	    HttpGet geturl = new HttpGet(url.toString());
	    HttpClient client = new DefaultHttpClient();
	    HttpResponse response;
	 
	    try {
	        response = client.execute(geturl);
	        int status = response.getStatusLine().getStatusCode();
	        if (status == 200) {
	            HttpEntity entity = response.getEntity();
	            String data = EntityUtils.toString(entity);
	 
	            JSONArray item = new JSONArray(data);
	 
	            List<Brand> resultList = parseJson(item);
	 
	            return resultList;
	        }
	 
	    } catch (ClientProtocolException clientExcep) {
	        clientExcep.printStackTrace();
	    } catch (IOException ioExcep) {
	        ioExcep.printStackTrace();
	    } catch (JSONException jsonExcep) {
	        jsonExcep.printStackTrace();
	    }
	    return null;
	}

	public static List<Brand> parseJson(JSONArray array) throws JSONException {
		System.out.println("JSON ITEM:" + array.toString());
	    int brandId = 0;
	    String brandName = "";
	    String imageName = "";
	 
	    List<Brand> brandList = new ArrayList<Brand>();
	 
	    if(array != null) {
	        for(int counter = 0; counter < array.length(); counter++) {
	            String rowItem = array.getString(counter);
	        	System.out.println("ROW: " + rowItem);
	            if(counter % 3 == 0) {
	            	brandId = Integer.parseInt(rowItem);
	            } else if(counter % 3 == 1) {
	            	brandName = rowItem;
	            } else if(counter % 3 == 2) {
	            	imageName = rowItem;
	            	
	        	    Brand row = new Brand();

	        	    row.setId(brandId);
	            	row.setName(brandName);
	            	row.setImageName(imageName);

	            	brandList.add(row);
	            }
	        }
	    }
	    return brandList;
	}
	
}