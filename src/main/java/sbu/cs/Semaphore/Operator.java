package sbu.cs.Semaphore;

import java.util.concurrent.Semaphore;

public class Operator extends Thread {
    public Semaphore semaphore ;
    public Operator(String name , Semaphore semaphore) {
        super(name);
        this.semaphore = semaphore ;
    }

    @Override
    public void run() {
        try {
            semaphore.acquire();
            System.out.println(getName() + "Entered");
            for (int i = 0; i < 10; i++) {
                Resource.accessResource();         // critical section - a Maximum of 2 operators can access the resource concurrently
                sleep(500);
            }
            semaphore.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
