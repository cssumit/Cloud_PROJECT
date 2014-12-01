#!/usr/bin/python
import time
import json
import random
def generate():
	#f=open("file.json","w")
	dicti={}
	chiid=1203020;
	longi=78.134570
	lati=17.41448
	fg=open("bounds","w")
	fg.write(str(lati-random.uniform(0.1,0.9))+","+str(longi-random.uniform(0.1,0.9))+","+str(lati+random.uniform(0.1,0.9))+","+str(longi+random.uniform(0.1,0.9))+","+str(random.randrange(50,151))+"\n")
	fg.write(str(lati-random.uniform(0.1,0.9))+","+str(longi-random.uniform(0.1,0.9))+","+str(lati+random.uniform(0.1,0.9))+","+str(longi+random.uniform(0.1,0.9))+","+str(random.randrange(50,151))+"\n")
	for i in range(1,75):
		dicti["chipID"]=str(chiid)
		dicti["longitude"]=str(longi)
		dicti["latitude"]=str(lati)
		dicti["accident"]=str(random.randrange(0,2))
		dicti["speed"]=str(random.randrange(20,201))
		chiid=chiid+1
		longi=longi+random.uniform(-0.009,0.009)
		lati=lati+random.uniform(-0.009,0.009)
		print json.dumps(dicti)
		#f.write(json.dumps(dicti)+'\n')
	#f.close()
	fg.close()

while(1):
	generate()
	time.sleep(10)
