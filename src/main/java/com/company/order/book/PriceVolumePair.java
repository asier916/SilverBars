package com.company.order.book;

public class PriceVolumePair {

    private final double price;
    private final double volume;

    public PriceVolumePair(double price,double volume){
        this.price = price;
        this.volume = volume;
    }

    public double getVolume(){
        return volume;
    }

    public double getPrice(){
        return price;
    }
}
