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
            <!-- Sidebar Start -->
            <jsp:include page="/WEB-INF/view/includes/sidebar.jsp" />
            <!-- Sidebar End -->

            <!-- Main Content Start -->
            <div class="content" style="flex-grow: 1;">
                <!-- Navbar Start -->
                <jsp:include page="/WEB-INF/view/includes/navbar.jsp" />
                <!-- Navbar End -->

                <!-- Spinner Start -->
                <div id="spinner" class="show bg-white position-fixed translate-middle w-100 vh-100 top-50 start-50 d-flex align-items-center justify-content-center"></div>
                <!-- Spinner End -->
                <div>
                    <!-- Setting List Start -->
                    <div class="container-fluid pt-4 px-4">
                        <div class="bg-light text-center rounded p-4 mb-4">
                            <div class="d-flex align-items-center justify-content-between mb-4">
                                <h6 class="mb-0">Setting List</h6>
                            </div>

                            <!-- Combine Filter, Search, and Add Button in One Row -->
                            <div class="d-flex align-items-center justify-content-between mb-3">
                                <!-- Filter Form -->
                                <form action="setting" method="get" class="d-flex me-3">
                                    <!-- Type Filter -->
                                    <div class="form-group me-3">
                                        <label for="typeFilter" class="form-label me-2">Filter by Type</label>
                                        <select id="typeFilter" name="typeFilter" class="form-select" onchange="this.form.submit()">
                                            <option value="">All Types</option>
                                            <c:forEach items="${types}" var="type">
                                                <option value="${type}" ${type == param.typeFilter ? 'selected' : ''}>${type}</option>
                                            </c:forEach>
                                        </select>
                                    </div>

                                    <!-- Status Filter -->
                                    <div class="form-group me-3">
                                        <label for="statusFilter" class="form-label me-2">Filter by Status</label>
                                        <select id="statusFilter" name="statusFilter" class="form-select" onchange="this.form.submit()">
                                            <option value="">All Status</option>
                                            <option value="1" ${param.statusFilter == '1' ? 'selected' : ''}>Active</option>
                                            <option value="0" ${param.statusFilter == '0' ? 'selected' : ''}>Inactive</option>
                                        </select>
                                    </div>
                                </form>

                                <!-- Search Form -->
                                <form action="setting" method="get" class="d-flex me-3">
                                    <div class="form-group">
                                        <label for="searchQuery" class="form-label me-2">Search</label>
                                        <div class="input-group">
                                            <input type="text" class="form-control" name="searchQuery" placeholder="What do you need?" value="${param.searchQuery}">
                                            <button type="submit" class="btn btn-primary">SEARCH</button>
                                        </div>
                                    </div>
                                </form>

                                <!-- Add Button -->
                                <button type="button" class="btn btn-success" onclick="window.location.href = 'setting/new'">Add</button>
                            </div>

                        </div>
                    </div>
                </div>

                <div class="table-responsive" style="flex-grow: 1; margin-left: 25px;margin-right: 25px ">
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
                            <c:forEach var="slist" items="${settingList}">
                                <tr>
                                    <td>${slist.id}</td>
                                    <td>${slist.settingName}</td>
                                    <td>${slist.type}</td>
                                    <td>${slist.priority}</td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${slist.status == 1}">Active</c:when>
                                            <c:otherwise>Inactive</c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <a href="${pageContext.request.contextPath}/setting/edit?id=${slist.id}&page=${param.page}&typeFilter=${param.typeFilter}&searchQuery=${param.searchQuery}">
                                            <button type="button" class="btn btn-primary">Edit</button>
                                        </a>
                                        <c:choose>
                                            <c:when test="${slist.status == 1}">
                                                <form action="${pageContext.request.contextPath}/setting/change-status" method="post" style="display:inline;">
                                                    <input type="hidden" name="id" value="${slist.id}" />
                                                    <input type="hidden" name="newStatus" value="0" />
                                                    <input type="hidden" name="page" value="${currentPage}" /> 
                                                    <input type="hidden" name="typeFilter" value="${param.typeFilter}" />
                                                    <input type="hidden" name="statusFilter" value="${param.statusFilter}" />
                                                    <input type="hidden" name="searchQuery" value="${param.searchQuery}" />
                                                    <button type="submit" class="btn btn-warning">Inactive</button>
                                                </form>
                                            </c:when>
                                            <c:otherwise>
                                                <form action="${pageContext.request.contextPath}/setting/change-status" method="post" style="display:inline;">
                                                    <input type="hidden" name="id" value="${slist.id}" />
                                                    <input type="hidden" name="newStatus" value="1" />
                                                    <input type="hidden" name="page" value="${currentPage}" /> 
                                                    <input type="hidden" name="typeFilter" value="${param.typeFilter}" />
                                                    <input type="hidden" name="statusFilter" value="${param.statusFilter}" />
                                                    <input type="hidden" name="searchQuery" value="${param.searchQuery}" />
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

                <!-- Ph?n trang -->
                <div class="d-flex justify-content-between align-items-center mt-3">
                    <div>
                        <c:if test="${currentPage > 1}">
                            <a href="?page=${currentPage - 1}&typeFilter=${param.typeFilter}&searchQuery=${param.searchQuery}" class="btn btn-secondary">Previous</a>
                        </c:if>
                        <c:if test="${currentPage < totalPages}">
                            <a href="?page=${currentPage + 1}&typeFilter=${param.typeFilter}&searchQuery=${param.searchQuery}" class="btn btn-secondary">Next</a>
                        </c:if>
                    </div>
                    <div>
                        Page ${currentPage} of ${totalPages}
                    </div>
                </div>
            </div>

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
