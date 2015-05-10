import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JPanel;
import javax.swing.Timer;


public final class World extends JPanel implements ActionListener, KeyListener, MouseListener {

	private static final long serialVersionUID = 1L;
	private Random r = new Random();
	
	private static final int boundsX = 800;
	private static final int boundsY = 600;
	
	public ArrayList<Boid> boids = new ArrayList<Boid>();
	public ArrayList<Obstacle> obstacles = new ArrayList<Obstacle>();
	
	private static World instance = null;	
	private World() {
		this.setBackground(Color.white);
		this.addKeyListener(this);
		this.addMouseListener(this);
		
		timer.start();
        timerStatus = true;
        
        setFocusable(true);
        requestFocusInWindow();
	}
	
	Timer timer = new Timer(1000/60, this);
	private static boolean timerStatus;
	
	public static World getInstance(){
		if(instance == null)
			instance = new World();
		
		return instance;
	}
	
	public int getBoundsX() { return boundsX; }
	public int getBoundsY() { return boundsY; }
	
	private Boid selectedBoid = null;
	private void setSelected(Boid b){
		if (selectedBoid != null)
			selectedBoid.setSelected(false);
		
		selectedBoid = b;
		
		if (b != null)
			selectedBoid.setSelected(true);
	}
	
	public void createBoid(){
		Boid b = new Boid() ;
        b.setX(r.nextInt(getBoundsX() - (int) b.getSize()));
        b.setY(r.nextInt(getBoundsY() - (int) b.getSize()));       

        boids.add(b);       
	}
	
	@Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        paintBoids(g);
    }

	private void paintBoids(Graphics g) {
		
		if (selectedBoid != null)
			selectedBoid.drawScanArea(g);
		
		for(Boid b : boids)
			b.draw(g);
		drawSelectedBoidInfo(g);
	}
	
	private void drawSelectedBoidInfo(Graphics g) {
		if (selectedBoid != null){
			
			Graphics2D g2d = (Graphics2D) g.create();
			
        	g2d.setColor(Color.black);
        	g2d.drawString("Pos ["+String.format("%1$,.2f",selectedBoid.x)+" ; "+String.format("%1$,.2f",selectedBoid.y)+"]", (int)selectedBoid.x+5, (int)selectedBoid.y+13); 
        	g2d.drawString("Vel ["+String.format("%1$,.2f",selectedBoid.vx)+" ; "+String.format("%1$,.2f", selectedBoid.vy)+"]", (int)selectedBoid.x+5, (int)selectedBoid.y); 
        	g2d.drawString("Ngb ["+selectedBoid.getNeighbours().size()+"]", (int)selectedBoid.x+5, (int)selectedBoid.y-13); 
        	
        	g2d.dispose();
        }
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		for(Boid b : boids)
			b.move();	
		
		repaint();
	}

	@Override
	public void keyTyped(KeyEvent e) {
		if (e.getKeyChar() != ' ')
			return;
		
		if(timerStatus) {
			timer.stop();
			timerStatus = false;
			System.out.println("Pause!");
		}
		else {
			timer.start();
			timerStatus = true;
			System.out.println("Unpause!");
		}		
	}

	@Override
	public void keyPressed(KeyEvent arg0) {}

	@Override
	public void keyReleased(KeyEvent arg0) {}

	@Override
	public void mouseClicked(MouseEvent e) {
		for(Boid b : boids){
			if (dist(e.getPoint().x, e.getPoint().y, (int)b.x, (int)b.y) < b.getSize()){
				
				if (b == selectedBoid)
					setSelected(null);
				else 
					setSelected(b);
				
				this.repaint();
				return;
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}
	
	private double dist(int x1, int y1, int x2, int y2){
		
		int x = x2 - x1;
		int y = y2 - y1;
		
		return Math.sqrt(x*x + y*y);
	}
}
