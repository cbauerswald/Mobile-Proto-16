package com.example.Animal_Farm;

/**
 * Created by Cecelia on 9/9/16.
 */
public abstract class Animal {

    protected int legs;
    protected String name;
    protected String color;
    protected String species;
    protected double weight;

    public Animal(int legs, String name, String color, String species, double weight) {
        this.legs = legs;
        this.name = name;
        this.color = color;
        this.species = species;
        this.weight = weight;
    }

    public String toString() {
        return "[" + this.species + ": " + this.name + ", weight: " + this.weight + " color: " + this.color + "]";
    }

    public int getLegs() {
        return legs;
    }

    public void setLegs(int legs) {
        this.legs = legs;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public abstract void grow();
}
