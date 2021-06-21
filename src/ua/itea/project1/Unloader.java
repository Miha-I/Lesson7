package ua.itea.project1;

import java.util.concurrent.TimeUnit;

public class Unloader implements Runnable {

    public final static int MAX_UNLOAD = 2;

    private Cart cart;
    private final Heap heap;
    private final Exchanger<Cart> exchangeWithTransporter;

    public Unloader(Heap heap, Exchanger<Cart> exchangeWithTransporter) {
        this.heap = heap;
        this.exchangeWithTransporter = exchangeWithTransporter;
    }

    @Override
    public void run() {
        try {
            while (true) {
                getCart();
                unloadCart();
                if (!exchangeWithTransporter.isWork()) {
                    break;
                }
                handOverCart();
            }
            System.out.println("UnLoader finished work");
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }

    private void getCart() throws InterruptedException {
        cart = exchangeWithTransporter.get();
        System.out.println("UnLoader gets cart");
    }

    private void unloadCart() throws InterruptedException {
        int money;
        while (true) {
            TimeUnit.SECONDS.sleep(1);
            money = Math.min(cart.getLoaded(), MAX_UNLOAD);
            cart.unload(money);
            heap.put(money);
            System.out.println("UnLoader unloaded " + money + " money from cart");
            if (cart.getLoaded() == 0) {
                return;
            }
        }
    }

    private void handOverCart() {
        exchangeWithTransporter.set(cart);
        cart = null;
        System.out.println("UnLoader hand over cart");
    }
}
