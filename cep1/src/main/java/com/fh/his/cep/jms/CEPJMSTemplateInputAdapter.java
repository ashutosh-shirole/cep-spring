/**
 * 
 */
package com.fh.his.cep.jms;

import java.util.List;
import java.util.Map;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jms.core.JmsTemplate;
import org.w3c.dom.Node;

import com.espertech.esper.adapter.AdapterState;
import com.espertech.esper.client.EPException;
import com.espertech.esper.client.EventBean;
import com.espertech.esperio.jms.JMSInputAdapter;

/**
 * @author ashu
 *
 */
public class CEPJMSTemplateInputAdapter extends JMSInputAdapter implements
		MessageListener {

	private JmsTemplate jmsTemplate;

    private final Log log = LogFactory.getLog(getClass());

    /**
     * Returns the jms template.
     * @return Spring JMS template
     */
    public JmsTemplate getJmsTemplate()
    {
        return jmsTemplate;
    }

    /**
     * Sets the Spring JMS template
     * @param jmsTemplate is the jms template
     */
    public void setJmsTemplate(JmsTemplate jmsTemplate)
    {
        this.jmsTemplate = jmsTemplate;
    }

    public void onMessage(Message message)
    {
        try
        {
            if (stateManager.getState() == AdapterState.DESTROYED)
            {
                log.warn(".onMessage Event message not sent to engine, state of adapter is destroyed, message ack'd");
                message.acknowledge();
                return;
            }

            if (epServiceProviderSPI == null)
            {
                log.warn(".onMessage Event message not sent to engine, service provider not set yet, message ack'd");
                message.acknowledge();
                return;
            }

            synchronized (message)
            {
                Object theEvent = null;
                try {
                    theEvent = jmsMessageUnmarshaller.unmarshal(epServiceProviderSPI.getEventAdapterService(), message);
                }
                catch (RuntimeException ex) {
                    log.error("Failed to unmarshal event: " + ex.getMessage(), ex);
                }

                if (theEvent != null)
                {
                    if (theEvent instanceof Node) {
                        epServiceProviderSPI.getEPRuntime().sendEvent((Node)theEvent);
                    }
                    else if(theEvent instanceof EventBean) {
                    	if(((EventBean)theEvent).getUnderlying() instanceof List) {
                    		List l = (List)((EventBean)theEvent).getUnderlying();
							for (Object o : l) {
								Map m = (Map)o;
								String key = (String)m.keySet().iterator().next();
								Object ev = m.get(key);
								//log.info(ev);
								epServiceProviderSPI.getEPRuntime()
										.sendEvent((Map)ev,key);
							}
                    	} else {
                    		epServiceProviderSPI.getEPRuntime().sendEvent(theEvent);
                    	}
                    }                
                    else {
                        epServiceProviderSPI.getEPRuntime().sendEvent(theEvent);
                    }
                }
                else
                {
                    if (log.isWarnEnabled())
                    {
                        log.warn(".onMessage Event object not sent to engine: " + message.getJMSMessageID());
                    }
                }

                message.acknowledge();
            }
        }
        catch (JMSException ex)
        {
            throw new EPException(ex);
        }
        catch (EPException ex)
        {
            log.error(".onMessage exception", ex);
            if (stateManager.getState() == AdapterState.STARTED)
            {
                stop();
            }
            else
            {
                destroy();
            }
        }
    }

}
