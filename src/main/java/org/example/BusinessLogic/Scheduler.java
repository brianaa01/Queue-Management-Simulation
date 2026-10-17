package org.example.BusinessLogic;

import org.example.Model.Server;
import org.example.Model.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Scheduler {
    private List<Server> servers;
    private int maxNoServers;
    private int maxTasksPerServer;
    private Strategy strategy;
    private  ExecutorService executor;

    public Scheduler(int maxNoServers, int maxTasksPerServer) {
        // for maxNoServers
        // - create server object
        // - create thread with the object
        this.maxNoServers = maxNoServers;
        this.maxTasksPerServer = maxTasksPerServer;
        servers = new ArrayList<Server>();
        executor = Executors.newFixedThreadPool(maxNoServers);

        for(int i = 0; i < maxNoServers; i++){
            Server server = new Server();
            servers.add(server);
            executor.execute(server);
        }


    }

    public void changeStrategy(SelectionPolicy policy) {
        // apply strategy pattern in order to instantiate the strategy
        // with the concrete strategy corresponding to policy
        if (policy == SelectionPolicy.SHORTEST_TIME) {
            strategy = new ConcreteStrategyTime();
        }

        if (policy == SelectionPolicy.SHORTEST_QUEUE) {
            strategy = new ConcreteStrategyQueue();
        }
    }

    public void dispatchTask(Task t) {
        // call the strategy addTask method
                strategy.addTask(servers,t);

    }

    public List<Server> getServers() {
        return servers;
    }
}