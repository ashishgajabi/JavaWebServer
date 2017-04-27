package com.webserver.utils;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author Ashish Gajabi
 *
 */
public interface FileUtils {

	void Listdir(String directory, OutputStream pr) throws IOException;

	void ReadFile(String fileName, OutputStream outputStream) throws IOException;

}