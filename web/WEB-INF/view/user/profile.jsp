<%-- 
    Document   : profile
    Created on : Sep 18, 2024, 8:40:26 PM
    Author     : Do Duan
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="true" %>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8">


        <title>profile edit data and skills - Bootdey.com</title>
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@4.5.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <style type="text/css">
            body {
                background: #f7f7ff;
                margin-top: 20px;
            }

            .card {
                position: relative;
                display: flex;
                flex-direction: column;
                min-width: 0;
                word-wrap: break-word;
                background-color: #fff;
                background-clip: border-box;
                border: 0 solid transparent;
                border-radius: .25rem;
                margin-bottom: 1.5rem;
                box-shadow: 0 2px 6px 0 rgb(218 218 253 / 65%), 0 2px 6px 0 rgb(206 206 238 / 54%);
            }

            .me-2 {
                margin-right: .5rem !important;
            }
        </style>
    </head>
    <body>
        <div class="container">
            <div class="main-body">
                <div class="row">
                    <div class="col-lg-4 ">
                        <div class="card">

                        </div>
                    </div>
                    <div class="col-lg-12">
                        <form action="profile/submit" method="get" enctype="multipart/form-data">
                            <div class="card">
                                <%-- Lấy đối tượng người dùng từ session --%>
                                <c:set var="user" value="${sessionScope.currentUser}" />

                                <div class="card-body">
                                    <div class="d-flex flex-column align-items-center text-center">

                                        <img src="${user.image}" alt="Admin"
                                             class="rounded-circle p-1 bg-primary" width="110">
                                        <input type="file" name="file" accept="image/*">
                                        <div class="mt-3">
                                            <h4>${user.userName}</h4>

                                        </div>
                                    </div>
                                    <hr class="my-4">                                              
                                </div>


                                <div class="card-body">
                                    <div class="row mb-3">
                                        <div class="col-sm-3">
                                            <h6 class="mb-0">User Name</h6>
                                        </div>
                                        <div class="col-sm-9 text-secondary">
                                            <input type="text" class="form-control" value="${user.userName}">
                                        </div>
                                    </div>
                                    <div class="row mb-3">
                                        <div class="col-sm-3">
                                            <h6 class="mb-0">Full Name</h6>
                                        </div>
                                        <div class="col-sm-9 text-secondary">
                                            <input type="text" class="form-control" value="${user.fullName}">
                                        </div>
                                    </div>
                                    <div class="row mb-3">
                                        <div class="col-sm-3">
                                            <h6 class="mb-0">Email</h6>
                                        </div>
                                        <div class="col-sm-9 text-secondary">
                                            <input type="text" class="form-control" value="${user.email}">
                                        </div>
                                    </div>

                                    <div class="row mb-3">
                                        <div class="col-sm-3">
                                            <h6 class="mb-0">Mobile</h6>
                                        </div>
                                        <div class="col-sm-9 text-secondary">
                                            <input type="text" class="form-control" value="${user.mobile}">
                                        </div>
                                    </div>
                                    <div class="row mb-3">
                                        <div class="col-sm-3">
                                            <h6 class="mb-0">Note</h6>
                                        </div>
                                        <div class="col-sm-9  text-secondary">
                                            <textarea class="form-control" rows="4">${user.notes}</textarea>
                                            <!--<input type="text" class="form-control" value="Bay Area, San Francisco, CA">-->
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="col-sm-3"></div>
                                        <div class="col-sm-9 text-secondary">
                                            <input type="submit" class="btn btn-primary px-4" value="Save Changes">

                                            <a href="${pageContext.request.contextPath}/user" class="btn btn-primary px-4" role="button">Change Password</a>
                                            <a href="dashboard" class="btn btn-primary px-4" role="button">Back home</a>

                                            <!--<input type="button" class="btn btn-primary px-4" value="Changes password">-->
                                        </div>
                                    </div>
                                </div>

                            </div>
                        </form>

                    </div>
                </div>
            </div>
        </div>
        <script src="https://code.jquery.com/jquery-1.10.2.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.5.0/dist/js/bootstrap.bundle.min.js"></script>
        <script type="text/javascript">

        </script>
    </body>
</html>
