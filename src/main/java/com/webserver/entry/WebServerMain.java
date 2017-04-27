package com.webserver.entry;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;

import com.webserver.utils.FileUtils;
import com.webserver.utils.impl.FileUtilsImpl;

/**
 * Starting class for web server. This class create ServerSocket object on configured port and accept requests. 
 * @author Ashish Gajabi
 *
 */
public class WebServerMain {
	
	private static final int SERVER_PORT = 8080;
	
	
	/**
	 * Starting point of web server
	 * @param args
	 */
	public static void main(String[] args) {		
		startWebServer();
	}

	/**
	 * Method to bind socket to port and then call method to accept request on given socket and port.
	 */
	private static void startWebServer() {
		try (final ServerSocket listener = new ServerSocket(SERVER_PORT)) {			
			acceptRequest(listener);
		}
		catch(BindException e) {
			System.out.println("SERVER IS ALREADY RUNNING");
		} catch (IOException e) {
			System.out.println("Internal Server Error: "+e.getMessage());
		}
	}

	/**
	 * Method which accepts request on particular socket and port and write response back on Socket OutputStream
	 * @param listener ServerSocket object
	 * @throws IOException
	 */
	private static void acceptRequest(final ServerSocket listener) throws IOException {
		FileUtils fileUtils = new FileUtilsImpl();
		String RequestString = null;
		int position1 = 0, position2 = 0;

		while (true) {
			try (Socket socket = listener.accept();
					InputStreamReader isr = new InputStreamReader(socket.getInputStream());
					BufferedReader in = new BufferedReader(isr);
					OutputStream outputStream = socket.getOutputStream();) {
				RequestString = in.readLine();
				if (RequestString == null) {
					continue;
				}
				position1 = RequestString.indexOf("/") + 1;
				position2 = RequestString.indexOf("HTTP") - 1;

				String pathName = RequestString.substring(position1, position2).replace('/', '\\');
				File file = new File(pathName);
				if (pathName == null || pathName.equals("") || file.isDirectory()) {
					fileUtils.Listdir(pathName, outputStream);
				} else if (file.isFile()) {
					fileUtils.ReadFile(pathName, outputStream);
				}
			}
		}
	}

}
