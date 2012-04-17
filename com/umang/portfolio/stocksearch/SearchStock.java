/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.umang.portfolio.stocksearch;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.StringTokenizer;

/**
 *
 * @author umang
 */
public class SearchStock
{

    /**
     * will be userful in searching for a stock
     * @param args the command line arguments
     */
    /**
     *
     * @param sURL is the page whose content we want to get
     * @return page content as string
     * @throws MalformedURLException if the url is not valid
     * @throws IOException if there is an error in IO
     */

    private static String getPageByURL(String sURL) throws MalformedURLException, IOException
    {
        String response = "";
        URL yahoofin = new URL(sURL);
        URLConnection yc = yahoofin.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
        String inputLine = "";
        while ((inputLine = in.readLine()) != null)
        {
            response += inputLine;
        }
        return response;
    }

    /**
     *
     * @param name name of the quote
     * @return will return the url formed to be passed further
     */
    private static String formURLForNameSearch(String name)
    {
        
        return "http://autoc.finance.yahoo.com/autoc?query=" + URLEncoder.encode(name).toString() + "&callback=YAHOO.Finance.SymbolSuggest.ssCallback";
    }

    /**
     *
     * @param response is the page contentt that we have got from the page
     * @return will return the linked list of SearchStock bean type.
     */
    private static LinkedList<SearchStockBean> getStockBeanFromString(String response)
    {
        LinkedList<SearchStockBean> li = new LinkedList<SearchStockBean>();
        StringTokenizer stok = new StringTokenizer(response, "()");

        stok.nextToken();

        String temp = stok.nextToken();



        StringTokenizer stok1 = new StringTokenizer(temp, "[]");
        stok1.nextToken();
        temp = stok1.nextToken();
        //System.out.println(jsonInput);

        StringTokenizer stok2 = new StringTokenizer(temp, "{}");
        while (stok2.hasMoreTokens())
        {
            String chkSpace = stok2.nextToken();
            if (chkSpace.equalsIgnoreCase(",") == false)
            {

                StringTokenizer stok3 = new StringTokenizer(chkSpace, "\"");
                SearchStockBean stockBean = new SearchStockBean();
                while (stok3.hasMoreTokens())
                {
                    String chkCol = stok3.nextToken();
                    if (chkCol.trim().equalsIgnoreCase(":") || chkCol.trim().equalsIgnoreCase(","))
                    {
                        chkCol = stok3.nextToken();
                    }
                    String val = stok3.nextToken();
                    if (val.trim().equalsIgnoreCase(":") || val.trim().equalsIgnoreCase(","))
                    {
                        val = stok3.nextToken();
                    }

                    if (chkCol.equalsIgnoreCase("symbol") == true)
                    {
                        stockBean.setSymbol(val);


                    }
                    else if (chkCol.equalsIgnoreCase("name") == true)
                    {
                        stockBean.setName(val);
                    }
                    else if (chkCol.equalsIgnoreCase("exch") == true)
                    {
                        stockBean.setExhange(val);
                    }
                    else if (chkCol.equalsIgnoreCase("type") == true)
                    {
                        stockBean.setType(val);
                    }
                    else if (chkCol.equalsIgnoreCase("exchDisp") == true)
                    {

                        stockBean.setExchDisp(val);
                    }
                    else if (chkCol.equalsIgnoreCase("typeDisp") == true)
                    {
                        stockBean.setType(val);
                        li.add(stockBean);
                        stockBean = new SearchStockBean();
                    }

                }
            }

        }
        return li;
    }

    /**
     *
     * @param name name of stock
     * @return linked list of SearchStockBean
     * @throws MalformedURLException of ur is not valid
     * @throws IOException it here is an error
     */
    public static LinkedList<SearchStockBean> getCompanyListByName(String name) throws MalformedURLException, IOException
    {
        String URL = formURLForNameSearch(name);
        String response = getPageByURL(URL);
        return getStockBeanFromString(response);
    }

    /**
     * a method used to test the class itself
     * @param args noting
     * @throws MalformedURLException
     * @throws IOException
     */
    public static void main(String[] args) throws MalformedURLException, IOException
    {

        LinkedList<SearchStockBean> li = getCompanyListByName("hospital");
        for (SearchStockBean s : li)
        {
            System.out.println(s.getName());
        }
    }
}
