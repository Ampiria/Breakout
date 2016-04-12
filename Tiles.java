/**
 * Created by nigel on 4/11/2016.
 */
import java.awt.Color;
import java.awt.Graphics2D;

public class Tiles {
    public static final int TILE_WIDTH = 60;
    public static final int TILE_HEIGHT = 20;
    private int     xPosition, yPosition;
    public Type   tileType;

    //different types of blocks in the game
    enum Type{
        SUPER   (6, 700, Color.black),
        HIGH    (3, 150, Color.RED),
        MEDIUM  (2, 100, Color.BLUE),
        LOW     (1, 50, Color.GREEN),
        DEAD    (0, 0, Color.WHITE);
        private int life;
        private Color color;
        private int points;

        Type(int life, int points, Color color){
            this.life = life;
            this.points = points;
            this.color = color;
        }
        public int getPoints(){ return points;  }
        public Color getColor(){    return color;   }
        public int getLife(){   return life;    }
    }

    public Tiles(int xPosition, int yPosition, Type tileType){
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.tileType = tileType;
    }

    public int getX(){  return xPosition;    }
    public int getY(){  return yPosition;    }
    public Type gettileType(){ return tileType;   }

    //checks if the ball hits a tile
    public boolean hitBy(Ball b){

        if(b.getX() <= (xPosition + TILE_WIDTH) && b.getX() >= xPosition){
            //hits bottom
            if(b.getY() <= (yPosition + TILE_HEIGHT) && b.getY() >= (yPosition + (TILE_HEIGHT / 2))){
                b.setDy(b.getDy() * -1);
                return true;
            }
            //hits top
            else if(b.getY() >= (yPosition - Ball.DIAMETER) && b.getY() < (yPosition + (Ball.DIAMETER / 3))){
                b.setDy(b.getDy() * -1);
                return true;
            }
        }

        else if(b.getY() <= (yPosition + TILE_HEIGHT) && b.getY() >= yPosition){
            //hits right
            if(b.getX() <= (xPosition + TILE_WIDTH) && b.getX() > (xPosition + (TILE_WIDTH - (Ball.DIAMETER / 2)))){
                b.setDx(b.getDx() * -1);
                return true;
            }
            //hits left
            else if(b.getX() >= (xPosition - Ball.DIAMETER) && b.getX() < (xPosition + (Ball.DIAMETER / 2))){
                b.setDx(b.getDx() * -1);
                return true;
            }
        }
        return false;
    }

    public void lessenType(){
        switch(tileType.life){
            case 6:
            case 5:
            case 4:
                --tileType.life;
                break;
            case 3:
                tileType = Type.MEDIUM;
                break;
            case 2:
                tileType = Type.LOW;
                break;
            case 1:
            default:
                tileType = Type.DEAD;
                break;
        }
    }

    public void drawTiles(Graphics2D g){
        g.setColor(Color.white);
        g.fillRect(xPosition, yPosition, TILE_WIDTH, TILE_HEIGHT);
        g.setColor(tileType.color);
        g.fillRect((xPosition+2), (yPosition+2), TILE_WIDTH-4, TILE_HEIGHT-4);
        g.setColor(Color.black);
        g.drawRect((xPosition+2), (yPosition+2), TILE_WIDTH-4, TILE_HEIGHT-4);
    }

    public boolean dead() {
        if(tileType.life == 0)
            return true;
        return false;
    }
}