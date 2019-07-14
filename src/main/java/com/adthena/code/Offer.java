package com.adthena.code;

import java.math.BigDecimal;

public class Offer {
    public enum OfferType {
        MONEY_DISCOUNT, DISCOUNTED_ITEM
    }

    public enum DiscountType {
        PERCENTAGE, AMOUNT
    }

    private Item cartItem;
    private Integer quantity;
    private OfferType type;
    private BigDecimal discount;
    private DiscountType discountType;
    private Item offerItem;
    private BigDecimal offerItemDiscount;
    private DiscountType offerItemDiscountType;

    public Offer(Item cartItem, Integer quantity, DiscountType discountType, BigDecimal discount) {
        this.cartItem = cartItem;
        this.quantity = quantity == null ? 0 : quantity;
        this.type = OfferType.MONEY_DISCOUNT;
        this.discountType = discountType;
        this.discount = discount;
    }

    public Offer(Item cartItem, Integer quantity, Item item, DiscountType discountType, BigDecimal offerItemDiscount) {
        this.cartItem = cartItem;
        this.quantity = quantity == null ? 0 : quantity;
        this.type = OfferType.DISCOUNTED_ITEM;
        this.offerItem = item;
        this.offerItemDiscountType = discountType;
        this.offerItemDiscount = offerItemDiscount;
    }

    public Item getCartItem() {
        return cartItem;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public OfferType getType() {
        return type;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public DiscountType getDiscountType() {
        return discountType;
    }

    public Item getOfferItem() {
        return offerItem;
    }

    public BigDecimal getOfferItemDiscount() {
        return offerItemDiscount;
    }

    public DiscountType getOfferItemDiscountType() {
        return offerItemDiscountType;
    }

    @Override
    public String toString() {
        switch (type) {
        case MONEY_DISCOUNT:
            if (discountType == DiscountType.PERCENTAGE) {
                return cartItem.getName() + " " + discount + "%" + " off";
            } else {
                return cartItem.getName() + " £" + discount + " off";
            }
        case DISCOUNTED_ITEM:
            if (offerItemDiscountType == DiscountType.PERCENTAGE) {
                return offerItem.getName() + " " + offerItemDiscount + "% off";
            } else {
                return offerItem.getName() + " £" + discount + " off";
            }
        default:
            return null;
        }
    }

}
