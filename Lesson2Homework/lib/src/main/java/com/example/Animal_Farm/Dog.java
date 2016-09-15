package com.example.Animal_Farm;

import java.util.Random;

public class Dog extends com.example.Animal_Farm.Animal {

    public Dog(String name, String color) {
        super(4,name, color, "dog", 0);

        Random random = new Random();
        this.weight = (double)random.nextInt(26);

    }

    public void grow() {
        this.setWeight(this.getWeight()*1.5);
    }
}
