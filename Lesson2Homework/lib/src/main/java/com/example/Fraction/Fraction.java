package com.example.Fraction;

public class Fraction {

    private int numerator;
    private int denominator;

    public Fraction(int numerator, int denominator) {
        this.numerator = numerator;
        this.denominator = denominator;
    }

    public String toString() {
        return Integer.toString(numerator) + "/" + Integer.toString(denominator);
    }

    public static int gcd(int m, int n) {
        if (m > n) {
            return euclidGCD(m, n);
        } else {
            return euclidGCD(n, m);
        }
    }

    private static int euclidGCD(int a, int b) {
        // a must be greater than b
        if (b == 0) {
            return a;
        } else {
            return euclidGCD(b, a%b);
        }
    }

    public void simplify() {
        int fractionGCD = gcd(this.numerator, this.denominator);
        this.numerator = this.numerator/fractionGCD;
        this.denominator = this.denominator/fractionGCD;
    }

    public Fraction add(Fraction f) {
        int newDenominator = f.denominator * this.denominator;
        int newNumerator = f.numerator*this.denominator + this.numerator*f.denominator;
        return new Fraction(newNumerator, newDenominator);
    }

    public static void main(String[] args) {
        System.out.println(gcd(27, 63));

        Fraction f = new Fraction(17*3, 17*7);
        f.simplify();

        Fraction g = new Fraction(4, 5);

        System.out.println(f.add(g));
    }
}