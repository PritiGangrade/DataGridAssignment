package com.glint.persistance;

import com.glint.data.Grid;
import org.apache.commons.lang.StringUtils;
import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Priti on 10/5/15.
 */
public class DBConnect {
    private static final int MAX_RETIRES=100;
    private int retries;
    private Grid gridFromDB;
    private Jedis jedis;
    private int dimensions;

    public DBConnect() {
        //open connection
        jedis = new Jedis("localhost", 6379);
        jedis.connect();
    }

    public Grid getGridFromDB(int dimensions) {
        this.dimensions=dimensions;
        Map<String, String> map = jedis.hgetAll("grid");
        if(map == null || map.size()==0) {
            gridFromDB = new Grid(dimensions);
        } else {
            map = jedis.hgetAll("grid");
            for (Map.Entry<String, String> entry : map.entrySet())
            {
                String str = entry.getKey();
                String coordinates [] = str.split(":");
                String coordinateX[] = coordinates[0].split("_");
                String coordinateY[] = coordinates[1].split("_");
                int x = Integer.parseInt(coordinateX[1]);
                int y = Integer.parseInt(coordinateY[1]);
                int cellValue = Integer.parseInt(entry.getValue());
                System.out.println("row"+ x + ", col" + y + ": " + cellValue);
                gridFromDB.setCellValue(x,y, cellValue);
            }
        }
        return gridFromDB;
    }

    public void persistGrid(Grid grid) {
        System.out.println("Persisting grid: dimensions: " + dimensions);
        StringBuilder stringBuilder = new StringBuilder();
        for(int i=0;i<dimensions;i++) {
            for(int j=0;j<dimensions;j++) {
                stringBuilder.append("x_" + i + ":y_" + j + " " + grid.getCellValue(i,j) + " ");
            }
        }
        System.out.println("persist Statement: " + stringBuilder);
        jedis.hmget("grid", stringBuilder.toString());
    }

    /*public static void main(String[] args) {
        DBConnect dbConnect = new DBConnect();
        //dbConnect.test();
        dbConnect.testGridInsert();
        dbConnect.testGetGrid();
        dbConnect.clearDB();
    }*/

    public void testGridInsert() {
        //data = "cell_00", "0", "cell_01", "0", "cell_10", "0", "cell_11", "0";
        Map<String, String> gridMap = new HashMap<String, String>();
        gridMap.put("x_0:y_0", "98");
        gridMap.put("x_0:y_1", "14");
        gridMap.put("x_1:y_0", "0");
        gridMap.put("x_1:y_1", "11");
        jedis.hmset("grid2", gridMap);
    }

    public void testGetGrid(){
        Map<String,String> map = jedis.hgetAll("grid2");
        for (Map.Entry<String, String> entry : map.entrySet())
        {
            String str = entry.getKey();
            String coordinates [] = str.split(":");
            String coordinateX[] = coordinates[0].split("_");
            String coordinateY[] = coordinates[1].split("_");
            int x = Integer.parseInt(coordinateX[1]);
            int y = Integer.parseInt(coordinateY[1]);
            //System.out.println(entry.getKey() + ": " + entry.getValue());
            System.out.println("row"+ x + ", col" + y + ": " + entry.getValue());


        }
    }

    public void clearDB() {
        jedis.flushDB();
    }


    public void test() {
        jedis.set("testB", "some valueB");
        Set<String> keys = jedis.keys("*");
        System.out.println("keys: " + StringUtils.join(keys, " "));
        for(String key:keys) {
            System.out.println("Key: " + key + ", value: " + jedis.get(key));
        }
    }
}
