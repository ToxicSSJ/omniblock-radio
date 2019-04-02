package net.omniblock.radio.protocol.manager.console.drawer;

import java.io.IOException;
import java.io.InputStream;

public class DrawLoader {

	private String name;
	
	public DrawLoader(String name) {
		
		this.name = name;
		
	}
	
	public InputStream getDraw() throws IOException {
		
	    InputStream file = getResource("/net/omniblock/resources/draws/" + name);
	    return file;
	    
	}
	
    private InputStream getResource(String path) throws IOException {
    	
    	InputStream file = this.getClass().getResourceAsStream(path);
        return file;
        
    }

	public String getName() {
		return name;
	}
	
}
