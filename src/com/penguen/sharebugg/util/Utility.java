package com.penguen.sharebugg.util;

public class Utility {

	public static String getBrandsURL() {
		return "http://www.sharebugg.com/bar.php?sec=barcodesec&getir=markalar";
	}

	public static String getProductsByBrand(int brandId) {
		return "http://www.sharebugg.com/bar.php?sec=barcodesec&getir=markaurun&companyid=" + brandId;
	}


}
