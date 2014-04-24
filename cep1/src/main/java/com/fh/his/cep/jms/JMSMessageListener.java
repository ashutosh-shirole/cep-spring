/**************************************************************************************
 * Copyright (C) 2008 EsperTech, Inc. All rights reserved.                            *
 * http://esper.codehaus.org                                                          *
 * http://www.espertech.com                                                           *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the GPL license       *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package com.fh.his.cep.jms;

import com.espertech.esper.client.EPRuntime;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.activemq.command.ActiveMQObjectMessage;

import java.io.IOException;
import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MapMessage;
import javax.jms.MessageListener;
import java.util.List;
import java.util.Map;

public class JMSMessageListener implements MessageListener
{
    private static Log log = LogFactory.getLog(JMSMessageListener.class);
    private EPRuntime engine;
    private int count;

    public JMSMessageListener(EPRuntime engine)
    {
        this.engine = engine;
    }

    public void onMessage(Message message)
    {
	try {
		if (message instanceof ActiveMQObjectMessage) {
			ActiveMQObjectMessage objMessage = (ActiveMQObjectMessage) message;
			//log.info(objMessage.getObject().getClass());
			//Thread.currentThread().setContextClassLoader(this.getClass().getClassLoader()); 
			//List vec = (List) objMessage.getObject();
			Object vec = objMessage.getObject();
			//log.info(vec);
			List li = (List)vec;
			for( Object o : li) {
				//log.info(count);
				Map m = (Map) ((Map)o).get("sniffer.header.parsed");
				//Map m = (Map)o;
				//log.info(m);
				engine.sendEvent(m,"sniffer.header.parsed");
				count++;	    
			}
		} 
	} catch (JMSException e) {
		e.printStackTrace();
	}
    }

    public int getCount()
    {
        return count;
    }

    private String getBody(BytesMessage bytesMsg)
    {
        try
        {
            long length = bytesMsg.getBodyLength();
            byte[] buf = new byte[(int)length];
            bytesMsg.readBytes(buf);
            return new String(buf);
        }
        catch (JMSException e)
        {
            String text = "Error getting message body";
            log.error(text, e);
            throw new RuntimeException(text, e);
        }
    }
}
