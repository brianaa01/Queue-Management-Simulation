package org.example.BusinessLogic;

import org.example.GUI.SimulationFrame;
import org.example.Model.Server;
import org.example.Model.Task;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class SimulationManager implements Runnable {
    // following data read from UI
    // maximum processing time, it is read from UI
    public int timeLimit = 20;
    public int minArrivalTime = 2;
    public int maxArrivalTime = 15;

    public int maxProcessingTime = 10;
    public int minProcessingTime = 2;
    public int numberOfServers = 2;
    public int numberOfClients = 4;
    public SelectionPolicy selectionPolicy = SelectionPolicy.SHORTEST_TIME;

    // entity responsible with queue management and client distribution
    private Scheduler scheduler;
    // frame for displaying simulation
    private SimulationFrame frame;
    // pool of tasks (client shopping in the store)
    private List<Task> generatedTasks;

    private double avgService=0;
    private double avgWaiting=0;
    private int peakHour=0;
    public static AtomicInteger totalServiceTime=new AtomicInteger(0);
    public static AtomicInteger servedClients=new AtomicInteger(0);


    //constructor for SimulationFrame
    public SimulationManager(SimulationFrame frame,int timeLimit,int minArrivalTime,int maxArrivalTime,int maxProcessingTime,int minProcessingTime,int numberOfServers,int numberOfClients) {
        this.frame = frame;
        this.timeLimit = timeLimit;
        this.minArrivalTime = minArrivalTime;
        this.maxArrivalTime = maxArrivalTime;
        this.maxProcessingTime = maxProcessingTime;
        this.minProcessingTime = minProcessingTime;
        this.numberOfServers = numberOfServers;
        this.numberOfClients = numberOfClients;

        scheduler = new Scheduler(numberOfServers,100);
        scheduler.changeStrategy(selectionPolicy);

        generateNRandomTasks();
    }

    public SimulationManager() {
        // initialize the scheduler
        // => create and start numberOfServers threads
        // => initialzie selection strategy => createStrategy
        // initialize frame to display simulation
        // generate numberOfClients clients using generateNRandomTasks()
        // store them to generatedTasks

        scheduler = new Scheduler(numberOfServers,100);
        scheduler.changeStrategy(selectionPolicy);

        generateNRandomTasks();
    }

    private void generateNRandomTasks() {
        Random random = new Random();
        generatedTasks = new ArrayList<Task>();
        // generate N random tasks;
        // - random processing time
        // minProcessingTIme < processingTime < maxProcessingTime
        // - random arrivalTime
        // sort list with respect to arrivalTime
        for(int i = 1; i <= numberOfClients; i++) {
            int serviceTime = random.nextInt(maxProcessingTime-minProcessingTime+1) + minProcessingTime;
            int arrivalTime = random.nextInt(maxArrivalTime-minArrivalTime+1) + minArrivalTime;

            Task task = new Task(arrivalTime,serviceTime,i);
            generatedTasks.add(task);

        }

        generatedTasks.sort(new Comparator<Task>() {
            @Override
            public int compare(Task o1, Task o2) {
                return Integer.compare(o1.getArrivalTime(), o2.getArrivalTime());
            }
        });

    }

    @Override
    public void run() {
        int currentTime = 0;
        int maxClients = -1;

        /*
        for(Task task : generatedTasks) {
            avgService+=task.getServiceTime();
        }
        */

        try(PrintWriter log = new PrintWriter(new FileWriter("log.txt")))
        {
            while (currentTime < timeLimit) {
                // iterate generatedTasks list and pick tasks that have the arrivalTime = currentTime
                // - send task to queue by calling dispatchTask method from Scheduler
                // - delete client from list
                // update UI frame


                Iterator<Task> iterator = generatedTasks.iterator();

                while (iterator.hasNext()) {
                    Task task = iterator.next();
                    if(task.getArrivalTime() == currentTime){
                        scheduler.dispatchTask(task);
                        iterator.remove();
                    }
                }

                //waiting period and nb of clients at this current time for servers
                int currentNbClients = 0;
                for(Server server : scheduler.getServers()) {
                    int inQueue = server.getTasksQueue().size();
                    int actuallyWaiting = inQueue;
                    if(server.getCurrentTask()==null && actuallyWaiting>0)
                    {
                        actuallyWaiting--;
                    }
                    avgWaiting+=actuallyWaiting;

                    currentNbClients+=inQueue;
                    if(server.getCurrentTask()!=null) {
                        currentNbClients++;
                    }

                }

                if(currentNbClients>maxClients) {
                    maxClients = currentNbClients;
                    peakHour=currentTime;
                }

                System.out.println("Time: " + currentTime);
                log.println("Time: " + currentTime);
                System.out.println("Waiting clients: " + generatedTasks);
                log.println("Waiting clients: " + generatedTasks);

                for(int i=0;i<scheduler.getServers().size();i++) {
                    Server server = scheduler.getServers().get(i);

                    List<Task> allInQueue=new ArrayList<>();
                    if(server.getCurrentTask()!=null){
                        allInQueue.add(server.getCurrentTask());
                    }

                    allInQueue.addAll(server.getTasksQueue());
                    if(allInQueue.isEmpty()) {
                        System.out.println("Queue " + (i+1) + ": closed");
                        log.println("Queue " + (i+1) + ": closed");
                    }
                    else{
                        System.out.println("Queue " + (i+1) + ": " + allInQueue);
                        log.println("Queue " + (i+1) + ": " + allInQueue);
                    }
                }
                System.out.println();
                log.println();

                if(frame!=null)
                {
                    frame.getSimulationPanel().updateState(currentTime,generatedTasks,scheduler.getServers(),0.0,0.0,peakHour);
                }

                currentTime++;
                // wait an interval of 1 second

                try{
                    Thread.sleep(1000);
                }catch(InterruptedException e){
                    Thread.currentThread().interrupt();
                    break;
                }

            }

            int nrClientsServed=servedClients.get();
            if(nrClientsServed>0){
                double serviceTime=(double) totalServiceTime.get()/nrClientsServed;
                avgService=serviceTime;
            }

            int clientsWhoEntered=numberOfClients-generatedTasks.size();

            System.out.println("Average service time: " + avgService);
            System.out.println("Average waiting time: " + avgWaiting/clientsWhoEntered);
            System.out.println("Peak Hour: " + peakHour);

            log.println("Average service time: " + avgService);
            log.println("Average waiting time: " + avgWaiting/clientsWhoEntered);
            log.println("Peak Hour: " + peakHour);

            avgWaiting/=clientsWhoEntered;

        }catch(IOException e)
        {
            System.out.println("IO Exception");
        }

        if(frame!=null)
        {
            frame.getSimulationPanel().updateState(currentTime,generatedTasks,scheduler.getServers(),avgWaiting,avgService,peakHour);
        }

        for(Server server : scheduler.getServers()){
            server.stopServer();
        }
    }
}
