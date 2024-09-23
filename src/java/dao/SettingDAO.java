/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Setting;
import util.JDBCUtils;

public class SettingDAO {

    // Phương thức để lấy tất cả cài đặt từ cơ sở dữ liệu
//    public List<Setting> getAllSettings() {
//        List<Setting> settings = new ArrayList<>();
//        String sql = "SELECT * FROM Setting";
//
//        try (Connection conn = JDBCUtils.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
//            while (rs.next()) {
//                Setting setting = new Setting();
//                setting.setId(rs.getInt("id"));
//                setting.setSettingName(rs.getString("setting_name"));
//                setting.setType(rs.getString("type"));
//                setting.setDescription(rs.getString("description"));
//                setting.setPriority(rs.getInt("priority"));
//                setting.setStatus(rs.getInt("status"));
//                settings.add(setting);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return settings;
//    }
    public List<Setting> getAllSettings(int offset, int pageSize) {
        List<Setting> settings = new ArrayList<>();
        String sql = "SELECT * FROM setting LIMIT ? OFFSET ?";

        try (Connection conn = JDBCUtils.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Thiết lập giá trị cho LIMIT và OFFSET
            stmt.setInt(1, pageSize);
            stmt.setInt(2, offset);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Setting setting = new Setting();
                    setting.setId(rs.getInt("id"));
                    setting.setSettingName(rs.getString("setting_name"));
                    setting.setType(rs.getString("type"));
                    setting.setPriority(rs.getInt("priority"));
                    setting.setStatus(rs.getInt("status"));
                    // Thêm setting vào danh sách
                    settings.add(setting);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return settings;
    }

    // Đếm tổng số bản ghi
    public int countAll() {
        String sql = "SELECT COUNT(*) AS total FROM setting";
        int totalRecords = 0;

        try (Connection conn = JDBCUtils.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                totalRecords = rs.getInt("total");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return totalRecords;
    }

    public Setting getSettingById(int id) {
        Setting setting = null;
        String query = "SELECT * FROM setting WHERE id = ?";

        try (Connection conn = JDBCUtils.getConnection(); PreparedStatement preparedStatement = conn.prepareStatement(query)) {

            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                setting = new Setting();
                setting.setId(rs.getInt("id"));
                setting.setSettingName(rs.getString("setting_name"));
                setting.setType(rs.getString("type"));
                setting.setDescription(rs.getString("description"));
                setting.setPriority(rs.getInt("priority"));
                setting.setStatus(rs.getInt("status"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return setting;
    }

    public List<Setting> searchSettingByName(String name, int offset, int pageSize) {
        List<Setting> settings = new ArrayList<>();
        String query = "SELECT * FROM setting WHERE setting_name LIKE ? LIMIT ? OFFSET ?";

        try (Connection conn = JDBCUtils.getConnection(); PreparedStatement preparedStatement = conn.prepareStatement(query)) {

            // Sử dụng ký tự đại diện '%' cho việc tìm kiếm tên cài đặt
            preparedStatement.setString(1, "%" + name + "%");
            preparedStatement.setInt(2, pageSize);
            preparedStatement.setInt(3, offset);

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                Setting setting = new Setting();
                setting.setId(rs.getInt("id"));
                setting.setSettingName(rs.getString("setting_name"));
                setting.setType(rs.getString("type"));
                setting.setDescription(rs.getString("description"));
                setting.setPriority(rs.getInt("priority"));
                setting.setStatus(rs.getInt("status"));
                settings.add(setting);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return settings;
    }

    public int countSettingsByName(String name) {
        String query = "SELECT COUNT(*) AS total FROM setting WHERE setting_name LIKE ?";
        int totalRecords = 0;

        try (Connection conn = JDBCUtils.getConnection(); PreparedStatement preparedStatement = conn.prepareStatement(query)) {

            preparedStatement.setString(1, "%" + name + "%");
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                totalRecords = rs.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return totalRecords;
    }

    public void addSetting(Setting setting) {
        String query = "INSERT INTO setting (setting_name, type, description, priority, status) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = JDBCUtils.getConnection(); PreparedStatement preparedStatement = conn.prepareStatement(query)) {

            preparedStatement.setString(1, setting.getSettingName());
            preparedStatement.setString(2, setting.getType());
            preparedStatement.setString(3, setting.getDescription());
            preparedStatement.setInt(4, setting.getPriority());
            preparedStatement.setInt(5, setting.getStatus());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateSetting(Setting setting) {
        String query = "UPDATE setting SET setting_name = ?, type = ?, description = ?, priority = ?, status = ? WHERE id = ?";
        try (Connection conn = JDBCUtils.getConnection(); PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setString(1, setting.getSettingName());
            preparedStatement.setString(2, setting.getType());
            preparedStatement.setString(3, setting.getDescription());
            preparedStatement.setInt(4, setting.getPriority());
            preparedStatement.setInt(5, setting.getStatus());
            preparedStatement.setInt(6, setting.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<String> getAllTypes() {
        List<String> types = new ArrayList<>();
        String query = "SELECT DISTINCT type FROM setting";

        try (Connection conn = JDBCUtils.getConnection(); PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                types.add(rs.getString("type"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return types;
    }

    public List<Setting> getSettingsByType(String type, int offset, int pageSize) {
        List<Setting> settings = new ArrayList<>();
        String query = "SELECT * FROM setting WHERE type = ? LIMIT ? OFFSET ?";

        try (Connection conn = JDBCUtils.getConnection(); PreparedStatement preparedStatement = conn.prepareStatement(query)) {

            preparedStatement.setString(1, type);
            preparedStatement.setInt(2, pageSize);
            preparedStatement.setInt(3, offset);

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                Setting setting = new Setting();
                setting.setId(rs.getInt("id"));
                setting.setSettingName(rs.getString("setting_name"));
                setting.setType(rs.getString("type"));
                setting.setDescription(rs.getString("description"));
                setting.setPriority(rs.getInt("priority"));
                setting.setStatus(rs.getInt("status"));
                settings.add(setting);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return settings;
    }
    
    public List<Setting> getSettingsByType(String type) {
        List<Setting> settings = new ArrayList<>();
        String query = "SELECT * FROM setting WHERE type = ?";

        try (Connection conn = JDBCUtils.getConnection(); 
                PreparedStatement preparedStatement = conn.prepareStatement(query)) {

            preparedStatement.setString(1, type);           

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                Setting setting = new Setting();
                setting.setId(rs.getInt("id"));
                setting.setSettingName(rs.getString("setting_name"));
                setting.setType(rs.getString("type"));
                setting.setDescription(rs.getString("description"));
                setting.setPriority(rs.getInt("priority"));
                setting.setStatus(rs.getInt("status"));
                settings.add(setting);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return settings;
    }
    
    public static void main(String[] args) {
        for (Setting setting : new SettingDAO().getSettingsByType("Semester")) {
            System.out.println(setting);
        }
    }

    public int countSettingsByType(String type) {
        String query = "SELECT COUNT(*) AS total FROM setting WHERE type = ?";
        int totalRecords = 0;

        try (Connection conn = JDBCUtils.getConnection(); PreparedStatement preparedStatement = conn.prepareStatement(query)) {

            preparedStatement.setString(1, type);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                totalRecords = rs.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return totalRecords;
    }

    public List<Setting> getSettingsByStatus(int status, int offset, int pageSize) {
        List<Setting> settings = new ArrayList<>();
        String sql = "SELECT * FROM setting WHERE status = ? LIMIT ? OFFSET ?";

        try (Connection conn = JDBCUtils.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, status);  // Đặt giá trị status
            ps.setInt(2, pageSize); // Đặt kích thước trang
            ps.setInt(3, offset);   // Đặt giá trị offset

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Setting setting = new Setting();
                setting.setId(rs.getInt("id"));
                setting.setSettingName(rs.getString("setting_name"));
                setting.setType(rs.getString("type"));
                setting.setPriority(rs.getInt("priority"));
                setting.setStatus(rs.getInt("status"));
                settings.add(setting);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return settings;
    }

    public int countSettingsByStatus(int status) {
        String sql = "SELECT COUNT(*) AS total FROM setting WHERE status = ?";
        int totalRecords = 0;

        try (Connection conn = JDBCUtils.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, status);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                totalRecords = rs.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return totalRecords;
    }

    public List<Setting> getSettingsByPage(int page, int pageSize) {
        List<Setting> settings = new ArrayList<>();
        String query = "SELECT * FROM setting LIMIT ? OFFSET ?";

        try (Connection conn = JDBCUtils.getConnection(); PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setInt(1, pageSize);
            preparedStatement.setInt(2, (page - 1) * pageSize);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                Setting setting = new Setting();
                setting.setId(rs.getInt("id"));
                setting.setSettingName(rs.getString("setting_name"));
                setting.setType(rs.getString("type"));
                setting.setPriority(rs.getInt("priority"));
                setting.setStatus(rs.getInt("status"));
                settings.add(setting);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return settings;
    }

    // Đếm tổng số bản ghi trong bảng setting
    public int getTotalSettings() {
        String query = "SELECT COUNT(*) FROM setting";
        try (Connection conn = JDBCUtils.getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

}
