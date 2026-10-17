# Multithreaded Queue Management Simulation 

A Java desktop application that simulates a real-time queuing system (e.g., store checkouts, server task scheduling). The project showcases advanced Java concepts, including multithreading, thread synchronization, and structural design patterns.

## Core Features
* **Real-time Simulation:** Simulates clients (Tasks) dynamically arriving and being dispatched to multiple queues (Servers).
* **Multithreading:** Each server/queue runs on its own independent thread, processing tasks concurrently.
* **Dynamic Dispatching:** Clients are distributed to servers based on selectable policies.
* **Graphical Interface:** Real-time visual tracking of queues, waiting times, and simulation events built with Java Swing.

## Architecture & Design Patterns
The application separates concerns cleanly into `GUI`, `BusinessLogic`, and `Model` packages:
* **Strategy Design Pattern:** Implemented for client dispatching. The user can switch between different strategies (`ConcreteStrategyTime` - dispatch to the server with the lowest waiting time, or `ConcreteStrategyQueue` - dispatch to the server with the fewest clients).
* **Producer-Consumer Logic:** The `SimulationManager` acts as the producer generating tasks, while the multithreaded `Server` instances act as consumers processing them.
* **Thread Safety:** Usage of synchronized blocks and thread-safe data structures to prevent race conditions during concurrent modifications.

## Technologies Used
* **Language:** Java
* **UI Framework:** Java Swing (`SimulationFrame`, `SimulationPanel`)
* **Core Concepts:** Multithreading, Concurrency, Strategy Pattern, OOP
