package main.java.com;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Property {
	
	public final static String PROPSFILE = "application.properties";
	private static Properties props;

	public static Properties getProperties(){
        if(props == null)
        {
            props = new Properties();
            InputStream inputStream = 
            	Property.class.getClassLoader().getResourceAsStream(PROPSFILE);
            	try {
					props.load(inputStream);
				} catch (IOException e) {
					e.printStackTrace();
				}
        }
        return props;
    }
}
