package com.company.order.book;

import com.company.model.Order;

import java.util.ArrayList;
import java.util.List;


public class PriceLevel {

    /**
     * The price for this particular price level
     */
    private double price;
    /**
     * A list of all the orders that have this price.
     */
    private List<Order> orders;

    public PriceLevel(double price) {
        this.price = price;
        this.orders = new ArrayList();
    }

    public double getPrice() {
        return price;
    }

    /**
     *
     * @return the total volume for all the orders stored at this price level.
     */
    public double getTotalVolume() {
        return orders.stream().mapToDouble(Order::getVolume).sum();
    }

    public void addOrder(final Order order) {
        orders.add(order);
    }

    @Override
    public int hashCode() {
        return Double.hashCode(price);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof PriceLevel)) {
            return false;
        }
        PriceLevel that = (PriceLevel) o;

        return that.getPrice() == price;
    }

    /**
     * Removes an order from the dashboard.
     * @param order order to remove from the dashboard.
     * @return true if successful, false if the other was not found.
     * @see Order
     */
    public boolean removeOrder(Order order) {
        for(int i=0;i<orders.size();i++){
            if(orders.get(i).equals(order)){
                orders.remove(i);
                return true;
            }
        }
        return false;
    }

    /**
     * Removes an order by id.
     * @param id id of order to remove
     * @return true if successful, false if the other was not found.
     * @see Order
     */
    public boolean removeOrder(String id) {
        for (int i = 0; i < orders.size(); i++) {
            if (orders.get(i).getId().equals(id)) {
                orders.remove(i);
                return true;
            }
        }
        return false;
    }


    /**
     * Removes an order from the dashboard, based on side, userId, price and quantity.
     * If there are more than one order with the same parameters it provides no guarantees about which
     * one is removed.
     *
     * @param userId owner of order to remove
     * @param quantity quantity of order to remove
     * @return true if successful, false if the other was not found.
     *
     * @see Order
     */
    public boolean removeOrder(String userId, double quantity) {
        for (int i = 0; i < orders.size(); i++) {
            Order order = orders.get(i);
            if (order.getUserId().equals(userId) && order.getVolume() == quantity) {
                orders.remove(i);
                return true;
            }
        }
        return false;
    }


    /**
     *
     * @return the total number of orders with this price.
     */
    public int getNumberOfOrders() {
        return orders.size();
    }
}
