package ua.itea.project1;

public class Bank {

    private int repository;

    public Bank(int repository){
        this.repository = repository;
    }

    public int getMoney(int count){
        if(count > repository){
            throw new IllegalArgumentException("Money is less then " + count);
        }
        repository -= count;
        return count;
    }

    public int getRepository(){
        return repository;
    }
}
