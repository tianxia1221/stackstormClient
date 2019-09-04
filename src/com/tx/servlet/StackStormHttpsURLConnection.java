package com.tx.servlet;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.security.cert.X509Certificate;
import java.util.Base64;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import javax.security.cert.CertificateException;

//import javax.net.ssl.SSLSession;
//import javax.net.ssl.TrustManager;
//import javax.security.cert.X509Certificate;

import org.json.JSONArray;
import org.json.JSONObject;

public class StackStormHttpsURLConnection {

	private String token;
	private StackStormHttpsURLConnection http;

	public static void main(String[] args) throws Exception {
		StackStormHttpsURLConnection http = new StackStormHttpsURLConnection();

		trustAllHttpsCertificates();
		HttpsURLConnection.setDefaultHostnameVerifier(new CustomizedHostnameVerifier1());

		http.getTokenByPostMethod();
		http.createWorkflowInstanceByPostMethod1();
	}

	public void init() throws Exception {
		http = new StackStormHttpsURLConnection();

		trustAllHttpsCertificates();
		HttpsURLConnection.setDefaultHostnameVerifier(new CustomizedHostnameVerifier1());

		http.getTokenByPostMethod();
		http.createWorkflowInstanceByPostMethod1();
	}

	public int createWorkflow() throws Exception {
		http = new StackStormHttpsURLConnection();

		trustAllHttpsCertificates();
		HttpsURLConnection.setDefaultHostnameVerifier(new CustomizedHostnameVerifier1());

		http.getTokenByPostMethod();
		return http.createWorkflowInstanceByPostMethod1();
	}

	private static void trustAllHttpsCertificates() throws Exception {
		javax.net.ssl.TrustManager[] trustAllCerts = new javax.net.ssl.TrustManager[1];
		javax.net.ssl.TrustManager tm = new miTM1();
		trustAllCerts[0] = tm;
		javax.net.ssl.SSLContext sc = javax.net.ssl.SSLContext.getInstance("SSL");
		sc.init(null, trustAllCerts, null);
		javax.net.ssl.HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
	}

	private void getTokenByPostMethod() throws Exception {
		String url = "https://stackstorm/auth/v1/tokens";
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

	public int createWorkflowInstanceByPostMethod1() throws IOException {
		String url = "https://stackstorm/api/v1/executions";
		// String body = "{\"action\": \"examples.orquesta-ask-idm-workflow\"}";

		String body = "{\"action\": \"examples.orquesta-https-test\",\"user\": \"zhangsan\"}";

		URL obj = new URL(url);
		HttpsURLConnection connection = (HttpsURLConnection) obj.openConnection();

		connection.setHostnameVerifier(new CustomizedHostnameVerifier1());

		// 设置是否向connection输出，因为这个是post请求，参数要放在
		// http正文内，因此需要设为true
		connection.setDoOutput(true);
		// Read from the connection. Default is true.
		connection.setDoInput(true);
		// 默认是 GET方式
		connection.setRequestMethod("POST");
		connection.setRequestProperty("X-Auth-Token", token);
		// connection Post 请求不能使用缓存
		connection.setUseCaches(false);
		// 设置本次连接是否自动重定向
		connection.setInstanceFollowRedirects(true);
		connection.setRequestProperty("Content-Type", "application/json; utf-8");
		// 连接，从postUrl.openConnection()至此的配置必须要在connect之前完成，
		// 要注意的是connection.getOutputStream会隐含的进行connect。
		// connection.connect();

		DataOutputStream out = new DataOutputStream(connection.getOutputStream());
		out.writeBytes(body);
		// 流用完记得关
		out.flush();
		out.close();

		int responseCode = connection.getResponseCode();
		System.out.println("\nSending 'POST' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);

		// 获取响应
		BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		String line;
		while ((line = reader.readLine()) != null) {
			System.out.println(line);
		}
		reader.close();
		// 该干的都干完了,记得把连接断了
		connection.disconnect();

		return responseCode;
	}

	private void createWorkflowInstanceByPostMethod() throws Exception {

		String url = "https://stackstorm/api/v1/executions";
		String body = "{\"action\": \"examples.orquesta-ask-basic\"}";
		// String body = "{action: orquesta-ask-basic}";
		byte[] postDataBytes = body.toString().getBytes("UTF-8");
		URL obj = new URL(url);
		HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

		con.setHostnameVerifier(new CustomizedHostnameVerifier1());

		con.setRequestMethod("POST");
		con.setRequestProperty("X-Auth-Token", token);
		con.setRequestProperty("Accept", "application/json");
		con.setRequestProperty("Content-Type", "application/json; utf-8");
		// con.setRequestProperty("Accept", "application/json; utf-8");
		// con.setRequestProperty("Content-Length",
		// String.valueOf(postDataBytes.length));

		// // Send post request
		// con.setDoOutput(true);
		// DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		// //wr.writeBytes(body);
		// wr.writeChars(body);
		// wr.write(postDataBytes);
		// wr.flush();
		// wr.close();
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

		String url = "https://stackstorm/auth/v1/tokens";

		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// optional default is GET
		con.setRequestMethod("GET");

		// add request header
		// con.setRequestProperty("User-Agent", USER_AGENT);

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

}

class CustomizedHostnameVerifier1 implements HostnameVerifier {

	@Override
	public boolean verify(String urlHostName, SSLSession session) {
		System.out.println("Warning: URL Host: " + urlHostName + " vs. " + session.getPeerHost());
		return true;
	}
}


class miTM1 implements X509TrustManager {
    /*
     * The default X509TrustManager returned by SunX509.  We'll delegate
     * decisions to it, and fall back to the logic in this class if the
     * default X509TrustManager doesn't trust it.
     */
    X509TrustManager sunJSSEX509TrustManager;
    miTM1() throws Exception {
        // create a "default" JSSE X509TrustManager.
        KeyStore ks = KeyStore.getInstance("JKS");
        // ks.load(new FileInputStream("C:\\Workshop\\stackstorm\\stackstormClient\\resource\\st.keystore"),
        ks.load(this.getClass().getResourceAsStream("/st.keystore"),
       // ks.load(new FileInputStream("st.keystore"),
                "111111".toCharArray());
        TrustManagerFactory tmf =
                TrustManagerFactory.getInstance("SunX509", "SunJSSE");
        tmf.init(ks);
        TrustManager tms [] = tmf.getTrustManagers();
            /*
             * Iterate over the returned trustmanagers, look
             * for an instance of X509TrustManager.  If found,
             * use that as our "default" trust manager.
             */
        for (int i = 0; i < tms.length; i++) {
            if (tms[i] instanceof X509TrustManager) {
                sunJSSEX509TrustManager = (X509TrustManager) tms[i];
                return;
            }
        }
            /*
             * Find some other way to initialize, or else we have to fail the
             * constructor.
             */
        throw new Exception("Couldn't initialize");
    }
    /*
     * Delegate to the default trust manager.
     */
    public void checkClientTrusted(X509Certificate[] chain, String authType) throws java.security.cert.CertificateException {
        sunJSSEX509TrustManager.checkClientTrusted(chain, authType);
    }
    /*
     * Delegate to the default trust manager.
     */
    public void checkServerTrusted(X509Certificate[] chain, String authType) throws java.security.cert.CertificateException {
        sunJSSEX509TrustManager.checkServerTrusted(chain, authType);
    }
    /*
     * Merely pass this through.
     */
    public X509Certificate[] getAcceptedIssuers() {
        return sunJSSEX509TrustManager.getAcceptedIssuers();
    }
}
