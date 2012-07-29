package com.penguen.sharebugg;

import java.util.List;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.penguen.sharebugg.application.Brand;
import com.penguen.sharebugg.service.ReadBrandsTask;
import com.penguen.sharebugg.util.Utility;


public class BuggListPage extends BasePage {
	 private List<Brand> brandList;
	 
		protected void DrawPage() {
			super.DrawPage();
			this.header.setBackgroundDrawable(getResources().getDrawable(R.drawable.menu4));
		}

		protected void Bind() {
			System.out.println("At BIND!!!");
			try {
				brandList = new ReadBrandsTask().execute(Utility.getBrandsURL()).get();
				System.out.println("Brand List received: " + brandList.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		protected void OnAfterBind() {
			System.out.println("At AFTER BIND!!!");
			this.bindBrands();
			super.OnAfterBind();
		}

		private void bindBrands() {
			ScrollView scrollView = new ScrollView(this);
			this.container.addView(scrollView);
			this.list = new LinearLayout(this);
		    list.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT ));
		    scrollView.addView(list);
		    list.setOrientation(LinearLayout.VERTICAL);
		    this.drawBrands();	 
		}

		private void drawBrands() {
			if (this.isOnline()) {
				list.removeAllViews();
				
				View.OnClickListener activityLauncher = new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						if(v instanceof LinearLayout) {
							int brandIdSelected = ((LinearLayout) v).getId();
							System.out.println("SELECTED BRAND ID: " + brandIdSelected);

							Intent intent = new Intent(v.getContext(), BrandProductsListPage.class);
							intent.putExtra("brandIdSelected", brandIdSelected);
							startActivityForResult(intent, 0);
							
						}
					}
				};

				for (Brand row : brandList) {
					System.out.println("Brand Name: " + row.getName());
					
					LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
					LinearLayout brandContainer =  new LinearLayout(this);
					brandContainer.setPadding(0, 5, 0, 0);
					brandContainer.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
					
					LinearLayout nameContainer = this.AddTextCell(row.getName(), false , Color.parseColor("#000000"), 15, 10, 10, 10, 5);
					nameContainer.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
					brandContainer.setOnClickListener(activityLauncher);
					brandContainer.addView(nameContainer);
					brandContainer.setId(row.getId());
					
					list.addView(brandContainer);  
					list.setClickable(true);
				}
			}
		}

}
