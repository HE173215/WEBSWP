package dao;

import java.security.Timestamp;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.User;
import util.JDBCUtils;
import service.SendMail;
import service.BcryptService;
import service.EmailUtils;

public class UserDAO extends JDBCUtils {

    // Phương thức lấy danh sách tất cả người dùng
    public ArrayList<User> getAllUsers() throws SQLException {
        ArrayList<User> userList = new ArrayList<>();
        String sql = "SELECT id, user_name, full_name, email, mobile, password, status, role_id, image, notes, otp, otp_expiry FROM project_evaluation_system.user";

        try (Connection con = getConnection(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUserName(rs.getString("user_name"));
                user.setFullName(rs.getString("full_name"));
                user.setEmail(rs.getString("email"));
                user.setMobile(rs.getString("mobile"));
                user.setPassword(rs.getString("password"));
                user.setStatus(rs.getInt("status"));
                user.setRoleID(rs.getInt("role_id"));
                user.setImage(rs.getString("image"));
                user.setNotes(rs.getString("notes"));
                user.setOtp(rs.getString("otp"));
                user.setOtp_expiry(rs.getTimestamp("otp_expiry")); // Sửa từ getDate thành getTimestamp
                userList.add(user);
            }
        } catch (SQLException ex) {
            throw new SQLException("Lỗi khi lấy danh sách người dùng: " + ex.getMessage(), ex);
        }

        return userList;
    }

