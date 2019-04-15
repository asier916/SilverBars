package com.company.order.book;

import com.company.model.Order;
import com.company.model.Side;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

public class Book {

    private final Side side;
    /**
     *  Both bids and offers use the same data structure and methods, but they use a different comparator to sort
     *  the orders. SELL orders are ordered incrementally and BUY orders are ordered from highest to lowest value.
     */
    private final Comparator<PriceLevel> comparator;
    /**
     * Each book keeps an ordered list of PriceLevels.
     *
     * Each PriceLevel corresponds to a given price, and contains a list of all the orders that have been stored
     * with the same value for the price.
     *
     * I haven't used a HashMap structure with either a Double or a BigDecimal as a key, because for values close
     * enough the hash code would be the same and therefore would not be able to distinguish two different price levels.
     */
    private final List<PriceLevel> priceLevels;

    public Book(final Side side, final Comparator<PriceLevel> comparator) {
        this.side = side;
        this.priceLevels = new ArrayList<>();
        this.comparator = comparator;
    }

    public Side getSide() {
        return side;
    }

    /**
     * Adds an order to the book.
     * @param order Order to add to book.
     */
    public void add(final Order order) {
        requireNonNull(order);

        PriceLevel priceLevel;

        for (int i = 0; i < priceLevels.size(); i++) {
            if (priceLevels.get(i).getPrice() == order.getPrice()) {
                priceLevel = priceLevels.get(i);
                priceLevel.addOrder(order);
                return;
            }
        }

        priceLevel = new PriceLevel(order.getPrice());
        priceLevel.addOrder(order);

        priceLevels.add(priceLevel);
        Collections.sort(priceLevels, comparator);
    }

    public List<PriceVolumePair> getPriceVolumePairs() {
        return priceLevels.stream().map(priceLevel -> new PriceVolumePair(priceLevel.getPrice(), priceLevel.getTotalVolume())).collect(Collectors.toList());
    }

    /**
     * Removes an order from the dashboard.
     * @param order order to remove
     * @return true if successful, false if the other was not found.
     * @see Order
     */
    public boolean removeOrder(Order order) {
        for (int i = 0; i < priceLevels.size(); i++) {
            PriceLevel level = priceLevels.get(i);
            if(level.removeOrder(order)){
                if(level.getNumberOfOrders()<1){
                    //If there are no more orders stored with this price, the PriceLevel is removed from the book.
                    priceLevels.remove(i);
                }
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
        for (int i = 0; i < priceLevels.size(); i++) {
            PriceLevel level = priceLevels.get(i);
            if (level.removeOrder(id)) {
                if (level.getNumberOfOrders() < 1) {
                    //If there are no more orders stored with this price, the PriceLevel is removed from the book.
                    priceLevels.remove(i);
                }
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
     * @param price price of order to remove
     * @param quantity quantity of order to remove
     * @return true if successful, false if the other was not found.
     *
     * @see Order
     */
    public boolean removeOrder(String userId, double price, double quantity) {
        for (int i = 0; i < priceLevels.size(); i++) {
            PriceLevel level = priceLevels.get(i);
            if (level.getPrice() == price && level.removeOrder(userId, quantity)) {
                if (level.getNumberOfOrders() < 1) {
                    //If there are no more orders stored with this price, the PriceLevel is removed from the book.
                    priceLevels.remove(i);
                }
                return true;
            }
        }
        return false;
    }
}
