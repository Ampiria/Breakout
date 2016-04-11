/**
 * Created by nigel on 4/11/2016.
 */
import java.awt.Color;
import java.awt.Graphics2D;

public class Paddle {
    public static final int Y_POSITION = Screen.HEIGHT - 30;
    public static final int P_WIDTH = 80;
    public static final int P_HEIGHT = 10;
    public static final Color P_COLOR = Color.black;
    private int xPosition;
    public static final int DELTA_X = 5;
    private int score;
    private int lives;

    public Paddle(int xPosition){
        this.xPosition = xPosition;
        score = 0;
        lives = 5;
    }

    public void setX(int xPosition){
        this.xPosition = xPosition;
        if(xPosition < 0) this.xPosition = 0;
        if(xPosition > (Screen.WIDTH - P_WIDTH)) this.xPosition = (Screen.WIDTH - P_WIDTH);
    }

    public int getX(){ return xPosition; }
    public int getScore(){ return score; }
    public void setScore(int score){ this.score = score; }
    public int getLives(){ return lives; }
    public void setLives(int lives){ this.lives = lives; }
    
    public boolean hitPaddle(Ball b){
        if(b.getX() <= xPosition + (P_WIDTH + 15)){
            if(b.getX() >= xPosition - 10){
                if((b.getY() + (Ball.DIAMETER - 1)) >= (Y_POSITION)){
                    if((b.getY() + (Ball.DIAMETER - 1)) <= (Y_POSITION + (P_HEIGHT - 5))){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void drawPaddle(Graphics2D g){
        g.setColor(P_COLOR);
        g.fillRect(xPosition, Y_POSITION, P_WIDTH, P_HEIGHT);
        g.setColor(Color.gray);
        g.drawRect(xPosition, Y_POSITION, P_WIDTH, P_HEIGHT);
    }

    public static void main(String[] args){
        Ball b = new Ball(110, (Y_POSITION - (Ball.DIAMETER - 5)), 5, 5);
        Paddle p = new Paddle(110);
        for(int i = 1; i <= P_WIDTH; ++i){
            b.setX(b.getX() + 1);
            System.out.println(p.hitPaddle(b));
        }
        System.out.println(p.hitPaddle(b));
    }
}