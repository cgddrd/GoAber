/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JSF.device;

import DeviceApi.DeviceApi;
import GoAberDatabase.User;
import JSF.auth.AuthController;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Craig
 */
@WebServlet(name = "JawboneCallbackServlet", urlPatterns = {"/JawboneCallbackServlet"})
public class JawboneCallbackServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //Check if the user have rejected 
        String error = request.getParameter("error"); 
        if ((null != error) && ("access_denied".equals(error.trim()))) { 
            HttpSession session = request.getSession(); 
            session.invalidate(); 
            response.sendRedirect(request.getContextPath()); 
            return; 
        }
      
        String code = request.getParameter("code");
        DeviceApi deviceApi = (DeviceApi)request.getSession().getAttribute("DeviceApi");
        AuthController authController = (AuthController) request.getSession().getAttribute("authController");
        User user = authController.getActiveUser();
        deviceApi.getAndSaveTokens(code, user);
        
        response.sendRedirect("faces/Jawbone/index.xhtml");
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
