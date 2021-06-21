package ua.itea.project2;

import java.util.concurrent.Exchanger;

public class Application {

    private static final int cartCapacity = 6;
    private static final int bankRepository = 100;
    private static final int loaderMaxLoad = 3;
    private static final int unLoaderMaxUnLoad = 2;
    private static final int transporterTimeTransport = 5;

    public static void main(String[] args) {
        Heap heap = new Heap();
        Cart cart = new Cart(cartCapacity);
        Bank bank = new Bank(bankRepository);
        Exchanger<Cart> exchangerLoaderTransporter = new Exchanger<>();
        Exchanger<Cart> exchangerUnLoaderTransporter = new Exchanger<>();
        Loader loader = new Loader(loaderMaxLoad, bank, cart, exchangerLoaderTransporter);
        Transporter transporter = new Transporter(transporterTimeTransport, exchangerLoaderTransporter, exchangerUnLoaderTransporter);
        Unloader unloader = new Unloader(unLoaderMaxUnLoad, heap, exchangerUnLoaderTransporter);
        bank.addWorkListener(loader);
        bank.addWorkListener(transporter);
        bank.addWorkListener(unloader);
        new Thread(loader).start();
        new Thread(transporter).start();
        new Thread(unloader).start();
    }
}
