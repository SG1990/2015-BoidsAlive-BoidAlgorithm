
public class Boid extends WorldObject{

	public Boid() {
		graphicEle = new GraphicElement(this);
		
		World world = World.getInstance() ;
        world.add(graphicEle) ;
        world.repaint() ;
        world.validate() ;
	}
}
