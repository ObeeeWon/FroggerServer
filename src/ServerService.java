/* SERVER INSTANCE */
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ServerService implements Runnable {
	
	//declare but not initialize the passed variables from
	//BankServer (we need to use the originals)
	private Socket s;
	private Character1 frog;
	
	private String user_input;
	private int tempScore;
	
//	private Character2 car;//car setup
	private Character2 carArrays[];
	private Character2 carArrays2[];
	private Character2 carArrays3[];

//	private Character3 loggie;
	private Character3 logArrays[];
	private Character3 logArrays2[];
	private Character3 logArrays3[];	
	
	//variables to process our incoming socket data
	private Scanner in;
	private PrintWriter out;
	
	public ServerService() {}
	
	public ServerService(Socket s, Character1 c1, 
			String user_input, int tempScore,
			Character2[] c2, Character2[] c3, Character2[] c4,
			Character3[] c5, Character3[] c6, Character3[] c7
				) {
		this.s = s;
		this.frog = c1;
		
		this.user_input = user_input;
		this.tempScore = tempScore;
		
		this.carArrays = c2;
		this.carArrays2 = c3;
		this.carArrays3 = c4;

//		Character3 loggie;
		this.logArrays = c5;
		this.logArrays2 = c6;
		this.logArrays3 = c7;
	}
	
	@Override
	public void run() {
		
		try {
			in = new Scanner( s.getInputStream() );
			out = new PrintWriter ( s.getOutputStream() );
			processRequest();
	}	catch (Exception e) {
		e.printStackTrace();
	} finally {
		try {
			s.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
	
	private void processRequest() {
		//need a loop to process the command tokens as they are
		//parsed one at a time
		while (true) {
			if ( !in.hasNext() ) return;
			
			//extract the first token (command)
			String command = in.next();//in.next() gets String
			
			excuteCommand(command);
		}
	}
	
	private void excuteCommand(String command) {
		if ( command.equals("MOVEFROG")) {
			//MOVEFROG UP\n
			//MOVEFROG LEFT\n
			//extract the string passed through socket
			
			// UP
			String direction = in.next();
			if (direction.equals("UP")) {
				int y = frog.getY();
				y-= GameProperties.CHARACTER_STEP;
				frog.setY(y);
			}	// DOWN
			else if (direction.equals("DOWN")) {
				int y = frog.getY();
				y+= GameProperties.CHARACTER_STEP;
				frog.setY(y);
			}	// LEFT
			else if (direction.equals("LEFT")) {
				int x = frog.getX();
				x-= GameProperties.CHARACTER_STEP;
				frog.setX(x);
			}	// RIGHT
			else if (direction.equals("RIGHT")) {
				int x = frog.getX();
				x+= GameProperties.CHARACTER_STEP;
				frog.setX(x);
			}	
			out.println("FROGPOSITION" + frog.getX() + " " + frog.getY());
			out.flush();

			return;
			
		} else if (command.equals("GETSCORE")) {
			
			out.println("SCORE" + tempScore);
			out.flush();
			
			return;
			
		} else if ( command.equals("GETFROG")) {
			//open a socket to the client
			
			//FROGPOSITION + frog.getX() + + frog.getY() + \n
			//loop through and start car arrays/ log arrays moving
			System.out.println("FROGPOSITION");
			out.println("FROGPOSITION" + frog.getX() + " " + frog.getY() + "\n");

			out.flush();

			return;
			
		} else if ( command.equals("GETCARS")) {
			//open a socket to the client, send car arrays coordinates
			
			for(int i = 0; i < carArrays.length; i++) {
				out.println("CARSPOSITION" + carArrays[i].getX() + + carArrays[i].getY() + "\n");
				out.println("CARSPOSITION" + carArrays2[i].getX() + + carArrays2[i].getY() + "\n");
				out.println("CARSPOSITION" + carArrays3[i].getX() + + carArrays3[i].getY() + "\n");
			}
			out.flush();

			return;
			
		} else if ( command.equals("GETLOGS")) {
			//open a socket to the client, send log arrays coordinates
			
			for(int i = 0; i < carArrays.length; i++) {
				out.println("LOGSPOSITION" + logArrays[i].getX() + + logArrays[i].getY() + "\n");
				out.println("LOGSPOSITION" + logArrays2[i].getX() + + logArrays2[i].getY() + "\n");
				out.println("LOGSPOSITION" + logArrays3[i].getX() + + logArrays3[i].getY() + "\n");
			}
			out.flush();
			return;
			
		} else if (command.equals("GETUSERNAME")) {
		
			user_input = in.next();
			out.println("USERNAME: " + user_input);
			out.flush();
			
		} else if (command.equals("GETSCORE")) {
		
			tempScore = in.nextInt();
			out.println("SCORE: " + tempScore);
			out.flush();
			
		} else {
			
			System.out.println("Wrong command\n");
			return;
			
		}
	}
}
