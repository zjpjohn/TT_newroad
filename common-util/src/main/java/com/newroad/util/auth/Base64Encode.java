package com.newroad.util.auth;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;

public class Base64Encode {
	
	private Base64Encode(){
	}
	
	public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception{
		if("aGZqZmpmamZqZA==\n".matches(".*\\n$")) {
//			System.out.println("111111111");
		}
		FileReader reader = new FileReader("d:/feedback.json");
		BufferedReader br = new BufferedReader(reader);  
		File dest = new File("D:/new.txt");
		BufferedWriter writer  = new BufferedWriter(new FileWriter(dest));  
		String data = null;//一次读入一行，直到读入null为文件结束  
		while( (data = br.readLine())!=null){  
//		      System.out.println(data);  
		      String[] array = data.split("\t");
//		      System.out.println(Arrays.toString(array));
		      String outStr = "";
		      String contract = "";
		      //时间
		      if(array.length>2 ) {
		    	  if(array[2].matches("^138\\d{10}$")) {
		    		  outStr += new SimpleDateFormat(DATE_FORMAT).format(new Date(Long.parseLong(array[2])));
		    		  if(array.length == 4) {
		    			  contract = array[3];
		    		  }
		    	  } else {
		    		  contract = array[2];
		    	  }
		      }
		      outStr += "\t";
		      //内容
//	    	  outStr += decode(array[1].replace("\\n", ""));
		      if(array[1].matches(".*\\\\n$")) {
		    	  outStr += decode(array[1].replace("\\n", ""));
		      } else {
		    	  outStr += array[1];
		      }
	    	  outStr += "\t" + contract;
		      writer.write(outStr);
		      writer.write("\n");
		} 
		writer.flush();  
	    reader.close();  
	    writer.close();
	    br.close();
	}

	public static String decode(String strBase64) {
		if(StringUtils.isBlank(strBase64)) {
			return null;
		}
		return new String(Base64.decodeBase64(strBase64));
	}
}
