package com.company.order.book;

import com.company.model.Order;
import com.company.model.Side;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;


@RunWith(MockitoJUnitRunner.class)
public class PriceLevelTest {


    private Order createOrder(double price, double volume, String user) {
        return new Order(Side.BUY, user, volume, price);
    }

    @Test
    public void createsPriceLevel() {
        PriceLevel underTest = new PriceLevel(13.4);

        assertEquals(underTest.getPrice(), 13.4, 0.0);
    }

    @Test
    public void addOneOrderToPriceLevel() {
        PriceLevel underTest = new PriceLevel(13.4);

        Order order1 = createOrder(13.4, 23.5, "user 1");
        Order order2 = createOrder(13.4, 26.7, "user 2");

        underTest.addOrder(order1);
        underTest.addOrder(order2);

        assertEquals(underTest.getPrice(), 13.4, 0.0);
        assertEquals(underTest.getTotalVolume(), 23.5 + 26.7, 0.0);
    }

    @Test
    public void returnsFalseWhenTryingToRemoveAnUnexistantOrder(){
        PriceLevel underTest = new PriceLevel(13.4);

        Order order1 = createOrder(13.4, 23.5, "user 1");
        Order order2 = createOrder(13.4, 26.7, "user 2");

        underTest.addOrder(order1);
        underTest.addOrder(order2);

        boolean result = underTest.removeOrder("order id not in book");

        assertFalse(result);

        assertEquals(underTest.getPrice(), 13.4, 0.0);
        assertEquals(underTest.getTotalVolume(), 23.5 + 26.7, 0.0);


    }
}
