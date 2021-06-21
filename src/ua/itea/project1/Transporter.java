package ua.itea.project1;

import java.util.concurrent.TimeUnit;

public class Transporter implements Runnable {

    private Cart cart;
    private final Exchanger<Cart> exchangeWithLoader;
    private final Exchanger<Cart> exchangeWithUnLoader;

    private boolean work;

    public Transporter(Exchanger<Cart> exchangeWithLoader, Exchanger<Cart> exchangeWithUnLoader) {
        this.exchangeWithLoader = exchangeWithLoader;
        this.exchangeWithUnLoader = exchangeWithUnLoader;
        work = true;
    }

    @Override
    public void run() {
        try {
            while (work) {
                getCartFromLoader();
                checkWork();
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

    private void getCartFromLoader() throws InterruptedException {
        cart = exchangeWithLoader.get();
        System.out.println("Transporter gets cart from loader");
    }

    private void getCartFromUnLoader() throws InterruptedException {
        cart = exchangeWithUnLoader.get();
        System.out.println("Transporter gets cart from unLoader");
    }

    private void transportCart() throws InterruptedException {
        int count = 5;
        System.out.print("Transporter transport cart");
        while (count-- > 0) {
            TimeUnit.SECONDS.sleep(1);
            System.out.print(".");
        }
        System.out.print("\n");
    }

    private void handOverCartToUnLoader() {
        if (!work) {
            exchangeWithUnLoader.setWork(false);
        }
        exchangeWithUnLoader.set(cart);
        cart = null;
        System.out.println("Transporter hand over cart to unLoader");
    }

    private void handOverCartToLoader() {
        exchangeWithLoader.set(cart);
        cart = null;
        System.out.println("Transporter hand over cart to loader");
    }

    private void checkWork() {
        if (!exchangeWithLoader.isWork()) {
            work = false;
            exchangeWithUnLoader.setWork(false);
        }
    }
}
