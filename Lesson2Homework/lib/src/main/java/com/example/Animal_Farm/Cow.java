package com.example.Animal_Farm;

import java.util.Random;

public class Cow extends com.example.Animal_Farm.Animal {

    public Cow(String name, String color) {
        super(4,name, color, "cow", 0);

        Random random = new Random();
        this.weight = (double)random.nextInt(101) + 100;

    }

    public void grow() {
        this.setWeight(this.getWeight()*5);
    }
}

