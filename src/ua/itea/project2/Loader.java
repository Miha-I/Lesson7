package ua.itea.project2;

import java.util.concurrent.Exchanger;
import java.util.concurrent.TimeUnit;

public class Loader implements Runnable, WorkListener {

    public final int maxLoad;
    private final Bank bank;
    private final Exchanger<Cart> exchangeWithTransporter;

    private Cart cart;

    private boolean work;

    public Loader(int maxLoad, Bank bank, Cart cart, Exchanger<Cart> exchangeWithTransporter) {
        this.maxLoad = maxLoad;
        this.cart = cart;
        this.bank = bank;
        this.exchangeWithTransporter = exchangeWithTransporter;
        work = true;
    }

    @Override
    public void run() {
            try {
                while (work) {
                    loadCart();
                    handOverCart();
                    if(!work){
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
            loadCount = Math.min(cart.getFreeSpace(), maxLoad);
            if (bank.getRepository() >= loadCount) {
                money = bank.getMoney(loadCount);
            } else {
                money = bank.getMoney(bank.getRepository());
            }
            cart.download(money);
            System.out.println("Loader load cart " + money + " money");
        } while (cart.getFreeSpace() != 0 && bank.getRepository() != 0);
    }

    private void handOverCart() throws InterruptedException {
            cart = exchangeWithTransporter.exchange(cart);
            System.out.println("Loader hand over cart");
    }

    private synchronized void getCart() throws InterruptedException {
        cart = exchangeWithTransporter.exchange(null);
        System.out.println("Loader gets cart");
    }

    @Override
    public void workIsOver() {
        work = false;
    }
}
