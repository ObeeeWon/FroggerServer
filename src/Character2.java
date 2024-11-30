/* SERVER INSTANCE */


//Character2 is used for car
public class Character2 extends Frogger_Sprite implements Runnable {
	
//	private Boolean visible;
	private Boolean moving;
	private Thread t;
	
	//declare the label from the main program
	//DO NOT INSTANTIATE IT!!!!!!!!!!!!!!!!! (no = new JLabel)	
	private Character1 frog;
	private int point = 0;
	public void setCharacter1 (Character1 temp) {
		frog = temp;
	}

	public Boolean getMoving() {
		return moving;
	}

	public void setMoving(Boolean moving) {
		this.moving = moving;
	}

		
//	public void setpoint(int point) {
//		this.point = point;
//	}
//	
//	public int getpoint() {
//		return point;
//	}

	public Character2() {
		super();
		// TODO Auto-generated constructor stub
		this.moving = false;
	}

	public Character2(int x, int y, int height, int width, String image) {
		super(x, y, height, width, image);
		// TODO Auto-generated constructor stub
		this.moving = false;
	}
	
	public void startThread() {
		//run will be triggered
		System.out.println("Current moving: " + this.moving);

		//if already moving, do not start again
		if ( !this.moving ) {
			this.moving = true;

			frog.setImage("nobgd_grogu.png");

			System.out.println("Starting thread");
			t = new Thread(this, "Character2 thread");
			t.start(); //automatic call to the run method
		}
		
	}
	
	public void stopThread() {
		//will end the thread on next repeated cycle
		if (this.moving) {
			this.moving = false;
		}
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		// set x position for both cars and logs
		int x = this.x;
		int x2 = this.x;
		
		while (this.moving) {
			
			x += GameProperties.CHARACTER_STEP;
			x2 -= GameProperties.CHARACTER_STEP;
			
			if ( x >= GameProperties.SCREEN_WIDTH) {
				x = -1 * this.width;
				
			}
			if ( x2 <= -1 * this.width) {//if position goes off-screen
				x2 = GameProperties.SCREEN_WIDTH;
				
			}
			
			//just make sure cars moving from opposite direction
			if (this.y == 460 ) {
				this.setX(x2); 

			} else {
				this.setX(x); 

			}
			
			//detect collisions between frog and char2
			this.detectCollision();
			
			System.out.println("x, y: " + this.x + " " + this.y);
			//cars take a break and refill some gas here
			try {
				Thread.sleep(700);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		
		System.out.println("Thread Stopped");
	}
	
	int losepoint() {
		point = point - 50;
		System.out.println("hit a car, score -50");
//	        if (scoreLabel != null) {
//	            scoreLabel.setText("Score: " + point);
//	    }	
        return point;
    }
	
	void detectCollision() {
			

	if ( this.r.intersects( frog.getRectangle() ) ) {
	//collision detected

//	this.stopThread();
	System.out.println("BOOM!");
	losepoint();
	sendMrfrogBackHome();
	
	this.setImage("nobgd_car.png");

	}
}
	
	//send Mr. Frog back to original position safely
	void sendMrfrogBackHome() {
		
		frog.setX(600);// Grogu blink! Bit me!
		frog.setY(640);// Grogu blink! Bit me!
		frog.setImage("nobgd_grogu.png");

	}
	
}
