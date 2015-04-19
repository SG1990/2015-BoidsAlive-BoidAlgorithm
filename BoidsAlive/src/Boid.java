import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Path2D;
import java.util.ArrayList;


public class Boid extends WorldObject {
	
	private final int size = 20;
	private final double radius = 50 ;
	private final double angle = 120;
	private final double distance = 20;
	private double vx, vy;
	
	public double getVX() { return vx; }
	public void setVX(double vx) { this.vx = vx; }
	public double getVY() { return vy; }
	public void setVY(double vy) { this.vy = vy; }
	
	public Boid() { 
		vx = 0.5;
		vy = 0.5;
	}
	
	public void move() {		
		//Apply Boid rules
		double[] v1, v2, v3;
		
		//get neighbours		
		ArrayList<Boid> neighbours = getNeighbours();
		
		//get centre of mass
		double[] c = getMassCentre(neighbours);
		
		v1 = flyTowardsTheCentre(c);
		v2 = keepDistance(neighbours);
		v3 = matchVelocity(neighbours);
		
		vx = 0.5 + v1[0] + v2[0] + v3[0];
		vy = 0.5 + v1[1] + v2[1] + v3[1];
		
		x = x + vx;
		y = y + vy;
		
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
	
	private double[] getMassCentre(ArrayList<Boid> neighbours) {
		double[] c = new double[2];
		c[0] = 0;
		c[1] = 0;
		
		for(Boid n : neighbours){
			c[0] += n.getX();
			c[1] += n.getY();
		}
		
		if(neighbours.size() > 0) {
			c[0] = c[0] / neighbours.size();
			c[1] = c[1] / neighbours.size();
		}
		
		return c;	
	}

	private double[] matchVelocity(ArrayList<Boid> neighbours) {		//TODO
		double[] v3 = new double[2];
		v3[0] = 0;
		v3[1] = 0;
		
		for(Boid n : neighbours) {
			if(Math.sqrt(Math.pow(n.getX() - this.getX(), 2) + Math.pow(n.getY() - this.getY(), 2)) <= distance){
				v3[0] += n.getVX();
				v3[1] += n.getVY();
			}
		}
		
		if(neighbours.size() > 0) {
			v3[0] = v3[0] / neighbours.size();
			v3[1] = v3[1] / neighbours.size();
		}
		
		v3[0] = v3[0] / 8;
		v3[1] = v3[1] / 8;
		
		return v3;
	}

	private double[] keepDistance(ArrayList<Boid> neighbours) {	//TODO
		double[] v2 = new double[2];
		v2[0] = 0;
		v2[1] = 0;
		
		for(Boid n : neighbours) {
			if(Math.sqrt(Math.pow(n.getX() - this.getX(), 2) + Math.pow(n.getY() - this.getY(), 2)) <= distance){
				v2[0] -= (n.getX() - this.getX());
				v2[1] -= (n.getY() - this.getY());
			}
		}
		
		return v2;
	}

	private double[] flyTowardsTheCentre(double[] c) {	//TODO
		double[] v1 = new double[2];
		
		v1[0] = c[0] * 0.01;
		v1[1] = c[1] * 0.01;
		
		return v1;
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