    // Phương thức đăng nhập người dùng
    public User getUserLogin(String username, String password) throws Exception {
        String query = "SELECT id, user_name, full_name, email, mobile, password, status, role_id, notes, image, otp, otp_expiry "
                + "FROM project_evaluation_system.user WHERE user_name = ?";

        try (Connection con = getConnection(); PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, username);

            try (ResultSet resultSet = ps.executeQuery()) {
                if (resultSet.next()) {
                    User user = new User();
                    user.setId(resultSet.getInt("id"));
                    user.setUserName(resultSet.getString("user_name"));
                    user.setEmail(resultSet.getString("email"));
                    user.setFullName(resultSet.getString("full_name"));
                    user.setMobile(resultSet.getString("mobile"));
                    user.setStatus(resultSet.getInt("status"));
                    user.setRoleID(resultSet.getInt("role_id"));
                    user.setNotes(resultSet.getString("notes"));
                    user.setImage(resultSet.getString("image"));
                    user.setOtp(resultSet.getString("otp"));
                    user.setOtp_expiry(resultSet.getTimestamp("otp_expiry"));

                    // Kiểm tra mật khẩu
                    String hashedPassword = resultSet.getString("password");
                    if (BcryptService.checkPassword(password, hashedPassword)) {
                        return user; // Trả về user nếu mật khẩu đúng
                    } else {
                        throw new Exception("Mật khẩu không đúng."); // Mật khẩu sai
                    }
                } else {
                    throw new Exception("Không tìm thấy người dùng."); // Không tìm thấy người dùng
                }
            }
        } catch (SQLException e) {
            throw new Exception("Lỗi khi truy vấn cơ sở dữ liệu: " + e.getMessage(), e);
        }
    }

    // Phương thức đăng ký người dùng mới
    public String registerUser(String username, String pass, String email, String name, String phone) {
        String sql = "INSERT INTO project_evaluation_system.user (user_name, password, email, full_name, mobile, role_id, status) VALUES (?, ?, ?, ?, ?, 1, 0)";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            String hashedPassword = BcryptService.hashPassword(pass);

            ps.setString(1, username);
            ps.setString(2, hashedPassword);
            ps.setString(3, email);
            ps.setString(4, name);
            ps.setString(5, phone);

            ps.executeUpdate();

            // Tạo và gửi mã OTP
            String otp = EmailUtils.generateOTP(); // Tạo mã OTP
            EmailUtils.sendEmail(email, "Your OTP Code", "Your OTP is: " + otp + ". It expires in 5 minutes.");

            // Lưu mã OTP vào cơ sở dữ liệu
            String updateOtpSql = "UPDATE project_evaluation_system.user SET otp = ?, otp_expiry = ? WHERE email = ?";
            Date otpExpiry = new Date(System.currentTimeMillis() + (5 * 60 * 1000)); // 5 phút

            try (PreparedStatement otpPs = conn.prepareStatement(updateOtpSql)) {
                otpPs.setString(1, otp);
                otpPs.setTimestamp(2, new java.sql.Timestamp(otpExpiry.getTime()));
                otpPs.setString(3, email);
                otpPs.executeUpdate();
            }

            return email; // Trả về email để sử dụng trong xác thực OTP
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Các phương thức khác giữ nguyên nhưng cập nhật kiểm tra mật khẩu cũ và thay đổi mật khẩu bằng Bcrypt
    public boolean checkOldPassword(String username, String oldPassword) {
        String sql = "SELECT password FROM project_evaluation_system.user WHERE user_name = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String hashedPassword = rs.getString("password");
                    return BcryptService.checkPassword(oldPassword, hashedPassword);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean checkOldPasswordByEmail(String email, String oldPassword) {
        String sql = "SELECT password FROM project_evaluation_system.user WHERE email = ?";

        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String hashedPassword = rs.getString("password");
                    return BcryptService.checkPassword(oldPassword, hashedPassword);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void changePassword(String email, String newPassword) {
        String sql = "UPDATE project_evaluation_system.user SET password = ? WHERE email = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, BcryptService.hashPassword(newPassword));
            ps.setString(2, email);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void changePasswordByUsername(String username, String newPassword) {
        String sql = "UPDATE project_evaluation_system.user SET password = ? WHERE user_name = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, BcryptService.hashPassword(newPassword));
            ps.setString(2, username);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean checkEmailExists(String email) {
        String sql = "SELECT COUNT(*) FROM project_evaluation_system.user WHERE email = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0; // Kiểm tra số lượng bản ghi trả về
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Trả về false nếu không tìm thấy email
    }

    public boolean sendOTP(String email) {
        // Kiểm tra xem email có tồn tại trong cơ sở dữ liệu không
        if (!checkEmailExists(email)) {
            return false; // Nếu không tồn tại, trả về false
        }

        // Tạo mã OTP mới
        String otp = String.format("%06d", new Random().nextInt(999999));
        Date otpExpiry = new Date(System.currentTimeMillis() + (5 * 60 * 1000)); // Thời gian hết hạn 5 phút

        // Cập nhật mã OTP và thời gian hết hạn vào cơ sở dữ liệu
        String updateOtpSql = "UPDATE project_evaluation_system.user SET otp = ?, otp_expiry = ? WHERE email = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(updateOtpSql)) {
            ps.setString(1, otp);
            ps.setTimestamp(2, new java.sql.Timestamp(otpExpiry.getTime()));
            ps.setString(3, email);
            ps.executeUpdate(); // Thực hiện cập nhật

            // Gửi email chứa mã OTP
            SendMail.send(email, "Your OTP for verification", "Your OTP is: " + otp + ". It expires in 5 minutes.");
        } catch (Exception e) {
            e.printStackTrace();
            return false; // Trả về false nếu có lỗi
        }
        return true; // Trả về true nếu gửi thành công
    }

    // Method to verify OTP
    public boolean verifyOTP(String email, String submittedOtp) {
        String sql = "SELECT otp, otp_expiry FROM project_evaluation_system.user WHERE email = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String storedOtp = rs.getString("otp");
                    java.sql.Timestamp otpExpiry = rs.getTimestamp("otp_expiry");
                    Date currentTime = new Date(System.currentTimeMillis());

                    return storedOtp.equals(submittedOtp) && currentTime.before(otpExpiry);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public User getUserByMobile(String phone) throws Exception {
        String sql = "SELECT * FROM project_evaluation_system.user WHERE mobile = ?";
        try (Connection conn = getConnection(); PreparedStatement pre = conn.prepareStatement(sql)) {
            pre.setString(1, phone);
            try (ResultSet rs = pre.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setId(rs.getInt("id"));
                    user.setUserName(rs.getString("user_name")); // Sử dụng đúng tên trường
                    user.setPassword(rs.getString("password"));
                    user.setEmail(rs.getString("email"));
                    user.setFullName(rs.getString("full_name")); // Sử dụng đúng tên trường
                    user.setMobile(rs.getString("mobile")); // Sử dụng đúng tên trường
                    user.setRoleID(rs.getInt("role_id")); // Sử dụng đúng tên trường
                    user.setStatus(rs.getInt("status")); // Sử dụng đúng tên trường
                    return user;
                }
            }
        } catch (SQLException ex) {
            throw new Exception("Lỗi khi lấy thông tin người dùng theo số điện thoại: " + ex.getMessage(), ex);
        }
        return null; // Trả về null nếu không tìm thấy người dùng
    }

    public boolean checkUsernameExists(String username) {
        String sql = "SELECT COUNT(*) FROM project_evaluation_system.user WHERE user_name = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0; // Kiểm tra số lượng bản ghi trả về
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Trả về false nếu không tìm thấy username
    }

    public boolean verifyOtp(String email, String enteredOtp) {
        String sql = "SELECT otp, otp_expiry FROM project_evaluation_system.user WHERE email = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String otp = rs.getString("otp");
                java.sql.Timestamp otpExpiry = rs.getTimestamp("otp_expiry");
                java.sql.Timestamp currentTime = new java.sql.Timestamp(System.currentTimeMillis());

                // In ra để kiểm tra giá trị
                System.out.println("OTP từ DB: " + otp);
                System.out.println("OTP nhập vào: " + enteredOtp);
                System.out.println("Thời gian hết hạn OTP: " + otpExpiry);
                System.out.println("Thời gian hiện tại: " + currentTime);

                // Kiểm tra null trước khi so sánh
                if (otp != null && otpExpiry != null) {
                    // Kiểm tra nếu OTP khớp và chưa hết hạn
                    if (otp.trim().equals(enteredOtp.trim()) && otpExpiry.after(currentTime)) {
                        return true; // OTP hợp lệ
                    } else {
                        System.out.println("OTP không hợp lệ hoặc đã hết hạn");
                    }
                } else {
                    System.out.println("OTP hoặc thời gian hết hạn bị null");
                }
            } else {
                System.out.println("Không tìm thấy người dùng với email: " + email);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false; // OTP không hợp lệ
    }

    public void activateUser(String email) {
        String sql = "UPDATE project_evaluation_system.user SET status = 1 WHERE email = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.executeUpdate(); // Kích hoạt tài khoản người dùng
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isUserExist(String username, String email) {
        String sql = "SELECT COUNT(*) FROM project_evaluation_system.user WHERE user_name = ? OR email = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0; // Nếu tồn tại tài khoản
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public User getUserByEmail(String email) {
        User user = null;
        String sql = "SELECT * FROM project_evaluation_system.user WHERE email = ?";

        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // Giả sử bạn có hàm map dữ liệu từ ResultSet sang đối tượng User
                    user = mapResultSetToUser(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }

    public boolean updatePassword(String email, String newPassword) {
        String sql = "UPDATE project_evaluation_system.user SET password = ? WHERE email = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, BcryptService.hashPassword(newPassword)); // Mã hóa mật khẩu mới
            ps.setString(2, email);
            int rowsUpdated = ps.executeUpdate(); // Số hàng được cập nhật
            return rowsUpdated > 0; // Trả về true nếu có hàng được cập nhật
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Trả về false nếu có lỗi
        }
    }

// Hàm này có thể giúp bạn map từ ResultSet sang User object
    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setUserName(rs.getString("user_name"));
        user.setPassword(rs.getString("password"));
        user.setEmail(rs.getString("email"));
        user.setFullName(rs.getString("full_name"));
        user.setMobile(rs.getString("mobile"));
        user.setRoleID(rs.getInt("role_id"));
        user.setStatus(rs.getInt("status"));
        user.setOtp(rs.getString("otp"));
        user.setOtp_expiry(rs.getTimestamp("otp_expiry"));
        return user;
    }

    public void createUser(User user) {
        String sql = "INSERT INTO project_evaluation_system.user (user_name, password, email, full_name, mobile, role_id, status) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            String hashedPassword = BcryptService.hashPassword(user.getPassword()); // Hash mật khẩu

            // Đặt các tham số cho PreparedStatement
            ps.setString(1, user.getUserName());
            ps.setString(2, hashedPassword);
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getFullName());
            ps.setString(5, user.getMobile());
            ps.setInt(6, user.getRoleID()); // Role ID
            ps.setInt(7, user.getStatus()); // Status

            ps.executeUpdate(); // Thực thi câu truy vấn
        } catch (SQLException e) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, e);
        }
    }

}
