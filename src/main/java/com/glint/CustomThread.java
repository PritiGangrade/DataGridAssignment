package com.glint;

import com.glint.data.Grid;
import com.glint.persistance.DBConnect;

import java.util.List;

public class CustomThread implements Runnable {
    private char name;
    private int iterations;
    private int randomCellCount;
    private Grid grid;
    private DBConnect dbConnect;

    public CustomThread(char c, int iterations, Grid grid, int randomCellCount, DBConnect dbConnect){
        name = c;
        this.iterations = iterations;
        this.randomCellCount = randomCellCount;
        this.grid = grid;
        this.dbConnect = dbConnect;
    }

    public void run() {
        try {
            for (int i=1; i<=iterations; i++) {
                boolean transaction = false;
                //grid.printGrid();
                try {
                    System.out.println("Thread" + name);
                    List<String> listRandomCells = grid.getRandomCells(randomCellCount);
                    if (name == 'A') {
                        grid.setRandomCells(listRandomCells, 1);
                    } else if (name == 'B') {
                        grid.setRandomCells(listRandomCells, 10 * i);
                    }
                    transaction = true;
                } finally {
                    //grid.printGrid();
                    if(transaction) {
                        persistGrid(grid);
                    }
                }
                Thread.sleep(10);
            }
        } catch(InterruptedException e) {
            System.out.println("Thread was interrupted: Thread"+ name);
        }
    }


    private void persistGrid(Grid grid) {
        //Persist grid state
        System.out.println("Persisting grid with following state");
        //grid.printGrid();
        dbConnect.persistGrid(grid);
    }
}
