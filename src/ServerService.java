/* SERVER INSTANCE */
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Scanner;

public class ServerService implements Runnable {
	final static int CLIENT_PORT = 5656;

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
			String user_input, int tempScore
//			Character2[] c2, Character2[] c3, Character2[] c4,
//			Character3[] c5, Character3[] c6, Character3[] c7
				) {
		this.s = s;
		this.frog = c1;
		
		this.user_input = user_input;
		this.tempScore = tempScore;
		
//		this.carArrays = c2;
//		this.carArrays2 = c3;
//		this.carArrays3 = c4;
//
////		Character3 loggie;
//		this.logArrays = c5;
//		this.logArrays2 = c6;
//		this.logArrays3 = c7;
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
	
	public void processRequest () throws IOException {
		//if next request is empty then return
		while(true) {
			if(!in.hasNext( )){
				return;
			}
			String command = in.next();
			if (command.equals("Quit")) {
				return;
			} else {
				executeCommand(command);
			}
		}
	}
	
	static void updateDB(String user_input, int tempScore) {
		
		//update data
		Connection conn = null;
		
		try {
		//load the database driver
		Class.forName("org.sqlite.JDBC");
		System.out.print("Frogger DB Loaded");
		
		//create connection string and connect to database
		String dbURL = "jdbc:sqlite:froggerdb.db";
		conn = DriverManager.getConnection(dbURL);
		
		if (conn != null) {
			System.out.println("connected to database");
			
			//show meta data for database
			DatabaseMetaData db = (DatabaseMetaData) conn.getMetaData();
			System.out.println("Driver Name: " + db.getDriverName());
			System.out.println("Driver Version: " + db.getDriverVersion());
			System.out.println("Product Name: " + db.getDatabaseProductName());
			System.out.println("Product Version: " + db.getDatabaseProductVersion());
		
		   String sqlUpdate = "UPDATE SCORE_RECORDS SET SCORE = ? WHERE NAME = ?";
		   try (PreparedStatement pstmtUpdate = conn.prepareStatement(sqlUpdate)) {
		
		   	pstmtUpdate.setInt(1, tempScore);
		   	pstmtUpdate.setString(2, user_input);
		   	pstmtUpdate.executeUpdate();
		   }
		}
		conn.close();			
				
		} catch (Exception e) {
		e.printStackTrace();
		
		}

}

	
	private void executeCommand(String command) throws IOException{
		if ( command.equals("MOVEFROG")) {
			//MOVEFROG UP\n
			//MOVEFROG LEFT\n
			//extract the string passed through socket
			
			String direction = in.next();
			int x = frog.getX();
			int y = frog.getY();

			// UP
			if (direction.equals("UP")) {
				y-= GameProperties.CHARACTER_STEP;
			}	// DOWN
			else if (direction.equals("DOWN")) {
				y+= GameProperties.CHARACTER_STEP;
			}	// LEFT
			else if (direction.equals("LEFT")) {
				x-= GameProperties.CHARACTER_STEP;
			}	// RIGHT
			else if (direction.equals("RIGHT")) {
				x+= GameProperties.CHARACTER_STEP;
			}	
			
			frog.setX(x);
			frog.setY(y);
			out.println("FROGPOSITION" + frog.getX() + " " + frog.getY());
			
				
			//send a response
			Socket s2 = new Socket("localhost", CLIENT_PORT);
			
			//Initialize data stream to send data out
			OutputStream outstream = s2.getOutputStream();
			PrintWriter out = new PrintWriter(outstream);

			String commandOut = "FROGPOSITION: " + x + " " + y + "\n";
			System.out.println("Sending: " + commandOut);
			out.println(commandOut);
			out.flush();
				
			s2.close();

			return;
			
		} else if (command.equals("GETSCORE")) {
			
			out.println("SCORE" + tempScore);
			out.flush();
			
			updateDB(user_input, tempScore);

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
