package org.example.BusinessLogic;

import org.example.Model.Server;
import org.example.Model.Task;

import java.util.List;

public class ConcreteStrategyQueue implements Strategy{
    @Override
    public void addTask(List<Server> servers, Task t) {
        // TODO Auto-generated method stub
        Server minServer = servers.get(0);

        for(Server server : servers) {
            if(server.getTasksQueue().size() >= minServer.getTasksQueue().size()) {
                minServer = server;
            }
        }

        minServer.addTask(t);

    }

}
