package ru.web.servlets;

import ru.web.controllers.SearchController;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

@WebServlet(name = "ShowImage",urlPatterns = "/ShowImage")
public class ShowImage extends HttpServlet {
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
response.setContentType("image/jpeg");
        OutputStream out=response.getOutputStream();

        try{
            int id=Integer.valueOf(request.getParameter("id"));
            SearchController controller=(SearchController)
                    request.getSession(false).getAttribute("searchController");
            byte[] image=controller.getImage(id);
            response.setContentLength(image.length);
                    out.write(image);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            out.close();
        }
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
processRequest(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request,response);

    }
}
