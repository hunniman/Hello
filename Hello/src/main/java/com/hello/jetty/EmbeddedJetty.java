package com.hello.jetty;


import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jetty.annotations.AnnotationConfiguration;
import org.eclipse.jetty.annotations.ClassInheritanceHandler;
import org.eclipse.jetty.annotations.ServletContainerInitializersStarter;
import org.eclipse.jetty.apache.jsp.JettyJasperInitializer;
import org.eclipse.jetty.jsp.JettyJspServlet;
import org.eclipse.jetty.plus.annotation.ContainerInitializer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.ConcurrentHashSet;
import org.eclipse.jetty.webapp.Configuration;
import org.eclipse.jetty.webapp.WebAppContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.WebApplicationInitializer;

import com.hello.spring.SpringMvcInitializer;

/* 
 * The embedded Jetty Server Java configuration
 */
public class EmbeddedJetty {
	
	public static void startServer(JettyConfig jettyConfig) throws Exception {
		
		if(!jettyConfig.isQuietMode())
		{
			System.out.println();
			System.out.println("Starting embedded jetty...");
		}
		
		//create the server
		Server server = new Server();
		ServerConnector c = new ServerConnector(server);
		
		//set the context path
	     WebAppContext webAppContext = new WebAppContext();
	     webAppContext.setContextPath("/");
	     webAppContext.setWelcomeFiles(new String[] {"/static/test.html"});
	     
	     /*******************************************hold the jsp**********************************************/
	     
	     
	  // Add JSP Servlet (must be named "jsp")
	     ServletHolder holderJsp = new ServletHolder("jsp", JettyJspServlet.class);
	     holderJsp.setInitOrder(0);
	     holderJsp.setInitParameter("logVerbosityLevel","INFO");
	     holderJsp.setInitParameter("fork","false");
	     holderJsp.setInitParameter("xpoweredBy","false");
	     holderJsp.setInitParameter("compilerTargetVM","1.7");
	     holderJsp.setInitParameter("compilerSourceVM","1.7");
	     holderJsp.setInitParameter("keepgenerated","true");
	     webAppContext.addServlet(holderJsp, "*.jsp");
	     
	     
	     
	   //Ensure the jsp engine is initialized correctly
//	     JettyJasperInitializer sci = new JettyJasperInitializer();
//
//	     ServletContainerInitializersStarter sciStarter = new ServletContainerInitializersStarter(webAppContext);
//	     ContainerInitializer initializer = new ContainerInitializer(sci, null);
//	     List<ContainerInitializer> initializers = new ArrayList<ContainerInitializer>();
//	     initializers.add(initializer);
//	     webAppContext.setAttribute("org.eclipse.jetty.containerInitializers", initializers);
//	     webAppContext.addBean(sciStarter, true);

	     // Set Classloader of Context to be sane (needed for JSTL)
	     // JSP requires a non-System classloader, this simply wraps the
	     // embedded System classloader in a way that makes it suitable
	     // for JSP to use
	     
//	     ClassLoader jspClassLoader = new URLClassLoader(new URL[0], EmbeddedJetty.class.getClassLoader());
//	     webAppContext.setClassLoader(jspClassLoader);

	     // Add JSP Servlet (must be named "jsp")
//	     ServletHolder holderJsp = new ServletHolder("jsp", JettyJspServlet.class);
//	     holderJsp.setInitOrder(0);
//	     holderJsp.setInitParameter("logVerbosityLevel","INFO");
//	     holderJsp.setInitParameter("fork","false");
//	     holderJsp.setInitParameter("xpoweredBy","false");
//	     holderJsp.setInitParameter("compilerTargetVM","1.7");
//	     holderJsp.setInitParameter("compilerSourceVM","1.7");
//	     holderJsp.setInitParameter("keepgenerated","true");
//	     webAppContext.addServlet(holderJsp, "*.jsp");
//	     
//	    System.err.print(EmbeddedJetty.class.getResource(""));
//	 	webAppContext.setResourceBase(EmbeddedJetty.class.getResource("webapp").toURI().toASCIIString());
	     
	     
//	    webAppContext.setContextPath("webapp");
//	    webAppContext.setConfigurationDiscovered(true);
//		webAppContext.setParentLoaderPriority(true);
	     
	     String ProPath= System.getProperty("user.dir");
	     webAppContext.setResourceBase(ProPath+"/src/main/webapp");
	     
	     /****************************************jsp end ****************************************************/
//	     webAppContext.addServlet(jspServletHolder(), "*.jsp");
	     
	     //tell the webApp about our Spring MVC web initializer. The hoops I jump through here are because
	     //Jetty 9 AnnotationConfiguration doesn't scan non-jar classpath locations for a class that implements WebApplicationInitializer.
	     //The code below explicitly tells Jetty about our implementation of WebApplicationInitializer.
	     
	     //this Jetty bug: https://bugs.eclipse.org/bugs/show_bug.cgi?id=404176  and the discussion around it defines the issue best. I decided
	     //I would not rely on the potentially buggy solution put into Jetty 9 (discussed in the bug thread) and just go for a fix I know would work.  
	     webAppContext.setConfigurations(new Configuration[] { 
	    		 new AnnotationConfiguration() {
	    		        @Override
	    		        public void preConfigure(WebAppContext context) throws Exception {
	    		        	ClassInheritanceMap map = new ClassInheritanceMap();
	    		            ConcurrentHashSet<String> hashSet = new  ConcurrentHashSet<String>();
	    		            hashSet.add(SpringMvcInitializer.class.getName());
	    		            map.put(WebApplicationInitializer.class.getName(), hashSet);
	    		            context.setAttribute(CLASS_INHERITANCE_MAP, map);
	    		            _classInheritanceHandler = new ClassInheritanceHandler(map);
	    		        }
	    		    } 
		 });
	     server.setHandler(webAppContext);
		
		//core server configuration
		c.setIdleTimeout(jettyConfig.getIdleTimeout());
		c.setAcceptQueueSize(jettyConfig.getAcceptQueueSize());
		c.setPort(jettyConfig.getPort());
		c.setHost(jettyConfig.getHost());
		server.addConnector(c);
		
		//start the server and make it available when initialization is complete
		server.start();
		server.join();
	}

	 /**
     * Create JSP Servlet (must be named "jsp")
     */
    private static ServletHolder jspServletHolder()
    {
        ServletHolder holderJsp = new ServletHolder("jsp", JettyJspServlet.class);
        holderJsp.setInitOrder(0);
        holderJsp.setInitParameter("logVerbosityLevel", "DEBUG");
        holderJsp.setInitParameter("fork", "false");
        holderJsp.setInitParameter("xpoweredBy", "false");
        holderJsp.setInitParameter("compilerTargetVM", "1.7");
        holderJsp.setInitParameter("compilerSourceVM", "1.7");
        holderJsp.setInitParameter("keepgenerated", "true");
        return holderJsp;
    }

}
