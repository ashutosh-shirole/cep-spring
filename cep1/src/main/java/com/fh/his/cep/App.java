package com.fh.his.cep;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;

/**
 * Hello world!
 * 
 */
public class App {
	private static Log log = LogFactory.getLog(App.class);

	private boolean isShutdown;

	public App() throws Exception {
		Configuration configuration = new Configuration();
		configuration.configure("esper-config.xml");
		EPServiceProvider engine = EPServiceProviderManager
				.getDefaultProvider(configuration);
		Map<String, Object> conEvent = new HashMap<String, Object>();
		conEvent.put("PcapHeader", "PcapHeader");
		conEvent.put("DataLinkLayer", "DataLinkLayer");
		conEvent.put("NetworkLayer", "NetworkLayer");
		conEvent.put("Tcp", "Tcp");
		log.info("Creating sample statement");
		engine.getEPAdministrator().getConfiguration().addEventType("sniffer.header.parsed", conEvent);
		TCPCEInsertStatement.createStatement(engine.getEPAdministrator());
		TCPCFStatement.createStatement(engine.getEPAdministrator());
		TCPDurationStatement.createStatement(engine.getEPAdministrator());

		reportStats();
	}

	private void reportStats()
			throws InterruptedException {
		// Register shutdown hook
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				isShutdown = true;
			}
		});

		// Report statistics
		long startTime = System.currentTimeMillis();
		long currTime;
		double deltaSeconds;
		do {
			// sleep
			Thread.sleep(1000);
			currTime = System.currentTimeMillis();
			deltaSeconds = (currTime - startTime) / 1000.0;


			//log.info(" time=" + deltaSeconds);
		} while (!isShutdown);
		
		log.info(" time=" + deltaSeconds);
		log.info("Exiting");
		System.exit(-1);

	}

	public static void main(String[] args) {
		try {
			new App();
		} catch (Throwable t) {
			log.error("Error starting server shell : " + t.getMessage(), t);
			System.out.println(t);
			System.exit(-1);
		}
	}
}
