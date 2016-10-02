package com.example.Money;

/**
 * Created by Cecelia on 9/9/16.
 */
public class CheckingAccount extends com.example.Money.Account {

    public CheckingAccount(long amount) {
        super(amount);
    }

    public String toString() {
        return "Checking Account Balance: $" + this.getAmount();
    }

    public void withdraw(int amount) {
        if (amount <= this.getAmount()) {
            this.setAmount(this.getAmount() - amount);
        }
    }

    public static void main(String[] args) {
        CheckingAccount a = new CheckingAccount(10);
        System.out.println(a);
    }
}
