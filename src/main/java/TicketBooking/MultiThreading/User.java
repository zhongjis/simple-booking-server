package TicketBooking.MultiThreading;

import TicketBooking.MultiThreading.TicketServer;

public class User {

    TicketServer server;
    String username;

    public User(TicketServer server, String username) {
        this.server = server;
        this.username = username;
    }

    public int placeOrder(String movieName, int amount) throws Exception{
        int orderId = server.placeOrder(username, movieName, amount);
        return orderId;
    }

    public boolean confirmOrder(int orderId) {
        try {
            confirmOrder(orderId);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false; 
        }
    }

}
