/**
 * 
 */
package com.fh.his.cep.stock.app;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.fh.his.cep.stock.event.StockBean;
import com.fh.his.cep.stock.statement.StockEventStatement;

/**
 * @author ashu
 *
 */
public class StockTickMain {
	private static Log log = LogFactory.getLog(StockTickMain.class);
	private boolean isShutdown;
	
	public StockTickMain() throws Exception {
		Configuration configuration = new Configuration();
		configuration.configure("esper-config-stock.xml");
		EPServiceProvider engine = EPServiceProviderManager
				.getDefaultProvider(configuration);
		StockEventStatement.createStatement(engine.getEPAdministrator());
		StockTickGenerator gen = new StockTickGenerator();
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				isShutdown = true;
			}
		});

		do {
			// sleep
			Thread.sleep(50);
			
			List<StockBean> l = gen.setYahooFinanceQuoteValues();
			for(StockBean b : l) {
				engine.getEPRuntime().sendEvent(b);
				//log.info("event sent : "+b);
				Thread.sleep(1000);
			}
		} while (!isShutdown);
		
		log.info("Exiting");
		System.exit(-1);

	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			new StockTickMain();
		} catch (Throwable t) {
			log.error("Error starting server shell : " + t.getMessage(), t);
			System.out.println(t);
			System.exit(-1);
		}

	}

}
