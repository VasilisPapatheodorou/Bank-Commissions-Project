import java.util.Scanner;
import java.util.ArrayList;
import java.util.HashMap;

public class mainApp {

    private static SalesmanList salesmenlist = new SalesmanList();
    private static ProductList productList = new ProductList();
    private static SalesList saleslist = new SalesList();
    private static TransactionList transactionlist = new TransactionList();
    private static Scanner in;

    private static HashMap<Integer, Long> commissions = new HashMap<Integer, Long>();

    private static Salesman promptFindSalesman() {
        if (salesmenlist.size() > 0) {
            salesmenlist.display();
            System.out.println("Select a salesman by key or type 'b' to go back");
            String answer = in.next();
            
            //Gets user input
            if (!answer.equalsIgnoreCase("b")) {
                try {
                    Salesman salesman = salesmenlist.getElementByKey(Integer.parseInt(answer));
                    if (salesman == null) {
                        System.out.println("No salesman with the specified key was found.");
                    } else {
                        return salesman;
                    }
                // Handles invalid input
                } catch (NumberFormatException exception) {
                    System.out.println("Invalid key format.");
                }
            }
        } else {
            System.out.println("No salesmen exist.");
        }
        return null;
    }

    private static BankProduct promptFindProduct() {
        if (productList.size() > 0) {       
            productList.displayCreditCards();
            System.out.println();
            productList.displayLoans();
            System.out.println("Select a product by key or type 'b' to go back: ");
            String answer = in.next();
            // An h apanthsh einai 'b', tote den mpainei sto if kai apla
            // vgainei apo thn synarthsh kai epistrefei sto main loop
            if (!answer.equalsIgnoreCase("b")) {
                try {
                    BankProduct product = productList.getElementByKey(Integer.parseInt(answer));
                    if (product != null) {
                        return product;
                    } else {
                        System.out.println("No product with the specified key was found.");
                    }
                // Se periptwsh pou den dw8ei kapoio 'valid' kleidi, to parseInt
                // 8a kanei throw NumberFormatException, ara prepei na to xeiristoume
                } catch (NumberFormatException exception) {
                    System.out.println("Invalid key format.");
                }
            }

        } else {
            System.out.println("There aren't any products available.");
        }
        return null;
    }

    private static CreditCard promptFindCreditCard() {
        if (productList.size() > 0)
        {
            productList.displayCreditCards();
            System.out.println("Enter the credit card key or type 'b' to go back.");
            String answer = in.next();
            // An h apanthsh einai 'b', tote den mpainei sto if kai apla
            // vgainei apo thn synarthsh kai epistrefei sto main loop
            if (!answer.equalsIgnoreCase("b")) {
                try {
                    BankProduct card = productList.getElementByKey(Integer.parseInt(answer));
                    // Epeidh psaxnoume thn karta sto productList, endexetai
                    // na mas dwsei o xrhsths ena kleidi pou antistoixei se kapoio Loan
                    // opote elegxoume pws einai karta
                    if (card == null || !(card instanceof CreditCard)) {
                        System.out.println("Credit card not found.");
                    } else {
                        return (CreditCard)card;
                    }
                // Se periptwsh pou den dw8ei kapoio 'valid' kleidi, to parseInt
                // 8a kanei throw NumberFormatException, ara prepei na to xeiristoume
                } catch (NumberFormatException e) {
                    System.out.println("Invalid key format.");
                }
            }
        } else {
            System.out.println("No credit cards are available");
        }
        return null;
    }

    private static void printMenu() {
        System.out.println("\n1. Insert Salesman");
        System.out.println("2. Insert new Bank Product");
        System.out.println("3. Insert new Sale");
        System.out.println("4. Insert new Transaction");
        System.out.println("5. Print Loans");
        System.out.println("6. Calculate Salesman Commission");
        System.out.println("7. Print Salesman Transactions");
        System.out.println("8. Calculate Commission of all Salesmen");
        System.out.println("9. Print Commission for each Salesman");
        System.out.println("10. Save to file");
        System.out.println("0. Exit");
    }

    private static void onInsertSalesman() {
        System.out.print("Provide Salesman Information (Name, Last Name, Code, SSN) or type 'b' to go back\n");

        final String answer = in.next();

        if (!answer.equalsIgnoreCase("b")) {
            salesmenlist.append(
                new Salesman(
                    answer,
                    in.next(),
                    in.next(), 
                    in.next()
                )
            );
        }
    }

