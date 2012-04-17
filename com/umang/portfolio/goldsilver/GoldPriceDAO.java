
package com.umang.portfolio.goldsilver;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
//import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * this is a pojo class which will give then gold price
 * when invoked.
 * @author umang
 */
public class GoldPriceDAO
{

    private static Float price = null;
    private static Date lastUpdated = null;

    /**
     * @param args the command line arguments
     */
    private static void updateValues() throws FileNotFoundException, IOException
    {

        // open the url and get the content in the source string variable
        String sURL = "http://www.mcxindia.com/SitePages/MarketWatch.aspx?mwtype=1&pageno=0&symbol=GOLD&exp=ALL&shortby=&shortdir=ASC&isrefreshed=1&etype=GO";
        URL ul = new URL(sURL);
        URLConnection uc = ul.openConnection();
        //InputStream is = uc.getInputStream(uc.geti);
        InputStreamReader isr = new InputStreamReader(uc.getInputStream());
        BufferedReader br = new BufferedReader(isr);
        String source = "";
        String line= "" ;
        do {
            line = br.readLine();
            source += line;
        } while (line != null);
        //System.out.println(source);

        // match the pattern and ge tthe price
        Pattern pattern = Pattern.compile("<tr class=\"tablerow\" [0-9a-zA-Z<>.:;\" =(),'\\t\\n\\r%/_-]*<td [0-9a-zA-Z<>.:;\" =(),'\\t\\n\\r%/_-]*</td>[0-9a-zA-Z<>.:;\" =(),'\\t\\n\\r%/_-]*</tr>");
        Matcher matcher = pattern.matcher(source);
        String temp1 = "";
        while (matcher.find())
        {
            temp1 += "\n" + matcher.group();
        }
	//System.out.println("temp1: " + temp1);

        
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

    public static Float getValue()
    {
        // if it was updated in the last 10000 miliseconds then it will \
        // return the old price and inturn it will increase the performance
        Date now = new Date();
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

     //main method to test the class itself
     public static void main(String[] args)
    {
        System.out.println(getValue());
    }
    
}
