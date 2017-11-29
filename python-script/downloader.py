import pandas as pd
import pandas_datareader.data as web
import datetime
import logging
from time import gmtime, strftime

logger = logging.getLogger(__name__)
logger.setLevel(logging.INFO)

handler = logging.FileHandler('../log/download.log')
handler.setLevel(logging.INFO)

formatter = logging.Formatter('%(asctime)s - %(name)s - %(levelname)s - %(message)s')
handler.setFormatter(formatter)

logger.addHandler(handler)


start = datetime.datetime(2016, 1, 1)
end = datetime.datetime(2017, 2, 5)


today = datetime.date.today()
#first = today.replace(day=1)
two_month_ago = today - datetime.timedelta(days=60)
#print (two_month_ago.strftime("%Y-%m-%d"))


time_end =  strftime("%Y-%m-%d", gmtime())
#print(time_end)

def getSymbols():
    with open("../data/symbols.csv","r") as f:
        symbols = f.readlines()
    f.close()
    return symbols

def download(symbol,start,end,source='yahoo'):
    try:
        f = web.DataReader(symbol, source, start, end)
        f.to_csv("../data/histData/"+symbol+".csv",header=None)
        logger.info(symbol+" data from network done!")
        print(symbol+" data from network done!")
    except:
        logger.error(symbol+"download hist data error!")
        
#print(symbols)
symbols = getSymbols()
for symbol in symbols:
    download(symbol.rstrip('\n'),two_month_ago,time_end)
#download("LBTYB",two_month_ago,time_end)


