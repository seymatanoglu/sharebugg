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
import com.penguen.sharebugg.application.Product;

public class ReadProductsOfBrandTask extends AsyncTask<String, List<Product>, List<Product>> {
	 
	private ProgressDialog dialog;
	protected Context applicationContext;
	 
	protected void onPreExecute() {
	}
	
	protected void onPostExecute(List<String> posts) {
	}

	protected List<Product> doInBackground(String... params) {
		
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
	 
	            List<Product> resultList = parseJson(item);
	 
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

	public static List<Product> parseJson(JSONArray array) throws JSONException {
		System.out.println("JSON ITEM for Products:" + array.toString());

		int productId = 0;
	    int companyId = 0;
	    String barcode = "";
	    String description = "";
	    String model = "";
	    String price = "";
	    String discount = "";
	    String img1 = "";
	    String img2 = "";
	    String img3 = "";
	 
	    List<Product> productList = new ArrayList<Product>();
	 
	    if(array != null) {
	        for(int counter = 0; counter < array.length(); counter++) {
	            String rowItem = array.getString(counter);
	        	System.out.println("Product ROW: " + rowItem);
	        	switch(counter % 10) {
	        	case 0:
	        		productId = Integer.parseInt(rowItem);
	        		break;
	        	case 1:
	        		barcode = rowItem;
	        		break;
	        	case 2:
	        		companyId = Integer.parseInt(rowItem);
	        		break;
	        	case 3:
	        		description = rowItem;
	        		break;
	        	case 4:
	        		model = rowItem;
	        		break;
	        	case 5:
	        		price = rowItem;
	        		break;
	        	case 6:
	        		discount = rowItem;
	        		break;
	        	case 7:
	        		img1 = rowItem;
	        		break;
	        	case 8:
	        		img2 = rowItem;
	        		break;
	        	case 9: {
		        		img3 = rowItem;
		        		
		        		Product row = new Product();
		        		row.setProductId(productId);
		        		row.setBarcode(barcode);
		        		row.setCompanyId(companyId);
		        		row.setDescription(description);
		        		row.setModel(model);
		        		row.setPrice(price);
		        		row.setDiscount(discount);
		        		row.setImg1Name(img1);
		        		row.setImg2Name(img2);
		        		row.setImg3Name(img3);
		        		
		        		productList.add(row);
		        		break;
	        		}
	        	default:
	        		break;
	        	}
	        }
	    }
	    return productList;
	}
	
}