package org.example.GUI;

import org.example.BusinessLogic.SimulationManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SimulationFrame extends JFrame {
    private JPanel mainPanel;
    private JTextField simTxt;
    private JTextField queueTxt;
    private JTextField minServiceTxt;
    private JTextField maxArrivalTxt;
    private JTextField maxServiceTxt;
    private JLabel queueLabel;
    private JTextField clientTxt;
    private JLabel minArrivalLabel;
    private JLabel minServiceLabel;
    private JTextField minArrivalTxt;
    private JLabel maxArrivalLabel;
    private JLabel maxServiceLabel;
    private JLabel simLabel;
    private JLabel clientLabel;
    private JButton startBtn;
    private JPanel panel1;
    private JPanel panel2;
    private JPanel panel3;
    private JPanel simulationPanel;

    private SimulationPanel customSimPanel;
    public SimulationFrame() {
        setContentPane(mainPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000,800);
        setTitle("Simulator");
        setLocationRelativeTo(null);

        customSimPanel = new SimulationPanel();
        simulationPanel.setLayout(new BorderLayout());
        simulationPanel.add(customSimPanel, BorderLayout.CENTER);

        startBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    int clients=Integer.parseInt(clientTxt.getText());
                    int queues=Integer.parseInt(queueTxt.getText());
                    int minService=Integer.parseInt(minServiceTxt.getText());
                    int maxService=Integer.parseInt(maxServiceTxt.getText());
                    int minArrival=Integer.parseInt(minArrivalTxt.getText());
                    int maxArrival=Integer.parseInt(maxArrivalTxt.getText());
                    int timeLimit=Integer.parseInt(simTxt.getText());

                    SimulationManager manager = new SimulationManager(SimulationFrame.this,timeLimit,minArrival,maxArrival,maxService,minService,queues,clients);

                    ExecutorService executor= Executors.newSingleThreadExecutor();
                    executor.execute(manager);

                    executor.shutdown();



                }catch(NumberFormatException ex){
                    JOptionPane.showMessageDialog(mainPanel,"Enter valid arguments!","Error",JOptionPane.ERROR_MESSAGE);
                }
            }
        });


        setVisible(true);

    }

    public SimulationPanel getSimulationPanel(){
        return customSimPanel;
    }
}
