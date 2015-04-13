
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
		
		checkBounds();		
		try {
			sleep(15);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//graphicEle.refreshLocation();
	}

	private void checkBounds() {
		World world = World.getInstance() ;
		if(this.x < 0) x = world.getBoundsX();
		if(this.x > world.getBoundsX()) x = 0;
		if(this.y < -20) y = world.getBoundsY(); //TODO: get the actual size
		if(this.y > world.getBoundsY()) y = 0;		
	}
}
