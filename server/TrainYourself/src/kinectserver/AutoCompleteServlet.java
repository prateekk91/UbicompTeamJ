package kinectserver;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class AutoCompleteServlet
 */
@WebServlet(name = "AutoCompleteServlet", urlPatterns = { "/autocomplete" })
public class AutoCompleteServlet extends HttpServlet
{
	private static final long serialVersionUID = 1L;
	private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	private static final String DB_URL = "jdbc:mysql://localhost/kinect";
	private static final String USER = "username";
	private static final String PASS = "password";

	@Override
	public void init(ServletConfig config) throws ServletException
	{
		super.init(config);
	}

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AutoCompleteServlet()
	{
		super();
		// TODO Auto-generated constructor stub
		// Create the event notifier and pass ourself to it.
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		response.setContentType("text/xml");
		response.setHeader("Cache-Control", "no-cache");
		String line = "";
		Connection connection = null;
		String query = "";
		Statement statement = null;
		try
		{
			Class.forName(JDBC_DRIVER);
			connection = DriverManager.getConnection(DB_URL, USER, PASS);
			query = "SELECT * FROM kinectfeedback";
			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(query);
			if (rs.next())
			{
				line = Integer.toString(rs.getInt(1));
				System.out.println("Line=" + line);
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		response.getWriter().write("<feedback>" + line + "</feedback>");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		// TODO Auto-generated method stub
	}
}
