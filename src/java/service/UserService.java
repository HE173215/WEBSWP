package service;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dao.UserDAO;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import model.User;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class UserService {

    private UserDAO userDAO;

    public UserService() {
        userDAO = new UserDAO(); // Khởi tạo đối tượng UserDAO
    }

    // Lấy danh sách tất cả người dùng
    public ArrayList<User> getAllUsers() {
        try {
            return userDAO.getAllUsers();
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>(); // Trả về danh sách rỗng trong trường hợp có lỗi
        }
    }

    // Đăng nhập người dùng
    public User login(String username, String password) throws Exception {
        return userDAO.getUserLogin(username, password);
    }

    // Đăng ký người dùng mới
    public String register(String username, String pass, String email, String name, String phone) {
        return userDAO.registerUser(username, pass, email, name, phone);
    }

    // Kiểm tra mật khẩu cũ
    public boolean checkOldPassword(String username, String oldPassword) {
        return userDAO.checkOldPassword(username, oldPassword);
    }

    // Thay đổi mật khẩu
    public void changePassword(String email, String newPassword) {
        userDAO.changePassword(email, newPassword);
    }

    public void changePasswordByUsername(String username, String newPassword) {
        userDAO.changePasswordByUsername(username, newPassword);
    }

    // Kiểm tra sự tồn tại của email
    public boolean checkEmailExists(String email) {
        return userDAO.checkEmailExists(email);
    }

    // Gửi OTP
    public boolean sendOTP(String email) {
        return userDAO.sendOTP(email);
    }

    // Xác thực OTP
    public boolean verifyOTP(String email, String submittedOtp) {
        return userDAO.verifyOtp(email, submittedOtp);
    }

    // Kích hoạt người dùng
    public void activateUser(String email) {
        userDAO.activateUser(email);
    }

    // Kiểm tra sự tồn tại của người dùng
    public boolean isUserExist(String username, String email) {
        return userDAO.isUserExist(username, email);
    }

    // Lấy người dùng theo email
    public User getUserByEmail(String email) {
        return userDAO.getUserByEmail(email);
    }

    // Cập nhật mật khẩu
    public boolean updatePassword(String email, String newPassword) {
        return userDAO.updatePassword(email, newPassword);
    }

    // Lấy người dùng theo số điện thoại
    public User getUserByMobile(String phone) throws Exception {
        return userDAO.getUserByMobile(phone);
    }

    // Kiểm tra sự tồn tại của tên đăng nhập
    public boolean checkUsernameExists(String username) {
        return userDAO.checkUsernameExists(username);
    }

    public String validateRegisterInfo(String username, String pass, String confirmPass, String email, String name, String phone) {
        StringBuilder errorMessages = new StringBuilder();

        if (username.isEmpty() || pass.isEmpty() || name.isEmpty() || email.isEmpty() || phone.isEmpty()) {
            errorMessages.append("Tất cả các trường đều bắt buộc! ");
        }
        if (username.length() > 63 || pass.length() > 255 || name.length() > 255 || email.length() > 255 || phone.length() != 10) {
            errorMessages.append("Đầu vào vượt quá chiều dài tối đa! ");
        }
        if (!pass.equals(confirmPass)) {
            errorMessages.append("Mật khẩu xác nhận không khớp! ");
        }
        String mailRegex = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
        if (!email.matches(mailRegex)) {
            errorMessages.append("Định dạng email không hợp lệ! ");
        }
        String mobileRegex = "(09|03|07|08|05)+([0-9]{8})";
        if (!phone.matches(mobileRegex)) {
            errorMessages.append("Định dạng số điện thoại không hợp lệ! ");
        }

        return errorMessages.toString();
    }

    public boolean changePassword(String email, String oldPassword, String newPassword) {
        // Kiểm tra mật khẩu cũ
        try {
            if (userDAO.checkOldPasswordByEmail(email, oldPassword)) {
                // Gọi phương thức updatePassword để cập nhật mật khẩu mới
                return userDAO.updatePassword(email, newPassword);
            } else {
                System.out.println("Mật khẩu cũ không chính xác."); // Thông báo nếu mật khẩu cũ không đúng
            }
        } catch (Exception e) {
            e.printStackTrace(); // In lỗi nếu có ngoại lệ
        }
        return false; // Trả về false nếu có lỗi hoặc mật khẩu cũ không chính xác
    }

    public String parseAccessToken(String jsonResponse) {
        JsonObject jsonObject = JsonParser.parseString(jsonResponse).getAsJsonObject();
        System.out.println("JSON Response for Access Token: " + jsonResponse);
        return jsonObject.has("access_token") ? jsonObject.get("access_token").getAsString() : null;
    }

    public User parseUserInfo(String jsonResponse) {
        JsonObject jsonObject = JsonParser.parseString(jsonResponse).getAsJsonObject();
        User user = new User();

        System.out.println("JSON Response for User Info: " + jsonResponse);

        if (jsonObject.has("email")) {
            user.setEmail(jsonObject.get("email").getAsString());
        } else {
            System.out.println("Missing email in JSON response.");
        }

        return user;
    }

    public String getAccessToken(String code) throws IOException {
        String tokenUrl = "https://oauth2.googleapis.com/token";
        URL url = new URL(tokenUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);

        String postData = "code=" + code
                + "&client_id="
                + "&client_secret="
                + "&redirect_uri=http://localhost:8080/Project_Evaluation_System/register"
                + "&grant_type=authorization_code";

        try (OutputStream os = conn.getOutputStream()) {
            os.write(postData.getBytes());
            os.flush();
        }

        StringBuilder response = new StringBuilder();
        try (Scanner scanner = new Scanner(conn.getInputStream())) {
            while (scanner.hasNext()) {
                response.append(scanner.nextLine());
            }
        }

        return parseAccessToken(response.toString());
    }

    public User getUserInfo(String accessToken) {
        String userInfoUrl = "https://www.googleapis.com/oauth2/v3/userinfo?access_token=" + accessToken;
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(userInfoUrl).openConnection();
            conn.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            String jsonResponse = response.toString();
            System.out.println("User Info Response: " + jsonResponse);

            return parseUserInfo(jsonResponse);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
