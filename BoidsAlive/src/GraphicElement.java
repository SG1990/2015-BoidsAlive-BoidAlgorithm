import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;


public class GraphicElement extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	private BufferedImage image;
    private WorldObject owner;
    
    public GraphicElement(WorldObject obj) {        
    	owner = obj ;
        //setPreferredSize(new Dimension(30, 30));
        setOpaque(false);
    }
    
    public void setIcon(String path) {        
    	try {    		
            image = ImageIO.read(new File(path));
        } catch (IOException ex) {        	
            //log error?
        }
    }
    
    protected void paintComponent(Graphics g) {    	
        super.paintComponent(g);
        g.drawImage(image, 0, 0, null);
    }
    
    protected void refreshLocation() {    	
        setLocation(owner.getX(), owner.getY());
    }
}
