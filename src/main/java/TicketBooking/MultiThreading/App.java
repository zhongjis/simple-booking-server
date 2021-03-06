/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package TicketBooking.MultiThreading;

import TicketBooking.MultiThreading.TicketServer;

public class App {
    public String getGreeting() {
        return "Hello world.";
    }

    public static void main(String[] args) {
        TicketServer server = TicketServer.getInstance();
        server.printTicketInfo();

        Thread thread1 = createUserAndBuy(server, "user1", "Movie A", 20, 0, 5000);
        Thread thread2 = createUserAndBuy(server, "user2", "Movie A", 20, 0, 1000);
        Thread thread3 = createUserAndBuy(server, "user3", "Movie A", 20, 4000, 1000);

        thread1.start();
        thread2.start();
        thread3.start();

        server.printOrderInfo();

    }

    private static Thread createUserAndBuy(TicketServer server, String username, String movieName, int amount, 
                                            long bookGap, long confirmGap) {
        User user = new User(server, username);

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    if (bookGap != 0) {
                        Thread.sleep(bookGap);
                    }
                    System.out.println("[action-user1] booking Movie A - 20");
                    int orderId = user.placeOrder(movieName, amount);
                    System.out.println("[success-" + username + "] order placed id - " + orderId);
                    
                    // wait confirmGap
                    Thread.sleep(confirmGap);
                    // place order
                    server.confirmOrder(orderId);
                    System.out.println("[success-" + username + "] order - " + orderId + " is paid");
                } catch (Exception e) {
                    System.out.println("[error-" + username + "] " + e.getMessage());
                    // wait 10 sec
                    // place again`
                    // this.run();
                }
            }
        };

        return new Thread(runnable);
    }
}

