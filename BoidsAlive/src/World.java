import java.util.ArrayList;


public final class World {

	public ArrayList<Boid> boids = new ArrayList<Boid>();
	public ArrayList<Obstacle> obstacles = new ArrayList<Obstacle>();
	
	private static World instance = null;	
	private World() {}
	
	public static World getInstance(){
		if(instance == null)
			instance = new World();
		
		return instance;
	}
	
	public void createBoid(){
		
	}
}
