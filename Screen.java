/**
 * Created by nigel on 4/11/2016.
 */
import sun.audio.AudioData;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;
import sun.audio.ContinuousAudioDataStream;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

import javax.swing.*;


public class Screen extends JPanel implements ActionListener, MouseMotionListener, MouseListener, KeyListener {

    public static final int HEIGHT = 600;
    public static final int WIDTH = 910;

    private int horizontalCount;
    private BufferedImage image;
    private Graphics2D bufferedGraphics;
    private Timer time;
    private static final Font endFont = new Font(Font.SANS_SERIF, Font.BOLD, 20);
    private static final Font scoreFont = new Font(Font.SANS_SERIF, Font.BOLD, 15);
    private Paddle player;
    private Ball ball;
    ArrayList<ArrayList<Tiles> > bricks;

    //Prepares the screen

    public Screen(){
        super();
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        bufferedGraphics = image.createGraphics();
        time = new Timer(15, this);
        player = new Paddle((WIDTH/2)-(Paddle.P_WIDTH/2));
        ball = new Ball(((player.getX() + (Paddle.P_WIDTH / 2)) - (Ball.DIAMETER / 2)),
                (Paddle.Y_POSITION - (Ball.DIAMETER + 10)), -5, -5);

        bricks = new ArrayList<ArrayList<Tiles> >();
        horizontalCount = WIDTH / Tiles.TILE_WIDTH;
        for(int i = 0; i < 8; ++i){
            ArrayList<Tiles> temp = new ArrayList<Tiles>();
            Tiles.Type rowColor = null;
            switch(i){
                case 0:
                case 2:
                    rowColor = Tiles.Type.LOW;
                    break;
                case 1:
                case 3:
                case 5:
                    rowColor = Tiles.Type.MEDIUM;
                    break;
                case 4:
                case 6:
                    rowColor = Tiles.Type.HIGH;
                    break;
                case 7:
                default:
                    rowColor = Tiles.Type.SUPER;
                    break;
            }
            for(int j = 0; j < horizontalCount; ++j){
                Tiles tempTiles = new Tiles((j * Tiles.TILE_WIDTH), ((i+2) * Tiles.TILE_HEIGHT), rowColor);
                temp.add(tempTiles);
            }
            bricks.add(temp);
            addMouseMotionListener(this);
            addMouseListener(this);
            addKeyListener(this);
            requestFocus();
        }
    }

    @Override public void actionPerformed(ActionEvent e){
        checkCollisions();
        ball.ballMove();
        for(int i = 0; i < bricks.size(); ++i){
            ArrayList<Tiles> al = bricks.get(i);
            for(int j = 0; j < al.size(); ++j){
                Tiles b = al.get(j);
                if(b.dead()){
                    al.remove(b);
                }
            }
        }
        repaint();
    }

    //Checks for any collisions

    private void checkCollisions() {
        if(player.hitPaddle(ball)){
            ball.setDy(ball.getDy() * -1);
            return;
        }

        if(ball.getX() >= (WIDTH - Ball.DIAMETER) || ball.getX() <= 0){
            ball.setDx(ball.getDx() * -1);
        }
        if(ball.getY() > (Paddle.Y_POSITION + Paddle.P_HEIGHT + 10)){
            resetBall();
        }
        if(ball.getY() <= 0){
            ball.setDy(ball.getDy() * -1);
        }

        int brickRowsActive = 0;
        for(ArrayList<Tiles> alb : bricks){
            if(alb.size() == horizontalCount){
                ++brickRowsActive;
            }
        }

        for(int i = (brickRowsActive==0) ? 0 : (brickRowsActive - 1); i < bricks.size(); ++i){
            for(Tiles b : bricks.get(i)){
                if(b.hitBy(ball)){
                    player.setScore(player.getScore() + b.tileType.getPoints());
                    b.lessenType();
                }
            }
        }
    }

    //Resets the ball after losing a life

    private void resetBall() {
        if(gameOver()){
            time.stop();
            return;
        }
        ball.setX(WIDTH/2);
        ball.setY((HEIGHT/2) + 80);
        player.setLives(player.getLives() - 1);
        player.setScore(player.getScore() - 1000);
    }

    private boolean gameOver() {
        if(player.getLives() <= 1)
            return true;
        return false;
    }

    //background music made by Andrew Thompson
    public void backgroundMusic(){
        AudioPlayer music = AudioPlayer.player;
        ContinuousAudioDataStream myLoop = null;
        try {
            AudioStream myBackgroundMusic = new AudioStream(getClass().getResourceAsStream("song.mp3"));
            AudioData myData = myBackgroundMusic.getData();
            myLoop = new ContinuousAudioDataStream(myData);
        }catch(Exception error){
            System.out.println("File Not Found");
            System.out.println(error);
        }
        music.start(myLoop);
    }


    //Draws the screen
    @Override public void paintComponent(Graphics g){
        super.paintComponent(g);
        bufferedGraphics.clearRect(0, 0, WIDTH, HEIGHT);
        bufferedGraphics.setColor(Color.WHITE);
        bufferedGraphics.fillRect(0, 0, WIDTH, HEIGHT);
        player.drawPaddle(bufferedGraphics);
        ball.drawBall(bufferedGraphics);
        bufferedGraphics.setFont(scoreFont);
        bufferedGraphics.drawString("Instructions: Click the mouse in order to start the game." +
                "Move the mouse to hit the ball into the bricks until they are all destroyed.", 5,595);
        for(ArrayList<Tiles> row : bricks){
            for(Tiles b : row){
                b.drawTiles(bufferedGraphics);
            }
        }
        bufferedGraphics.setFont(scoreFont);
        bufferedGraphics.drawString("Score: " + player.getScore(), 10, 25);
        bufferedGraphics.drawString("Lives: " + player.getLives(),125,25);
        if(gameOver() && ball.getY() >= HEIGHT){
            bufferedGraphics.setColor(Color.red);
            bufferedGraphics.setFont(endFont);
            bufferedGraphics.drawString("Game Over!  Score: " + player.getScore(), (WIDTH/2) - 85, (HEIGHT/2));
        }
        if(empty()){
            bufferedGraphics.setColor(Color.black);
            bufferedGraphics.setFont(endFont);
            bufferedGraphics.drawString("You won!  Score: " + player.getScore(), (WIDTH/2) - 85, (HEIGHT/2));
            time.stop();
        }
        g.drawImage(image, 0, 0, this);
        Toolkit.getDefaultToolkit().sync();
    }



    private boolean empty() {
        for(ArrayList<Tiles> al : bricks){
            if(al.size() != 0){
                return false;
            }
        }
        return true;
    }

    @Override public void mouseMoved(MouseEvent e){
        player.setX(e.getX() - (Paddle.P_WIDTH / 2));
    }

    @Override public void mouseDragged(MouseEvent e){}

    @Override public void mouseClicked(MouseEvent e){
        if(time.isRunning()){
            return;
        }
        time.start();
    }

    public static void main(String[] args){
        JFrame frame = new JFrame();
        Screen c = new Screen();
        frame.add(c);
        frame.pack();
        frame.setResizable(false);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e){

    }

    @Override
    public void keyTyped(KeyEvent e){

    }

    @Override
    public void keyPressed(KeyEvent e){

    }
}
