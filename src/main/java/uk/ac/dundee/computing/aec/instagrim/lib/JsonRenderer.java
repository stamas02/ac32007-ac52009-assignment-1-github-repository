package uk.ac.dundee.computing.aec.instagrim.lib;

import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


import uk.ac.dundee.computing.aec.instagrim.stores.Pic;

import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

public class JsonRenderer 
{
	public static String render(Object obj)
	{
		if (obj == null)
			return "";
		Class c = obj.getClass();
		String className=c.getName();
		if (className.compareTo("java.util.LinkedList")==0){ //Deal with a linked list
			List Data = (List)obj;
			Iterator iterator;
			JSONObject JSONObj=new JSONObject();
			JSONArray Parts=new JSONArray();
			iterator = Data.iterator();     
			while (iterator.hasNext()){
				Object Value=iterator.next();
				JSONObject jobj =ProcessObject(Value);
				try {
					Parts.put(jobj);
				}catch (Exception JSONet){
         			System.out.println("JSON Fault"+ JSONet);
         		}
			}
			try{
				JSONObj.put("Data",Parts);
			}catch (Exception JSONet){
     			System.out.println("JSON Fault"+ JSONet);
     		}
			if (JSONObj!=null){
				System.out.print(JSONObj);
				return JSONObj.toString();
			}	
			
		}else{
			Object Data=obj;
			JSONObject jobj =ProcessObject(Data);
			if (jobj!=null){
				System.out.print(jobj);
				return jobj.toString();
			}	
		}
		return "";
	}
	
	private static JSONObject ProcessObject(Object Value){
		JSONObject Record=new JSONObject();
		
		try {
            Class c = Value.getClass();
            Method methlist[] = c.getDeclaredMethods();
            for (int i = 0; i < methlist.length; i++) {  
            	 Method m = methlist[i];
            	 //System.out.println(m.toString());
            	 String mName=m.getName();
            	
                 if (mName.startsWith("get")==true){
                	 String Name=mName.replaceFirst("get", "");
                	 //Class pvec[] = m.getParameterTypes(); //Get the Parameter types
	                 //for (int j = 0; j < pvec.length; j++)
	                 //   System.out.println("param #" + j + " " + pvec[j]);
	                 //System.out.println(mName+" return type = " +  m.getReturnType());
	                 Class partypes[] = new Class[0];
	                 Method meth = c.getMethod(mName, partypes);
	                
	                 Object rt= meth.invoke(Value);
	                 if (rt!=null){
	                	 System.out.println(Name+" Return "+ rt);
	                	 try{
	                		 Record.put(Name,rt);
	                	 }catch (Exception JSONet){
	             			System.out.println("JSON Fault"+ JSONet);
	             			return null;
	             		}
	             	
	                 }
                 }
            }
            
            
         }
         catch (Throwable e) {
            System.err.println(e);
         }
         return Record;
	}
}
