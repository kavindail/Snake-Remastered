package org.example;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class MazeMaker {

    public static Tile[][] loadMaze(String wallImage, int rows, int cols, int cellSize, int level) {
        Tile[][] tiles = new Tile[rows][cols];

        //read json file
        FileReader reader = null;
        try {
            reader = new FileReader("src" + File.separator + "main" + File.separator + "levels" + File.separator + "ultraHard" + level + ".json");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        StringBuffer buffer = new StringBuffer();
        int i;
        try {
            while ((i = reader.read()) != -1) {
                buffer.append((char) i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //create maze
        JSONObject obj = new JSONObject(buffer.toString());
        JSONArray maze = obj.getJSONArray("maze");

        for (int j = 0; j < maze.length(); j++) {
            JSONObject row = maze.getJSONObject(j);
            int x = row.getInt("y");
            int y = row.getInt("x");
            tiles[x][y] = new Tile(x*cellSize, y*cellSize, cellSize, wallImage, true);
        }


        return tiles;
    }
}
