
with open("../data/Stock_Universe.csv") as f:
    contents = f.readlines()
    f.close()

symbols = [x.split(' ')[0] for x in contents]    
    
print(symbols)

with open("../data/symbols.csv","w") as f:
    a = [f.write(x+'\n') for x in symbols]
    f.close()