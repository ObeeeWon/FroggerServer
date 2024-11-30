/* SERVER INSTANCE */

import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.io.IOException;
//import java.sql.ResultSet;


public class Frogger_GamePrepServer {
		
	public static void main(String[] args) throws IOException{
		//declare copies of our character
//		Character1 bgd;
		Character1 frog;
//		private Character2 car;//car setup
		Character2 carArrays[] = new Character2 [4] ;
		Character2 carArrays2[] = new Character2 [4] ;
		Character2 carArrays3[] = new Character2 [4] ;
//		private Character3 loggie;
		Character3 logArrays[] = new Character3 [4] ;
		Character3 logArrays2[] = new Character3 [4] ;
		Character3 logArrays3[] = new Character3 [4] ;
				
		String user_input = "";	
		UpdatePoint updPoint;
		int tempScore = 0;
		
		
//		Frogger_GamePrepServer myGame = new Frogger_GamePrepServer();
		frog = new Character1(100, 200, 51, 55, "nobgd_grogu.png");
//		bgd = new Character1(0, 0, 1551, 700, "bgd_fullscreen_1.png");
		
		//set scoreJLabel after collision from character2
		//update points for db
		updPoint = new UpdatePoint();
		updPoint.setPoint(0);
		
		//frog setup
		frog.setX(600);
		frog.setY(620);
		frog.setWidth(51);
		frog.setHeight(55);
		frog.setImage("nobgd_grogu.png");

		//car loop 1st lane
		int carCount = 4;
		//carArrays = new Character2[carCount];
		
		//start a loop to initiate more than one car in the same lane
		//https://stackoverflow.com/questions/44339372/print-to-java-gui-with-for-loop
		for(int i=0; i < carCount; i++) {
			if (carArrays[i] == null) {//just in case it's null 
				
				carArrays[i] = new Character2(0 + (i * 650), 390, 68, 121, "nobgd_car.png");
				carArrays[i].setCharacter1(frog);
				
			}
		}
		
		//car loop 2nd lane
		int carCount2 = 4;
		//carArrays2 = new Character2[carCount2];
		
		//start a loop to initiate more than one car in the same lane
		for(int i=0; i < carCount2; i++) {
			if (carArrays2[i] == null) {//just in case it's null again
				
				carArrays2[i] = new Character2(350 + (i * 550), 460, 68, 121, "nobgd_car.png");
				carArrays2[i].setCharacter1(frog);
			}
		}
			
		//car loop 3rd lane
		int carCount3 = 4;
		//carArrays3 = new Character2[carCount3];
		
		//start a loop to initiate more than one car in the same lane
		for(int i=0; i < carCount3; i++) {
			if (carArrays3[i] == null) {//just in case it's null again
				
				carArrays3[i] = new Character2(0 + (i * 550), 540, 68, 121, "nobgd_car.png");				
				carArrays3[i].setCharacter1(frog);
			}
		}
		
		
		//log loop 1st lane
		int logCount = 4;
		//logArrays = new Character3[logCount];
		
		//start a loop to initiate more logs
		for(int i=0; i < logCount; i++) {
			if (logArrays[i] == null) {//just in case it's null again
				
				logArrays[i] = new Character3(0 + (i * 350), 85, 54, 68, "nobg_x-wing.png");
				logArrays[i].setCharacter1(frog);
				
				//drag the log labels collision inside loop
				logArrays[i].setLogArrays(logArrays);
				
			}
		}
		
		//log loop 2nd lane
		int logCount2 = 4;
		//logArrays2 = new Character3[logCount2];
		
		//start a loop to initiate more logs
		for(int i=0; i < logCount2; i++) {
			if (logArrays2[i] == null) {//just in case it's null again
				
				logArrays2[i] = new Character3(300 + (i * 300), 165, 54, 68, "nobg_x-wing.png");
				logArrays2[i].setCharacter1(frog);
				
				//drag the log labels collision inside loop
				logArrays2[i].setLogArrays(logArrays2);
			}
		}
		
		//log loop 3rd lane
		int logCount3 = 4;
		//logArrays3 = new Character3[logCount3];
		
		//start a loop to initiate more logs
		for(int i=0; i < logCount3; i++) {
			if (logArrays3[i] == null) {//just in case it's null again
				
				logArrays3[i] = new Character3(0 + (i * 350), 250, 54, 68, "nobg_x-wing.png");
				logArrays3[i].setCharacter1(frog);
				
				//drag the log labels collision inside loop
				logArrays3[i].setLogArrays(logArrays3);
			}
		}
			
		//server commands
		final int SOCKET_PORT = 5556;
		
		Thread t1 = new Thread ( new Runnable () {
			public void run ( ) {
				synchronized(this) {

					ServerSocket server;
					try {
						
						server = new ServerSocket(SOCKET_PORT);
						System.out.println("Waiting for clients to connect...");
						while(true) {
							Socket s = server.accept();
							System.out.println("client connected");
							
							ServerService myService = new ServerService (s, frog, user_input, tempScore, carArrays, carArrays2, carArrays3, logArrays, logArrays2, logArrays3);
							Thread t2 = new Thread(myService);
							t2.start();
						}
						
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
		
				}
			}
		});
		t1.start( );
		
		KeepMoving(carArrays, carArrays2, carArrays3,
				logArrays, logArrays2, logArrays3);
		
		DetectCollision(carArrays, carArrays2, carArrays3,
				logArrays, logArrays2, logArrays3);
		
		ConnectandSavetoDB(user_input, tempScore);
		
		updateDB(user_input, tempScore, frog, 
				logArrays, logArrays2, logArrays3, 
				updPoint);

	}
	
	static String ConnectandSavetoDB(String user_input, int tempScore) {
		//declare a connection and sql statement to execute
		Connection conn = null;
//		String user_input = "";
//		int tempScore = 0;
		
		if(user_input == "" || user_input == null) {
			System.out.println("use a default name when nothing input");
			user_input = "Grogu";
		}
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
				
				//create table using prepared statement
                String sqlCreateTable = "CREATE TABLE IF NOT EXISTS SCORE_RECORDS " +
                        "(ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        " NAME TEXT NOT NULL, " +
                        " SCORE INT NOT NULL )" ;

                try (PreparedStatement pstmtCreateTable = conn.prepareStatement(sqlCreateTable)) {
                	pstmtCreateTable.executeUpdate();
                	System.out.println("Table Successfully Created");
                }
                
				//insert data using a prepared statement
                String sqlInsert = "INSERT INTO SCORE_RECORDS (NAME, SCORE ) VALUES (?, ?)";
                try (PreparedStatement pstmtInsert = conn.prepareStatement(sqlInsert)) {

                	//execute calls to prepared statement
                	pstmtInsert.setString(1, user_input);
                	pstmtInsert.setInt(2, tempScore);
                	pstmtInsert.executeUpdate();
                	System.out.println("record inserted");
                	
                }
			}
			
			//close connection 
			conn.close();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return user_input;
	}
	
