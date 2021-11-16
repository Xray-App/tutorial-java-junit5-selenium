package com.idera.xray.tutorials;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.nio.file.Path;

public class RepositoryParser
{

	private FileInputStream stream;
	private String RepositoryFile;
	private Properties propertyFile = new Properties();

	public RepositoryParser(String fileName)
	{
        try {
            this.RepositoryFile = fileName;
		    stream = new FileInputStream(RepositoryFile);
		    propertyFile.load(stream);    
        } catch (IOException ex) {
            System.out.println(ex);
        }
		
	}

    public String getBy(String locatorName){
        return propertyFile.getProperty(locatorName);
    }

}
