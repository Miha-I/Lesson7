package ua.itea.project2;

import java.util.LinkedList;
import java.util.List;

public class Bank {

    private int repository;
    private final List<WorkListener> workListeners;

    public Bank(int repository){
        workListeners = new LinkedList<>();
        this.repository = repository;
    }

    public int getMoney(int count){
        if(count > repository){
            throw new IllegalArgumentException("Money is less then " + count);
        }
        repository -= count;
        if(repository == 0){
            for (WorkListener workListener : workListeners){
                workListener.workIsOver();
            }
        }
        return count;
    }

    public int getRepository(){
        return repository;
    }

    public void addWorkListener(WorkListener workListener){
        workListeners.add(workListener);
    }
}
