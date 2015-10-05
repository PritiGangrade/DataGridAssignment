package com.glint;

import com.glint.data.Grid;
import com.glint.persistance.DBConnect;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Priti on 10/4/15.
 */
public class GridService implements Runnable {
    private ExecutorService executorService1 = Executors.newSingleThreadExecutor();
    private ExecutorService executorService2 = Executors.newSingleThreadExecutor();
    private ExecutorService executorService3 = Executors.newSingleThreadExecutor();
    private static final int MAX_ITERATIONS=10;
    private static final int GRID_DIMENSION=5;
    private static final int RANDOM_CELLS=5;
    private DBConnect dbConnect = new DBConnect();

    private Grid grid = new Grid(GRID_DIMENSION);

    public static void main(String[] args) {
        GridService main = new GridService();
        System.out.println("Print Starting values for the grid");
        main.initializeGrid();
        main.start();
    }

    public void printGrid() {
        grid.printGrid();
    }

    public void initializeGrid() {
        Grid gridFromDB = getGridFromDB();
        this.grid=gridFromDB;
    }

    private Grid getGridFromDB(){
        return dbConnect.getGridFromDB(GRID_DIMENSION);
    }

    @Override
    public void run() {
        for (int i=0;i<MAX_ITERATIONS/2;i++) {
            try {
                Thread.sleep(10);
            }catch (InterruptedException e) {}
        }
        System.out.println("Crashing the system");
        stop();
        throw new RuntimeException("Crashing the system");
    }

    public void start(){
        CustomThread threadA = new CustomThread('A', MAX_ITERATIONS, grid, RANDOM_CELLS, dbConnect);
        CustomThread threadB = new CustomThread('B', MAX_ITERATIONS, grid, RANDOM_CELLS, dbConnect);

        executorService1.execute(threadA);
        executorService2.execute(threadB);
        executorService3.execute(this);
    }

    public void stop(){
        System.out.println("Stopping executor service");
        executorService1.shutdownNow();
        executorService2.shutdownNow();
        printGrid();
        executorService3.shutdown();
    }

}
