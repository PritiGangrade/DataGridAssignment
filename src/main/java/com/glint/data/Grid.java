package com.glint.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 2D Grid
 */
public class Grid {
    private int row;
    private int col;
    private int grid[][];

    public Grid(int dimension) {
        this.row=dimension;
        this.col=dimension;
        grid = new int[row][col];
    }

    public int[][] getGrid(){
        return grid;
    }

    public void setCellValue(int x, int y, int value) {
        grid[x][y]= value;
    }

    public int getCellValue(int x, int y) {
        return grid[x][y];
    }

    public List<String> getRandomCells(int m) {
        ArrayList<String> randomCells = new ArrayList<String>();
        for (int i=1;i<=m;i++){
            int randomRow = getRandomNumber(1,row);
            int randomCol = getRandomNumber(1,col);
            //System.out.println("getRandomcells-row:col::"+randomRow+":"+randomCol);
            String randomCellCoordindates=randomRow+":"+randomCol;
            randomCells.add(randomCellCoordindates);
        }
        return randomCells;
    }

    public void setRandomCells(List<String> randomCells, int value) {
        for(String randomCellCoordinate: randomCells) {
            String coordinates[] = randomCellCoordinate.split(":");
            int row = Integer.parseInt(coordinates[0]);
            int col = Integer.parseInt(coordinates[1]);
            int origValue = grid[row][col];
            int newValue = origValue+value;
            grid[row][col] = newValue;
            //System.out.println("setRandomCells row:col:orig-value:new-value::::"+row+":"+col+":"+origValue+":"+newValue);
        }
    }

    private int getRandomNumber(int min, int max) {
        return (int) (Math.random()*(max-min));
    }

    public void printGrid() {
        System.out.println("Print grids"+ Arrays.deepToString(grid));
    }

}
