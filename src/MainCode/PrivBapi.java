package MainCode;
import javax.crypto.*;
import javax.crypto.spec.*;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Comparator;

@SuppressWarnings("serial")
public class PrivBapi {
	private static final String pubkey = "0";
	private static final String privkey = "0";
	
	
	
	private static String encodeBySHA256(String msg, String key) {
		String result = null;
		Mac sha256_HMAC;

		try {
			sha256_HMAC = Mac.getInstance("HmacSHA256");      
			SecretKeySpec secretkey = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA256");
			sha256_HMAC.init(secretkey);
			byte[] mac_data = sha256_HMAC.doFinal(msg.getBytes("UTF-8"));
			StringBuilder sb = new StringBuilder(mac_data.length * 2);
			for(byte b: mac_data)
				sb.append(String.format("%02x", b & 0xff));
			result = sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	public static String getAuthmsg(String method, String params) {
		String returnmsg = "";
		
		String nonce = String.valueOf(System.currentTimeMillis() );
		String sign = "";	
		String postparams = "key=" + pubkey + "&signature=" + sign + "&nonce=" + nonce;
		if (!params.equals("") )
			postparams += "&" + params;
		
		/*try {
			URL authapiURL = new URL("https://www.bitstamp.net/api/" + method);
			HttpURLConnection uc = (HttpURLConnection) authapiURL.openConnection();
			
			uc.setDoOutput(true);
			uc.setRequestMethod("POST");
			uc.setRequestProperty("Key", pubkey);
			uc.setRequestProperty("Sign", encodeBySHA512(postmsg, privkey));
			uc.setRequestProperty("Content-Length", String.valueOf(postmsg.length()) );
			
			OutputStreamWriter out = new OutputStreamWriter(uc.getOutputStream());
			out.write(postmsg);
			out.flush();
			out.close();
			
			InputStreamReader in = new InputStreamReader(uc.getInputStream());
			BufferedReader br = new BufferedReader(in);
						
			returnmsg = br.readLine();
			
			br.close();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
			BTrader.addLogEntry("Connecting to PrivBapi failed! Postdata:" + postData);
		}*/
				
		return returnmsg;
	}
}
