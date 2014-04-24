/**
 * 
 */
package com.fh.his.cep.stock.event;


/**
 * @author ashu
 * 
 */
public class StockBean {
	private double askRealtime;
	private double bidRealtime;
	private String change;
	private String changeRealtime;
	private String daysLow;
	private String daysHigh;
	private String lastTradePriceOnly;
	private String highLimit;
	private String lowLimit;
	private String symbol;
	
	public double getAskRealtime() {
		return askRealtime;
	}
	public void setAskRealtime(double askRealtime) {
		this.askRealtime = askRealtime;
	}
	public double getBidRealtime() {
		return bidRealtime;
	}
	public void setBidRealtime(double bidRealtime) {
		this.bidRealtime = bidRealtime;
	}
	public String getChange() {
		return change;
	}
	public void setChange(String change) {
		this.change = change;
	}
	public String getChangeRealtime() {
		return changeRealtime;
	}
	public void setChangeRealtime(String changeRealtime) {
		this.changeRealtime = changeRealtime;
	}
	public String getDaysLow() {
		return daysLow;
	}
	public void setDaysLow(String daysLow) {
		this.daysLow = daysLow;
	}
	public String getDaysHigh() {
		return daysHigh;
	}
	public void setDaysHigh(String daysHigh) {
		this.daysHigh = daysHigh;
	}
	public String getLastTradePriceOnly() {
		return lastTradePriceOnly;
	}
	public void setLastTradePriceOnly(String lastTradePriceOnly) {
		this.lastTradePriceOnly = lastTradePriceOnly;
	}
	public String getHighLimit() {
		return highLimit;
	}
	public void setHighLimit(String highLimit) {
		this.highLimit = highLimit;
	}
	public String getLowLimit() {
		return lowLimit;
	}
	public void setLowLimit(String lowLimit) {
		this.lowLimit = lowLimit;
	}
	public String getSymbol() {
		return symbol;
	}
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "StockBean [askRealtime=" + askRealtime + ", bidRealtime="
				+ bidRealtime + ", change=" + change + ", changeRealtime="
				+ changeRealtime + ", daysLow=" + daysLow + ", daysHigh="
				+ daysHigh + ", lastTradePriceOnly=" + lastTradePriceOnly
				+ ", highLimit=" + highLimit + ", lowLimit=" + lowLimit
				+ ", symbol=" + symbol + "]";
	}
	
}
