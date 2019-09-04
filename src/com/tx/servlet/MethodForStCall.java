package com.tx.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class MethodForStCall extends HttpServlet {

    public MethodForStCall() {
        super();
    }

    public void destroy() {
        super.destroy(); 
    }


    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
        out.println("<HTML>");
        out.println("  <HEAD><TITLE>A Servlet</TITLE></HEAD>");
        out.println("  <BODY>");
        out.print("hello world !  This is ");
        out.print(this.getClass());
        out.println(", using the GET method!");
        out.println("</BODY>");
        out.println("</HTML>");
        out.flush();
        out.close();
        System.out.println("doGet successfully");
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");

        /**
         * Ω” ’json
         */
        BufferedReader reader = request.getReader();
        String json = reader.readLine();
        System.out.println(json);
        reader.close();

        /**
         * ∑µªÿjson
         */
        PrintWriter out = response.getWriter();
     //   out.write("{\"status\":200,\"description\":\"create roles successfully\", \"intput info:\"} ");
        out.write("{\"status_code\":200,\"description\":\"create roles successfully\", \"intput parameters\":");
        out.write(json);
        out.write("} \r\n");
        out.close();

    }

}