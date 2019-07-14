package com.adthena.code;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.adthena.code.Offer.DiscountType;
import com.adthena.code.util.EShopUtil;
import com.adthena.code.util.EShopUtil.Items;

public class EShop {

	private List<Item> items = new ArrayList<Item>();
    private List<Offer> offers = new ArrayList<Offer>();
    
    public EShop() {
        Item soup = new Item(Items.SOUP.name().toUpperCase(), Unit.TIN, BigDecimal.valueOf(0.65));
        items.add(soup);
        Item bread = new Item(Items.BREAD.name().toUpperCase(), Unit.LOAF, BigDecimal.valueOf(0.80));
        items.add(bread);
        Item milk = new Item(Items.MILK.name().toUpperCase(), Unit.BOTTLE, BigDecimal.valueOf(1.30));
        items.add(milk);
        Item apple = new Item(Items.APPLE.name().toUpperCase(), Unit.BAG, BigDecimal.valueOf(1.00));
        items.add(apple);

        offers.add(new Offer(apple, null, DiscountType.PERCENTAGE, BigDecimal.valueOf(10.00)));
        offers.add(new Offer(soup, 2, bread, DiscountType.PERCENTAGE, BigDecimal.valueOf(50.00)));
    }
    
	public class Cart {
        private List<CartItem> cartItems = new ArrayList<CartItem>();
        private BigDecimal subtotal = new BigDecimal(0);
        private BigDecimal total = new BigDecimal(0);

        public List<CartItem> addCartItem(String itemName, int quantity) {
            return addCartItem(new CartItem(getItem(itemName), quantity));
        }

        public List<CartItem> addCartItem(CartItem cartItem) {
            cartItem.price = cartItem.item.getPrice();
            cartItem.total = cartItem.price.multiply(BigDecimal.valueOf(cartItem.quantity));
            cartItems.add(cartItem);
            return cartItems;
        }

        public List<CartItem> getCartItems() {
            return cartItems;
        }

        public BigDecimal getSubtotal() {
            return subtotal;
        }

        public List<Offer> getOffers() {
            return offers;
        }

        public BigDecimal getTotal() {
            return total;
        }

        public CartItem getByItemName(String itemName) {
        	return cartItems.stream().filter(cartItem -> cartItem.getItem().getName().equalsIgnoreCase(itemName)).findAny().orElse(null);
        }
    }
	
	public static class CartItem {
        private Item item;
        private int quantity;
        private BigDecimal price;
        private BigDecimal discount;
        private BigDecimal subtotal;
        private BigDecimal total;
        private Offer offer;

        public CartItem(Item item, int quantity) {
            this.item = item;
            this.quantity = quantity;
        }

        public Item getItem() {
            return item;
        }

        public int getQuantity() {
            return quantity;
        }

        public BigDecimal getPrice() {
            return price;
        }

        public BigDecimal getDiscount() {
            return discount;
        }

        public void setDiscount(BigDecimal discount) {
            this.discount = discount;
        }

        public BigDecimal getTotal() {
            return total;
        }

        public Offer getOffer() {
            return offer;
        }

        @Override
        public String toString() {
            return "CartItem [" + (item != null ? "item=" + item + ", " : "") + "quantity=" + quantity + ", "
                    + (price != null ? "price=" + price + ", " : "")
                    + (discount != null ? "discount=" + discount + ", " : "")
                    + (subtotal != null ? "subtotal=" + subtotal + ", " : "")
                    + (total != null ? "total=" + total + ", " : "") + (offer != null ? "offer=" + offer : "") + "]";
        }

    }
	
	public Cart createCart() {
        return new Cart();
    }
	
