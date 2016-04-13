import java.awt.*;

/**
 * Created by nigel on 4/7/2016.
 */
public class Ball {
    private int xPosition, yPosition;
    private double dx, dy;
    public static final int DIAMETER = 10;
    public static Color ballColor = Color.black;

    public Ball(int xPosition, int yPosition, int dx, int dy){
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.dx = dx;
        this.dy = dy;
    }

    public static void setBallColor(Color ballColor) {
        Ball.ballColor = ballColor;
    }

    public void setX(int xPosition){
        this.xPosition = xPosition;
    }

    public void setY(int yPosition){
        this.yPosition = yPosition;
    }

    public void setDx(double dx) {
        this.dx = dx;
    }

    public void setDy(double dy) {
        this.dy = dy;
    }

    public int getX(){
        return xPosition;
    }

    public int getY(){
        return yPosition;
    }

    public double getDx(){
        return dx;
    }

    public double getDy() {
        return dy;
    }

    //Moves the ball by adding the change in x (dx) and change in y (dy) to xPosition and yPosition respectively

    public void ballMove(){
        xPosition += dx;
        yPosition += dy;
    }

    public void drawBall(Graphics2D g){
        g.setColor(ballColor);
        g.fillOval(xPosition,yPosition,DIAMETER,DIAMETER);
        g.setColor(Color.gray);
        g.drawOval(xPosition,yPosition,DIAMETER,DIAMETER);
    }
}