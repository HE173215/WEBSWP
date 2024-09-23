<%-- 
    Document   : dashboard
    Created on : Sep 15, 2024, 9:16:28 PM
    Author     : Do Duan
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en" xmlns="http://www.w3.org/1999/html">

<head>
    <meta charset="utf-8">
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
    <link href="lib/tempusdominus/css/tempusdominus-bootstrap-4.min.css" rel="stylesheet"/>

    <!-- Customized Bootstrap Stylesheet -->
    <link href="css/bootstrap.min.css" rel="stylesheet">

    <!-- Template Stylesheet -->
    <link href="css/style.css" rel="stylesheet">
</head>

<body>
<div class="container-fluid position-relative bg-white d-flex p-0">
    <!-- Spinner Start -->
    <div id="spinner"
         class="show bg-white position-fixed translate-middle w-100 vh-100 top-50 start-50 d-flex align-items-center justify-content-center">
        <div class="spinner-border text-primary" style="width: 3rem; height: 3rem;" role="status">
            <span class="sr-only">Loading...</span>
        </div>
    </div>
    <!-- Spinner End -->


    <!-- Sidebar Start -->
            <jsp:include page="/WEB-INF/view/includes/sidebar.jsp" />

    <!-- Sidebar End -->


    <!-- Content Start -->
    <div class="content">
        <!-- Navbar Start -->
        <jsp:include page="/WEB-INF/view/includes/navbar.jsp" />
        <!-- Navbar End -->

        <div class="container-fluid pt-4 px-4">
            <div class="row g-4">
                <div class="col-sm-12 col-xl-3">
                    <select class=" form-select mb-3
        " aria-label="Default select example">
                        <option selected="">Semester: Fall 2024</option>
                        <option value="1">One</option>
                        <option value="2">Two</option>
                        <option value="3">Three</option>
                    </select>
                </div>

                <div class="col-sm-12 col-xl-3">
                    <select class=" form-select mb-3
        " aria-label="Default select example">
                        <option selected="">All subject</option>
                        <option value="1">One</option>
                        <option value="2">Two</option>
                        <option value="3">Three</option>
                    </select>
                </div>

                <div class="col-sm-12 col-xl-3">
                    <select class=" form-select mb-3
        " aria-label="Default select example">
                        <option selected="">All status</option>
                        <option value="1">One</option>
                        <option value="2">Two</option>
                        <option value="3">Three</option>
                    </select>
                </div>
                <div class="col-sm-12 col-xl-2">
                    <!--                    <form class="d-none d-md-flex ms-4">-->
                    <!--                        <input class="form-control border-1" type="search" placeholder="Search">-->
                    <!--                    </form>-->
                    <!--                    <a class="btn btn-sm btn-primary" href="">Search</a>-->
                    <button type="button" class="btn btn-primary mb-3">
                        <a href="" style="color: white">Search</a>
                    </button>
                </div>
            </div>
        </div>

        <div class="container-fluid pt-4 px-4">
            <div class="row g-4">
                <!-- Assigned Classes -->
                <p class="h3">Assigned Classes</p>
            </div>
        </div>

        <div class="container-fluid pt-4 px-4">
            <div class="row g-4">
                <div class="col-sm-12 col-md-6 col-xl-4">
                    <div class="h-100 bg-light rounded p-4">
                        <div class="d-flex align-items-center justify-content-between mb-4">
                            <h5 class="mb-0">Class name</h5>
                        </div>
                        <div>
                            <p>Class: SWP391 - Software Engineering</p>
                            <p>Instructor: Dr. Smith</p>
                            <p>Status: Ongoing</p>
                            <p>Start Date: Sept 1, 2024 - End Date: Dec 1, 2024</p>
                            <!--                            <a href="">View Class Details</a>-->
                            <!--                            <button type="button" class="btn btn-primary m-2">-->
                            <!--                                <a href="" style="color: white">View All Classes</a>-->
                            <!--                            </button>-->
                            <a class="btn btn-sm btn-primary" href="">Detail</a>
                        </div>

                    </div>
                </div>

            </div>
        </div>

        <div class="container-fluid pt-4 px-4">
            <div class="row g-4">
                <!-- Assigned Classes -->
                <p class="h3">Related Classes</p>
            </div>
        </div>

        <div class="container-fluid pt-4 px-4">
            <div class="row g-4">
                <div class="col-sm-12 col-md-6 col-xl-4">
                    <div class="h-100 bg-light rounded p-4">
                        <div class="d-flex align-items-center justify-content-between mb-4">
                            <h5 class="mb-0">Class name</h5>
                        </div>
                        <div>
                            <p>Class: SWP391 - Software Engineering</p>
                            <p>Instructor: Dr. Smith</p>
                            <p>Status: Ongoing</p>
                            <p>Start Date: Sept 1, 2024 - End Date: Dec 1, 2024</p>
                            <!--                            <a href="">View Class Details</a>-->
                            <!--                            <button type="button" class="btn btn-primary m-2">-->
                            <!--                                <a href="" style="color: white">View All Classes</a>-->
                            <!--                            </button>-->
                            <a class="btn btn-sm btn-primary" href="">Detail</a>
                        </div>

                    </div>
                </div>
                <div class="col-sm-12 col-md-6 col-xl-4">
                    <div class="h-100 bg-light rounded p-4">
                        <div class="d-flex align-items-center justify-content-between mb-4">
                            <h5 class="mb-0">Class name</h5>
                        </div>
                        <div>
                            <p>Class: SWP391 - Software Engineering</p>
                            <p>Instructor: Dr. Smith</p>
                            <p>Status: Ongoing</p>
                            <p>Start Date: Sept 1, 2024 - End Date: Dec 1, 2024</p>
                            <!--                            <a href="">View Class Details</a>-->
                            <!--                            <button type="button" class="btn btn-primary m-2">-->
                            <!--                                <a href="" style="color: white">View All Classes</a>-->
                            <!--                            </button>-->
                            <a class="btn btn-sm btn-primary" href="">Detail</a>
                        </div>

                    </div>
                </div>

                <div class="col-sm-12 col-md-6 col-xl-4">
                    <div class="h-100 bg-light rounded p-4">
                        <div class="d-flex align-items-center justify-content-between mb-4">
                            <h5 class="mb-0">Class name</h5>
                        </div>
                        <div>
                            <p>Class: SWP391 - Software Engineering</p>
                            <p>Instructor: Dr. Smith</p>
                            <p>Status: Ongoing</p>
                            <p>Start Date: Sept 1, 2024 - End Date: Dec 1, 2024</p>
                            <!--                            <a href="">View Class Details</a>-->
                            <!--                            <button type="button" class="btn btn-primary m-2">-->
                            <!--                                <a href="" style="color: white">View All Classes</a>-->
                            <!--                            </button>-->
                            <a class="btn btn-sm btn-primary" href="">Detail</a>
                        </div>

                    </div>
                </div>


            </div>
        </div>

        <div class="container-fluid pt-4 px-4">
            <div class="align-content-center">
                <div class="btn-toolbar" role="toolbar">
                    <div class="btn-group " role="group" aria-label="First group">
                        <button type="button" class="btn btn-info">1</button>
                        <button type="button" class="btn btn-info">2</button>
                        <button type="button" class="btn btn-info">3</button>
                        <button type="button" class="btn btn-info">4</button>
                    </div>


                </div>
            </div>
        </div>

        <!-- Sales Chart End -->


        <!-- Footer Start -->
        <jsp:include page="/WEB-INF/view/includes/footer.jsp">
        <!--         Footer End-->
    </div>
    <!-- Content End -->


    <!-- Back to Top -->
    <a href="#" class="btn btn-lg btn-primary btn-lg-square back-to-top"><i class="bi bi-arrow-up"></i></a>
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
