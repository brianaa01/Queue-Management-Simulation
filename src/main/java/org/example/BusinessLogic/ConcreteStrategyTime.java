package org.example.BusinessLogic;

import org.example.Model.Server;
import org.example.Model.Task;

import java.util.List;

public class ConcreteStrategyTime implements Strategy {
     @Override
     public void addTask(List<Server> servers, Task t) {
             // TODO Auto-generated method stub
         Server minServer = servers.get(0);

         for(Server server : servers) {
             if(server.getWaitingPeriod().get()<minServer.getWaitingPeriod().get()) {
                 minServer = server;
             }
         }

         minServer.addTask(t);

     }
 }
