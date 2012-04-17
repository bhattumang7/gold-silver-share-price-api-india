cd com
cd umang
cd portfolio
cd stockticker
javac *.java
cd ..
cd stocksearch
javac *.java
cd ..
cd goldsilver
javac *.java
cd ..
cd ..
cd ..
cd .. 
echo "compilation done. now running the stock ticker\n"
java com.umang.portfolio.stockticker.StockTickerDAO

echo "\nnow running the stock search"
java com.umang.portfolio.stocksearch.SearchStock

echo "\nnow running the gold price"
java com.umang.portfolio.goldsilver.GoldPriceDAO

echo "\nnow running the silver price"
java com.umang.portfolio.goldsilver.SilverPriceDAO
