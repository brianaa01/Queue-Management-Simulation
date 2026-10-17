package org.example.Model;

public class Task {
    private int arrivalTime;
    private int serviceTime;
    private int ID;

    public Task(int arrivalTime, int serviceTime, int ID) {
        this.arrivalTime = arrivalTime;
        this.serviceTime = serviceTime;
        this.ID = ID;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(int arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public int getServiceTime() {
        return serviceTime;
    }

    public void setServiceTime(int serviceTime) {
        this.serviceTime = serviceTime;
    }

    public int getID() {
        return ID;
    }

    @Override
    public String toString() {
        return "(" + ID + ", " + arrivalTime + ", " + serviceTime + ")";
    }
}
