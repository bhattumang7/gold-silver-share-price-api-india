package com.umang.portfolio.goldsilver;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileNotFoundException;
import java.io.IOException;
//import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * this class will get the silver price when requested
 * @author umang
 */
public class SilverPriceDAO
{

    private static Float price = null;
    private static Date lastUpdated = null;

    /**
     *  this method will be invoked to update the values from the web. will be called after some interval
     * @throws FileNotFoundException
     * @throws IOException
     */
    private static void updateValues() throws FileNotFoundException, IOException
    {

         // get content of page in source variable
        String sURL = "http://www.mcxindia.com/SitePages/MarketWatch.aspx?mwtype=1&pageno=0&symbol=SILVER&exp=ALL&shortby=&shortdir=ASC&isrefreshed=1&etype=GO";
        URL ul = new URL(sURL);
        URLConnection uc = ul.openConnection();
        InputStreamReader isr = new InputStreamReader(uc.getInputStream());
        BufferedReader br = new BufferedReader(isr);
        String source = "";
        String line= "" ;
        do {
            line = br.readLine();
            source += line;
        } while (line != null);
        

        // get the gold price from the page
        Pattern pattern = Pattern.compile("<tr class=\"tablerow\" [0-9a-zA-Z<>.:;\" =(),'\\t\\n\\r%/_-]*<td [0-9a-zA-Z<>.:;\" =(),'\\t\\n\\r%/_-]*</td>[0-9a-zA-Z<>.:;\" =(),'\\t\\n\\r%/_-]*</tr>");
        Matcher matcher = pattern.matcher(source);
        String temp1 = "";
        while (matcher.find())
        {
            temp1 += "\n" + matcher.group();
        }

        //System.out.print(temp1);
        Pattern patternInner = Pattern.compile("[0-9][0-9][A-Z][A-Z][A-Z][0-9][0-9][0-9][0-9][a-zA-Z<>/ =\":%;0-9\\n\\r\\t]*.[0-9][0-9]"); // [<][/a-z><    =\":0-9%;\\n\\r\\t]*.[0][0]

        Matcher m1 = patternInner.matcher(temp1);
        String first = null;
        String last = null;
        while (m1.find())
        {
            String val = m1.group();

            StringTokenizer stok = new StringTokenizer(val, "<>");
            while (stok.hasMoreTokens())
            {
                String temp = stok.nextToken();
                if (first == null)
                {
                    first = temp.trim();
                }
                else
                {
                    last = temp.trim();
                    //    li.add(last);
                }
            }
            //System.out.println(first + " " + last);
            price = Float.parseFloat(last);
            lastUpdated = new Date();

        }
    }

    /**
     *
     * @return the price of silver
     */
    public static Float getValue()
    {
        Date now = new Date();
        // if no value is set, set it
        if (lastUpdated == null)
        {
            try
            {
                updateValues();
            }
            catch (Exception e)
            {
            }
        }
        // it it was not recently updated, update it
        else if (lastUpdated.compareTo(now) > 10000)
        {
            try
            {
                updateValues();
            }
            catch (Exception e)
            {
            }
        }
        return price;
    }

    /**
     * will be used to test class itself
     * @param args
     */
    public static void main(String[] args)
    {
        System.out.println(getValue());
    }
    //   price = Float.parseFloat(last);
}
