from downloader import getSymbols
import urllib.request
import ast
import json
import logging

path="../data/funData/"
logging.basicConfig(filename="../log/fun.log",format='%(asctime)s %(message)s',level=logging.INFO)
#trailingPE,priceToSales,dividendYields

def localSymbols():
    return [symbol.rstrip('\n') for symbol in getSymbols()]
    
def getCsvFundamental(symbol):
    url = "http://download.finance.yahoo.com/d/quotes.csv?s="+symbol+"&f=rp5y"
    response = urllib.request.urlopen(url)
    html = response.read().decode("utf8").rstrip("\n")
    #print(html)
    return html
    
def getDefaultKeyStatistics(symbol,keys = None,format = "raw" ):
    result = {}
    url = "https://query2.finance.yahoo.com/v10/finance/quoteSummary/"+symbol+"?modules=defaultKeyStatistics"
    response = urllib.request.urlopen(url)
    html = response.read().decode('utf8')
    j = json.loads(html)
    j = j["quoteSummary"]["result"][0]
    m = j["defaultKeyStatistics"]
    #for key,value in m.items():
    
     #  print(key+" : "+str(value))
       
    if keys !=None:
        for key in keys:
            if key in m.keys():
                result[key] = m[key][format]
    return result
            
    #print(j)
    
def geFinancialData(symbol,keys =["revenueGrowth","operatingCashflow","returnOnEquity"],format = "raw" ):
    result = {}
    url = "https://query2.finance.yahoo.com/v10/finance/quoteSummary/"+symbol+"?modules=financialData"
    response = urllib.request.urlopen(url)
    html = response.read().decode('utf8')
    j = json.loads(html)
    j = j["quoteSummary"]["result"][0]
    m = j["financialData"]
    #for key,value in m.items():
    
     #  print(key+" : "+str(value))
       
    if keys !=None:
        for key in keys:
            if key in m.keys():
                result[key] = m[key][format]
    return result

def combine(symbol):
    file = path+"/"+symbol+".csv"
    s = ""
    try:
        s+=getCsvFundamental(symbol)
        s+=","
        #print(s)
        m1 = getDefaultKeyStatistics(symbol,keys=["priceToBook","enterpriseToEbitda","earningsQuarterlyGrowth","netIncomeToCommon"])
        #print(m1)
        s+=str(m1["priceToBook"])
        s+=","
        s+=str(m1["enterpriseToEbitda"])
        s+=","
        s+=str(m1["earningsQuarterlyGrowth"])
        s+=","
        s+=str(m1["netIncomeToCommon"])
        s+=","
        #print(s)
        m2 = geFinancialData(symbol)
        s+=str(m2["revenueGrowth"])
        s+=","
        s+=str(m2["operatingCashflow"])
        s+=","
        s+=str(m2["returnOnEquity"])
        with open(file,"w") as f:
            f.write(s+"\n")
        f.close()
        logging.info(symbol+" fundamental data done!")
        print(symbol+" fundamental data done!")
    except:
        print(symbol+" fundamental data error!")   
        logging.warn(symbol+" fundamental data error!")
     
    return s 


def write_symbols(symbols,num=100):
    [combine(s) for s in symbols[:num]]
    print("done!")
       
    
if __name__=="__main__":
    symbols = localSymbols()
    write_symbols(symbols)
   #ast.literal_eval(geFinancialData("T"))[quoteSummary]
   #s = geFinancialData("T",key="operatingCashflow")
   #s = geFinancialData("T")
   #s = getDefaultKeyStatistics("T",keys=["priceToBook","enterpriseToEbitda","earningsQuarterlyGrowth","netIncomeToCommon"])
   #print(s)
   
   #t = getCsvFundamental("T")
   #print(t)
   
    #combine("GOOG")
   #j = json.loads(s)
   #j = j["quoteSummary"]["result"][0]
   #m = j["financialData"]
   #for key,value in m.items():
    
       #print(key+" : "+str(value))
   #print(j[0].keys())