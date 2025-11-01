package snakegame;

import javax.swing.*; // to make baord a panel
import java.awt.*; // for the colouring
import java.awt.event.*;

// panel makes sure a specified zone we can take from baord and customise
public class Board extends JPanel implements ActionListener{
     // actionlistener is an interpahse used to computer actions movements and for the timer
     private Image apple;
     private Image head;
     private Image dot;

     private final int totaldots = 900;
     private final int dotsize = 10;
     // Define the board dimensions for the Game Over message (assuming 300x300 based on your collision check)
     private final int B_WIDTH = 300;
     private final int B_HEIGHT = 300;


     private final int[]x = new int[totaldots];
     private final int[]y = new int[totaldots];
     private int apple_x;
     private int apple_y;

     private boolean leftd = false;
     private boolean rytd = false;
     private boolean upd = false;
     private boolean downd = false;

     private boolean endgame = true;

     private int dots;
     private final int randompos = 25;
     private Timer timer;
     Board(){
         addKeyListener(new TAdapter());
         // Set the preferred size of the panel to match the boundary checks
         setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));
         setBackground(Color.PINK);
         setFocusable(true);
         loadImages();
         initGame();
     }
     public void loadImages(){
       ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("snakegame/icons/apple.png"));
       apple = i1.getImage();
       ImageIcon i2 = new ImageIcon(ClassLoader.getSystemResource("snakegame/icons/dot.png"));
       dot = i2.getImage();
       ImageIcon i3 = new ImageIcon(ClassLoader.getSystemResource("snakegame/icons/head.png"));
       head = i3.getImage();

     }
     public void initGame(){
         dots = 3;

         for(int i = 0; i < dots;i++){
             y[i] = 50;
             x[i] = 50 - i * dotsize;
         }
         // Set an initial direction (e.g., right) so the snake moves immediately
         rytd = true;

         locateApple();

         timer = new Timer(140,this);
         timer.start();
     }
     public void locateApple() {
           // Ensure random position is within the board bounds
           int r = (int) (Math.random() * (B_WIDTH / dotsize));
           apple_x = r * dotsize;
           r = (int) (Math.random() * (B_HEIGHT / dotsize));
           apple_y = r * dotsize;
     }

     public void paintComponent(Graphics g){
         super.paintComponent(g);

         draw(g);
     }
     public void draw(Graphics g){
         if(endgame == true){
             g.drawImage(apple,apple_x,apple_y,this);
             for(int i = 0; i < dots;i++){
                 if(i == 0){
                     g.drawImage(head,x[i],y[i],this);
                 }
                 else{
                     g.drawImage(dot,x[i],y[i],this);
                 }
             }
             Toolkit.getDefaultToolkit().sync();

         } else {
             gameOver(g);
         }
     }

     public void gameOver(Graphics g) {
         String msg = "Game Over! Score: " + (dots - 3);
         Font small = new Font("Helvetica", Font.BOLD, 14);
         FontMetrics metr = getFontMetrics(small);

         g.setColor(Color.WHITE);
         g.setFont(small);
         // Draw the message centered on the board
         g.drawString(msg, (B_WIDTH - metr.stringWidth(msg)) / 2, B_HEIGHT / 2);

         // Optional: Display the final score
         String score = "Score: " + (dots - 3);
         g.drawString(score, (B_WIDTH - metr.stringWidth(score)) / 2, B_HEIGHT / 2 + 30);
     }
     // ----------------------------------------


     public void move() {
         for(int i = dots; i > 0 ;i--){
             x[i] = x[i-1];
             y[i] = y[i-1];
         }
         if(leftd){
             x[0] = x[0]-dotsize;
         }
         if(rytd){
             x[0] = x[0]+dotsize;
         }
         if(upd){
             y[0] = y[0]-dotsize;
         }
         if(downd){
             y[0] = y[0]+dotsize;
         }
     }

     public void checkApple(){
         if((x[0] == apple_x) && (y[0]==apple_y)){
             dots++;
             locateApple(); //creates a new apple after collison between same coorditonates in a random name
         }
     }
     private void checkCollison(){
         // Collision with itself
         for(int i = dots; i > 0; i--){
             if((i > 4) && (x[0]==x[i]) && (y[0]==y[i])){
                 endgame = false;
             }
         }
         // Collision with walls (Assuming the board size is 300x300 based on your current boundary checks)
         if(y[0] >= B_HEIGHT){
             endgame = false;
         }
         if(x[0] >= B_WIDTH){
             endgame = false;
         }
         if(y[0] < 0){ // Changed y[0] <= 0 to y[0] < 0 for top boundary
             endgame = false;
         }
         if(x[0] < 0){ // Changed x[0] <= 0 to x[0] < 0 for left boundary
             endgame = false;
         }

         if(endgame == false){
             timer.stop();
         }
     }
     @Override
     public void actionPerformed(ActionEvent ae) {
         if(endgame){
             move();
             checkApple();
             checkCollison();
             repaint();
         } else {
             // **NEW**: Repaint even when the game has ended to display the "Game Over" screen
             repaint();
         }
     }
    // keyadapter is used to make the actions based on key fucntion
     public class TAdapter extends KeyAdapter {
         @Override
         public void keyPressed(KeyEvent e){
             int key = e.getKeyCode();

             // Only allow movement if the game is still running
             if(endgame) {
                 if(key == KeyEvent.VK_LEFT && !rytd){
                     leftd = true;
                     upd = false;
                     downd=false;
                     rytd = false; // Added to be explicit
                 }
                 if(key == KeyEvent.VK_RIGHT && !leftd){
                     rytd = true;
                     upd = false;
                     downd=false;
                     leftd = false; // Added to be explicit
                 }
                 if(key == KeyEvent.VK_UP && !downd){
                     upd = true;
                     leftd = false;
                     rytd=false;
                     downd = false; // Added to be explicit
                 }
                 if(key == KeyEvent.VK_DOWN && !upd){
                     downd = true;
                     leftd = false;
                     rytd=false;
                     upd = false; // Added to be explicit
                 }
             }
         }
     }
}