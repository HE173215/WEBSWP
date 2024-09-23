/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import dao.SettingDAO;
import java.util.List;
import model.Setting;

/**
 *
 * @author vqman
 */
public class SettingService {

    private SettingDAO settingDAO;

    // Constructor khởi tạo SettingDAO
    public SettingService() {
        this.settingDAO = new SettingDAO();
    }

    // Lấy tất cả cài đặt với phân trang
    public List<Setting> getAllSettings(int offset, int pageSize) {
        return settingDAO.getAllSettings(offset, pageSize);
    }

    // Lấy thông tin cài đặt dựa vào ID
    public Setting getSettingById(int id) {
        return settingDAO.getSettingById(id);
    }

    // Tìm kiếm cài đặt dựa theo tên với phân trang
    public List<Setting> searchSettingByName(String name, int offset, int pageSize) {
        return settingDAO.searchSettingByName(name, offset, pageSize);
    }

    // Đếm tổng số bản ghi khi tìm kiếm theo tên
    public int countSettingsByName(String name) {
        return settingDAO.countSettingsByName(name);
    }

    // Lấy cài đặt theo loại với phân trang
    public List<Setting> getSettingsByType(String type, int offset, int pageSize) {
        return settingDAO.getSettingsByType(type, offset, pageSize);
    }
    
    public List<Setting> getSettingsByType(String type) {
        return settingDAO.getSettingsByType(type);
    }

    // Đếm tổng số bản ghi khi lọc theo loại
    public int countSettingsByType(String type) {
        return settingDAO.countSettingsByType(type);
    }

    public List<Setting> getSettingsByStatus(int status, int offset, int pageSize) {
        return settingDAO.getSettingsByStatus(status, offset, pageSize);
    }

    public int countSettingsByStatus(int status) {
        return settingDAO.countSettingsByStatus(status);
    }

    // Thêm mới cài đặt
    public void addSetting(Setting setting) {
        // Có thể thêm logic kiểm tra dữ liệu trước khi thêm vào database
        settingDAO.addSetting(setting);
    }

    // Cập nhật cài đặt
    public void updateSetting(Setting setting) {
        if (setting != null && setting.getId() > 0) {
            // Gọi phương thức cập nhật từ DAO
            settingDAO.updateSetting(setting);
        } else {
            System.out.println("Dữ liệu không hợp lệ để cập nhật.");
        }
    }

    // Lấy tất cả các loại (types) cài đặt
    public List<String> getAllTypes() {
        return settingDAO.getAllTypes();
    }

    // Đếm tổng số bản ghi
    public int countAll() {
        return settingDAO.countAll();
    }
}
