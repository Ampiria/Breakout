import java.awt.*;

/**
 * Created by nigel on 4/7/2016.
 */
public class Ball {
    private int xPosition, yPosition;
    private int dx, dy;
    public static final int DIAMETER = 10;
    public static final Color BALL_COLOR = Color.black;

    public Ball(int xPosition, int yPosition, int dx, int dy){
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.dx = dx;
        this.dy = dy;
    }

    public void setX(int xPosition){
        this.xPosition = xPosition;
    }

    public void setY(int yPosition){
        this.yPosition = yPosition;
    }

    public void setDx(int dx) {
        this.dx = dx;
    }

    public void setDy(int dy) {
        this.dy = dy;
    }

    public int getX(){
        return xPosition;
    }

    public int getY(){
        return yPosition;
    }

    public int getDx(){
        return dx;
    }

    public int getDy() {
        return dy;
    }

    public void ballMove(){
        xPosition += dx;
        yPosition += dy;
    }

    public void drawBall(Graphics g){
        g.setColor(BALL_COLOR);
        g.fillOval(xPosition,yPosition,DIAMETER,DIAMETER);
        g.setColor(Color.gray);
        g.drawOval(xPosition,yPosition,DIAMETER,DIAMETER);
    }
}