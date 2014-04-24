package com.fh.his.cep.stock.app;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.espertech.esper.epl.generated.EsperEPL2GrammarParser.newAssign_return;
import com.fh.his.cep.stock.event.StockBean;

public class StockTickGenerator {
	private static Log log = LogFactory.getLog(StockTickGenerator.class);

	public List<StockBean> setYahooFinanceQuoteValues() {

		StockBean event = new StockBean();
		String query = "";
		URL url;
		List<StockBean> li = new ArrayList<StockBean>();

		try {
			query = URLEncoder.encode(query, "UTF-8");
			url = new URL(
					"https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20yahoo.finance.quotes%20where%20symbol%20in%20(%22YHOO%22%2C%22AAPL%22%2C%22GOOG%22%2C%22MSFT%22)&diagnostics=true&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys");
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			URLConnection connection = url.openConnection();
			Document doc = docBuilder.parse(connection.getInputStream());
			doc.getDocumentElement().normalize();
			NodeList listOfResults = doc.getElementsByTagName("quote");
			// message = "";
			for (int s = 0; s < listOfResults.getLength(); s++) {
				Node resultNode = listOfResults.item(s);
				if (resultNode.getNodeType() == Node.ELEMENT_NODE) {
					Element firstResultElement = (Element) resultNode;
					// Symbol
					NodeList firstResultList = firstResultElement
							.getElementsByTagName("Symbol");
					Element firstSymbolElement = (Element) firstResultList
							.item(0);
					NodeList textsymbolList = firstSymbolElement
							.getChildNodes();
					String Symbol = textsymbolList.item(0).getNodeValue()
							.trim();
					// LastTradePrice
					firstResultList = firstResultElement
							.getElementsByTagName("LastTradePriceOnly");
					firstSymbolElement = (Element) firstResultList.item(0);
					textsymbolList = firstSymbolElement.getChildNodes();
					String LastTradePriceOnly = textsymbolList.item(0)
							.getNodeValue().trim();
					// Bid
					firstResultList = firstResultElement
							.getElementsByTagName("BidRealtime");
					firstSymbolElement = (Element) firstResultList.item(0);
					textsymbolList = firstSymbolElement.getChildNodes();
					String BidRealtime = textsymbolList.item(0).getNodeValue()
							.trim();
					// Ask

					firstResultList = firstResultElement
							.getElementsByTagName("AskRealtime");
					firstSymbolElement = (Element) firstResultList.item(0);
					textsymbolList = firstSymbolElement.getChildNodes();
					String AskRealtime = textsymbolList.item(0).getNodeValue()
							.trim();

					// Change

					firstResultList = firstResultElement
							.getElementsByTagName("Change");
					firstSymbolElement = (Element) firstResultList.item(0);
					textsymbolList = firstSymbolElement.getChildNodes();
					String Change = textsymbolList.item(0).getNodeValue()
							.trim();

					// DaysLow

					firstResultList = firstResultElement
							.getElementsByTagName("DaysLow");
					firstSymbolElement = (Element) firstResultList.item(0);
					textsymbolList = firstSymbolElement.getChildNodes();
					String DaysLow = "";
					if(textsymbolList.getLength() > 0) {
					DaysLow = textsymbolList.item(0).getNodeValue()
							.trim();
					}

					// DaysHigh

					firstResultList = firstResultElement
							.getElementsByTagName("DaysHigh");
					firstSymbolElement = (Element) firstResultList.item(0);
					textsymbolList = firstSymbolElement.getChildNodes();
					String DaysHigh = "";
					if(textsymbolList.getLength() > 0) {
						DaysHigh = textsymbolList.item(0).getNodeValue()
							.trim();
					}
					event = new StockBean();
					event.setSymbol(Symbol);
					event.setLastTradePriceOnly(LastTradePriceOnly);
					event.setBidRealtime(Double.parseDouble(BidRealtime));
					event.setAskRealtime(Double.parseDouble(AskRealtime));
					event.setChange(Change);
					event.setDaysLow(DaysLow);
					event.setDaysHigh(DaysHigh);
					li.add(event);
				}
			}
		} catch (DOMException | ParserConfigurationException | IOException
				| SAXException e) {
			log.error(e);
			e.printStackTrace();
		}
		return li;
	}
}
