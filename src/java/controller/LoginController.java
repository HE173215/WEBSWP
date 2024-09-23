package controller;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import dao.UserDAO;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URLEncoder;
import model.User;
import service.UserService;

@WebServlet({"/register", "/register/submit", "/login", "/login/submit", "/logout", "/otp", "/otp/submit", "/otp/reset", "/otp/reset/submit", "/sendOtp", "/resetPassword", "/googleLogin"})
public class LoginController extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private UserService userService; // Khởi tạo UserService

    public void init() {
        userService = new UserService(); // Khởi tạo UserService khi servlet khởi động
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String action = req.getServletPath();
        switch (action) {
            case "/register":
                showRegisterForm(req, res);
                break;
            case "/login":
                showLoginForm(req, res);
                break;
            case "/logout":
                handleLogout(req, res);
                break;
            case "/otp":
                showOtpForm(req, res);
                break;
            case "/resetPassword":
                showResetPasswordForm(req, res);
                break;
            case "/googleLogin":
                handleGoogleLogin(req, res);
                break;
        }
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String action = req.getServletPath();
        switch (action) {
            case "/register/submit":
                registerUser(req, res);
                break;
            case "/login/submit":
                authenticate(req, res);
                break;
            case "/otp/submit":
                handleOtpSubmission(req, res);
                break;
            case "/otp/reset/submit":
                handleOtpForPasswordReset(req, res);
                break;
            case "/sendOtp":
                sendOtp(req, res);
                break;
        }
    }

    private void showRegisterForm(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String email = req.getParameter("email"); // Lấy email từ tham số URL
        req.setAttribute("email", email); // Đặt email vào request attribute
        forwardToPage(req, res, "WEB-INF/view/login/register.jsp");
    }

    private void registerUser(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        req.setCharacterEncoding("UTF-8");
        String username = req.getParameter("username").trim();
        String pass = req.getParameter("password").trim();
        String confirmPass = req.getParameter("confirmPass").trim();
        String email = req.getParameter("email").trim();
        String name = req.getParameter("name").trim();
        String phone = req.getParameter("mobile").trim();

        try {
            String errorMessage = userService.validateRegisterInfo(username, pass, confirmPass, email, name, phone);
            if (!errorMessage.isEmpty()) {
                req.getSession().setAttribute("errorMessage", errorMessage);
                res.sendRedirect(req.getContextPath() + "/register");
                return;
            }

            if (userService.isUserExist(username, email)) {
                req.getSession().setAttribute("errorMessage", "Tên người dùng hoặc email đã tồn tại!");
                res.sendRedirect(req.getContextPath() + "/register");
                return;
            }

            String registeredEmail = userService.register(username, pass, email, name, phone);
            req.getSession().setAttribute("email", registeredEmail);
            res.sendRedirect(req.getContextPath() + "/otp");

        } catch (Exception e) {
            req.getSession().setAttribute("errorMessage", "Có lỗi xảy ra: " + e.getMessage());
            forwardToPage(req, res, "WEB-INF/view/login/register.jsp");
        }
    }

    private void forwardToPage(HttpServletRequest req, HttpServletResponse res, String page) throws ServletException, IOException {
        RequestDispatcher dispatcher = req.getRequestDispatcher(page);
        dispatcher.forward(req, res);
    }

    private void showLoginForm(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        forwardToPage(req, res, "WEB-INF/view/login/loginpage.jsp");
    }

    private void authenticate(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        HttpSession session = req.getSession();
        String enteredUsername = req.getParameter("username");
        String enteredPassword = req.getParameter("password");
        User user;

        try {
            user = userService.login(enteredUsername, enteredPassword);
        } catch (Exception e) {
            session.setAttribute("errorMessage", "Lỗi đăng nhập: " + e.getMessage());
            res.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        if (user != null) {
            if (user.getStatus() == 0) {
                session.setAttribute("errorMessage", "Truy cập bị từ chối!");
                res.sendRedirect(req.getContextPath() + "/login");
            } else {
                session.setAttribute("currentUser", user);
                session.setAttribute("userEmail", user.getEmail());
                res.sendRedirect(req.getContextPath() + "/setting");
            }
        } else {
            session.setAttribute("errorMessage", "Đăng nhập thất bại. Vui lòng thử lại.");
            res.sendRedirect(req.getContextPath() + "/login");
        }
    }

    private void handleLogout(HttpServletRequest req, HttpServletResponse res) throws IOException {
        HttpSession session = req.getSession();
        session.invalidate();
        res.sendRedirect(req.getContextPath() + "/login");
    }

    private void showOtpForm(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        forwardToPage(req, res, "/WEB-INF/view/login/enterotp.jsp");
    }

    private void handleOtpSubmission(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        HttpSession session = req.getSession();
        session.removeAttribute("errorMessage");
        String email = (String) session.getAttribute("email");
        String enteredOtp = req.getParameter("otp");

        try {
            boolean isValidOtp = userService.verifyOTP(email, enteredOtp);
            if (isValidOtp) {
                userService.activateUser(email);
                User user = userService.getUserByEmail(email);
                session.setAttribute("currentUser", user);
                res.sendRedirect(req.getContextPath() + "/login");
            } else {
                req.setAttribute("errorMessage", "OTP không hợp lệ. Vui lòng thử lại.");
                showOtpForm(req, res); // Hiển thị lại form nhập OTP mà không kích hoạt tài khoản
            }
        } catch (Exception e) {
            req.setAttribute("errorMessage", "Có lỗi xảy ra: " + e.getMessage());
            showOtpForm(req, res);
        }
    }

    private void showResetPasswordForm(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        forwardToPage(req, res, "/WEB-INF/view/login/resetPassword.jsp");
    }

    private void handleOtpForPasswordReset(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String enteredOtp = req.getParameter("otp");
        String newPassword = req.getParameter("password");
        HttpSession session = req.getSession();
        String email = (String) session.getAttribute("email");

        try {
            boolean isValidOtp = userService.verifyOTP(email, enteredOtp);
            if (isValidOtp) {
                boolean isPasswordUpdated = userService.updatePassword(email, newPassword);
                if (isPasswordUpdated) {
                    session.setAttribute("resetMessage", "Mật khẩu đã được cập nhật thành công.");
                    res.sendRedirect(req.getContextPath() + "/login");
                } else {
                    session.setAttribute("resetError", "Có lỗi xảy ra khi cập nhật mật khẩu.");
                    forwardToResetPasswordPage(req, res);
                }
            } else {
                session.setAttribute("resetError", "OTP không hợp lệ. Vui lòng thử lại.");
                forwardToResetPasswordPage(req, res);
            }
        } catch (Exception e) {
            session.setAttribute("resetError", "Có lỗi xảy ra: " + e.getMessage());
            forwardToResetPasswordPage(req, res);
        }
    }

    private void forwardToResetPasswordPage(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/view/login/resetPassword.jsp");
        dispatcher.forward(req, res);
    }

    private void sendOtp(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String email = req.getParameter("email").trim();
        HttpSession session = req.getSession();

        boolean isSent = userService.sendOTP(email);
        if (isSent) {
            session.setAttribute("isOtpValid", true);
            session.setAttribute("otpMessage", "Mã OTP đã được gửi đến email của bạn.");
            session.setAttribute("email", email);
        } else {
            session.setAttribute("otpError", "Không thể gửi mã OTP. Vui lòng kiểm tra lại email.");
        }

        res.sendRedirect(req.getContextPath() + "/resetPassword");
    }

    private void handleGoogleLogin(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String code = req.getParameter("code");
        if (code != null) {
            String accessToken = userService.getAccessToken(code);
            User user = userService.getUserInfo(accessToken);
            if (user != null) {
                // Lưu email vào session để sử dụng trong trang đăng ký
                req.getSession().setAttribute("email", user.getEmail());
                System.out.println("Email trong session: " + user.getEmail()); // Thêm dòng này
                res.sendRedirect(req.getContextPath() + "/register?email=" + user.getEmail());
            } else {
                res.sendRedirect(req.getContextPath() + "/login?error=google");
            }
        } else {
            res.sendRedirect("https://accounts.google.com/o/oauth2/auth?scope=email&redirect_uri=http://localhost:8080/Project_Evaluation_System/register&response_type=code&client_id=582129044725-417tt15qkg6cbq4tshm2qj2i3d01f0qk.apps.googleusercontent.com&approval_prompt=force"); // URL xác thực Google
        }
    }

    

}
