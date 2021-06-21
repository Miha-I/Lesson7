package ua.itea.project1;

public class Exchanger<T> {

    private T obj;
    private boolean work;

    public synchronized T get() throws InterruptedException {
        if(obj == null){
            wait();
        }
        T temp = obj;
        obj = null;
        return temp;
    }

    public synchronized void set(T obj){
        this.obj = obj;
        notify();
    }

    public void setWork(boolean work) {
        this.work = work;
    }

    public boolean isWork(){
        return work;
    }
}
