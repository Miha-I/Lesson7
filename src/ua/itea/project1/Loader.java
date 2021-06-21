package ua.itea.project1;

import java.util.concurrent.TimeUnit;

public class Loader implements Runnable {

    public final static int MAX_LOAD = 3;

    private Cart cart;
    private final Bank bank;
    private final Exchanger<Cart> exchangeWithTransporter;

    private boolean work;

    public Loader(Bank bank, Cart cart, Exchanger<Cart> exchangeWithTransporter) {
        this.bank = bank;
        this.cart = cart;
        this.exchangeWithTransporter = exchangeWithTransporter;
        work = true;
    }

    @Override
    public void run() {
        try {
            while (work) {
                loadCart();
                checkWork();
                handOverCart();
                if (!work) {
                    break;
                }
                getCart();
            }
            System.out.println("Loader finished work");
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }

    private void loadCart() throws InterruptedException {
        int money;
        int loadCount;
        do {
            TimeUnit.SECONDS.sleep(1);
            loadCount = Math.min(cart.getFreeSpace(), MAX_LOAD);
            if (bank.getRepository() >= loadCount) {
                money = bank.getMoney(loadCount);
            } else {
                money = bank.getMoney(bank.getRepository());
            }
            cart.download(money);
            System.out.println("Loader load cart " + money + " money");
        } while (cart.getFreeSpace() != 0 && bank.getRepository() != 0);
    }

    private void handOverCart() {
        if (!work) {
            exchangeWithTransporter.setWork(false);
        }
        exchangeWithTransporter.set(cart);
        cart = null;
        System.out.println("Loader hand over cart");
    }

    private void getCart() throws InterruptedException {
        cart = exchangeWithTransporter.get();
        System.out.println("Loader gets cart");
    }

    private void checkWork() {
        if (bank.getRepository() == 0) {
            work = false;
            exchangeWithTransporter.setWork(false);
        }
    }
}
