import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Path2D;

import javax.swing.JPanel;

public class GraphicElement extends JPanel {
	
	private static final long serialVersionUID = 1L;	
    private WorldObject owner;
    
    public GraphicElement(WorldObject obj) {        
    	owner = obj ;
        setPreferredSize(new Dimension(20, 20));
        setOpaque(false);
    }
    
    protected void paintComponent(Graphics g) {    	
        super.paintComponent(g);        
        Graphics2D g2d = (Graphics2D) g.create(); //TODO Move to boid class
        g2d.setColor(Color.orange);
        
        Path2D.Double triangle = new Path2D.Double();
        triangle.moveTo(0, 20);
        triangle.lineTo(10, 0);
        triangle.lineTo(20, 20);
        triangle.closePath();
        
        g2d.fill(triangle);
        g2d.dispose();
    }
    
    protected void refreshLocation() {    	
        setLocation(owner.getX(), owner.getY());
    }
}