	static void updateDB(String user_input, int tempScore,
						 Character1 frog, 
			Character3 logArrays[], Character3 logArrays2[], Character3 logArrays3[],
					 UpdatePoint updPoint) {

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
		
		//same as destination detection, use .getY() to detect boarder
		// if frog drop into water
		detectAliveOnWater(frog, logArrays, logArrays2, logArrays3,
						tempScore, updPoint);
		detectDestination(frog, tempScore, updPoint);
	}


	static int winpoint(int tempScore, UpdatePoint updPoint) {
		tempScore = updPoint.getPoint() + 50;
		updPoint.setPoint(tempScore);
//		updateDB(user_input);
		return tempScore;
	}
	
	static int losepoint(int tempScore, UpdatePoint updPoint) {		
		tempScore = updPoint.getPoint() - 50;
		updPoint.setPoint(tempScore);
//		updateDB(user_input);
		return tempScore;

	}
	
	
	static void detectDestination(Character1 frog, int tempScore, UpdatePoint updPoint) {
		if (frog.getY() <= 40) {
			System.out.println("Welcome home, Master Grogu!");
			winpoint(tempScore, updPoint); // we won! add 50 points
			sendMrfrogBackHome(frog);
		}
	}
	
	//send Mr. Frog back to original position safely
	public static void sendMrfrogBackHome(Character1 frog) {
		frog.setX(600);// Grogu blink!
		frog.setY(640);// Grogu blink!
		
	}
	public static void KeepMoving(
			Character2 carArrays[], Character2 carArrays2[], Character2 carArrays3[],
			Character3 logArrays[], Character3 logArrays2[], Character3 logArrays3[]) {


		for (int i = 0; i < carArrays.length; i++) {
			if (carArrays[i] != null) {
				
				carArrays[i].startThread();
				carArrays2[i].startThread();
				carArrays3[i].startThread();
//				System.out.println("car array is moving...");
//				System.out.println("Car no." + i + " is moving.");
			}
		}
		
		
		for (int i = 0; i < logArrays.length; i++) {
			if (logArrays[i] != null) {
				
				logArrays[i].startThread();
				logArrays2[i].startThread();
				logArrays3[i].startThread();
//				System.out.println("log array is moving...");
//				System.out.println("Log no." + i + " is moving.");
			}
		}
	}
	
	public static void DetectCollision(
			Character2 carArrays[], Character2 carArrays2[], Character2 carArrays3[],
			Character3 logArrays[], Character3 logArrays2[], Character3 logArrays3[]	) {
		
//		car.detectCollision();
//		loggie.detectCollision();
		for (int i = 0; i < carArrays.length; i++) {
			if (carArrays[i] != null) {
				carArrays[i].detectCollision();
			}
		}
		
		for (int i = 0; i < logArrays.length; i++) {
			if (logArrays[i] != null) {
				logArrays[i].detectCollision();
			}
		}
	}
	public void DetectDestination(Character1 frog, int tempScore, UpdatePoint updPoint) {
		detectDestination(frog, tempScore, updPoint);
	}
		


	 //same as destination detection, use .getY() to detect boarder
	// if frog drop into water
	static void detectAliveOnWater(Character1 frog, 
			Character3 logArrays[], Character3 logArrays2[], Character3 logArrays3[],
			int tempScore, UpdatePoint updPoint) {

		boolean Alive = false;
		
		if (frog.getY() >= 40 && frog.getY() <= 200) {
			for(int i = 0; i < logArrays.length; i++ ) {
				//if the frog is not standing on any of the logs 
				if(logArrays[i].r.intersects( frog.getRectangle() ) == false ||
				   logArrays2[i].r.intersects( frog.getRectangle() ) == false ||
				   logArrays3[i].r.intersects( frog.getRectangle() ) == false
					){
					Alive = true;
				} else {
					Alive = false;
					break;
				}
			}
		}
		if (Alive == false) {
			losepoint(tempScore, updPoint);
//			sendMrfrogBackHome();
		}
	}


}
