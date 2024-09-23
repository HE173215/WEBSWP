
<!DOCTYPE html>
<html lang="en">
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

    <head>
        <meta charset="UTF-8">
        <title>DASHMIN - Bootstrap Admin Template</title>
        <meta content="width=device-width, initial-scale=1.0" name="viewport">
        <meta content="" name="keywords">
        <meta content="" name="description">

        <!-- Favicon -->
        <link href="img/favicon.ico" rel="icon">

        <!-- Google Web Fonts -->
        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
        <link href="https://fonts.googleapis.com/css2?family=Heebo:wght@400;500;600;700&display=swap" rel="stylesheet">

        <!-- Icon Font Stylesheet -->
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.10.0/css/all.min.css" rel="stylesheet">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.4.1/font/bootstrap-icons.css" rel="stylesheet">

        <!-- Libraries Stylesheet -->
        <link href="lib/owlcarousel/assets/owl.carousel.min.css" rel="stylesheet">
        <link href="lib/tempusdominus/css/tempusdominus-bootstrap-4.min.css" rel="stylesheet" />

        <!-- Customized Bootstrap Stylesheet -->
        <link href="css/bootstrap.min.css" rel="stylesheet">

        <!-- Template Stylesheet -->
        <link href="css/style.css" rel="stylesheet">
    </head>

    <body>
        <div class="container-fluid position-relative bg-white d-flex p-0">
            <!-- Spinner Start -->
            <div id="spinner" class="show bg-white position-fixed translate-middle w-100 vh-100 top-50 start-50 d-flex align-items-center justify-content-center">
            </div>
            <!-- Spinner End -->
     
                <!-- Setting List Start -->
                <div class="container-fluid pt-4 px-4">
                    <div class="bg-light text-center rounded p-4">
                        <div class="d-flex align-items-center justify-content-between mb-4">
                            <h6 class="mb-0">Setting List</h6>
                        </div>
                        <div class="filter-bar">
                            <!-- Dropdown for Types (1) -->
                            <select id="typeFilter" name="typeFilter">
                                <option value="">All Types</option>
                                <option value="User Role">User Role</option>
                                <option value="Other Type">Other Type</option>
                            </select>

                            <!-- Dropdown for Statuses (2) -->
                            <select id="statusFilter" name="statusFilter">
                                <option value="">All Statuses</option>
                                <option value="Active">Active</option>
                                <option value="Inactive">Inactive</option>
                            </select>

                            <div class="hero__search__form">
                                    <form action="setting">
                                        <input type="text" name="searchQuery" placeholder="What do yo u need?">
                                        <button type="submit" class="site-btn">SEARCH</button>
                                    </form>
                                </div>
                        </div>
                        <div class="table-responsive">
                            <table class="table text-start align-middle table-bordered table-hover mb-0">
                                <thead>
                                    <tr class="text-dark">
                                        <th scope="col">ID</th>
                                        <th scope="col">Setting Name</th>
                                        <th scope="col">Type</th>
                                        <th scope="col">Priority</th>
                                        <th scope="col">Status</th>
                                        <th scope="col">Action</th>

                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="slist" items="${settingL}">
                                        <tr>
                                            <td>${slist.id}</td>
                                            <td>${slist.settingName}</td>
                                            <td>${slist.type}</td>
                                            <td>${slist.priority}</td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${slist.status == 1}">
                                                        Active
                                                    </c:when>
                                                    <c:otherwise>
                                                        Inactive
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td>
                                                <a href="editSetting?id=${slist.id}">Edit</a> |
                                                <c:choose>
                                                    <c:when test="${slist.status == 1}">
                                                        <form action="setting" method="post" style="display:inline;">
                                                            <input type="hidden" name="id" value="${slist.id}" />
                                                            <input type="hidden" name="newStatus" value="0" />
                                                            <button type="submit" class="btn btn-warning">Inactive</button>
                                                        </form>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <form action="setting" method="post" style="display:inline;">
                                                            <input type="hidden" name="id" value="${slist.id}" />
                                                            <input type="hidden" name="newStatus" value="1" />
                                                            <button type="submit" class="btn btn-success">Active</button>
                                                        </form>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
                <!-- Setting List End -->               
            </div>
            <!-- Content End -->          
        
        <!-- JavaScript Libraries -->
        <script src="https://code.jquery.com/jquery-3.4.1.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0/dist/js/bootstrap.bundle.min.js"></script>
        <script src="lib/chart/chart.min.js"></script>
        <script src="lib/easing/easing.min.js"></script>
        <script src="lib/waypoints/waypoints.min.js"></script>
        <script src="lib/owlcarousel/owl.carousel.min.js"></script>
        <script src="lib/tempusdominus/js/moment.min.js"></script>
        <script src="lib/tempusdominus/js/moment-timezone.min.js"></script>
        <script src="lib/tempusdominus/js/tempusdominus-bootstrap-4.min.js"></script>

        <!-- Template Javascript -->
        <script src="js/main.js"></script>
    </body>

</html>