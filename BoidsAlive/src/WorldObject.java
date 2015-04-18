import java.awt.Graphics;
import java.awt.Point;


public abstract class WorldObject {
	 
	protected Point position = new Point();
	protected double x, y;
	
	public Point getPosition() { return position; }	
    public void setPosition(int x, int y) { position.setLocation(x, y); }
    
    public double getX() { return x; }
    public void setX(double x) { this.x = x; }
    
    public double getY() { return y; }
    public void setY(double y) { this.y = y; }
    
    public abstract void draw(Graphics g);
}
