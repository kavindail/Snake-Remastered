package org.Snake;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

import static java.lang.Math.round;

/**
 * SpriteManager class which manages all the sprites on the screen
 *
 * @author
 * @version
 */
public class SpriteManager {
    private ArrayList<Sprite> sprites;


    /**
     * The player's snake
     */
    private Snake player;

    /**
     * The enemies
     */
    private Enemy enemy;
    private int tileWidth;
    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }
    private String level;
    private int rows;
    private int cols;
    private Tile[][] tiles;
    private Food[][] food;
    private final Window window;
    private int spawnPoint[] = {10,10};


    private String wallImage = "src/main/images/wall.png";
    private String foodImage = "src/main/images/apple.png";
    private String snakeImage = "src/main/images/snakeDown.png";
    private String bodyImage = "src/main/images/bodyNS.png";
    private String tailImage = "src/main/images/tailUp.png";

    public SpriteManager(Window window, int cellsize, int rows, int cols) {
        this.window = window;
        this.rows = rows;
        this.cols = cols;
        this.tileWidth = cellsize;
        Sprite.setWindow(window);
//      this.tiles = MazeMaker3.generateMaze(wallImage, rows, cols, cellsize, 0);
        //this.tiles = MazeMaker.loadMaze(wallImage, rows, cols, cellsize, 0);
        //this.spawnPoint = MazeMaker.loadSpawn(0);
        //this.food = MazeMaker.loadFood(foodImage, rows, cols, cellsize, 0);
        sprites = new ArrayList<>();


        player = Snake.getInstance(spawnPoint[0]*tileWidth, (int) (spawnPoint[1]*tileWidth+ window.getTopOffset()), tileWidth, snakeImage);
//        //TODO make this an array or linked list

        sprites.add(player);

//        //TODO this can be looped once a data structure is used


        window.fill(0, 0, 0);
        window.rect(0, 0, window.getWIDTH()*3, window.getWIDTH()*3);
    }
    /**
     * Method to update all the sprites
     */
    public Tile[][] getTiles() {
        return tiles;
    }
    //TODO: Make an intance of the mazemaker and call generate maze on it instead of calling the static method
    //mazemaker parent class, mazemaker interface that mazemaker classes implement
    //mazemaker interface that mazemaker classes implement
    public void makeTiles() {
        if (Objects.equals(this.level, "random")) {
            this.tiles = MazeMaker3.generateMaze(wallImage, rows, cols, tileWidth);
        } else {
            this.tiles = MazeMaker.loadMaze(wallImage, rows, cols, tileWidth, this.level);
            this.spawnPoint = MazeMaker.loadSpawn(this.level);
            this.food = MazeMaker.loadFood(foodImage, rows, cols, tileWidth, this.level);
            for (Tile[] tile : tiles) {
                for (Tile tile1 : tile) {
                    sprites.add(tile1);
                    if (tile1 != null) {
                        tile1.draw();
                    }
                }
            }
            try {
                for (Food[] food1 : food) {
                    for (Food food2 : food1) {
                        sprites.add(food2);
                    }
                }
            }
            catch (Exception e){
                //System.out.println("No food");
            }
        }

        for (Tile[] tile : tiles) {
            for (Tile tile1 : tile) {
                sprites.add(tile1);
            }
        }
    }

    /**
     * Method to animate the player
     */
    public ArrayList<Sprite> animate() {

        //calculate the next position of the player
        float moveX = player.getDirectionX() * (this.tileWidth) / Clock.getFramesPerClock();
        float moveY = player.getDirectionY() * (this.tileWidth) / Clock.getFramesPerClock();
        float nextX = player.getxPos() + moveX;
        float nextY = player.getyPos() + moveY;
//        player.animateBody(Clock.getFramesPerClock(), player.getBody().size()-2);
//        System.out.println("moveX: " + moveX + " moveY: " + moveY);
//        float prevX = player.getxPos();
//        float prevY = player.getyPos();
        player.setxPos(nextX);
        player.setyPos(nextY);
//        player.slither(prevX, prevY);ne

        return sprites;
    }

    /**
     * Method to update all the sprites
     */
    public ArrayList<Sprite> update(int lastKeyPressed){

        //if the player direction is 0, then the snake is pointing down, and should not be able to move up
        if (player.getDirectionX() == 0 && player.getDirectionY() == 0 && (lastKeyPressed == 38 || lastKeyPressed == 87)) {
            return sprites;
        }


        //update the sprites to the next frame
        int trueX = round(player.getxPos() / this.tileWidth) * this.tileWidth;
        int trueY = round(player.getyPos() / this.tileWidth) * this.tileWidth;
        float prevX = player.getxPos();
        float prevY = player.getyPos();

        player.setxPos(trueX);
        player.setyPos(trueY);
        player.moveBody(player.getxPos(), player.getyPos());


        //MOVE PLAYER BASED TO KEY PRESS

        //update the sprites to the next frame
       //before updating the sprites , check for collisions and update the sprites accordingly
        if (lastKeyPressed >= 37 && lastKeyPressed <= 40) {
            player.move(lastKeyPressed);
        }
        if(lastKeyPressed == 87 || lastKeyPressed == 83 || lastKeyPressed == 65 || lastKeyPressed == 68){
            player.move(lastKeyPressed);
        }
//        if (lastKeyPressed !=0 ) {
//            player.moveBody(prevX, prevY);
//        }

        this.collide();
        return sprites;
    }

    /**
     * Method to check if the player is colliding with a wall or a tile
     */
    private void collide() {
        //check if the player is colliding with a wall
        int x = (int) (player.getxPos() / this.tileWidth);
        int y = (int) (player.getyPos() / this.tileWidth);
        //LEFT
        if (x + player.getDirectionX() < 0) {window.reset();}
        //RIGHT
        if (x + player.getDirectionX() >= cols) {window.reset();}
        //TOP
        if (y + player.getDirectionY() <= 0) {window.reset();}
        //BOTTOM
        if (y + player.getDirectionY() > rows) {window.reset();}
        //check if the player is colliding with a tile
        if (tiles[x+player.getDirectionX()][y+player.getDirectionY()-1] != null) {
            if (tiles[x+player.getDirectionX()][y+player.getDirectionY()-1].isWall()){window.reset();}
            //if (tiles[y+player.getDirectionX()][x+player.getDirectionX()].isFood()){player.grow();}
        }
        // collision with food
        if (food[x+player.getDirectionX()][y+player.getDirectionY()-1] != null && !food[x+player.getDirectionX()][y+player.getDirectionY()-1].isEaten()) {
            player.grow();
            window.incrementScore();
            if (this.level.equals("classic")){
                Random rand = new Random();
                int newX = rand.nextInt(rows-3)+1;
                int newY = rand.nextInt(cols-3)+1;
                food[x+player.getDirectionX()][y+player.getDirectionY()-1].eat();
                food[newX][newY] = new Food(newX*tileWidth, newY*tileWidth, tileWidth, foodImage);
                sprites.remove(food[x+player.getDirectionX()][y+player.getDirectionY()-1]);
                food[x+player.getDirectionX()][y+player.getDirectionY()-1] = null;
                sprites.add(food[newX][newY]);
            }
            else if(this.level.equals("modern")) {
                Random rand = new Random();
                int newX = rand.nextInt(rows-3)+1;
                int newY = rand.nextInt(cols-3)+1;
                while (tiles[newX][newY] != null || (player.getxPos() == newX*tileWidth && player.getyPos() == newY*tileWidth)|| (player.getxPos()+ player.getDirectionX()*tileWidth == newX*tileWidth && player.getyPos()+ player.getDirectionY()*tileWidth == newY*tileWidth)){
                    newX = rand.nextInt(rows-3)+1;
                    newY = rand.nextInt(cols-3)+1;
                }
                tiles[newX][newY] = new Tile(newX*tileWidth, newY*tileWidth, tileWidth, wallImage, true);
                tiles[newX][newY].draw();
                sprites.add(tiles[newX][newY]);

                newX = rand.nextInt(rows-3)+1;
                newY = rand.nextInt(cols-3)+1;
                while (tiles[newX][newY] != null){
                    newX = rand.nextInt(rows-3)+1;
                    newY = rand.nextInt(cols-3)+1;
                }
                food[x+player.getDirectionX()][y+player.getDirectionY()-1].eat();
                sprites.remove(food[x+player.getDirectionX()][y+player.getDirectionY()-1]);
                food[newX][newY] = new Food(newX*tileWidth, newY*tileWidth, tileWidth, foodImage);
                food[x+player.getDirectionX()][y+player.getDirectionY()-1] = null;
                sprites.add(food[newX][newY]);
            }else {
                food[x + player.getDirectionX()][y + player.getDirectionY() - 1].eat();
            }
        }
        for (int i = 0; i < player.getBody().size()-1; i++) {
            if (player.getBody().get(i).getxPos() == player.getxPos() && player.getBody().get(i).getyPos() == player.getyPos()) {
                window.reset();
            }
        }


    }

    private void createMaze(){
        //Generate a maze
        this.tiles = new Tile[rows][cols];
        //generate 20 random walls
        for (int i = 0; i < 20; i++) {
            int x = (int) (Math.random() * cols);
            int y = (int) (Math.random() * rows-1)+1;
            tiles[x][y] = new Tile(x*tileWidth, y*tileWidth, tileWidth, wallImage, true);
        }

        tiles[30][0] = new Tile(30*tileWidth, 0*tileWidth, tileWidth, wallImage, true);

    }

    /**
     * Method to reset the game when the player dies
     */
    public void reset() {
        //System.out.println("x: " + player.getxPos() + " y: " + player.getyPos());
        sprites.clear();
        makeTiles();
        player.reset(spawnPoint[0], spawnPoint[1]);
        sprites.add(player);
        for (Food[] food1 : food) {
            for (Food food2 : food1) {
                try{
                    food2.reset();
                }
                catch (Exception e){
//                    System.out.println("No food");
                }
            }
        }
    }

    /**
     * Method to set the level of the game
     */
    public void setLevel(String level) {
        this.level = level;
    }
}
