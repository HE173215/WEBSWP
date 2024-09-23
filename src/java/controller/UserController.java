/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import service.UserService;

/**
 *
 * @author Do Duan
 */
@WebServlet({"/user", "/profile", "/profile/submit", "/user/submit"})
public class UserController extends HttpServlet {

    private UserService userService;

    public void init() {
        userService = new UserService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getServletPath();
        switch (action) {
            case "/profile":
                showUserProfile(request, response);
                break;
            case "/profile/submit":
                submitUserProfile(request, response);
                break;
            case "/user/submit":
                submitUserChangePass(request, response);
                break;
            case "/user":
                showUserChangePass(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("utf-8");
        doGet(request, response);

    }

    private void showUserProfile(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Gọi service để lấy dữ liệu và đẩy vào request
        HttpSession session = request.getSession();
        String email = (String) session.getAttribute("userEmail");
        System.out.println("Email: " + email);
        request.getRequestDispatcher("/WEB-INF/view/user/profile.jsp").forward(request, response);
    }

    private void submitUserProfile(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Lấy đường dẫn gốc của ứng dụng,sau đó thêm /profile vào URL
        response.sendRedirect(request.getContextPath() + "/profile");
    }

    private void submitUserChangePass(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();

        // Lấy thông tin mật khẩu từ form
        String oldPassword = request.getParameter("oldPassword");
        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");

        // Lấy email từ session (đã được lưu khi người dùng đăng nhập)
        String email = (String) session.getAttribute("userEmail");

        // In ra console để kiểm tra
        System.out.println("Email: " + email);
        System.out.println("Old Password: " + oldPassword);
        System.out.println("New Password: " + newPassword);
        System.out.println("Confirm Password: " + confirmPassword);

        if (email != null) {
            if (newPassword.equals(confirmPassword)) {
                // Gọi service để thực hiện việc đổi mật khẩu thông qua email, mật khẩu cũ và mật khẩu mới
                boolean success = userService.changePassword(email, oldPassword, newPassword);

                if (success) {
                    request.setAttribute("message", "Đổi mật khẩu thành công!");
                } else {
                    request.setAttribute("error", "Mật khẩu cũ không đúng!");
                }
            } else {
                request.setAttribute("error", "Mật khẩu xác nhận không khớp!");
            }
        } else {
            // Nếu không có session hoặc email, chuyển hướng về trang đăng nhập
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // Chuyển tiếp về trang profile
        request.getRequestDispatcher("/WEB-INF/view/user/profile.jsp").forward(request, response);
    }

    private void showUserChangePass(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        if (session != null) {
            // Nếu đã đăng nhập, chuyển hướng đến trang đổi mật khẩu
            request.getRequestDispatcher("/WEB-INF/view/user/ChangePassword.jsp").forward(request, response);
        } else {
            // Nếu chưa đăng nhập, chuyển hướng về trang đăng nhập
            response.sendRedirect(request.getContextPath() + "/login");
        }
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
