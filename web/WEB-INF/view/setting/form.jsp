<%-- 
    Document   : settingdetail
    Created on : Sep 14, 2024, 5:15:04 PM
    Author     : vqman
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Setting Details</title>
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
        <!-- Favicon -->
        <link href="img/favicon.ico" rel="icon">

        <!-- Google Web Fonts -->
        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
        <link href="https://fonts.googleapis.com/css2?family=Heebo:wght@400;500;600;700&display=swap" rel="stylesheet">

        <!-- Icon Font Stylesheet -->
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.10.0/css/all.min.css" rel="stylesheet">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.4.1/font/bootstrap-icons.css" rel="stylesheet">

        <!-- Customized Bootstrap Stylesheet -->
        <link href="css/bootstrap.min.css" rel="stylesheet">

        <!-- Template Stylesheet -->
        <link href="css/style.css" rel="stylesheet">
        <style>
            body {
                margin: 20px;
            }
        </style>
    </head>
    <body>
        <div class="container mt-4">
            <div class="card shadow-sm">
                <div class="card-header">
                    <h3>Setting Details</h3>
                </div>
                <div class="card-body">
                    <form action="${pageContext.request.contextPath}/setting/save" method="post">
                        <input type="hidden" name="id" value="${setting != null ? setting.id : ''}" />
                        <!-- Name Field -->
                        <div class="form-row">
                            <div class="form-group col-md-6">
                                <label for="name">Name*</label>
                                <input type="text" class="form-control" id="name" name="name" value="${setting.settingName}" required>
                            </div>
                            <div class="form-group col-md-6">
                                <label for="type">Type</label>
                                <select class="form-control" id="type" name="type">
                                    <option value="User Role" ${setting.type == 'User Role' ? 'selected' : ''}>User Role</option>
                                    <option value="Semester" ${setting.type == 'Semester' ? 'selected' : ''}>Semester</option>
                                </select>
                            </div>
                        </div>
                        <!-- Priority and Status Fields-->
                        <div class="form-row">
                            <div class="form-group col-md-6">
                                <label for="priority">Priority</label>
                                <input type="number" class="form-control" id="priority" name="priority" value="${setting.priority}">
                            </div>
                            <div class="form-group col-md-6">
                                <label>Status</label><br>
                                <div class="form-check form-check-inline">
                                    <input class="form-check-input" type="radio" id="active" name="status" value="Active" ${setting.status == 1 ? 'checked' : ''}>
                                    <label class="form-check-label" for="active">Active</label>
                                </div>
                                <div class="form-check form-check-inline">
                                    <input class="form-check-input"   
                                           type="radio" id="inactive" name="status"   
                                           value="Inactive" ${setting.status == 0 ? 'checked' : ''}>
                                    <label class="form-check-label" for="inactive">Inactive</label>
                                </div>
                            </div>
                        </div>

                        <!-- Description Field -->
                        <div class="form-group">
                            <label for="description">Description</label>
                            <textarea class="form-control" id="description"   
                                      name="description" rows="3">${setting.description}</textarea>
                        </div>

                        <!-- Submit Button -->
                        <button type="submit" class="btn btn-primary">Submit</button>
                    </form>
                </div>
            </div>
        </div>

        <!-- JS and Bootstrap Scripts -->
        <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.0.7/dist/umd/popper.min.js"></script>
        <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
    </body>
</html>