    public Cart closeCart(Cart cart) {
        applyOffers(cart);

        for (CartItem cartItem : cart.getCartItems()) {
            cartItem.subtotal = cartItem.price.multiply(BigDecimal.valueOf(cartItem.quantity));

            if (cartItem.discount != null) {
                cartItem.total = cartItem.price.multiply(BigDecimal.valueOf(cartItem.quantity))
                        .subtract(cartItem.discount);
            } else {
                cartItem.total = cartItem.subtotal;
            }

            cart.subtotal = cart.subtotal.add(cartItem.subtotal);
            cart.total = cart.total.add(cartItem.total);
        }
        return cart;
    }
    
    private void applyOffers(Cart cart) {
        for (CartItem cartItem : cart.cartItems) {
            for (Offer offer : offers) {
                if (offer.getCartItem().getName().equalsIgnoreCase(cartItem.item.getName())
                        && offer.getQuantity() <= cartItem.quantity) {
                    switch (offer.getType()) {
                    case MONEY_DISCOUNT:
                        cartItem.offer = offer;
                        switch (offer.getDiscountType()) {
                        case AMOUNT:
                            cartItem.discount = offer.getDiscount().multiply(BigDecimal.valueOf(cartItem.quantity));
                            break;
                        case PERCENTAGE:
                            cartItem.discount = offer.getDiscount().divide(BigDecimal.valueOf(100))
                                    .multiply(cartItem.price).multiply(BigDecimal.valueOf(cartItem.quantity));
                            break;
                        }
                        break;
                    case DISCOUNTED_ITEM:
                        CartItem offerItem = cart.getByItemName(offer.getOfferItem().getName());
                        if (offerItem != null) {
                            offerItem.offer = offer;
                            switch (offer.getOfferItemDiscountType()) {
                            case AMOUNT:
                                offerItem.discount = offer.getDiscount()
                                        .multiply(BigDecimal.valueOf(offerItem.quantity));
                                break;
                            case PERCENTAGE:
                                offerItem.discount = offer.getOfferItemDiscount().divide(BigDecimal.valueOf(100))
                                        .multiply(offerItem.price).multiply(BigDecimal.valueOf(offerItem.quantity));
                                break;
                            }
                        }
                        break;
                    }
                }
            }
        }
    }
	
	public Item getItem(String itemName) {
        if (itemName != null) {
        	return items.stream().filter(item -> item.getName().equalsIgnoreCase(itemName)).findFirst().get();
        }
        throw new RuntimeException("Unknown item " + itemName);
    }
	
	public static void main(String[] args) {
		System.out.println("Please enter items:");
		Cart cart = readItemsIntoCart();
		System.out.println("Subtotal: " + cart.subtotal);
		boolean hasOffer = false;
		
        for (CartItem cartItem : cart.cartItems) {
            if (cartItem.getOffer() != null) {
                hasOffer = true;
                System.out.println(cartItem.offer + ": -" + cartItem.discount);
            }
        }
        if (!hasOffer) {
            System.out.println("(no offers available)");
        }
        System.out.println("Total: " + cart.total);

    }
	
	public static Cart readItemsIntoCart() {		
		Map<String, List<String>> inputItems = new HashMap<>();
		EShop eShop = new EShop();
		Cart cart = eShop.createCart();
		try (Scanner scanner = new Scanner(System.in)) {
			String[] itemTokens = scanner.nextLine().split("\\s");
			List<String> insertItem = null;
			for(String item : itemTokens) {				
				if(item.equalsIgnoreCase(Items.BREAD.name()) || item.equalsIgnoreCase(Items.MILK.name())
						|| item.equalsIgnoreCase(Items.APPLE.name()) || item.equalsIgnoreCase(Items.SOUP.name())) {
					insertItem = inputItems.get(item);
					if(insertItem != null) {
						insertItem.add(item);
					} else {
						insertItem = new ArrayList<>();
						insertItem.add(item);
					}
					inputItems.put(item, insertItem);
				}
			}
		}
		EShopUtil.addToCart(inputItems, cart);
		eShop.closeCart(cart);
		return cart;
	}
}