    private static void onInsertBankProduct() {
        System.out.println("Enter the bank product type (1. Card - 2. Loan) or 'b' to go back: ");
        String answer= in.next();

        if (answer.equals("1")) {
            System.out.println("Enter the card code, its description, the client's number, the client's SSN, the commision rate, the annual spending limit (cents) and the one-time spending limit (cents)");
            productList.append(
                new CreditCard(
                    in.next(),
                    in.next(),
                    in.next(),
                    in.next(),
                    in.nextDouble(),
                    in.nextLong(),
                    in.nextLong()
                )
            );

        } else if (answer.equalsIgnoreCase("2")) {
            System.out.println("Enter the loan's code, its description, the client's number, the client's SSN, the amount (in cents) and the annual interest");
            productList.append(
                new Loan(
                    in.next(),
                    in.next(),
                    in.next(),
                    in.next(),
                    in.nextInt(),
                    in.nextDouble()
                )
            );

        // An den einai oute 'b' h apanthsh, tote kseroume oti den exei
        // dw8ei swsth epilogh. An einai 'b' tote apla proxwrame
        } else if (!answer.equalsIgnoreCase("b")) {
            System.out.println("Invalid choice");
        }
    }

    private static void onInsertSale() {
        Salesman salesman = promptFindSalesman();
        if (salesman != null) {
            BankProduct product = promptFindProduct();
            if (product != null) {
                System.out.print("Provide the reason behind the sale: ");
                // Yparxei ena \n sto stream opote kaloume thn in.nextLine() gia na to vgalei
                // wste na eisagoume ton logo ths pwlhshs
                in.nextLine();
                saleslist.append(
                  new Sale(salesman, product, in.nextLine())
                );
            }
        }
    }

    private static void onInsertTransaction() {
        CreditCard card = promptFindCreditCard();
        if (card != null) {
            System.out.println("Please enter the transaction amount (in cents) and the reason: ");
            long amountCents = in.nextLong();
            // An yparxoun arketa xrhmata h eimaste katw apo to spending limit
            // tha epistrepsei true h card.spend
            if (card.spend(amountCents))
            {
                transactionlist.append(
                    new Transaction(
                        card,
                        amountCents,
                        in.nextLine()
                    )
                );
                System.out.println("The transaction was successful");
            } else {
                System.out.println("The transaction was denied.");
            }
        }
    }

    private static void onPrintSalesmanTransactions() {
        // Pairnoume ton salesman, pairnoume tis kartes pou exei poulhsei,
        // kai gia kathe karta pairnoume thn kathe synallagh kai thn ektypwnoyme
        Salesman salesman = promptFindSalesman();
        if (salesman != null) {
            ArrayList<CreditCard> cards = saleslist.getSalesmanCards(salesman);
            if (cards.isEmpty()) {
                System.out.println("The chosen salesman hasn't sold any credit cards.");
            } else {
                boolean hasTransactions = false;
                for (CreditCard card : cards) {
                    ArrayList<Transaction> transactions = transactionlist.getCardTransactions(card);
                    for (Transaction t : transactions) {
                        hasTransactions = true;
                        System.out.println(t);
                    }
                    if (!hasTransactions) {
                        System.out.println("No transactions were found.");
                    }
                }
            }
        }
    }

    private static long calculateSalesmanCommission(Salesman salesman) {
        long commissionCents = 0;
        // Katarxas ypologizoume to commission apo ta loans
        for (Loan loan : saleslist.getSalesmanLoan(salesman)) {
             final long amountCents = loan.getAmountCents();
             final double interest = loan.getAnnualInterest();

             double chargePercent = 0.25;

             // To pososto ypologizetai vasei tou pinaka 
             // ths ekfwnhshs. Den elegxoume an einai anw ton 2 ek evrw
             // giati tote tha thetame chargePercent = 0.25, pragma
             // pou exoume kanei hdh
             if (amountCents <= 500000 * 100) {
                chargePercent = 0.1;
             } else if (amountCents <= 2000000 * 100) {
                 chargePercent = 0.2;
             }

             // Epishs den prepei to pososto na ksepernaei ton toko,
             // opote an ginei ayto tote pairnoume oso einai o tokos
             if (chargePercent > interest) {
                 chargePercent = interest;
             }

             commissionCents += (long)(amountCents * chargePercent);
        }

        // Kai epeita ta commissions apo tis kartes
        for (CreditCard card : saleslist.getSalesmanCards(salesman)) {
            commissionCents += card.getCommissionChargeCents();
        }

        return commissionCents;
    }

