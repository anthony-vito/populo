package populo;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import populo.controller.HomeController;
import populo.controller.Controller;
import populo.controller.LoginController;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * The Application class will include any application level configuration and code.
 * It will also handle the database for now. As the application grows these concerns
 * will be refactored away from this class.
 *
 * @author amv
 */
public class Application {

	private static final Logger log = LoggerFactory.getLogger(Application.class);

	static final Properties properties;

	static {
		properties = new Properties();
		InputStream appPropStream = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream("app.properties");
		if (appPropStream == null) {
			String errMsg = "'app.properties' MUST be on classpath. Runtime Failure";
			log.error(errMsg);
			throw new RuntimeException(errMsg);
		}
		try {
			properties.load(appPropStream);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}


	/**
	 * A hand coded path to controller class mapping.
	 * Keeping it simple.
	 *
	 * @param request
	 * @return the controller, or null if nothing is mapped.
	 */
	public static Controller resolveControllerForRequest(HttpServletRequest request) {

		// The context path could be anything depending on how the application is installed,
		// so we'll ignore it with a simple substring. This leaves us with the path we need
		// to select the controller.
		String path = request.getRequestURI().substring(request.getContextPath().length());

		if(path.equalsIgnoreCase("/")) { // matches welcome-file 'index.html'
			return new HomeController();
		} else if(path.equalsIgnoreCase("/login")) {
			return new LoginController();
		}

		return null;
	}

	/*
	 * Handle the Database setup
	 */
	static final DataSource dataSource;
	static {
		try {
			initDatabase();
			dataSource = createDataSource();
		} catch(Exception e) {
			log.error("Exception On Database Initialization.", e);
			throw new RuntimeException(e);
		}
	}

	/*
	 * We'll require the db_file and the db_name to be set in the properties file.
	 */
	private static void initDatabase() throws Exception {

		String dbFile = properties.getProperty("db_file");
		String dbName = properties.getProperty("db_name");

		if( dbFile == null || dbFile.length() == 0 ) {
			String errMsg = "'db_file' property required in app.properties. Runtime Failure.";
			log.error(errMsg);
			throw new RuntimeException(errMsg);
		}
		if( dbName == null || dbName.length() == 0 ) {
			String errMsg = "'db_name' property required in app.properties. Runtime Failure.";
			log.error(errMsg);
			throw new RuntimeException(errMsg);
		}

		// Standard JDBC url start for an H2 database.
		String baseUrl = "jdbc:h2:file:" + dbFile;
		Connection conn;
		try {
			// Attempt to simply connect. The IFEXISTS=true causes an exception
			// to be thrown if the database and schema do not yet exist. Below
			// we'll catch that exception, and attempt to create and initialize
			// the database.
			String url = baseUrl + ";SCHEMA="+dbName+";IFEXISTS=TRUE;AUTO_SERVER=TRUE";
			conn = DriverManager.getConnection(url);
		} catch(SQLException sqle) {
			log.debug("Exception on IFEXISTS getConnection. Trying to Create....");
			// Write the resource to a temp file. We could be inside of a war file
			// and thus a file path would be meaningless to the 'runscript' command
			// that H2 uses to run SQL on start up.
			InputStream sqlStream = Thread.currentThread().getContextClassLoader()
					.getResourceAsStream("schema/schema.sql");
			File tempFile = File.createTempFile("init_populo", "sql");
			FileUtils.copyInputStreamToFile(sqlStream, tempFile);
			// Construct a JDBC url that creates the schema, uses it,
			// then runs the SQL file.
			String url = baseUrl + ";" +
					"INIT=" +
					"create schema if not exists "+dbName+"\\;" +
					"set schema "+dbName+"\\;" +
					"runscript from '"+tempFile.getAbsolutePath()+"';" +
					"AUTO_SERVER=TRUE";
			try {
				// Attempt to do the full initialization.
				conn = DriverManager.getConnection(url);
			} catch (SQLException e) {
				// If we get an exception now we are screwed. log it and bail.
				String errMsg = "Exception Creating and Initializing H2 Database. Runtime Failure.";
				log.error(errMsg, e);
				throw new RuntimeException(errMsg, e);
			}
		}
		if(conn != null) {
			try {
				log.debug("Connection Success!!"+conn.getCatalog());
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	private static DataSource createDataSource() {
		// TODO: Setup a pooling Datasource
		return null;
	}
}
