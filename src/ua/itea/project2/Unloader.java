package ua.itea.project2;

import java.util.concurrent.Exchanger;
import java.util.concurrent.TimeUnit;

public class Unloader implements Runnable, WorkListener {

    public final int maxUnload;
    private final Exchanger<Cart> exchangeWithTransporter;
    private Cart cart;
    private final Heap heap;

    private boolean work;

    public Unloader(int maxUnLoad, Heap heap, Exchanger<Cart> exchangeWithTransporter) {
        this.maxUnload = maxUnLoad;
        this.exchangeWithTransporter = exchangeWithTransporter;
        this.heap = heap;
        work = true;
    }

    @Override
    public void run() {
        try {
            while (work) {
                getCart();
                unloadCart();
                if (!work) {
                    break;
                }
                handOverCart();
            }
            System.out.println("UnLoader finished work");
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }

    private synchronized void getCart() throws InterruptedException {
        cart = exchangeWithTransporter.exchange(null);
        System.out.println("UnLoader gets cart");
    }

    private void unloadCart() throws InterruptedException {
        int money;
        while (true) {
            TimeUnit.SECONDS.sleep(1);
            money = Math.min(cart.getLoaded(), maxUnload);
            cart.unload(money);
            heap.put(money);
            System.out.println("UnLoader unloaded " + money + " money from cart");
            if (cart.getLoaded() == 0) {
                return;
            }
        }
    }

    private void handOverCart() throws InterruptedException {
        cart = exchangeWithTransporter.exchange(cart);
        System.out.println("UnLoader hand over cart");
    }

    @Override
    public void workIsOver() {
        work = false;
    }
}
