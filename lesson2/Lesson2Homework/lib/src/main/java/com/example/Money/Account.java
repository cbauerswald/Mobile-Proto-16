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

    public static void main(String[] args) {
//        Account a = new Account(100);
//        System.out.println(a);
//        a.setAmount(20);
//        a.deposit(10);
//        System.out.println("New amount: " + a.getAmount());
//        Account small = new Account(20);
//        Account big = new Account(30);
//        System.out.println(Account.largerAccount(small, big));
    }

}