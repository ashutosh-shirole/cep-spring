/**
 * 
 */
package com.fh.his.cep.stock.statement;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;

/**
 * @author ashu
 *
 */
public class StockEventStatement {
	private static Log log = LogFactory.getLog(StockEventStatement.class);
	
	public static void createStatement(EPAdministrator admin) {
		String epl = "every (a=StockBean(symbol='AAPL') -> b=StockBean(symbol='AAPL' and askRealtime > a.askRealtime)) where timer:within(10 seconds)";
		EPStatement st = admin.createPattern(epl);
		st.addListener(new UpdateListener() {
			
			@Override
			public void update(EventBean[] newEvents, EventBean[] oldEvents) {
				log.info(newEvents[0].get("a"));
				log.info(newEvents[0].get("b"));
			}
		});
	}

}