    private static void calculateAllCommissions() {
        // Ypologizoume ta commissions olwn ton salesman kai ta kratame gia argotera
        if (salesmenlist.size() > 0) {
            for (Salesman salesman : salesmenlist.get()) {
                commissions.put(salesman.getKey(), calculateSalesmanCommission(salesman));
            }
            System.out.println("Commissions successfully calculated");
        } else {
            System.out.println("There aren't any salesmen available.");
        }
    }

    private static void showTotalCommissions() {
        // Se periptwsh pou den exei xrhsimopoih8ei to 8 prin to 9,
        // ta ypologizoume ksana giati alliws mporei to commissions.get na
        // kanei throw
        if (commissions.isEmpty()) {
            calculateAllCommissions();
        }
        long sum = 0;
        for (Salesman salesman : salesmenlist.get()) {
            long commission = commissions.get(salesman.getKey());
            sum += commission;
            System.out.println(salesman.toString() + "\t\t" + commission + " cents.");
        }
        System.out.println("\nTotal commissions: " + sum + " cents.");
    }

    public static void main(String[] args) {
        in = new Scanner(System.in);
        boolean done = false;

        BankProductIO bpio = new BankProductIO();
        SalesmanIO salesmanIO = new SalesmanIO();
        SalesIO salesIO = new SalesIO();
        TransactionIO trnIO = new TransactionIO();
        try {
            System.out.println("Reading from BANKITEM_LIST.txt...");
            productList = bpio.getListFromFile("BANKITEM_LIST.txt");
            System.out.println("Reading from SALESMAN_LIST.txt...");
            salesmenlist = salesmanIO.getListFromFile("SALESMAN_LIST.txt");
            System.out.println("Reading from SALES_LIST.txt...");
            saleslist = salesIO.getListFromFile("SALES_LIST.txt", salesmenlist, productList);
            System.out.println("Reading from TRANSACTION_LIST.txt...");
            transactionlist = trnIO.getListFromFile("TRANSACTION_LIST.txt", productList);
        } catch (Exception e) {
            System.out.println("" + e);
            return;
        }
        
        while (!done) {
            printMenu();

            System.out.print("\n> ");
            String answer = in.next();

            switch (answer) {
                case "1":
                    onInsertSalesman();
                    break;
                
                case "2":
                    onInsertBankProduct();
                    break;

                case "3":
                    onInsertSale();
                    break;

                case "4":
                    onInsertTransaction();
                    break;

                case "5":
                    productList.displayLoans();
                    break;

                case "6":
                    Salesman salesman = promptFindSalesman();
                    if (salesman != null) {
                        System.out.println("Salesman commission: " + calculateSalesmanCommission(salesman) + " cents.");
                    } 
                    break;

                case "7":
                    onPrintSalesmanTransactions();
                    break;

                case "8":
                    calculateAllCommissions();
                    break;

                case "9":
                    showTotalCommissions();
                    break;

                case "10":
                    System.out.println("Saving changes to BANKITEM_LIST.txt...");
                    bpio.saveListToFile("BANKITEM_LIST.txt", productList);
                    System.out.println("Saving changes to SALESMAN_LIST.txt...");
                    salesmanIO.saveListToFile("SALESMAN_LIST.txt", salesmenlist);
                    System.out.println("Saving changes to SALES_LIST.txt...");
                    salesIO.saveListToFile("SALES_LIST.txt", saleslist);
                    System.out.println("Saving changes to TRANSACTION_LIST.txt...");
                    trnIO.saveListToFile("TRANSACTION_LIST.txt", transactionlist);
                    break;

                case "0":
                    done = true;
                    break;
            }

        }

        System.out.println("Saving changes to BANKITEM_LIST.txt...");
        bpio.saveListToFile("BANKITEM_LIST.txt", productList);
        System.out.println("Saving changes to SALESMAN_LIST.txt...");
        salesmanIO.saveListToFile("SALESMAN_LIST.txt", salesmenlist);
        System.out.println("Saving changes to SALES_LIST.txt...");
        salesIO.saveListToFile("SALES_LIST.txt", saleslist);
        System.out.println("Saving changes to TRANSACTION_LIST.txt...");
        trnIO.saveListToFile("TRANSACTION_LIST.txt", transactionlist);

        in.close();
    }
}
