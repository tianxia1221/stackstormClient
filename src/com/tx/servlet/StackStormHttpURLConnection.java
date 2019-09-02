package com.tx.servlet;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.cert.X509Certificate;
import java.util.Base64;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;

//import javax.net.ssl.SSLSession;
//import javax.net.ssl.TrustManager;
//import javax.security.cert.X509Certificate;

import org.json.JSONArray;
import org.json.JSONObject;

class CustomizedHostnameVerifier implements HostnameVerifier {

	@Override
    public boolean verify(String urlHostName, SSLSession session) {
        System.out.println("Warning: URL Host: " + urlHostName + " vs. " + session.getPeerHost());
        return true;
    }
}

class miTM implements javax.net.ssl.TrustManager, javax.net.ssl.X509TrustManager {
    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
        return null;
    }

    public boolean isServerTrusted(java.security.cert.X509Certificate[] certs) {
        return true;
    }

    public boolean isClientTrusted(java.security.cert.X509Certificate[] certs) {
        return true;
    }

    public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType)
            throws java.security.cert.CertificateException {
        return;
    }

    public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType)
            throws java.security.cert.CertificateException {
        return;
    }
}

public class StackStormHttpURLConnection {

	private final String USER_AGENT = "Mozilla/5.0";
	private String token = "eee216fe624140cc8ebd0f0930e35038";

