package com.adthena.code.util;

import java.util.List;
import java.util.Map;

import com.adthena.code.EShop.Cart;

public class EShopUtil {
	
	public enum Items {
	    BREAD, MILK, APPLE, SOUP
	}

	public static void addToCart(Map<String, List<String>> inputItems, Cart cart) {
		inputItems.entrySet().stream()
	      .forEach(e -> {
	    	  cart.addCartItem(e.getKey(), e.getValue().size());
	      });
	}
}
