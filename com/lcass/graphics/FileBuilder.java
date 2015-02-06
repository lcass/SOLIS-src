package com.lcass.graphics;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class FileBuilder {
	 public static String gettext(String name)
	    {
	        StringBuilder source = new StringBuilder();
	        try
	        {
	            BufferedReader reader = new BufferedReader(new FileReader(new File(name)));
	            
	            String line;
	            while ((line = reader.readLine()) != null)
	            {
	                source.append(line).append("\n");
	            }
	            
	            reader.close();
	        }
	        catch (Exception e)
	        {
	            System.err.println("Error loading source code: " + name);
	            e.printStackTrace();
	          
	        }
	        
	        return source.toString();
	    }
}
