package com.ikasyk;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import com.ikasyk.utils.ListController;

public class ExpireSendHandler extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setHeader("Content-Type", "text/html;charset=utf-8");
        PrintWriter out = resp.getWriter();
        String line = req.getQueryString();
        if (line != null)
            out.print("<h1>Added line [<a href=\"/show\" target=\"_blank\">see all</a>]:<br/><pre>" + line + "</pre>");
        else
            out.print("<h1>Please, enter your line as /show?&lt;my_line&gt;.");
        ListController.add(req.getQueryString());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        doGet(req, resp);
    }

}
