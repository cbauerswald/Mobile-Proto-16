package com.example.Money;

public class MoneyTester {

    public static void main(String[] args) {
        com.example.Money.Account a = new com.example.Money.CheckingAccount(100);
        System.out.println(a);
        a.setAmount(20);
        System.out.println("New amount: " + a.getAmount());

        a = new com.example.Money.CheckingAccount(100);
        System.out.println(a);
        a.setAmount(20);
        a.deposit(10);
        System.out.println("New amount: " + a.getAmount());

        com.example.Money.Account small = new com.example.Money.CheckingAccount(20);
        com.example.Money.Account big = new com.example.Money.CheckingAccount(30);
        System.out.println(com.example.Money.Account.largerAccount(small, big));

        MoneySaver david = new MoneySaver("David", 100);
        david.signUpForChecking(50);  // put 50 dollars in a checking account
        System.out.println(david);
        System.out.println(david.getMyAccount());

        MoneySaver jim = new MoneySaver("Jim", 100);
        jim.signUpForChecking(30);
        com.example.Money.CheckingAccount acc = (com.example.Money.CheckingAccount) jim.getMyAccount();
        System.out.println(jim);
        System.out.println(acc);
        jim.deposit(40);
        System.out.println(jim);
        System.out.println(acc);
        jim.withdraw(60);
        System.out.println(jim);
        System.out.println(acc);
    }

}
