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
import util.JDBCUtils;

import model.Class;

/**
 *
 * @author Do Duan
 */
public class ClassDAO {

    Connection con = null; // ket noi vs sql
    PreparedStatement ps = null; // nhan cau lenh
    ResultSet rs = null; // tra kq

    public int createClass(Class c) {
        String query = "insert into class(name,detail,status,semester_id,group_id)\n"
                + "values (?,?,?,?,?)";
        int result = 0;
        try {
            con = JDBCUtils.getConnection();
            if (con != null) {
                ps = con.prepareStatement(query);
                ps.setString(1, c.getName());
                ps.setString(2, c.getDetail());
                ps.setInt(3, c.getStatus());
                ps.setInt(4, c.getSemesterID());
                ps.setInt(5, c.getSemesterID());
                result = ps.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return result;
    }

    public Class getClassByID(int id) {
        String query = "select * from class where id=?";
        Class c = null;
        try {
            con = JDBCUtils.getConnection();
            if (con != null) {
                ps = con.prepareStatement(query);
                ps.setInt(1, id);
                rs = ps.executeQuery();
                if (rs.next()) {
                    c = new Class();
                    c.setId(rs.getInt("id"));
                    c.setName(rs.getString("name"));
                    c.setDetail(rs.getString("detail"));
                    c.setStatus(rs.getInt("status"));
                    c.setSemesterID(rs.getInt("semester_id"));
                    c.setGroupID(rs.getInt("group_id"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return c;
    }

    public List<Class> searchClass(int userID, int settingID, int groupID, String searchString, int itemPerPage, int pageNumber) {
        List<Class> classList = new ArrayList<>();
        Class c = null;
        StringBuilder query = new StringBuilder("SELECT c.*,\n"
                + "g.code,g.name,\n"
                + "s.setting_name,\n"
                + "u.user_name,u.role_id FROM project_evaluation_system.class as c\n"
                + "join `group` as g on c.group_id = g.id\n"
                + "join setting as s on c.semester_id = s.id\n"
                + "join user_group as ug on ug.group_id = g.id\n"
                + "join user as u on ug.user_id=u.id \n"
                + "where c.status = 1\n"
                + "AND u.id = ?\n");

        try {
            con = JDBCUtils.getConnection();
            if (con != null) {
                // Bắt đầu với tham số đầu tiên (userID)
                int paramIndex = 1;

                // Thêm điều kiện cho settingID nếu có
                if (settingID != 0) {
                    query.append(" AND s.id = ?\n");
                }

                // Thêm điều kiện cho groupID nếu có
                if (groupID != 0) {
                    query.append(" AND g.id = ?\n");
                }

                // Thêm điều kiện cho searchString nếu có
                if (!searchString.equals("")) {
                    query.append(" AND lower(c.name) LIKE ?\n");
                }

                query.append("LIMIT ? OFFSET ?");

                // Chuẩn bị PreparedStatement với câu truy vấn động
                ps = con.prepareStatement(query.toString());

                // Đặt giá trị cho tham số đầu tiên (userID)
                ps.setInt(paramIndex++, userID);

                // Nếu settingID != 0, thêm giá trị cho tham số
                if (settingID != 0) {
                    ps.setInt(paramIndex++, settingID);
                }

                // Nếu groupID != 0, thêm giá trị cho tham số
                if (groupID != 0) {
                    ps.setInt(paramIndex++, groupID);
                }

                // Nếu searchString không rỗng, thêm giá trị cho tham số
                if (!searchString.equals("")) {
                    ps.setString(paramIndex++, "%" + searchString.toLowerCase() + "%");
                }

                ps.setInt(paramIndex++, itemPerPage);
                ps.setInt(paramIndex++, pageNumber);

                // Thực thi truy vấn
                rs = ps.executeQuery();

                // Xử lý kết quả từ ResultSet
                while (rs.next()) {
                    // Xử lý dữ liệu ở đây
                    c = new Class();
                    c.setId(rs.getInt("id"));
                    c.setName(rs.getString("name"));
                    c.setDetail(rs.getString("detail"));
                    c.setStatus(rs.getInt("status"));
                    c.setSemesterID(rs.getInt("semester_id"));
                    c.setGroupID(rs.getInt("group_id"));
                    c.setSemester(rs.getString("setting_name"));
                    c.setSubjectCode(rs.getString("code"));
                    c.setSubjectName(rs.getString("name"));
                    classList.add(c);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return classList;
    }

    public static void main(String[] args) {
        for (Class searchClas : new ClassDAO().searchClass(1, 0, 0, "",6,0)) {
            System.out.println(searchClas);
        }
    }
}
