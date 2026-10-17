package org.example.GUI;


public class Main {
    public static void main(String[] args) {
        /*
        SimulationManager manager = new SimulationManager();

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(manager);

        executor.shutdown();
        */

        new SimulationFrame();
    }
}