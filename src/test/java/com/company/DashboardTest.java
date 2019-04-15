package com.company;

import com.company.model.Order;
import com.company.model.Side;
import com.company.order.book.PriceVolumePair;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class DashboardTest {

    Dashboard underTest;

    @Before
    public void setUp() {
        underTest = new Dashboard();
    }

    private Order createOrder(Side side, double price, double volume, String userId) {
        return new Order(side, userId, volume, price);
    }

    @Test
    public void addOrder() {
        Order order = createOrder(Side.SELL, 12.4, 23.4, "user");
        underTest.addOrder(order);

        assertEquals(underTest.getPriceVolumePairs(Side.BUY).size(), 0);

        List<PriceVolumePair> priceVolumePairList = underTest.getPriceVolumePairs(Side.SELL);

        assertEquals(priceVolumePairList.size(), 1);

        assertEquals(priceVolumePairList.get(0).getPrice(), 12.4, 0.0);
        assertEquals(priceVolumePairList.get(0).getVolume(), 23.4, 0.0);

    }

    @Test
    public void bidsOrdersAreOrdered() {
        Order order1 = createOrder(Side.BUY, 12.4, 23.4, "user");
        Order order2 = createOrder(Side.BUY, 13.1, 23.4, "user 2");
        Order order3 = createOrder(Side.BUY, 9.87, 10.21, "user 3");
        Order order4 = createOrder(Side.BUY, 9.87, 11.45, "user 3");
        Order order5 = createOrder(Side.BUY, 5.56, 2.67, "user 4");
        Order order6 = createOrder(Side.BUY, 5.56, 11.6, "user 4");

        underTest.addOrder(order1);
        underTest.addOrder(order2);
        underTest.addOrder(order3);
        underTest.addOrder(order4);
        underTest.addOrder(order5);
        underTest.addOrder(order6);

        List<PriceVolumePair> bidPrices = underTest.getPriceVolumePairs(Side.BUY);
        List<PriceVolumePair> offerPrices = underTest.getPriceVolumePairs(Side.SELL);


        assertEquals(4, bidPrices.size());
        assertEquals(0, offerPrices.size());

        assertEquals(13.1, bidPrices.get(0).getPrice(), 0.0);
        assertEquals(12.4, bidPrices.get(1).getPrice(), 0.0);
        assertEquals(9.87, bidPrices.get(2).getPrice(), 0.0);
        assertEquals(5.56, bidPrices.get(3).getPrice(), 0.0);
    }

    @Test
    public void orderOrdersAreOrdered() {
        Order order1 = createOrder(Side.SELL, 12.4, 23.4, "user");
        Order order2 = createOrder(Side.SELL, 13.1, 23.4, "user 2");
        Order order3 = createOrder(Side.SELL, 9.87, 10.21, "user 3");
        Order order4 = createOrder(Side.SELL, 9.87, 11.45, "user 3");
        Order order5 = createOrder(Side.SELL, 5.56, 2.67, "user 4");
        Order order6 = createOrder(Side.SELL, 5.56, 11.6, "user 4");

        underTest.addOrder(order1);
        underTest.addOrder(order2);
        underTest.addOrder(order3);
        underTest.addOrder(order4);
        underTest.addOrder(order5);
        underTest.addOrder(order6);

        List<PriceVolumePair> bidPrices = underTest.getPriceVolumePairs(Side.BUY);
        List<PriceVolumePair> offerPrices = underTest.getPriceVolumePairs(Side.SELL);


        assertEquals(0, bidPrices.size());
        assertEquals(4, offerPrices.size());

        assertEquals(5.56, offerPrices.get(0).getPrice(), 0.0);
        assertEquals(9.87, offerPrices.get(1).getPrice(), 0.0);
        assertEquals(12.4, offerPrices.get(2).getPrice(), 0.0);
        assertEquals(13.1, offerPrices.get(3).getPrice(), 0.0);
    }

    @Test
    public void addMultipleOrdersSameSide() {
        Order order1 = createOrder(Side.SELL, 12.4, 23.4, "user");
        Order order2 = createOrder(Side.BUY, 13.1, 23.4, "user 2");
        Order order3 = createOrder(Side.BUY, 9.87, 10.21, "user 3");
        Order order4 = createOrder(Side.BUY, 9.87, 11.45, "user 3");
        Order order5 = createOrder(Side.SELL, 5.56, 2.67, "user 4");
        Order order6 = createOrder(Side.SELL, 5.56, 11.6, "user 4");

        underTest.addOrder(order1);
        underTest.addOrder(order2);
        underTest.addOrder(order3);
        underTest.addOrder(order4);
        underTest.addOrder(order5);
        underTest.addOrder(order6);

        List<PriceVolumePair> bidPrices = underTest.getPriceVolumePairs(Side.BUY);
        List<PriceVolumePair> offerPrices = underTest.getPriceVolumePairs(Side.SELL);


        assertEquals(2, bidPrices.size());
        assertEquals(2, offerPrices.size());

        assertEquals(13.1, bidPrices.get(0).getPrice(), 0.0);
        assertEquals(23.4, bidPrices.get(0).getVolume(), 0.0);
        assertEquals(9.87, bidPrices.get(1).getPrice(), 0.0);
        assertEquals(10.21 + 11.45, bidPrices.get(1).getVolume(), 0.0);

        assertEquals(5.56, offerPrices.get(0).getPrice(), 0.0);
        assertEquals(2.67 + 11.6, offerPrices.get(0).getVolume(), 0.0);
        assertEquals(12.4, offerPrices.get(1).getPrice(), 0.0);
        assertEquals(23.4, offerPrices.get(1).getVolume(), 0.0);

    }
    /*
    public boolean removeOrder(String id) {
        if (bidsBook.removeOrder(id)) {
            return true;
        } else {
            return offersBook.removeOrder(id);
        }
    }

    public boolean removeOrder(Side side,String userId, double price, double quantity) {
        if(side.equals(Side.BUY)){
           return bidsBook.removeOrder(userId,price,quantity);
        }else{
            return offersBook.removeOrder(userId,price,quantity);
        }
    }
     */

    @Test
    public void removeOrderWithId() {
        Order order1 = createOrder(Side.SELL, 12.4, 23.4, "user");
        Order order2 = createOrder(Side.BUY, 13.1, 23.4, "user 2");
        Order order3 = createOrder(Side.BUY, 9.87, 10.21, "user 3");
        Order order4 = createOrder(Side.BUY, 9.87, 11.45, "user 3");
        Order order5 = createOrder(Side.SELL, 5.56, 2.67, "user 4");
        Order order6 = createOrder(Side.SELL, 5.56, 11.6, "user 4");

        underTest.addOrder(order1);
        underTest.addOrder(order2);
        underTest.addOrder(order3);
        underTest.addOrder(order4);
        underTest.addOrder(order5);
        underTest.addOrder(order6);

        underTest.removeOrder(order2.getId());

        List<PriceVolumePair> bidPrices = underTest.getPriceVolumePairs(Side.BUY);
        List<PriceVolumePair> offerPrices = underTest.getPriceVolumePairs(Side.SELL);


        assertEquals(1, bidPrices.size());
        assertEquals(2, offerPrices.size());

        assertEquals(9.87, bidPrices.get(0).getPrice(), 0.0);
    }

    @Test
    public void removeOrder() {
        Order order1 = createOrder(Side.SELL, 12.4, 23.4, "user");
        Order order2 = createOrder(Side.BUY, 13.1, 23.4, "user 2");
        Order order3 = createOrder(Side.BUY, 9.87, 10.21, "user 3");
        Order order4 = createOrder(Side.BUY, 9.87, 11.45, "user 3");
        Order order5 = createOrder(Side.SELL, 5.56, 2.67, "user 4");
        Order order6 = createOrder(Side.SELL, 5.56, 11.6, "user 4");

        underTest.addOrder(order1);
        underTest.addOrder(order2);
        underTest.addOrder(order3);
        underTest.addOrder(order4);
        underTest.addOrder(order5);
        underTest.addOrder(order6);

        underTest.removeOrder(order2);

        List<PriceVolumePair> bidPrices = underTest.getPriceVolumePairs(Side.BUY);
        List<PriceVolumePair> offerPrices = underTest.getPriceVolumePairs(Side.SELL);


        assertEquals(1, bidPrices.size());
        assertEquals(2, offerPrices.size());

        assertEquals(9.87, bidPrices.get(0).getPrice(), 0.0);
    }

    @Test
    public void removeOrderByOrderParameters() {
        Order order1 = createOrder(Side.SELL, 12.4, 23.4, "user");
        Order order2 = createOrder(Side.BUY, 13.1, 23.4, "user 2");
        Order order3 = createOrder(Side.BUY, 9.87, 10.21, "user 3");
        Order order4 = createOrder(Side.BUY, 9.87, 11.45, "user 3");
        Order order5 = createOrder(Side.SELL, 5.56, 2.67, "user 4");
        Order order6 = createOrder(Side.SELL, 5.56, 11.6, "user 4");

        underTest.addOrder(order1);
        underTest.addOrder(order2);
        underTest.addOrder(order3);
        underTest.addOrder(order4);
        underTest.addOrder(order5);
        underTest.addOrder(order6);

        underTest.removeOrder(order2.getSide(),order2.getUserId(),order2.getPrice(),order2.getVolume());

        List<PriceVolumePair> bidPrices = underTest.getPriceVolumePairs(Side.BUY);
        List<PriceVolumePair> offerPrices = underTest.getPriceVolumePairs(Side.SELL);


        assertEquals(1, bidPrices.size());
        assertEquals(2, offerPrices.size());

        assertEquals(9.87, bidPrices.get(0).getPrice(), 0.0);
    }
}
