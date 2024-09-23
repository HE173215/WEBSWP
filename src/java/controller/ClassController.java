/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import model.User;
import service.ClassService;
import model.Class;
import model.Group;
import model.Setting;
import service.GroupService;
import service.SettingService;

/**
 *
 * @author Do Duan
 */
@WebServlet(urlPatterns = {"/dashboard"})
public class ClassController extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private ClassService classService;
    private SettingService settingService;
    private GroupService groupService;

    public void init() {
        classService = new ClassService();
        settingService = new SettingService();
        groupService = new GroupService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getServletPath();
        switch (action) {
            case "/dashboard":
                showDashboard(request, response);
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

    private void showDashboard(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("currentUser");

        // Kiểm tra xem người dùng đã đăng nhập chưa
//        if (currentUser == null) {
//            response.sendRedirect(request.getContextPath() + "/login"); // Chuyển đến trang đăng nhập nếu chưa đăng nhập
//            return;
//        }
//        int userID = currentUser.getId();

        List<Setting> semesterList = settingService.getSettingsByType("Semester");
        List<Group> subjectList = null;

        request.setAttribute("semesterList", semesterList);
        request.setAttribute("subjectList", subjectList);

        try {
             int selectedSemesterID = Integer.parseInt(request.getParameter("semesterID")); // Nhận semesterID từ JSP
             request.setAttribute("selectedSemesterID", selectedSemesterID); // Lưu trữ giá trị selectedSemesterId trong request
        System.out.println(selectedSemesterID);

//        int groupID = Integer.parseInt(request.getParameter("groupID_raw"));
//        String searchString = request.getParameter("searchString");
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        
        
        //Phan trang
        int page = 1;
        int pageSize = 5;
        // Lấy tham số trang nếu có
        if (request.getParameter("page") != null) {
            try {
                page = Integer.parseInt(request.getParameter("page"));
            } catch (NumberFormatException e) {
                page = 1;
            }
        }

        int offset = (page - 1) * pageSize;

        List<Class> classList = classService.getClassList(1, 1, 3, "");
        // Chuyển tiếp đến trang dashboard
//        request.setAttribute("user", currentUser); // Gửi thông tin người dùng đến JSP
        request.setAttribute("classList", classList);
        request.getRequestDispatcher("/WEB-INF/view/class/dashboard.jsp").forward(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
