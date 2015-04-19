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
	private final double minDistance = 20;
	private final double maxVelocity = 4;
	private double vx, vy;
	
	public double getVX() { return vx; }
	public void setVX(double vx) { this.vx = vx; }
	
	public double getVY() { return vy; }
	public void setVY(double vy) { this.vy = vy; }
	
	public double getSize() { return size; }
	
	public Boid() { 
		vx = 0.5;
		vy = 0.5;
	}
	
	public void move() {		
		//Apply Boid rules
		double[] v1, v2, v3;
		
		//get neighbours		
		ArrayList<Boid> neighbours = getNeighbours();
		
		v1 = matchVelocity(neighbours); 
		v2 = keepDistance(neighbours);
		v3 = flyTowardsTheCentre(neighbours);
		
		vx = vx + v1[0] + v2[0] + v3[0];
		vy = vy + v1[1] + v2[1] + v3[1];
		
		x = x + vx;
		y = y + vy;
		
		//checkVelocity();
		checkBounds();
	}

	private ArrayList<Boid> getNeighbours() {
		World world = World.getInstance();
		ArrayList<Boid> neighbours = new ArrayList<Boid>();
		
		for(Boid n : world.boids)
		{
			//check if in range
			if(Math.sqrt(Math.pow(n.getX() - this.getX(), 2) + Math.pow(n.getY() - this.getY(), 2)) <= radius)
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

	private double[] matchVelocity(ArrayList<Boid> neighbours) {
		double[] v1 = new double[2];
		v1[0] = 0;
		v1[1] = 0;
		
		for(Boid n : neighbours) {
			v1[0] += n.getVX();
			v1[1] += n.getVY();
		}		
		
		if(neighbours.size() > 0) {
			v1[0] = v1[0] / neighbours.size();
			v1[1] = v1[1] / neighbours.size();
		}
		
		v1[0] = (v1[0] - vx) * 0.05;
		v1[1] = (v1[1] - vy) * 0.05;
		
		return v1;
	}

	private double[] keepDistance(ArrayList<Boid> neighbours) {	
		double[] v2 = new double[2];
		v2[0] = 0;
		v2[1] = 0;
		
		for(Boid n : neighbours) {
			double d = Math.sqrt(Math.pow(n.getX() - this.getX(), 2) + Math.pow(n.getY() - this.getY(), 2));
			if(d <= minDistance){
				double xDiff = n.getX() - this.getX();
				double yDiff = n.getY() - this.getY();
				
				v2[0] -= ((((xDiff * minDistance) / d) - xDiff) * 0.05);
				v2[1] -= ((((yDiff * minDistance) / d) - yDiff) * 0.05);
			}
		}
		
		return v2;
	}

	private double[] flyTowardsTheCentre(ArrayList<Boid> neighbours) {	
		double avgD = 0;
		double[] v3 = new double[2];
		v3[0] = 0;
		v3[1] = 0;
		
		for(Boid n : neighbours) {
			avgD = avgD + Math.sqrt(Math.pow(n.getX() - this.getX(), 2) + Math.pow(n.getY() - this.getY(), 2));
		}
		
		if(neighbours.size() > 0)
			avgD = avgD / neighbours.size();
		
		for(Boid n : neighbours) {
			double d = Math.sqrt(Math.pow(n.getX() - this.getX(), 2) + Math.pow(n.getY() - this.getY(), 2));
			v3[0] += ((((n.getX() - this.getX())*(d - avgD))/d) * 0.05); 
			v3[1] += ((((n.getY() - this.getY())*(d - avgD))/d) * 0.05); 
		}
		
		return v3;		
	}
	
	private void checkVelocity() {
		if(vx > maxVelocity) vx *= 0.7;
		if(vy > maxVelocity) vy *= 0.7;		
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
