/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import jakarta.servlet.RequestDispatcher;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import model.Setting;
import service.SettingService;

/**
 *
 * @author Do Duan
 */
@WebServlet({"/setting", "/setting/new", "/setting/edit", "/setting/save"})
public class SettingController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final String jspList = "/WEB-INF/view/setting/list.jsp";
    private final String jspForm = "/WEB-INF/view/setting/form.jsp";

    private SettingService settingService;

    @Override
    public void init() {
        settingService = new SettingService();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        save(req, res);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        String action = req.getServletPath();
        try {
            switch (action) {
                case "/setting/new":
                    showNewForm(req, res);
                    break;
                case "/setting/edit":
                    showEditForm(req, res);
                    break;
                case "/setting/save":
                    save(req, res);
                    break;
                case "/setting":
                    showList(req, res);
                    break;
                default:
                    res.sendRedirect("login");
                    break;
            }
        } catch (Exception ex) {
            throw new ServletException(ex);
        }
    }

    private void showList(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        HttpSession session = req.getSession();
        String email = (String) session.getAttribute("userEmail");
        int page = 1;
        int pageSize = 5;

        // Lấy tham số trang nếu có
        if (req.getParameter("page") != null) {
            try {
                page = Integer.parseInt(req.getParameter("page"));
            } catch (NumberFormatException e) {
                page = 1;
            }
        }

        int offset = (page - 1) * pageSize;

        // Lấy tham số từ request
        String typeFilter = req.getParameter("typeFilter");
        String searchQuery = req.getParameter("searchQuery");
        String statusFilter = req.getParameter("statusFilter"); // Thêm lọc status

        List<Setting> settingList;
        int totalRecords;

        // Lọc theo status nếu có giá trị statusFilter
        if (statusFilter != null && !statusFilter.isEmpty()) {
            int status = Integer.parseInt(statusFilter);
            settingList = settingService.getSettingsByStatus(status, offset, pageSize);
            totalRecords = settingService.countSettingsByStatus(status);
        } else if (searchQuery != null && !searchQuery.isEmpty()) {
            settingList = settingService.searchSettingByName(searchQuery, offset, pageSize);
            totalRecords = settingService.countSettingsByName(searchQuery);
        } else if (typeFilter != null && !typeFilter.isEmpty()) {
            settingList = settingService.getSettingsByType(typeFilter, offset, pageSize);
            totalRecords = settingService.countSettingsByType(typeFilter);
        } else {
            settingList = settingService.getAllSettings(offset, pageSize);
            totalRecords = settingService.countAll();
        }

        int totalPages = (int) Math.ceil((double) totalRecords / pageSize);

        req.setAttribute("settingList", settingList);
        req.setAttribute("currentPage", page);
        req.setAttribute("totalPages", totalPages);
        req.setAttribute("statusFilter", statusFilter); // Truyền statusFilter để giữ lại giá trị sau khi chọn
        List<String> types = settingService.getAllTypes();
        req.setAttribute("types", types);
        RequestDispatcher dispatcher = req.getRequestDispatcher(jspList);
        dispatcher.forward(req, res);
    }

    private void showNewForm(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = req.getRequestDispatcher(jspForm);
        dispatcher.forward(req, res);
    }

    private void showEditForm(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        Setting currentSetting = settingService.getSettingById(id);
        req.setAttribute("setting", currentSetting);
        RequestDispatcher dispatcher = req.getRequestDispatcher(jspForm);
        dispatcher.forward(req, res);
    }

    private Setting getInputForm(HttpServletRequest req) {
        Setting inputSetting = new Setting();
        inputSetting.setSettingName(req.getParameter("name"));
        inputSetting.setType(req.getParameter("type"));

        String priorityStr = req.getParameter("priority");
        if (priorityStr != null && !priorityStr.isEmpty()) {
            inputSetting.setPriority(Integer.parseInt(priorityStr));
        } else {
            inputSetting.setPriority(0); // Giá trị mặc định
        }
        String status = req.getParameter("status");
        inputSetting.setStatus("Active".equals(status) ? 1 : 0);

        inputSetting.setDescription(req.getParameter("description"));

        return inputSetting;
    }

    private void save(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        String idStr = req.getParameter("id");
        Setting inputSetting = getInputForm(req);

        if (idStr == null || idStr.isEmpty()) {
            // Thực hiện insert
            settingService.addSetting(inputSetting);
        } else {
            // Thực hiện update
            inputSetting.setId(Integer.parseInt(idStr));
            settingService.updateSetting(inputSetting);
        }
        res.sendRedirect(req.getContextPath() + "/setting");
    }
}
