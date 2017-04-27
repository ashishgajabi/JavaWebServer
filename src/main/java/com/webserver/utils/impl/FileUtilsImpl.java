package com.webserver.utils.impl;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.Date;

import com.webserver.utils.Constant;
import com.webserver.utils.FileUtils;

/**
 * @author Ashish Gajabi 
 * This class is implementation of interface 'FileUtils'
 * provides listing of directories/files. Also read individual file to
 * display it on browser.
 */
public class FileUtilsImpl implements FileUtils {

	@Override
	public void Listdir(String directory, OutputStream pr) throws IOException {
		
		boolean isRootDirectory = false;
		StringBuilder httpResponse = new StringBuilder();
		String directoryName = getOnlyDirectoryName(directory);
		
		if (directory == null || directory.equals("")) {
			directory = Constant.ROOT_DIRECTORY;
			isRootDirectory = true;
		}
		File DirFile = new File(directory);
		httpResponse.append("HTTP/1.1 200 OK\r\n\r\n");

		String[] fileList = DirFile.list();
		httpResponse.append("<html><head></head><body><hr>");
		httpResponse.append("<Table border=0>");
		addHeaderRow(httpResponse);
		addBlankRow(httpResponse);
		if (!isRootDirectory) {
			addRowForParent(httpResponse, DirFile);
		}
		for (String currentName : fileList) {
			writeMainBody(directory, httpResponse, directoryName, currentName);
		}
		httpResponse.append("</Table><hr>" + "</body></html>");
		pr.write(httpResponse.toString().getBytes("UTF-8"));
	}
	

	/**
	 * Method to append main body of table with directory/file listing to StringBuilder httpResponse
	 * @param directory
	 * @param httpResponse
	 * @param directoryName
	 * @param currentName
	 */
	private void writeMainBody(String directory, StringBuilder httpResponse, String directoryName, String currentName) {
		httpResponse.append("<TR>");
		if (new File(directory + Constant.FILE_SEPERATOR + currentName).isDirectory()) {
			writeDirectoryList(directory, httpResponse, directoryName, currentName);
		} else {
			writeFileList(directory, httpResponse, directoryName, currentName);
		}
		httpResponse.append("</TR>");
	}

	/**
	 * Method to append file listing on StringBuilder httpResponse
	 * 
	 * @param directory
	 * @param httpResponse
	 * @param directoryName
	 * @param currentName
	 */
	private void writeFileList(String directory, StringBuilder httpResponse, String directoryName, String currentName) {
		writeNameModifiedDate(directory, httpResponse, directoryName, currentName);
		httpResponse.append("<TD align='center' width='100'>"
				+ String.valueOf(new File(directory + Constant.FILE_SEPERATOR + currentName).length()) + " Bytes</TD>");
	}

	/**
	 * Method to append directory listing on StringBuilder httpResponse
	 * 
	 * @param directory
	 * @param httpResponse
	 * @param directoryName
	 * @param currentName
	 */
	private void writeDirectoryList(String directory, StringBuilder httpResponse, String directoryName,
			String currentName) {
		writeNameModifiedDate(directory, httpResponse, directoryName, currentName);
		httpResponse.append("<TD align='center' width='100'>&lt;DIR&gt;</TD>");
	}
	

	/**
	 * Method to append file/directory name and last modified date to
	 * StringBuilder httpResponse
	 * 
	 * @param directory
	 * @param httpResponse
	 * @param directoryName
	 * @param currentName
	 */
	private void writeNameModifiedDate(String directory, StringBuilder httpResponse, String directoryName,
			String currentName) {
		httpResponse
				.append("<TD><a href='" + directoryName + '/' + currentName + "'>" + currentName + "</a>" + "</TD>");
		httpResponse.append("<TD  align='center' width='300'>" + getLastModifiedDate(directory, currentName) + "</TD>");
	}
	

	/**
	 * Method to get last modified date of file/directory
	 * 
	 * @param directory
	 * @param currentName
	 * @return
	 */
	private String getLastModifiedDate(String directory, String currentName) {
		return new Date(new File(directory + Constant.FILE_SEPERATOR + currentName).lastModified()).toString();
	}

	/**
	 * Method to append header row to StringBuilder httpResponse
	 * 
	 * @param httpResponse
	 * @throws IOException
	 */
	private static void addHeaderRow(StringBuilder httpResponse) throws IOException {
		httpResponse.append("<TR>").append("<TD>Name</TD>").append("<TD align='center'>Last Modified</TD>")
				.append("<TD align='center' width='100'>Size</TD>").append("</TR>");
	}
	

	/**
	 * Method to append blank row to StringBuilder httpResponse
	 * 
	 * @param httpResponse
	 * @throws IOException
	 */
	private void addBlankRow(StringBuilder httpResponse) throws IOException {
		httpResponse.append("<TR>").append("<TD></TD>").append("<TD></TD>").append("<TD></TD>").append("</TR>");
	}
	

	/**
	 * Method to append parent row to StringBuilder httpResponse. This is to
	 * navigate back to parent directory
	 * 
	 * @param dirFile
	 * @throws IOException
	 */
	private void addRowForParent(StringBuilder httpResponse, File dirFile) throws IOException {
		String parentPath = dirFile.getParent() == null ? "" : dirFile.getParent();
		httpResponse.append("<TR>").append("<TD><a href='/" + parentPath + "'>" + ".." + "</a>" + "</TD>")
				.append("<TD></TD>").append("<TD></TD>").append("</TR>");
	}
	

	/**
	 * Method to return directory name from complete path name
	 * 
	 * @param directory
	 * @return
	 */
	private String getOnlyDirectoryName(String directory) {
		if (directory == null || directory.equals("")) {
			return "";
		}
		return new File(directory).getName();
	}
	

	/**
	 * Method to read given file and write to OutputStream
	 * 
	 * @param String
	 * @param OutputStream
	 * @throws IOException
	 */
	@Override
	public void ReadFile(String fileName, OutputStream outputStream) throws IOException {
		writeSuccessResponse(outputStream, new File(fileName));
		Files.copy(new File(fileName).toPath(), outputStream);
		outputStream.flush();
	}
	

	/**
	 * Method to write success response headers to OutputStream
	 * 
	 * @param outputStream
	 * @param FILE_LENGTH
	 * @throws IOException
	 */
	private void writeSuccessResponse(OutputStream outputStream, File FILE_LENGTH) throws IOException {
		StringBuilder sb = new StringBuilder();
		sb.append("HTTP/1.1 200 OK\r\n").append("Date: " + new Date().toString() + "\r\n")
				.append("Accept-Ranges: bytes\r\n")
				.append("content-length: " + String.valueOf(FILE_LENGTH.length()) + "\r\n")
				.append("Content-Type: " + Files.probeContentType(FILE_LENGTH.toPath()) + "\r\n").append("\r\n");
		outputStream.write(sb.toString().getBytes());
		outputStream.flush();
	}
}
