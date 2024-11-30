/* SERVER INSTANCE */

import javax.swing.ImageIcon;
import javax.swing.JLabel;

//Character3 is used for loggie
public class Character3 extends Frogger_Sprite implements Runnable {
	
//	private Boolean visible;
	private Boolean moving;
	private Thread t;	
	private Character1 frog;
	private JLabel frogLabel, loggieLabel;	
	
	private Character3 logArrays[];
	
	public void setCharacter1 (Character1 temp) {
		frog = temp;
	}
	
	public void setCharacter1Label(JLabel temp) {
		frogLabel = temp;
	}
	
	public void setCharacter3Label(JLabel temp) {
		loggieLabel = temp;
	}

	public Boolean getMoving() {
		return moving;
	}

	public void setMoving(Boolean moving) {
		this.moving = moving;
	}
	
	public void setLogArrays(Character3 logArrays[]) {
		this.logArrays = logArrays;
	}

	public Character3() {
		super();
		// TODO Auto-generated constructor stub
		this.moving = false;
	}

	public Character3(int x, int y, int height, int width, String image) {
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
			if (this.frogLabel != null) {
frogLabel.setIcon(new ImageIcon(
		getClass().getResource("images/" + frog.getImage()
)));} 
			t = new Thread(this, "Character3 thread");
			t.start(); //automatic call to the run method
		}
		
	}
	
	public void stopThread() {
		//will end the thread on next repeated cycle
		if (this.moving) {
			this.moving = false;
//			startButton.setText("Run");
		}
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("run triggered");
		
		// set x position for both cars and logs
		int x = this.x;
		int x2 = this.x;
//		int y = this.y; feels like no need to use y at here
		
		while (this.moving) {
			
			x += GameProperties.CHARACTER_STEP - 10;
			
			x2 -= GameProperties.CHARACTER_STEP - 10;

			
			
			if ( x >= GameProperties.SCREEN_WIDTH) {
				x = -1 * this.width;
				
			}
			if ( x2 <= -1 * this.width) {//if position goes off-screen
				x2 = GameProperties.SCREEN_WIDTH;
				
			}

			//loggieLabel is moving at here !!!!!
			if (loggieLabel != null) {
				
				if(this.y == 165 ) {
					loggieLabel.setLocation(x, this.y);
					this.setX(x); 

				} else {
					loggieLabel.setLocation(x2, this.y);
					this.setX(x2); //set new position when moving left

				}
			}
			
			//detect if on board between frog and log
			this.detectCollision();
			
			// let the X-wing(log) refill gas : )
			try {
				Thread.sleep(380);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			System.out.println("x, y: " + this.x + " " + this.y);
		}
		
		System.out.println("Thread Stopped");
		
		
	}
	
	void detectCollision() {
		for (int i = 0; i < logArrays.length; i++) {
			//use for loop to detect every single item inside our arrays
			if ( this.r.intersects( frog.getRectangle() ) ) { 
			//On board detected : )
				System.out.println("Welcome board, Master Grogu!");
			//move frog with log
				frog.setX(this.x);
				//Taking the last log's x location
				frogLabel.setLocation(frog.getX(), frog.getY());
			}
		}
	}
	
	//send Mr. Frog back to original position safely
	public void sendMrfrogBackHome() {
		
		frog.setX(600);// Grogu blink!
		frog.setY(640);// Grogu blink!
		frogLabel.setLocation(600, 640);// Grogu blink!
		frog.setImage("nobgd_grogu.png");
	frogLabel.setIcon(new ImageIcon(
			getClass().getResource("images/" + frog.getImage()
	)));
		
	}

}
