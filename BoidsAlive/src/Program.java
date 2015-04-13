import javax.swing.JFrame;


public class Program {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		World world = World.getInstance();
		
		// Initialise and show world
		JFrame f = new JFrame();
        f.setSize(world.getBoundsX(),world.getBoundsY());
        f.setTitle("Boids Alive");
        f.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
        f.getContentPane().add(world);    
        f.setResizable(false);
        f.setVisible(true);
		
        for(int i = 0 ; i < 10; i++) 
        	world.createBoid();        
		
		while(true){
			//adjust all behaviours
			for(Boid b : world.boids){
				b.move(0, -2);
			}
			
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			world.refreshAll();
		}
	}

}
