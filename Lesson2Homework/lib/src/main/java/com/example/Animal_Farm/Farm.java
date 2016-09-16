package com.example.Animal_Farm;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Collections;

public class Farm {

//    remember to make variables private! That's why we create getters and setters
//    also, you don't have to reference your class as com.example.Animal_farm.Animal. Just Animal
//    will do, since you're importing the package above.
    private ArrayList<com.example.Animal_Farm.Animal> animals;

    public Farm() {
//        not necessary to say type twice since you say it above
        animals = new ArrayList<>();
    }

    public void addAnimal(com.example.Animal_Farm.Animal a) {
        animals.add(a);
    }

    public com.example.Animal_Farm.Animal getAnimal(int index) {
        return animals.get(index);
    }

    public ArrayList<com.example.Animal_Farm.Animal> getHeaviestAnimals() {
        ArrayList<com.example.Animal_Farm.Animal> sortedAnimals = new ArrayList<com.example.Animal_Farm.Animal>();
        for (com.example.Animal_Farm.Animal a: this.animals) {
            sortedAnimals.add(a);
        }

        Collections.sort(sortedAnimals, new Comparator<com.example.Animal_Farm.Animal>() {
            @Override public int compare(com.example.Animal_Farm.Animal a1, com.example.Animal_Farm.Animal a2) {
                return (int)(a2.getWeight() - a1.getWeight());
            }
        });

        return sortedAnimals;
    }

    public void printCatNames() {
        for (com.example.Animal_Farm.Animal a: this.animals) {
            if (a instanceof Cat) {
                System.out.println(a.getName());
            }
        }
    }

    public int averageLegs() {
        int totalLegs = 0;
        for (com.example.Animal_Farm.Animal a: this.animals) {
            totalLegs = totalLegs + a.getLegs();
        }
        return (int)(totalLegs/animals.size());
    }
}
