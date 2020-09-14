package TicketBooking.MultiThreading;

import java.util.Map;
import java.util.HashMap;

import TicketBooking.MultiThreading.Order;

public class TicketServer {
    private static TicketServer server = null;

    private Map<String, Integer> tickets;
    private Map<Integer, Order> orders; // orderId
    private int currOrder;
    private long countDownTime;

    private TicketServer() {
        // retrieve tickets data
        this.tickets = new HashMap<>();
        // ... put your tickets her
        tickets.put("Movie A", 50);
        tickets.put("Movie B", 55);
        tickets.put("Movie C", 20);
        tickets.put("Movie D", 25);
        
        this.orders = new HashMap<>();

        this.currOrder = 1;
        this.countDownTime = 2000; // default countdown is 2000ms
    }

    public void printTicketInfo() {
        for (Map.Entry<String, Integer> entry : this.tickets.entrySet()) {
            System.out.println(entry.getKey() + " - " + entry.getValue() + " tickets left");
        }
    }

    public void printOrderInfo() {
        for (Map.Entry<Integer, Order> entry : this.orders.entrySet()) {
            Order order = entry.getValue();
            order.printOrderInfo();
        }
    }

    // this method should be asynchro
    // you also need to block tickets - generate a thread blocking
    synchronized public int placeOrder(String username, String movieName, int amount) throws Exception{
        
        int stock = tickets.get(movieName);
        if (stock <= 0 || stock < amount) {
            throw new Exception("cannot find movie or not enough ticket left. please check");
        }

        // generate order and update stock
        Order order = new Order(currOrder, username, movieName, amount);
        orders.put(order.getOrderId(), order);
        currOrder ++;
        
        // lock tickets
        tickets.put(movieName, stock - amount);

        // start countdown
        Thread countDownThread = startCountDown(order.getOrderId());
        countDownThread.start();

        return order.getOrderId();
    }

    synchronized public void confirmOrder(int orderId) throws Exception {
        Order order = orders.get(orderId);
        if (order.getIsPaid()) {
            throw new Exception("order " + orderId + " is already paid. please try again");
        }
        if (order.getIsTimedOut()) {
            throw new Exception("order " + orderId + " is expired, please try to place another order");
        }

        order.payTicket();
        orders.put(orderId, order);
    }

    private Thread startCountDown(int orderId) {

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(countDownTime);
                    Order order = orders.get(orderId);
                    if (!order.getIsPaid()) {
                        order.setIsTimedOut(true);
                        orders.put(orderId, order);

                        System.out.println("[error-" + order.getUsername() + "] failed to pay order " + order.getOrderId() + ", releasing ticket" );
                        // release ticket
                        int newAmount = tickets.get(order.getMovieName()) + order.getTicketAmount();
                        tickets.put(order.getMovieName(), newAmount);
                    }
                } catch (Exception e) {
                    System.out.println("unknown error occured at startCountDown() - server");
                }
            }
        };

        return new Thread(runnable);
    }

    public static TicketServer getInstance() {
        if (server == null) {
            server = new TicketServer();
        }
        return server;
    }

}