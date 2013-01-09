package com.umang.portfolio.stockticker;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.HashMap;

public class StockTickerDAO
{
    private static final StockTickerDAO stockDAO = new StockTickerDAO();
    private static HashMap<String, StockBean> stocks = new HashMap<String, StockBean>();
    private static final long TWENTY_MIN = 1200000;

    private StockTickerDAO()
    {
    }

    public static StockTickerDAO getInstance()
    {
        return stockDAO;
    }

    /**
     *
     * @param symbol
     * @return StockBean
     * will return null if unable to retrieve information
     */
    public static StockBean getStockPrice(String symbol)
    {
        StockBean stock;
        long currentTime = (new Date()).getTime();
        // Check last updated and only pull stock on average every 20 minutes
        if (stocks.containsKey(symbol))
        {
            stock = stocks.get(symbol);
            if (currentTime - stock.getLastUpdated() > TWENTY_MIN)
            {
                stock = refreshStockInfo(symbol, currentTime);
            }
        }
        else
        {
            stock = refreshStockInfo(symbol, currentTime);
        }
        return stock;
    }
    //This is synched so we only do one request at a time
    //If yahoo doesn't return stock info we will try to return it from the map in memory

    private static synchronized StockBean refreshStockInfo(String symbol, long time)
    {
        try
        {
            URL yahoofin = new URL("http://finance.yahoo.com/d/quotes.csv?s=" + symbol + "&f=sl1d1t1c1ohgv&e=.csv");
            URLConnection yc = yahoofin.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null)
            {
                String[] yahooStockInfo = inputLine.split(",");
                StockBean stockInfo = new StockBean();
                stockInfo.setTicker(yahooStockInfo[0].replaceAll("\"", ""));
                stockInfo.setPrice(Float.valueOf(yahooStockInfo[1]));
                stockInfo.setChange(Float.valueOf(yahooStockInfo[4]));
                stockInfo.setChartUrlSmall("http://ichart.finance.yahoo.com/t?s=" + stockInfo.getTicker());
                stockInfo.setChartUrlLarge("http://chart.finance.yahoo.com/w?s=" + stockInfo.getTicker());
                stockInfo.setLastUpdated(time);
                stocks.put(symbol, stockInfo);
                break;
            }
            in.close();
        }
        catch (Exception ex)
        {
            System.out.print("Unable to get stock quote");
            ex.printStackTrace();
            //log.error("Unable to get stockinfo for: " + symbol + ex);
        }
        return stocks.get(symbol);
    }

    public static void main(String args[])
    {
        StockBean s = StockTickerDAO.getStockPrice("RELINFRA.NS");
        System.out.println(s.getPrice());
    }
}
