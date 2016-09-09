package com.example.Animal_Farm;

import java.util.Random;

public class Cat extends com.example.Animal_Farm.Animal {

    public Cat(String name, String color) {
        super(4,name, color, "cat", 0);

        Random random = new Random();
        this.weight = (double)random.nextInt(26);

    }

    public void grow() {
        this.setWeight(this.getWeight()*3);
    }
}
