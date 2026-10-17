package org.example.GUI;

import org.example.Model.Server;
import org.example.Model.Task;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class SimulationPanel extends JPanel {
    private List<Task> waitingClients=new ArrayList<Task>();
    private List<Server> servers=new ArrayList<>();
    private int currentTime=0;
    private double avgWait=0.0;
    private double avgService=0.0;
    private int taskHour=0;

    public void updateState(int currentTime,List<Task> waitingClients,List<Server> servers,double avgWait, double avgService,int peakHour) {
        this.waitingClients=new ArrayList<>(waitingClients);
        this.servers=new ArrayList<>(servers);
        this.avgWait=avgWait;
        this.avgService=avgService;
        this.taskHour=peakHour;
        this.currentTime=currentTime;

        this.repaint();
    }

    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2=(Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(Color.DARK_GRAY);
        g2.drawString("Time "+ currentTime,650,20);
        String stats = String.format("Average wait: %.2f | Average service: %.2f | Peak hour: %d",avgWait,avgService,taskHour);
        g2.drawString(stats,600,40);


        g2.setColor(Color.BLACK);
        g2.drawString("Waiting line:",20,30);

        int xWaiting=120;
        for(Task task:waitingClients){
            drawClient(g2,xWaiting,15,task,new Color(220,80,80));
            xWaiting+=85;
        }

        int yServer=80;
        for(int i=0;i<servers.size();i++){
            Server server=servers.get(i);

            g2.setColor(Color.BLUE);
            g2.fillRect(20,yServer,60,40);
            g2.setColor(Color.WHITE);
            g2.drawString("Q" + (i+1),40,yServer+25);

            int xQueue=100;
            if(server.getCurrentTask()!=null)
            {
                drawClient(g2,xQueue,yServer+5,server.getCurrentTask(),new  Color(46, 139, 87));
                xQueue+=85;
            }

            for(Task task:server.getTasksQueue()){
                drawClient(g2,xQueue,yServer+5,task,new Color(230,130,50));
                xQueue+=85;
            }

            yServer+=60;


        }
    }

    private void drawClient(Graphics2D g2,int x,int y,Task task,Color color){
        g2.setColor(color);
        g2.fillOval(x,y,80,30);

        g2.setColor(Color.WHITE);
        String info = String.format("(" + task.getID() + "," + task.getArrivalTime() + "," + task.getServiceTime() + ")");
        g2.drawString(info,x+5,y+20);
    }



}
