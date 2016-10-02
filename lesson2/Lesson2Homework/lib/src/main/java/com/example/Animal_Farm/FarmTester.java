package com.example.Animal_Farm;

import java.util.ArrayList;

public class FarmTester {

    public void ceceliaTest() {
        com.example.Animal_Farm.Animal c1 = new Cat("chester", "brown");
        com.example.Animal_Farm.Animal c2 = new Cat("leonard", "tabby");
        com.example.Animal_Farm.Animal d1 = new com.example.Animal_Farm.Dog("freida", "tan");
        com.example.Animal_Farm.Animal cw1 = new com.example.Animal_Farm.Cow("tammy", "black");

        ArrayList<com.example.Animal_Farm.Animal> animals = new ArrayList<com.example.Animal_Farm.Animal>();
        animals.add(c1);
        animals.add(c2);
        animals.add(d1);
        animals.add(cw1);

        com.example.Animal_Farm.Farm farm = new com.example.Animal_Farm.Farm();
        farm.addAnimal(c1);
        farm.addAnimal(c2);
        farm.addAnimal(d1);
        farm.addAnimal(cw1);

        System.out.println("Are the animals in descending order of weight?");
        System.out.println(farm.getHeaviestAnimals());
        System.out.println("\nThe farm animals arraylist was not modified: ");
        System.out.println(farm.animals == animals);

        System.out.println("\nExpected Output: \nchester \nleonard");
        System.out.println("Actual Output:");
        farm.printCatNames();
        System.out.println("\nExpected Output: 4");
        System.out.println("Actual Output: " + farm.averageLegs());
    }

    public void teacherTest() {
        Cat c = new Cat("Meowth", "black");
        com.example.Animal_Farm.Dog d = new com.example.Animal_Farm.Dog("Puppy", "brown");
        com.example.Animal_Farm.Cow cow = new com.example.Animal_Farm.Cow("Mooer", "white");
        System.out.println("Test 1 Passed: " + (c.getWeight() >= 0 && c.getWeight() <= 25));
        System.out.println("Test 2 Passed: " + (d.getWeight() >= 0 && d.getWeight() <= 25));
        System.out.println("Test 3 Passed: " + (cow.getWeight() >= 100 && cow.getWeight() <= 200));

        double old_weight_cat = c.getWeight();
        double old_weight_dog = d.getWeight();
        double old_weight_cow = cow.getWeight();
        c.grow();
        d.grow();
        cow.grow();
        System.out.println("Test 4 Passed: " + (c.getWeight() / old_weight_cat == 3));
        System.out.println("Test 5 Passed: " +
                (Math.abs(d.getWeight() / old_weight_dog - 1.5) < 0.01));
        System.out.println("Test 6 Passed: " + (cow.getWeight() / old_weight_cow == 5));

        com.example.Animal_Farm.Farm farm = new com.example.Animal_Farm.Farm();
        farm.addAnimal(c);
        farm.addAnimal(d);
        farm.addAnimal(cow);

        ArrayList<com.example.Animal_Farm.Animal> sorted = farm.getHeaviestAnimals();
        for(int i = 0; i < sorted.size() - 1; i++) {
            System.out.println("Test " + (i + 7) + " Passed: " + (sorted.get(i).getWeight() > sorted.get(i + 1).getWeight()));
        }
        System.out.println("Test 9 Passed: " + (farm.getAnimal(0) == c));
        System.out.println("Test 10 Passed: " + (farm.getAnimal(1) == d));
        System.out.println("Test 11 Passed: " + (farm.getAnimal(2) == cow));
        c.setLegs(7);  // lol 7 legged cat
        System.out.println("Test 12 Passed: " + (farm.averageLegs() == 5));

        System.out.println("Printing 'Meowth'...");
        farm.printCatNames();
    }

    public static void main(String[] args) {
        FarmTester ft = new FarmTester();
        ft.teacherTest();
    }
}


