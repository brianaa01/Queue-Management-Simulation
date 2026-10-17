package org.example.Model;

import org.example.BusinessLogic.SimulationManager;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class Server implements Runnable {
    private BlockingQueue<Task> tasks;
    private AtomicInteger waitingPeriod;
    private AtomicBoolean isRunning;
    private Task currentTask=null;

    public Server(){
        tasks = new LinkedBlockingQueue<>();
        waitingPeriod = new AtomicInteger(0);
        isRunning = new AtomicBoolean(true);
    }
    public void addTask(Task newTask){
        tasks.add(newTask);
        waitingPeriod.addAndGet(newTask.getServiceTime());


    }
    public void run(){
        while (isRunning.get()){
            // take next task from queue
            // stop the thread for a time equal with the task’s processing time
            // decrement the waitingPeriod
            try{
                Task task = tasks.take();
                this.currentTask = task;
                SimulationManager.totalServiceTime.addAndGet(task.getServiceTime());
                SimulationManager.servedClients.incrementAndGet();

                while(task.getServiceTime() > 0){
                    Thread.sleep(1000);
                    task.setServiceTime(task.getServiceTime() - 1);
                    waitingPeriod.decrementAndGet();
                }

                this.currentTask=null;

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    public void stopServer(){
        isRunning.set(false);
    }

    public Task getCurrentTask(){
        return currentTask;
    }

    public Task[] getTasks(){
        return tasks.toArray(new Task[0]);
    }

    public AtomicInteger getWaitingPeriod(){
        return waitingPeriod;
    }

    public BlockingQueue<Task> getTasksQueue(){
        return tasks;
    }
}