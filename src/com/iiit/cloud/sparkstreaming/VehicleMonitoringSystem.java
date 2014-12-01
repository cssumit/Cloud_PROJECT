package com.iiit.cloud.sparkstreaming;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.StorageLevels;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaReceiverInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.json.JSONException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;  

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


/**
 * processes chips data in Json format, '\n' delimited text received from the network every second.
 * Usage: Vms <hostname> <port>
 *   <hostname> and <port> describe the TCP server that Spark Streaming would connect to receive data.
 *
 * To run this on your local machine, you need to first run a Netcat server
 *    `$ nc -lk 9999`
 * and then run the example
 *    'java Vms localhost 9999'
 */
public  class VehicleMonitoringSystem 
{
	static JBrowser jb=new JBrowser();
	static int i=0;
	 static ArrayList<ArrayList<Double>> bounds = new ArrayList<ArrayList<Double>>();
	
	 public static void showOnMap(List<String> data)
	 {
		 float sumlat=0;
		 float sumlong=0;
		 
		 
		 
		 JSONParser parser=new JSONParser();
		 List<String> lati=new ArrayList<String>();
		 List<String> longi=new ArrayList<String>();
		 List<String> col=new ArrayList<String>();
		 List<String> chId=new ArrayList<String>();
		 for (int i=0;i<data.size();i++)
		 {
			System.out.println(data.get(i));
			 Object obj;
			try {
				obj = parser.parse(data.get(i));
				JSONObject jsonRead = (JSONObject) obj; 
				//url=url+"http://maps.googleapis.com/maps/api/staticmap?center=Brooklyn+Bridge,New+York,NY&zoom=20&size=600x300&maptype=roadmap&markers=color:blue|label:S|40.702147,-74.015794&markers=color:green|label:G|40.711614,-74.012318&markers=color:red|label:C|40.718217,-73.998284";
				//Process p = Runtime.getRuntime().exec("firefox "+url);
				String color=(String)jsonRead.get("alert");
				if(!color.equals("null"))
				{
					col.add(color);
					lati.add((String)jsonRead.get("latitude"));
					longi.add((String)jsonRead.get("longitude"));
					chId.add((String)jsonRead.get("chipID"));
					
				}
				 
			} catch (ParseException  e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 
			  
		 }
		 for(int i=0;i<col.size();i++)
		 {
			 sumlat+=Float.parseFloat(lati.get(i));
			 sumlong+=Float.parseFloat(longi.get(i));
		 }
		 sumlat=sumlat/col.size();
		 sumlong=sumlong/col.size();
		 String url="http://localhost/Cloud/index.php";
		 for(int i=0;i<col.size();i++)
		 {
			 url+="?"+lati.get(i)+","+longi.get(i)+","+col.get(i)+","+chId.get(i);
		 }
		 
		 jb.browser(url);
	 }
	 
	 
	 public static void populate() throws IOException
	 {
		 FileReader fr = new FileReader("bounds");
		 BufferedReader br = new BufferedReader(fr);
		 String rec;
		 int i=0;
		 while((rec = br.readLine()) != null) 
		 {
		 
			 String val[]=rec.split(",");
			 bounds.add(new ArrayList<Double>());
			 for(int j=0;j<5;j++)
			   bounds.get(i).add(Double.parseDouble(val[j]));
			 i++;
			 
		 }
		 fr.close(); 
	 }
 
	 public static Boolean checkSpeed(String speed,String longitude,String latitude)
	 {
		 Double s= Double.parseDouble(speed);
		 Double lat=Double.parseDouble(latitude);
		 Double lon=Double.parseDouble(longitude);
		 
		 for(int i=0;i<bounds.size();i++)
		 {
			 
			 if((lat<=bounds.get(i).get(0) &&  lat>=bounds.get(i).get(2)) || (lat>=bounds.get(i).get(0) &&  lat<=bounds.get(i).get(2)) )
			 {    if((lon<=bounds.get(i).get(1) &&  lon>=bounds.get(i).get(3)) || (lon>=bounds.get(i).get(1) &&  lon<=bounds.get(i).get(3)) )
				 {
				 if(s>bounds.get(i).get(4))
					 return true;
				 }
			 }
		 }
			 
		 
		 return false;
	 }
	
	 public static ArrayList<String> processRecord(String r) throws JSONException
	 {
		 List<String> records =new ArrayList<String>();
		 ArrayList<String> outrec =new ArrayList<String>();
		 
		 records=  Arrays.asList(r.split("}"));
		 
		 JSONParser parser = new JSONParser();  
		 
		 String alert="";
	
		 String chipID ="";
		   
		 String longitude = "";
		  
	     String latutude = "";
		   
		 String accident ="";
		   
		 String speed = "";
		   
		   // output json
		   
		 JSONObject output= new JSONObject();
		  for (String record : records) 
		  {
			  record+="}";
		
					  try {  
						  // read input json
						
						
						   Object obj = parser.parse(record);
						  
						   JSONObject jsonRead = (JSONObject) obj; 
						  
						   chipID = (String) jsonRead.get("chipID");  
						   
						   longitude = (String) jsonRead.get("longitude");  
						  
						   latutude = (String) jsonRead.get("latitude"); 
						   
						   accident = (String) jsonRead.get("accident"); 
						   
						   speed = (String) jsonRead.get("speed"); 
						   
						
					     
							if(accident.equals("1"))
							   {
								   alert="red";
								   output.put("chipID", chipID);  
							       output.put("longitude", longitude);  
							       output.put("latitude", latutude);  
							       output.put("alert", alert);  
							      
							       
							      outrec.add(output.toString());
							   }
							   else
								   if(checkSpeed(speed,longitude,latutude)==true)
								   { 
									   alert="yellow";
									   output.put("chipID", chipID);  
								       output.put("longitude", longitude);  
								       output.put("latitude", latutude);  
								       output.put("alert", alert);  
								       
								       outrec.add(output.toString());  
								   }
								   else
								   {
									      alert="null";
										  output.put("chipID", chipID);  
									      output.put("longitude", longitude);  
									      output.put("latitude", latutude);  
									      output.put("alert", alert);  
									      
									      outrec.add(output.toString());
								   }
								  
					  
				      }
					  catch (ParseException e) 
					  {  
						  e.printStackTrace();  
					  }  
		  }		 
		  
		  return outrec;
	
	 }

	  public static void main(String[] args) throws IOException {
	    
		  
		  if (args.length < 2) 
		  {
		      System.err.println("Usage: VehicleMonitoringSystem <hostname> <port>");
		      System.exit(1);
	      }
	
		    //populate bounds data 
		    populate();
	        // Create the context with a 1 second batch size
		    SparkConf sparkConf = new SparkConf().setAppName("Vms").setMaster("local[4]");
		    JavaStreamingContext ssc = new JavaStreamingContext(sparkConf,  new Duration(5000));
		    // Create a JavaReceiverInputDStream on target ip:port 
		    JavaReceiverInputDStream<String> chipData = ssc.socketTextStream(args[0], Integer.parseInt(args[1]), StorageLevels.MEMORY_AND_DISK_SER);
		   
		    JavaDStream<String> result = chipData.flatMap(new FlatMapFunction<String,String>(){

				@Override
				public Iterable<String> call(String arg0) throws Exception {
					
					ArrayList<String>  res= processRecord(arg0);
					return res;
				}

				
				
		   }).filter(new Function<String, Boolean>() {
		
			@Override
			public Boolean call(String arg0) throws Exception {
				
				 JSONParser parser = new JSONParser();  
				 Object obj = parser.parse(arg0);
				  
				 JSONObject jsonRead = (JSONObject) obj; 
				  
				 String alert = (String) jsonRead.get("alert");  
				 if(alert.equals("null"))
				 {
					 return false;
				 }
			  
				return true;
			}
		});
		   
		 //  result.fo
		    
		    
	result.foreachRDD(new Function<JavaRDD<String>,Void>(){

		@Override
		public Void call(JavaRDD<String> rdd) throws Exception {
			
			   
			if(rdd.count()>0)
			{
				 //rdd.saveAsTextFile("/home/veda/RDD/rddfile"+(i++));
				List<String> rL = rdd.collect();
				//System.out.println(rL);
				for (String r : rL) {
					System.out.print(r);
				}
				showOnMap(rL);
			}
			return null;
		}
		
		
	});
	
	    
        ssc.start();
	    ssc.awaitTermination();
	  }
}