package ua.itea.project1;

public class Cart {

    private final int capacity;
    private int loaded;

    public Cart(int capacity){
        this.capacity = capacity;
    }

    public int getLoaded(){
        return loaded;
    }

    public int getFreeSpace(){
        return capacity - loaded;
    }

    public void unload(int count){
        if(loaded < count){
            throw new IllegalArgumentException("Money is less then " + count);
        }
        loaded -= count;
    }

    public void download(int count){
        if(getFreeSpace() < count){
            throw new IllegalArgumentException("Will not fit into the cart " + count);
        }
        loaded += count;
    }
}
