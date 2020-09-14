package TicketBooking.MultiThreading;

public class Order {
    int orderId;
    String username;
    String movieName;
    int amount;
    boolean isPaid;
    boolean isTimedOut;

    public Order(int orderId, String username, String movieName, int amount) {
        this.orderId = orderId;
        this.username = username;
        this.movieName = movieName;
        this.amount = amount;
        this.isPaid = false;
        this.isTimedOut = false;
    }

    public int getOrderId() {
        return this.orderId;
    }

    public boolean getIsPaid() {
        return this.isPaid;
    }
    
    public void payTicket() {
        this.isPaid = true;
    }

    public boolean getIsTimedOut() {
        return this.isTimedOut;
    }

    public void setIsTimedOut(boolean value) {
        this.isTimedOut = value;
    }

    public String getMovieName() {
        return this.movieName;
    }

    public int getTicketAmount() {
        return this.amount;
    }

    public void printOrderInfo() {
        System.out.println(orderId + " - " + this.username + " " + this.movieName 
        + " ammount: " + this.amount + " isPaid: " + this.isPaid + "isExpired: " + this.isTimedOut);
    }

    public String getUsername() {
        return this.username;
    }
}