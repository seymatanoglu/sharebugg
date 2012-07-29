package com.penguen.sharebugg;

import java.util.List;

import android.graphics.Color;
import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.penguen.sharebugg.application.Product;
import com.penguen.sharebugg.service.ReadProductsOfBrandTask;
import com.penguen.sharebugg.util.Utility;


public class BrandProductsListPage extends BasePage {
	 private List<Product> productsList;
	 
		protected void DrawPage() {
			super.DrawPage();
			this.header.setBackgroundDrawable(getResources().getDrawable(R.drawable.menu4));
		}

		protected void Bind() {
			System.out.println("At Products BIND!!!");
			try {
				Bundle extraParams = getIntent().getExtras(); 
				int brandIdSelected = 0;
				if(extraParams!=null) {
					brandIdSelected = extraParams.getInt("brandIdSelected");
				}

				productsList = new ReadProductsOfBrandTask().execute(Utility.getProductsByBrand(brandIdSelected)).get();
				System.out.println("Product List received: " + productsList.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		protected void OnAfterBind() {
			System.out.println("At AFTER BIND Products!!!");
			this.bindProducts();
			super.OnAfterBind();
		}

		private void bindProducts() {
			ScrollView scrollView = new ScrollView(this);
			this.container.addView(scrollView);
			this.list = new LinearLayout(this);
		    list.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT ));
		    scrollView.addView(list);
		    list.setOrientation(LinearLayout.VERTICAL);
		    this.drawProducts();	 
		}

		private void drawProducts() {
			if (this.isOnline()) {
				list.removeAllViews();
				
				for (Product row : productsList) {
					System.out.println("Product Desc: " + row.getDescription());
					LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
					LinearLayout brandContainer =  new LinearLayout(this);
					brandContainer.setPadding(0, 5, 0, 0);
					brandContainer.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
					
					LinearLayout nameContainer = this.AddTextCell(row.getDescription(), false , Color.parseColor("#000000"), 15, 10, 10, 10, 5);
					nameContainer.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
					
					brandContainer.addView(nameContainer);
					list.addView(brandContainer);  
				}
			}
		}

}
