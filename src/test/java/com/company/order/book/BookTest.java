package com.company.order.book;

import com.company.model.Order;
import com.company.model.Side;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;


@RunWith(MockitoJUnitRunner.class)
public class BookTest {

    private Book underTest;

    private Comparator<PriceLevel> comparator;

    @Before
    public void setUp() {
        comparator = new Comparator<PriceLevel>() {
            @Override
            public int compare(PriceLevel p1, PriceLevel p2) {
                return Double.compare(p2.getPrice(), p1.getPrice());
            }
        };

        underTest = new Book(Side.BUY, comparator);
    }

    private Order createOrder(double price, double volume, String userId) {
        return new Order(Side.BUY, userId, volume, price);
    }

    @Test
    public void createsEmptyBook() {

        assertEquals(underTest.getSide(), Side.BUY);
        assertEquals(underTest.getPriceVolumePairs().size(), 0);
    }

    @Test
    public void storesOneOrder() {
        Order order = createOrder(10.0, 23.4, "userId");

        underTest.add(order);

        assertEquals(underTest.getPriceVolumePairs().size(), 1);

        List<PriceVolumePair> priceVolumePairs = underTest.getPriceVolumePairs();

        assertEquals(priceVolumePairs.get(0).getPrice(), 10.0);
        assertEquals(priceVolumePairs.get(0).getVolume(), 23.4);
    }

    @Test
    public void storesMoreThanOneOrderForTheSamePriceLevel() {
        Order order1 = createOrder(10.0, 23.4, "user 1");
        Order order2 = createOrder(10.0, 35.7, "user 1");

        underTest.add(order1);
        underTest.add(order2);

        assertEquals(1, underTest.getPriceVolumePairs().size());

        List<PriceVolumePair> priceVolumePairs = underTest.getPriceVolumePairs();


        assertEquals(10.0, priceVolumePairs.get(0).getPrice(), 0.0);
        assertEquals(23.4 + 35.7, priceVolumePairs.get(0).getVolume());

    }


    @Test
    public void storesMoreThanOneOrderForDifferentPriceLevel() {
        Order order1 = createOrder(10.0, 23.4, "user 1");
        Order order2 = createOrder(10.0, 0.7, "user 1");
        Order order3 = createOrder(13.3, 23.45, "user 2");
        Order order4 = createOrder(13.3, 12.35, "user 2");

        underTest.add(order1);
        underTest.add(order2);
        underTest.add(order3);
        underTest.add(order4);

        assertEquals(2, underTest.getPriceVolumePairs().size());

        List<PriceVolumePair> priceVolumePairs = underTest.getPriceVolumePairs();


        assertEquals(10.0, priceVolumePairs.get(1).getPrice(), 0.0);
        assertEquals(23.4 + 0.7, priceVolumePairs.get(1).getVolume());

        assertEquals(13.3, priceVolumePairs.get(0).getPrice(), 0.0);
        assertEquals(23.45 + 12.35, priceVolumePairs.get(0).getVolume());
    }

    @Test
    public void allOrdersAreSortedAccordingToTheComparator() {
        Order order1 = createOrder(10.0, 23.4, "user 1");
        Order order2 = createOrder(13.0, 0.7, "user 1");
        Order order3 = createOrder(11.5, 23.45, "user 2");
        Order order4 = createOrder(6.0, 12.35, "user 2");

        List<PriceVolumePair> expectedResult = new ArrayList<>();

        expectedResult.add(new PriceVolumePair(order1.getPrice(), order1.getVolume()));
        expectedResult.add(new PriceVolumePair(order2.getPrice(), order2.getVolume()));
        expectedResult.add(new PriceVolumePair(order3.getPrice(), order3.getVolume()));
        expectedResult.add(new PriceVolumePair(order4.getPrice(), order4.getVolume()));

        Collections.sort(expectedResult, (p1, p2) -> {
            return Double.compare(p2.getPrice(), p1.getPrice());
        });

        underTest.add(order1);
        underTest.add(order2);
        underTest.add(order3);
        underTest.add(order4);

        List<PriceVolumePair> result = underTest.getPriceVolumePairs();

        for(int i=0;i<expectedResult.size();i++){
            assertEquals(expectedResult.get(i).getPrice(),result.get(i).getPrice());
        }
    }

