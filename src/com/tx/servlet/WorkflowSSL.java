package com.tx.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class WorkflowSSL extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private StackStormHttpsURLConnection http;
	
	public void init() throws ServletException {
		http = new StackStormHttpsURLConnection();
//		try {
//			http.init();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// set response headers
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");

		// create HTML form
		PrintWriter writer = response.getWriter();
		writer.append("<!DOCTYPE html>\r\n").append("<html>\r\n").append("		<head>\r\n")
				.append("			<title>Form input</title>\r\n").append("		</head>\r\n")
				.append("		<body>\r\n").append("			<form action=\"workflowssl\" method=\"POST\">\r\n")
				.append("				Enter your name: \r\n")
				.append("				<input type=\"text\" name=\"user\" />\r\n")
				.append("				<input type=\"submit\" value=\"Submit\" />\r\n")
				.append("			</form>\r\n").append("		</body>\r\n").append("</html>\r\n");
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		
		
		String user = request.getParameter("user");

		//int resCode = http.createWorkflowSSLInstanceByPostMethod1();
		int resCode = 0;
		try {
			resCode = http.createWorkflow(user);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");

		// create HTML response
		PrintWriter writer = response.getWriter();
		writer.append("<!DOCTYPE html>\r\n").append("<html>\r\n").append("		<head>\r\n")
				.append("			<title>Welcome message</title>\r\n").append("		</head>\r\n")
				.append("		<body>\r\n");
		if (user != null && !user.trim().isEmpty() && resCode >=200 && resCode<300) {
			writer.append("	Welcome " + user + ".\r\n");
			writer.append("	A WorkflowSSL instanse has been created.\r\n");
		} else {
			writer.append("	You did not entered a name!\r\n");
		}
		writer.append("		</body>\r\n").append("</html>\r\n");
	}

	public void destroy() {
	}
}
