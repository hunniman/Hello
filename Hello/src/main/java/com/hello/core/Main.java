package com.hello.core;

import com.hello.jetty.EmbeddedJetty;
import com.hello.jetty.JettyConfig;


/* 
 * Main class to launch embedded Jetty server
 */
public class Main {

	public static void main(String[] args) {
		
		try {
			JettyConfig jettyConfig = new JettyConfig();
			EmbeddedJetty.startServer(jettyConfig);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

	}

}