    @Test
    public void removesOneOrder() {
        Order order1 = createOrder(10.0, 23.4, "user 1");
        Order order2 = createOrder(10.0, 0.7, "user 2");
        Order order3 = createOrder(13.3, 23.45, "user 1");
        Order order4 = createOrder(13.3, 12.35, "user 2");

        underTest.add(order1);
        underTest.add(order2);
        underTest.add(order3);
        underTest.add(order4);


        underTest.removeOrder(order1);

        assertEquals(2, underTest.getPriceVolumePairs().size());

        List<PriceVolumePair> priceVolumePairs = underTest.getPriceVolumePairs();

        assertEquals(10.0, priceVolumePairs.get(1).getPrice());
        assertEquals(0.7, priceVolumePairs.get(1).getVolume());
    }

    @Test
    public void removesOneOrderBasedOnUserIdPriceAndQuantity() {
        Order order1 = createOrder(10.0, 23.4, "user 1");
        Order order2 = createOrder(10.0, 0.7, "user 2");
        Order order3 = createOrder(13.3, 23.45, "user 1");
        Order order4 = createOrder(13.3, 12.35, "user 2");

        underTest.add(order1);
        underTest.add(order2);
        underTest.add(order3);
        underTest.add(order4);


        underTest.removeOrder("user 1", 10.0, 23.4);

        assertEquals(2, underTest.getPriceVolumePairs().size());

        List<PriceVolumePair> priceVolumePairs = underTest.getPriceVolumePairs();

        assertEquals(10.0, priceVolumePairs.get(1).getPrice());
        assertEquals(0.7, priceVolumePairs.get(1).getVolume());
    }

    @Test
    public void removesOneOrderBasedOnOrderId() {
        Order order1 = createOrder(10.0, 23.4, "user 1");
        Order order2 = createOrder(10.0, 0.7, "user 2");
        Order order3 = createOrder(13.3, 23.45, "user 1");
        Order order4 = createOrder(13.3, 12.35, "user 2");

        underTest.add(order1);
        underTest.add(order2);
        underTest.add(order3);
        underTest.add(order4);


        underTest.removeOrder(order1.getId());

        assertEquals(2, underTest.getPriceVolumePairs().size());

        List<PriceVolumePair> priceVolumePairs = underTest.getPriceVolumePairs();

        assertEquals(10.0, priceVolumePairs.get(1).getPrice());
        assertEquals(0.7, priceVolumePairs.get(1).getVolume());
    }

    @Test
    public void removesAllOrdersForOnePriceLevel() {
        Order order1 = createOrder(10.0, 23.4, "user 1");
        Order order2 = createOrder(10.0, 0.7, "user 2");
        Order order3 = createOrder(13.3, 23.45, "user 1");
        Order order4 = createOrder(13.3, 12.35, "user 2");

        underTest.add(order1);
        underTest.add(order2);
        underTest.add(order3);
        underTest.add(order4);


        underTest.removeOrder(order1.getId());
        underTest.removeOrder("user 2", order2.getPrice(), order2.getVolume());

        assertEquals(1, underTest.getPriceVolumePairs().size());

        List<PriceVolumePair> priceVolumePairs = underTest.getPriceVolumePairs();

        assertEquals(13.3, priceVolumePairs.get(0).getPrice(), 0.0);
        assertEquals(23.45 + 12.35, priceVolumePairs.get(0).getVolume(), 0.0);
    }

    @Test
    public void returnsFalseWhenTryingToRemoveAnUnexistantOrder(){
        Order order1 = createOrder(10.0, 23.4, "user 1");
        Order order2 = createOrder(10.0, 0.7, "user 2");


        underTest.add(order1);
        underTest.add(order2);


        assertFalse(underTest.removeOrder("id not in book"));

        assertEquals(1, underTest.getPriceVolumePairs().size());

        List<PriceVolumePair> priceVolumePairs = underTest.getPriceVolumePairs();

        assertEquals(10.0, priceVolumePairs.get(0).getPrice(), 0.0);
        assertEquals(23.4 + 0.7, priceVolumePairs.get(0).getVolume(), 0.0);
    }
}
