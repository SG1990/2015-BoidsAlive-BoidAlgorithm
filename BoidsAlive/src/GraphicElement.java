import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

public class GraphicElement extends JPanel {
	
	private static final long serialVersionUID = 1L;	
    private WorldObject owner;
    
    public GraphicElement(WorldObject obj) {        
    	owner = obj ;
        setPreferredSize(new Dimension(30, 30));
        setOpaque(false);
    }
    
    protected void paintComponent(Graphics g) {    	
        super.paintComponent(g);
        g.fillRect(0, 0, 30, 30);
        g.drawRect(0, 0, 30, 30);
    }
    
    protected void refreshLocation() {    	
        setLocation(owner.getX(), owner.getY());
    }
}
