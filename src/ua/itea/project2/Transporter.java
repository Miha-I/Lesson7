package ua.itea.project2;

import java.util.concurrent.Exchanger;
import java.util.concurrent.TimeUnit;

public class Transporter implements Runnable, WorkListener {

    private final Exchanger<Cart> exchangeWithLoader;
    private final Exchanger<Cart> exchangeWithUnLoader;
    private final int timeTransport;
    private Cart cart;
    private boolean work;

    public Transporter(int timeTransport, Exchanger<Cart> exchangeWithLoader, Exchanger<Cart> exchangeWithUnLoader) {
        this.timeTransport = timeTransport;
        this.exchangeWithLoader = exchangeWithLoader;
        this.exchangeWithUnLoader = exchangeWithUnLoader;
        work = true;
    }

    @Override
    public void run() {
        try {
            while (work) {
                getCartFromLoader();
                transportCart();
                handOverCartToUnLoader();
                if (!work) {
                    break;
                }
                getCartFromUnLoader();
                transportCart();
                handOverCartToLoader();
            }
            System.out.println("Transporter finished work");
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }

    private synchronized void getCartFromLoader() throws InterruptedException {
        cart = exchangeWithLoader.exchange(null);
        System.out.println("Transporter gets cart form loader");
    }

    private synchronized void getCartFromUnLoader() throws InterruptedException {
        cart = exchangeWithUnLoader.exchange(null);
        System.out.println("Transporter gets cart from unLoader");
    }

    private void transportCart() throws InterruptedException {
        int count = timeTransport;
        System.out.print("Transporter transport cart");
        while (count-- > 0) {
            TimeUnit.SECONDS.sleep(1);
            System.out.print(".");
        }
        System.out.print("\n");
    }

    private void handOverCartToUnLoader() throws InterruptedException {
            cart = exchangeWithUnLoader.exchange(cart);
            System.out.println("Transporter hand over cart to unLoader");
    }

    private void handOverCartToLoader() throws InterruptedException {
            cart = exchangeWithLoader.exchange(cart);
            System.out.println("Transporter hand over cart to loader");

    }

    @Override
    public void workIsOver() {
        work = false;
    }
}
