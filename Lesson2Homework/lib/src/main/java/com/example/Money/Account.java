package com.example.Money;

public abstract class Account {

    protected long amount;

    public Account(long amount) {
        this.amount = amount;
    }

    public String toString() {
        return "Account Balance: $" + amount;
    }

    public long getAmount() { return this.amount; }

    public void setAmount(long amount) { this.amount = amount; }

    public void deposit(long addedAmount) { this.amount = this.amount + addedAmount; }

    public static Account largerAccount(Account acc1, Account acc2) {
        if (acc1.amount >= acc2.amount) {
            return acc1;
        }
        return acc2;
    }

//    remember to delete things you don't care about anymore (unless you want to show us something)

}