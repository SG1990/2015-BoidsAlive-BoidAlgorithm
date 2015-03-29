import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JPanel;


public final class World extends JPanel {

	private static final long serialVersionUID = 1L;
	private Random r = new Random();
	
	public ArrayList<Boid> boids = new ArrayList<Boid>();
	public ArrayList<Obstacle> obstacles = new ArrayList<Obstacle>();
	
	private static World instance = null;	
	private World() {
		this.setBackground(Color.white);
	}
	
	public static World getInstance(){
		if(instance == null)
			instance = new World();
		
		return instance;
	}
	
	public void createBoid(){
		Boid b = new Boid() ;
        b.setX(r.nextInt(790));
        b.setY(r.nextInt(590));

        boids.add(b);           
        b.start() ;
        //b.graphicEle.refreshLocation();
        //return b;
	}
	
	@Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        paintAll();
    }

	private void paintAll() {
		for(Boid b : boids) {
			b.graphicEle.refreshLocation();
		}		
	}	
}
