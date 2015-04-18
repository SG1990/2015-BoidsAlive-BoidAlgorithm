import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Path2D;
import java.util.ArrayList;


public class Boid extends WorldObject {
	
	private final int size = 20;
	private final double radius = 50 ;
	private final double angle = 150;
	private double vx, vy;
	
	public double getVX() { return vx; }
	public void setVX(double vx) { this.vx = vx; }
	public double getVY() { return vy; }
	public void setVY(double vy) { this.vy = vy; }
	
	public Boid() { 
		vx = 0;
		vy = -1;
	}
	
	public void move() {		
		//Apply Boid rules
		double[] v1, v2, v3;
		
		//get neighbours		
		ArrayList<Boid> neighbours = getNeighbours();
		
		//get centre of mass
		getMassCentre(neighbours);
		
		v1 = flyTowardsTheCentre();
		v2 = keepDistance();
		v3 = matchVelocity();
		
		x += vx;
		y += vy;
		
		checkBounds();
	}

	private ArrayList<Boid> getNeighbours() { // TODO
		World world = World.getInstance();
		ArrayList<Boid> neighbours = new ArrayList<Boid>();
		
		for(Boid n : world.boids)
		{
			//check if in range
			if(Math.sqrt(Math.pow(n.getX() - this.getX(), 2) + Math.pow(n.getY() - this.getY(), 2)) <= radius) // watch out for sign on y!!! Test!!!
			{
				//check if in angle
				double a1 = Math.atan(this.getVY()/this.getVX()); // watch out for 0s!!!
				double a2 = Math.atan((n.getY() - this.getY())/(n.getX() - this.getX()));				  // watch out for 0s!!!
				
				if(Math.abs(a1 - a2) <= angle)
					neighbours.add(n);				
			}
		}
		
		return neighbours;
	}
	
	private void getMassCentre(ArrayList<Boid> neighbours) { // TODO
		// TODO Auto-generated method stub
		
	}

	private double[] matchVelocity() {		//TODO
		// TODO Auto-generated method stub
		return null;
	}

	private double[] keepDistance() {	//TODO
		// TODO Auto-generated method stub
		return null;
	}

	private double[] flyTowardsTheCentre() {	//TODO
		// TODO Auto-generated method stub
		return null;
	}
	
	private void checkBounds() {
		World world = World.getInstance() ;
		if(x < 0) x = world.getBoundsX();
		if(x > world.getBoundsX()) x = 0;
		if(y < -size) y = world.getBoundsY();
		if(y > world.getBoundsY()) y = 0;		
	}

	@Override
	public void draw(Graphics g) {
		Graphics2D g2d = (Graphics2D) g.create(); //TODO Move to boid class
        g2d.setColor(Color.orange);
        
        Path2D.Double triangle = new Path2D.Double();                
        triangle.moveTo(0 + getX(), size + getY());
        triangle.lineTo((size/2) + getX(), 0 + getY());
        triangle.lineTo(size + getX(), size + getY());        
        triangle.closePath();
        
        g2d.fill(triangle);
        g2d.dispose();		
	}
}
