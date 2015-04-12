
public class Boid extends WorldObject{

	public Boid() {
		graphicEle = new GraphicElement(this);
		
		World world = World.getInstance() ;
        world.add(graphicEle) ;
        world.repaint() ;
        world.validate() ;
	}
	
	public void move(int x, int y) {
		this.x += x;
		this.y += y;
		graphicEle.refreshLocation();
		
		try {
			sleep(10);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		World world = World.getInstance() ;
		world.repaint();
	}
}
