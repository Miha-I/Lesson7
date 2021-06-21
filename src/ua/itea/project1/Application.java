package ua.itea.project1;

public class Application {
    public static void main(String[] args) {
        Heap heap = new Heap();
        Cart cart = new Cart(6);
        Bank bank = new Bank(100);
        Exchanger<Cart> exchangerLoaderTransporter = new Exchanger<>();
        Exchanger<Cart> exchangerUnLoaderTransporter = new Exchanger<>();
        exchangerLoaderTransporter.setWork(true);
        exchangerUnLoaderTransporter.setWork(true);
        Loader loader = new Loader(bank, cart, exchangerLoaderTransporter);
        Unloader unloader = new Unloader(heap, exchangerUnLoaderTransporter);
        Transporter transporter = new Transporter(exchangerLoaderTransporter, exchangerUnLoaderTransporter);
        new Thread(loader).start();
        new Thread(transporter).start();
        new Thread(unloader).start();
    }
}
