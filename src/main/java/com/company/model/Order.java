package com.company.model;

import java.util.UUID;

import static java.util.Objects.requireNonNull;

public class Order {
    private final String id;
    private final Side side;
    private final String userId;
    private final double quantity;
    private final double price;

    public Order(final Side side, final String userId, double quantity, double price) {
        this.id = UUID.randomUUID().toString();
        this.side = requireNonNull(side);
        this.userId = requireNonNull(userId);

        this.quantity = quantity;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public Side getSide() {
        return side;
    }

    public String getUserId() {
        return userId;
    }

    public double getVolume() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Order)) {
            return false;
        }
        Order that = (Order) o;

        return this.getId().equals(that.getId())
                && this.getVolume() == that.getVolume()
                && this.getUserId().equals(that.getUserId())
                && this.getPrice() == that.getPrice()
                && this.getSide() == that.getSide();
    }
}
