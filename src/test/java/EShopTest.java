import java.math.BigDecimal;

import org.junit.Assert;
import org.junit.Test;

import com.adthena.code.EShop;
import com.adthena.code.EShop.Cart;

public class EShopTest {

	@Test
	public void getPrice_with_appleOffer() throws Exception {
		EShop eShop = new EShop();
		Cart cart = eShop.createCart();
		cart.addCartItem("apple", 2);
		Assert.assertEquals(new BigDecimal(1.80), EShop.readItemsIntoCart().getTotal());
    }
	
	@Test
	public void getPrice_with_appleOffer_alongWithOtherItems() throws Exception {
		EShop eShop = new EShop();
		Cart cart = eShop.createCart();
		cart.addCartItem("apple", 2);
		cart.addCartItem("milk", 1);
		cart.addCartItem("soup", 1);
		cart.addCartItem("bread", 1);
		Assert.assertEquals(new BigDecimal(4.55), EShop.readItemsIntoCart().getTotal());
    }
	
	@Test
	public void getPrice_with_soupOffer() throws Exception {
		EShop eShop = new EShop();
		Cart cart = eShop.createCart();
		cart.addCartItem("soup", 2);
		cart.addCartItem("bread", 1);
		Assert.assertEquals(new BigDecimal(1.70), EShop.readItemsIntoCart().getTotal());
    }
	
	@Test
	public void getPrice_with_noOffer() throws Exception {
		EShop eShop = new EShop();
		Cart cart = eShop.createCart();
		cart.addCartItem("milk", 1);
		cart.addCartItem("soup", 1);
		cart.addCartItem("bread", 1);
		Assert.assertEquals(new BigDecimal(2.75), EShop.readItemsIntoCart().getTotal());
    }
	
	@Test
	public void getPrice_with_InvalidItems() throws Exception {
		EShop eShop = new EShop();
		Cart cart = eShop.createCart();
		cart.addCartItem("invalid", 2);
		Assert.assertEquals(new BigDecimal(0.0), EShop.readItemsIntoCart().getTotal());
    }
}
