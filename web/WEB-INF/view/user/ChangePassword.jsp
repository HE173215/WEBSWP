<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Change Password</title>
        <!-- Thêm Bootstrap -->
        <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" rel="stylesheet">
    </head>
    <body>
        <div class="container mt-5">
            <h2 class="mb-4">Change Password</h2>

            <!-- Hiển thị thông báo thành công -->
            <c:if test="${not empty message}">
                <div class="alert alert-success" role="alert">
                    ${message}
                </div>
            </c:if>

            <!-- Hiển thị thông báo lỗi -->
            <c:if test="${not empty error}">
                <div class="alert alert-danger" role="alert">
                    ${error}
                </div>
            </c:if>

            <!-- Form đổi mật khẩu -->
            <form action="${pageContext.request.contextPath}/user/submit" method="post" class="needs-validation" novalidate>
                <label for="otp">Old Password:</label>
                <input type="password" class="form-control" id="oldPassword" name="oldPassword" required>

                <label for="password">New Password:</label>
                <input type="password" class="form-control" id="newpassword" name="newPassword" required>

                <label for="confirmPassword">Confirm New Password:</label>
                <input type="password" class="form-control" id="confirmPassword" name="confirmPassword" required>

                <button type="submit" class="btn btn-primary">Change Password</button>
            </form>
        </div>

        <!-- Thêm Bootstrap JS và Popper.js -->
        <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.9.3/dist/umd/popper.min.js"></script>
        <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>



    </body>
</html>
