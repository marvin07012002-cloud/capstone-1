# 😎Marbin's Accounting Ledger Application😎

## Description
This is an accounting application that allows users to add deposits, add payments, visualize 
all transactions, see different reports and search transactions by vendor.

## Running the Code
The easiest way to be able to run the code is load it into IntelliJ IDEA and run AccontingApp class.

## Code I'm Most Proud of
I'm particularly proud of the Search by Vendor in the report menu because I was able to write it really fast 
after my instructor explain me really well how should I code, and after practicing many times too.

```java
private static void searchByVendor() {
    ArrayList<Transaction> transactionList = loadTransactions();

    System.out.println("Enter Vendor: ");
    String input = scanner.nextLine();

    for (Transaction t : transactionList) {
        if (input.equalsIgnoreCase(t.getVendor())) {
            System.out.println(t.csvString());
            break;
        }
    }
}
```
The ASCII are interesting and cool also:
[Home_Screen](Home_screen.png);

## My Personal Challenges
I struggle with writing my code because I did not know where to start 
or how to write it; at some point I was kinda relay in Ninjava in a toxic way
that was not benefiting me.

My instructor was great and indicate me how to write my own code that I can understand
and write any time I need to write it.

After my Instructor explain me really well how to do my code, I stared practice and know I'm way 
more comfortable writing code.

## Next Time ....
Through this first capstone I have learned that I have to ask more questions, and communicate more with my instructor
to do a better job in understanding and writing code.

