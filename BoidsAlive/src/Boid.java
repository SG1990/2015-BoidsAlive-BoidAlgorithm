import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Path2D;


public class Boid extends WorldObject {
	
	public Boid() {	}
	
	public void move(int x, int y) {
		this.x += x;
		this.y += y;
		
		checkBounds();
	}

	private void checkBounds() {
		World world = World.getInstance() ;
		if(this.x < 0) x = world.getBoundsX();
		if(this.x > world.getBoundsX()) x = 0;
		if(this.y < -20) y = world.getBoundsY(); //TODO: get the actual size
		if(this.y > world.getBoundsY()) y = 0;		
	}

	@Override
	public void draw(Graphics g) {
		Graphics2D g2d = (Graphics2D) g.create(); //TODO Move to boid class
        g2d.setColor(Color.orange);
        
        Path2D.Double triangle = new Path2D.Double();
        triangle.moveTo(0 + x, 20 + y);
        triangle.lineTo(10 + x, 0 + y);
        triangle.lineTo(20 + x, 20 + y);
        triangle.closePath();
        
        g2d.fill(triangle);
        g2d.dispose();
		
	}
	
	
}