	public static void main(String[] args) throws Exception {
		StackStormHttpURLConnection http = new StackStormHttpURLConnection();
		
		trustAllHttpsCertificates();
		HttpsURLConnection.setDefaultHostnameVerifier(new CustomizedHostnameVerifier());	
		
		http.getTokenByPostMethod();
//		http.createWorkflowInstanceByPostMethod();
		http.readContentFromPost();
	}

	
    private static void trustAllHttpsCertificates() throws Exception {
        javax.net.ssl.TrustManager[] trustAllCerts = new javax.net.ssl.TrustManager[1];
        javax.net.ssl.TrustManager tm = new miTM();
        trustAllCerts[0] = tm;
        javax.net.ssl.SSLContext sc = javax.net.ssl.SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, null);
        javax.net.ssl.HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
    }
    
	private void getTokenByPostMethod() throws Exception {
		String url = "https://10.10.10.10/auth/v1/tokens";
		String authString = "st2admin:Ch@ngeMe";		

			  
		URL obj = new URL(url);
		HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

		con.setRequestMethod("POST");
		String encoded = Base64.getEncoder().encodeToString(authString.getBytes(StandardCharsets.UTF_8));
		con.setRequestProperty("Authorization", "Basic " + encoded);
		con.setRequestProperty("Accept", "application/json");

		// Send post request
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());		
		wr.flush();
		wr.close();

		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'POST' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
	    JSONObject jsonObject = new JSONObject(response.toString());
	    token = (String) jsonObject.get("token");

		System.out.println(response.toString());
	}
	
	 public  void readContentFromPost() throws IOException  {
		 
		 
			String url = "https://10.10.10.10/api/v1/executions";
			String body = "{\"action\": \"examples.orquesta-ask-basic\"}";
			//String body = "{action: orquesta-ask-basic}";
			byte[] postDataBytes = body.toString().getBytes("UTF-8");
			URL obj = new URL(url);
			HttpsURLConnection connection = (HttpsURLConnection) obj.openConnection();

			connection.setHostnameVerifier(new CustomizedHostnameVerifier());
		 
   
	        // 设置是否向connection输出，因为这个是post请求，参数要放在
	        // http正文内，因此需要设为true
	        connection.setDoOutput(true);
	        // Read from the connection. Default is true.
	        connection.setDoInput(true);
	        // 默认是 GET方式
	        connection.setRequestMethod("POST");      
	        connection.setRequestProperty("X-Auth-Token", token);
	        //connection Post 请求不能使用缓存
	        connection.setUseCaches(false);  
	           //设置本次连接是否自动重定向 
	        connection.setInstanceFollowRedirects(true);      
	        // 配置本次连接的Content-type，配置为application/x-www-form-urlencoded的
	        // 意思是正文是urlencoded编码过的form参数
	        connection.setRequestProperty("Content-Type", "application/json; utf-8");
	        // 连接，从postUrl.openConnection()至此的配置必须要在connect之前完成，
	        // 要注意的是connection.getOutputStream会隐含的进行connect。
	        connection.connect();
	        DataOutputStream out = new DataOutputStream(connection
	                .getOutputStream());
	        // 正文，正文内容其实跟get的URL中 '? '后的参数字符串一致
	      //  String content = "字段名=" + URLEncoder.encode("字符串值", "编码");
	        // DataOutputStream.writeBytes将字符串中的16位的unicode字符以8位的字符形式写到流里面
	        out.writeBytes(body);
	        //流用完记得关
	        out.flush();
	        out.close();
	        
	        
			int responseCode = connection.getResponseCode();
			System.out.println("\nSending 'POST' request to URL : " + url);
			System.out.println("Response Code : " + responseCode);
	        
	        
	        //获取响应
	        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
	        String line;
	        while ((line = reader.readLine()) != null){
	            System.out.println(line);
	        }
	        reader.close();
	        //该干的都干完了,记得把连接断了
	        connection.disconnect();
	}
	 



	private void createWorkflowInstanceByPostMethod() throws Exception {

		String url = "https://10.10.10.10/api/v1/executions";
		String body = "{\"action\": \"examples.orquesta-ask-basic\"}";
		//String body = "{action: orquesta-ask-basic}";
		byte[] postDataBytes = body.toString().getBytes("UTF-8");
		URL obj = new URL(url);
		HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

		con.setHostnameVerifier(new CustomizedHostnameVerifier());

		con.setRequestMethod("POST");
		con.setRequestProperty("X-Auth-Token", token);
		con.setRequestProperty("Accept", "application/json");
//		con.setRequestProperty("Content-Type", "application/json; utf-8");
//		con.setRequestProperty("Accept", "application/json; utf-8");
     //   con.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));

//		// Send post request
//		con.setDoOutput(true);
//		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
//		//wr.writeBytes(body);
//		wr.writeChars(body);
//		wr.write(postDataBytes);
//		wr.flush();
//		wr.close();
        // 设置是否向HttpURLConnection输出
		con.setDoOutput(true);
        // 设置是否从httpUrlConnection读入
		con.setDoInput(true);

        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(con.getOutputStream(), "UTF-8"));
        writer.write(body);
        writer.close();


		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'POST' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		// print result
		System.out.println(response.toString());
	}

	// HTTP GET request
	private void getsample() throws Exception {

		String url = "https://10.10.10.10/auth/v1/tokens";

		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// optional default is GET
		con.setRequestMethod("GET");

		// add request header
		con.setRequestProperty("User-Agent", USER_AGENT);

		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		// print result
		System.out.println(response.toString());

	}

	// // HTTP POST request
	// private void getTokenByPostMethod() throws Exception {
	//
	// String url = "https://10.10.10.10/auth/v1/tokens";
	// URL obj = new URL(url);
	// HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
	//
	// //add reuqest header
	// con.setRequestMethod("POST");
	// con.setRequestProperty("User-Agent", USER_AGENT);
	// con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
	//
	// String urlParameters = "sn=C02G8416DRJM&cn=&locale=&caller=&num=12345";
	//
	// // Send post request
	// con.setDoOutput(true);
	// DataOutputStream wr = new DataOutputStream(con.getOutputStream());
	// wr.writeBytes(urlParameters);
	// wr.flush();
	// wr.close();
	//
	// int responseCode = con.getResponseCode();
	// System.out.println("\nSending 'POST' request to URL : " + url);
	// System.out.println("Post parameters : " + urlParameters);
	// System.out.println("Response Code : " + responseCode);
	//
	// BufferedReader in = new BufferedReader(
	// new InputStreamReader(con.getInputStream()));
	// String inputLine;
	// StringBuffer response = new StringBuffer();
	//
	// while ((inputLine = in.readLine()) != null) {
	// response.append(inputLine);
	// }
	// in.close();
	//
	// //print result
	// System.out.println(response.toString());
	//
	// }

}
