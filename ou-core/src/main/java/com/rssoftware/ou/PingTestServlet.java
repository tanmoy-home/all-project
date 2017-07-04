package com.rssoftware.ou;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class PingTestServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8632266951988520611L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		PrintWriter writer = resp.getWriter();
		
		System.out.println("PingTestServlet called");
		
		writer.write("<HTML><BODY><P>Ping success</P></BODY></HTML>");
		writer.flush();
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}
}
