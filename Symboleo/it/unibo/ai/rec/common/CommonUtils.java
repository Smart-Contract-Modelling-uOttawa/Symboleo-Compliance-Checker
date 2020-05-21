package it.unibo.ai.rec.common;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class CommonUtils { 
	public static String[] splitInParts(String str) {
		String[] parts = new String[2];
		char[] array = str.toCharArray();
		int parNo = 0;
		parts[0] = "";
		int i;
		for(i = 0; i < array.length; i++) {
			char c = array[i];
			if(c == ',' && parNo == 0)
				break;
			else if(c == '(')
				parNo++;
			else if(c == ')')
				parNo--;
			parts[0] = parts[0] + c;
		}
		parts[1] = str.substring(i+1,str.length());
		return parts;
	}
	
	
	public static String readFromResource(String resource) throws Exception {
		InputStream stream = CommonUtils.class.getResourceAsStream(resource);
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
		String line = "";
		StringBuffer b = new StringBuffer();
		while((line = reader.readLine()) != null)
			b.append(line+"\n");
		stream.close();
		return b.toString();
	}
}
