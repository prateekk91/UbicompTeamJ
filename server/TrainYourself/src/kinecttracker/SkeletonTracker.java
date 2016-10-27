package kinecttracker;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Date;
import java.util.Timer;

import edu.ufl.digitalworlds.j4k.J4KSDK;

/**
 * 
 */

/**
 * @author Prateek
 *
 */
public class SkeletonTracker extends J4KSDK
{
	int counter = 0;
	private static long time = 1000;
	static Timer mTimer;
	
	@Override
	public void onSkeletonFrameEvent(boolean[] skeleton_tracked, float[] positions, float[] orientations,
			byte[] joint_status)
	{
		updateFeedback("Skeleton detected");
		mTimer.cancel();
		setTimer();
	}

	@Override
	public void onColorFrameEvent(byte[] color_frame)
	{
//		System.out.println("A new color frame was received.");
	}

	@Override
	public void onDepthFrameEvent(short[] depth_frame, byte[] body_index, float[] xyz, float[] uv)
	{
//		System.out.println("A new depth frame was received.");

		if (counter == 0)
			time = new Date().getTime();
		counter += 1;
	}

	// JDBC driver name and database URL
	private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	private static final String DB_URL = "jdbc:mysql://localhost/kinect";

	// Database credentials
	private static final String USER = "username";
	private static final String PASS = "password";
	private static Connection conn = null;
	private static PreparedStatement preparedStmt = null;
	

	private static void updateFeedback(String feedback) {
		try
		{
			preparedStmt.setString(1, feedback);
			preparedStmt.executeUpdate();
			
		} catch (Exception e)
		{

		}
	}
	
	private void setTimer() {
		mTimer = new java.util.Timer();
		mTimer.schedule( 
		        new java.util.TimerTask() {
		            @Override
		            public void run() {
		                updateFeedback("No skeleton Detected");
		            }
		        }, 
		        100 
		);
	}
	
	public static void main(String[] args) throws Exception
	{
		if (System.getProperty("os.arch").toLowerCase().indexOf("64") < 0)
		{
			System.out.println("WARNING: You are running a 32bit version of Java.");
			System.out.println("This may reduce significantly the performance of this application.");
			System.out.println("It is strongly adviced to exit this program and install a 64bit version of Java.\n");
		}
		try {
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			String query = "update kinectfeedback set value = ?";
			preparedStmt = conn.prepareStatement(query);
			
		}
		catch(Exception e) {}

		SkeletonTracker kinect = new SkeletonTracker();
		kinect.setTimer();
		kinect.start(J4KSDK.COLOR | J4KSDK.DEPTH | J4KSDK.SKELETON);

		try
		{
			Thread.sleep(200000000);
		} catch (InterruptedException e)
		{
		}
		conn.close();
		kinect.stop();
		System.out.println("FPS: " + kinect.counter * 1000.0 / (new Date().getTime() - kinect.time));
	}
}