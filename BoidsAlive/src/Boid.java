import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.util.ArrayList;


public class Boid extends WorldObject {
	
	private final int size = 14;			//20
	private final double radius = 50 ;		//50
	private final double angle = 100;		//120
	private final double minDistance = 20;	//20
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
		double[] v1, v2, v3, v4;
		
		//get neighbours		
		ArrayList<Boid> neighbours = getNeighbours();
		
		v1 = matchVelocity(neighbours); 
		v2 = keepDistance(neighbours);
		v3 = flyTowardsTheCentre(neighbours);
		v4 = addNoise();
		
		vx = vx + v1[0] + v2[0] + v3[0] + v4[0];
		vy = vy + v1[1] + v2[1] + v3[1] + v4[1];
		
		x = x + vx;
		y = y + vy;
		
		checkVelocity();
		checkBounds();
	}
	
	private ArrayList<Boid> getNeighbours() {
		World world = World.getInstance();
		ArrayList<Boid> neighbours = new ArrayList<Boid>();
		
		for(Boid n : world.boids) {
			if(n != this) {
				//check if in range - if distance is less than our radius; get from the readings
				if(Math.sqrt(Math.pow(n.getX() - this.getX(), 2) + Math.pow(n.getY() - this.getY(), 2)) <= radius)	{				
					if(getAngleBetween(this.getVX(), this.getVY(), n.getX() - this.getX(), n.getY() - this.getY()) <= angle)
						neighbours.add(n);			
				}
			}			
		}
		
		return neighbours;
	}
	
	private double getAngleBetween(double x1, double y1, double x2, double y2) {		
		double vod = (x1 * x2) + (y1 * y2);
		double vxd = (Math.sqrt(Math.pow(x1, 2) + Math.pow(y1, 2))) * 
				(Math.sqrt(Math.pow(x2, 2) + Math.pow(y2, 2)));
		
		return Math.acos(vod/vxd);
	}

	private double[] matchVelocity(ArrayList<Boid> neighbours) { //the tricky one, we need to get the speeds somehow
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
		
		v1[0] = (v1[0] - vx) * 0.1;
		v1[1] = (v1[1] - vy) * 0.1;
		
		return v1;
	}

	private double[] keepDistance2(ArrayList<Boid> neighbours) {	
		double[] v2 = new double[2];
		v2[0] = 0;
		v2[1] = 0;
		
		for(Boid n : neighbours) {
			double xDiff = n.getX() - this.getX();
			double yDiff = n.getY() - this.getY();
			
			double d = Math.sqrt(Math.pow(xDiff, 2) + Math.pow(yDiff, 2));
			if(d <= minDistance){				
				// calculate angle between
				double alpha = getDirectedAngleBetween(xDiff, yDiff);
				
				//get local change from the angle
				double localDiffX, localDiffY;
				if(Math.abs(alpha) < 90) {
					double beta = 90 - Math.abs(alpha);
					localDiffX = d * Math.sin(beta);
					localDiffY = d * Math.sin(Math.abs(alpha));
				}
				else {
					double beta = Math.abs(alpha) - 90;
					localDiffX = d * Math.cos(beta);
					localDiffY = d * Math.sin(beta);
				}
				
				if(alpha < 0) localDiffX = -localDiffX;
				if(alpha > 90) localDiffY = -localDiffY;
				
				v2[0] -= (((localDiffX * minDistance) / d) - localDiffX) * (0.15 / neighbours.size());
        		v2[1] -= (((localDiffY * minDistance) / d) - localDiffY) * (0.15 / neighbours.size());
				
//				v2[0] -= (((xDiff * minDistance) / d) - xDiff) * (0.15 / neighbours.size());
//        		v2[1] -= (((yDiff * minDistance) / d) - yDiff) * (0.15 / neighbours.size());
			}
		}
		
		return v2;
	}
	
	private double getDirectedAngleBetween(double xDiff, double yDiff) {
		double alpha = getAngleBetween(this.getVX(), this.getVY(), xDiff, yDiff);
		
		//check direction
		double ad = getAngleBetween(xDiff, yDiff, 2, 0);
		double av = getAngleBetween(this.getVX(), this.getVY(), 2, 0);
		
		if(this.getVY() >= 0 && yDiff > 0) {
			if(av < ad) alpha = -alpha;
		}
		else if (this.getVY() < 0 && yDiff < 0) {
			if(av >= ad) alpha = -alpha;
		}
		else if (this.getVY() >= 0 && yDiff < 0) {
			if(av + ad >= 180) alpha = -alpha;
		}
		else {
			if(av + ad < 180) alpha = -alpha;
		}
		
		return alpha;
	}
	
	private double[] keepDistance(ArrayList<Boid> neighbours) {	
		double[] v2 = new double[2];
		v2[0] = 0;
		v2[1] = 0;
		
		for(Boid n : neighbours) {
			double xDiff = n.getX() - this.getX();
			double yDiff = n.getY() - this.getY();
			
			double d = Math.sqrt(Math.pow(xDiff, 2) + Math.pow(yDiff, 2));
			if(d <= minDistance){				
				
				
				v2[0] -= (((xDiff * minDistance) / d) - xDiff) * (0.15 / neighbours.size());
        		v2[1] -= (((yDiff * minDistance) / d) - yDiff) * (0.15 / neighbours.size());
			}
		}
		
		return v2;
	}
	
	private double getRotationAngle() {
		//get angle
		double avy = getAngleBetween(this.getVX(), this.getVY(), 0, 2);
		
		//get its direction
		if(this.getVX() >= 0) avy = -avy;
		
		return avy;
	}
	
	private void systemToLocal(Boid n) {
		double a = getRotationAngle();
		
		double xDiff = n.getX() - this.getX();
		double yDiff = n.getY() - this.getY();
		
		//rotate
		double rVX = (this.getVX() * Math.cos(a)) - (this.getVY() * Math.sin(a));
		double rVY = (this.getVX() * Math.sin(a)) - (this.getVY() * Math.cos(a));	
		double rDX = (xDiff * Math.cos(a)) - (yDiff * Math.sin(a));
		double rDY = (xDiff * Math.sin(a)) - (yDiff * Math.cos(a));
		
		//translate
		rDX -= this.getX();
		rDY -= this.getY();
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
			double xDiff = n.getX() - this.getX();
			double yDiff = n.getY() - this.getY();
			
			if(Math.abs(xDiff) > minDistance) {
				double d = Math.sqrt(Math.pow(xDiff, 2) + Math.pow(yDiff, 2));
				
				v3[0] += ((xDiff * (d - avgD))/d) * (0.15 / neighbours.size()); 
				v3[1] += ((yDiff * (d - avgD))/d) * (0.15 / neighbours.size());
			}
			 
		}
		
		return v3;		
	}
	
	private double[] addNoise() {
		double[] v4 = new double[2];
		v4[0] = 0;
		v4[1] = 0;
		
		v4[0] = ((Math.random() - 0.5) * maxVelocity) * 0.1;
		v4[1] = ((Math.random() - 0.5) * maxVelocity) * 0.1;
		
		return v4;
	}
	
	private void checkVelocity() {
		if (Math.sqrt(Math.pow(this.getVX(), 2) + Math.pow(this.getVY (), 2)) > maxVelocity){
	    	vx *= 0.75;
			vy *= 0.75;			
		}
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
        
        AffineTransform oldTransform = g2d.getTransform();
        g2d.translate(getX(), getY());
        
        Path2D.Double triangle = new Path2D.Double();  
        triangle.moveTo(-(size/2), size);
        triangle.lineTo(0, 0);
        triangle.lineTo((size/2), size);  
        triangle.closePath();        
        
        //rotate
        if (vx < 0){
     	      g2d.rotate(Math.toRadians(-90.0 + Math.atan(vy/vx)*180.0/Math.PI));
        }
        else {
             g2d.rotate(Math.toRadians(90.0 + Math.atan(vy/vx)*180.0/Math.PI));
        }
        
        g2d.fill(triangle);        
        g2d.setTransform(oldTransform);        
        g2d.dispose();		
	}
}
