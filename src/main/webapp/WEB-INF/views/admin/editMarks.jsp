<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>
<html>
<head>
    <title>Edit Marks</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <style>
        /* Paste your Assign Marks CSS here, with minor tweaks if needed */
        :root {
            --bg: #f3f6fb;
            --card: #fff;
            --muted: #6b7280;
            --primary: #0b6cf0;
            --danger: #dc3545;
            --accent: #28a745;
        }

        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family: "Segoe UI", sans-serif;
            background: var(--bg);
            color: #0b2536;
        }

        .header {
            position: fixed;
            top: 0;
            left: 0;
            right: 0;
            height: 70px;
            background: var(--card);
            box-shadow: 0 1px 4px rgba(0, 0, 0, 0.06);
            padding: 14px 28px;
            display: flex;
            justify-content: space-between;
            align-items: center;
            z-index: 1000;
        }

        .header .brand {
            font-weight: bold;
            color: var(--primary);
            font-size: 18px;
        }

        .header .welcome {
            font-size: 14px;
            color: #333;
        }

        .header .right-side a {
            background: var(--danger);
            color: white;
            padding: 8px 12px;
            border-radius: 6px;
            text-decoration: none;
            font-weight: 600;
        }

        .sidebar {
            position: fixed;
            top: 70px;
            left: 0;
            bottom: 0;
            width: 220px;
            background: var(--card);
            padding: 20px;
            box-shadow: 2px 0 6px rgba(0, 0, 0, 0.05);
        }

        .nav a {
            display: block;
            padding: 12px 16px;
            color: #0b2536;
            text-decoration: none;
            font-weight: 600;
            border-radius: 6px;
            margin-bottom: 8px;
        }

        .nav a:hover {
            background-color: #f0f4ff;
        }

        .nav a.active {
            background-color: var(--primary);
            color: white;
        }

        .main-content {
            margin-left: 220px;
            margin-top: 70px;
            padding: 20px;
        }

        .form-container {
            background: var(--card);
            max-width: 600px;
            margin: 0 auto;
            padding: 25px 30px;
            border-radius: 10px;
            box-shadow: 0 0 12px rgba(0, 0, 0, 0.05);
        }

        h2 {
            text-align: center;
            color: var(--primary);
            margin-bottom: 20px;
        }

        label {
            display: block;
            margin-top: 15px;
            font-weight: 600;
            color: #333;
        }

        input[type="text"],
        input[type="number"],
        select,
        input[readonly] {
            width: 100%;
            padding: 10px;
            margin-top: 6px;
            border: 1px solid #ccc;
            border-radius: 6px;
            font-size: 14px;
            box-sizing: border-box;
            background-color: #f9f9f9;
        }

        input[readonly] {
            background-color: #e9ecef;
            cursor: not-allowed;
        }

        .btn {
            padding: 10px 20px;
            border-radius: 6px;
            border: none;
            font-size: 14px;
            font-weight: 600;
            cursor: pointer;
            color: #fff;
            background-color: var(--primary);
            text-decoration: none;
            margin-top: 20px;
        }

        .btn:hover {
            background-color: #003f7f;
        }

        .btn-secondary {
            background-color: #6c757d;
            margin-left: 10px;
        }

        .btn-secondary:hover {
            background-color: #5a6268;
        }

        .alert {
            text-align: center;
            font-weight: bold;
            padding: 10px;
            border-radius: 6px;
            margin-bottom: 15px;
        }

        .alert.success {
            background-color: #d4edda;
            color: #155724;
            border: 1px solid #c3e6cb;
        }

        .alert.error {
            background-color: #f8d7da;
            color: #721c24;
            border: 1px solid #f5c6cb;
        }

        .button-group {
            display: flex;
            justify-content: space-between;
            margin-top: 25px;
        }
    </style>
</head>
<body>

<!-- Header -->
<div class="header">
    <div class="left-side">
        <div class="brand">Student App — Admin</div>
        <div class="welcome">Welcome, <strong>${sessionScope.username}</strong></div>
    </div>
    <div class="right-side">
        <a href="${pageContext.request.contextPath}/logout">Logout</a>
    </div>
</div>

<!-- Sidebar -->
<aside class="sidebar">
    <nav class="nav">
        <a href="${pageContext.request.contextPath}/admin/dashboard">Dashboard</a>
        <a href="${pageContext.request.contextPath}/admin/registerStudent">Add Student</a>
        <a href="${pageContext.request.contextPath}/admin/students">All Students</a>
        <a href="${pageContext.request.contextPath}/admin/courses">Courses</a>
        <a href="${pageContext.request.contextPath}/admin/courses/add">Add Course</a>
        <a href="${pageContext.request.contextPath}/admin/marks/list" class="active">Marks</a>
        <a href="${pageContext.request.contextPath}/admin/courses/enroll">Enroll Students</a>
        <a href="${pageContext.request.contextPath}/admin/reports">View Final Report</a>
    </nav>
</aside>

<!-- Main Content -->
<main class="main-content">
    <div class="form-container">
        <h2>Edit Marks</h2>

        <c:if test="${not empty error}">
            <div class="alert error">${error}</div>
        </c:if>

        <c:if test="${not empty success}">
            <div class="alert success">${success}</div>
        </c:if>

        <form:form method="post" action="${pageContext.request.contextPath}/admin/marks/save" modelAttribute="marksDTO" cssClass="needs-validation" novalidate="true">

            <!-- Hidden IDs -->
            <form:hidden path="id"/>
            <form:hidden path="studentId"/>
            <form:hidden path="courseId"/>

            <!-- Student Name (readonly) -->
            <label>Student</label>
            <input type="text" value="${marksDTO.studentName}" readonly/>

            <!-- Course Name (readonly) -->
            <label>Course</label>
            <input type="text" value="${marksDTO.courseName}" readonly/>

            <!-- Marks Input -->
            <label for="marks">Marks</label>
            <form:input path="marks" cssClass="form-control" id="marks" type="number" min="0" max="100" required="true"/>

            <div class="button-group">
                <a href="${pageContext.request.contextPath}/admin/marks/list" class="btn btn-secondary">Cancel</a>
                <button type="submit" class="btn">Update Marks</button>
            </div>
        </form:form>
    </div>
</main>

</body>
</html>