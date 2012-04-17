/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.umang.portfolio.stocksearch;

/**
 * A pojo that will be passed. it will store the data.
 * @author umang
 */

public class SearchStockBean
{

    public String getExchDisp()
    {
        return exchDisp;
    }

    public void setExchDisp(String exchDisp)
    {
        this.exchDisp = exchDisp;
    }

    public String getExhange()
    {
        return exhange;
    }

    public void setExhange(String exhange)
    {
        this.exhange = exhange;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getSymbol()
    {
        return symbol;
    }

    public void setSymbol(String symbol)
    {
        this.symbol = symbol;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }
    
    private String symbol;
    private String name;
    private String exhange;
    private String exchDisp;
    private String type;
    /*
    "symbol" "X"
"name"  "United States Steel Corp."
"exch"  "NYQ"
"type"  "S"
"exchDisp" "NYSE"
"typeDisp" "Equity"*/
}
