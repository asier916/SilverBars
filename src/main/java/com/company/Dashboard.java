package com.company;

import com.company.model.Order;
import com.company.model.Side;
import com.company.order.book.*;

import java.util.Comparator;
import java.util.List;

import static java.util.Objects.requireNonNull;

public class Dashboard {

    /**
     *  The Dashboard keeps two separate books for SELL and BUY orders. They use the same interface and methods,
     *  but use a different comparator to order the orders.
     */
    private final Book offersBook;
    private final Book bidsBook;

    public Dashboard() {

        offersBook = new Book(Side.SELL, new Comparator<PriceLevel>() {
            @Override
            public int compare(PriceLevel p1, PriceLevel p2) {
                return Double.compare(p1.getPrice(), p2.getPrice());
            }
        });

        bidsBook = new Book(Side.BUY, new Comparator<PriceLevel>() {
            @Override
            public int compare(PriceLevel p1, PriceLevel p2) {
                return Double.compare(p2.getPrice(), p1.getPrice());
            }
        });
    }

    /**
     * Adds an order object to the dashboard.
     *
     * @param  order  The order object to be added to the dashboard.
     */
    public void addOrder(final Order order) {
        requireNonNull(order);
        if (order.getSide().equals(Side.BUY)) {
            bidsBook.add(order);
        } else {
            offersBook.add(order);
        }
    }

    /**
     * Returns a list of all the PriceVolumePairs contained in the dashboard for a side
     *
     * @param side The side for which all orders need to be retrieved
     * @return A list with all the PriceVolumePairs
     * @see PriceVolumePair
     * @see Side
     */
    public List<PriceVolumePair> getPriceVolumePairs(Side side) {
        if (side.equals(Side.BUY)) {
            return bidsBook.getPriceVolumePairs();
        } else {
            return offersBook.getPriceVolumePairs();
        }
    }

    /**
     * Removes an order by id.
     * @param id id of order to remove
     * @return true if successful, false if the other was not found.
     * @see Order
     */
    public boolean removeOrder(String id) {
        if (bidsBook.removeOrder(id)) {
            return true;
        } else {
            return offersBook.removeOrder(id);
        }
    }

    /**
     * Removes an order from the dashboard, based on side, userId, price and quantity.
     * If there are more than one order with the same parameters it provides no guarantees about which
     * one is removed.
     *
     * @param side of order to remove
     * @param userId owner of of order to remove
     * @param price price of of order to remove
     * @param quantity quantity of of order to remove
     * @return true if successful, false if the other was not found.
     *
     * @see Side
     * @see Order
     */
    public boolean removeOrder(Side side,String userId, double price, double quantity) {
        if(side.equals(Side.BUY)){
           return bidsBook.removeOrder(userId,price,quantity);
        }else{
            return offersBook.removeOrder(userId,price,quantity);
        }
    }

    /**
     * Removes an order from the dashboard.
     * @param order order to remove
     * @return true if successful, false if the other was not found.
     * @see Order
     */
    public boolean removeOrder(Order order){
        if(order.getSide().equals(Side.BUY)){
            return bidsBook.removeOrder(order);
        }else{
            return offersBook.removeOrder(order);
        }
    }
}
