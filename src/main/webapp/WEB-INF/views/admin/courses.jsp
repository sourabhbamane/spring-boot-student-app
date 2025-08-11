<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <title>Courses</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <style>
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

        /* Fixed header */
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

        /* Sidebar */
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

        /* Main content starts after header and sidebar */
        .main-content {
            margin-top: 70px;
            margin-left: 220px;
            padding: 20px;
        }

        .page-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 20px;
        }

        .btn {
            padding: 8px 14px;
            border-radius: 6px;
            font-weight: 600;
            font-size: 14px;
            text-decoration: none;
            color: white;
        }

        .btn.add-btn { background-color: var(--accent); }
        .btn.back-btn { background-color: #6c757d; }
        .btn.add-btn:hover { background-color: #218838; }
        .btn.back-btn:hover { background-color: #5a6268; }

        .table-wrap {
            background: white;
            padding: 20px;
            border-radius: 10px;
            box-shadow: 0 4px 12px rgba(0,0,0,0.05);
        }

        table {
            width: 100%;
            border-collapse: collapse;
            font-size: 14px;
        }

        th, td {
            padding: 12px;
            border-bottom: 1px solid #dee2e6;
            text-align: left;
        }

        tr:nth-child(even) {
            background-color: #f9fbfd;
        }

        .actions {
            text-align: right;
        }

        .btn.edit-btn {
            background-color: var(--primary);
            margin-right: 8px;
        }

        .btn.edit-btn:hover {
            background-color: #0a58ca;
        }

        .btn.delete-btn {
            background-color: var(--danger);
        }

        .btn.delete-btn:hover {
            background-color: #a71d2a;
        }

        .no-data {
            text-align: center;
            padding: 20px;
            font-style: italic;
            color: var(--muted);
        }

        @media (max-width: 768px) {
            .sidebar {
                position: relative;
                width: 100%;
                height: auto;
            }

            .main-content {
                margin-left: 0;
            }
        }
    </style>
</head>
<body>

<!-- HEADER -->
<div class="header">
    <div class="left-side">
        <div class="brand">Student App — Admin</div>
        <div class="welcome">Welcome, <strong>${sessionScope.username}</strong></div>
    </div>
    <div class="right-side">
        <a href="${pageContext.request.contextPath}/logout">Logout</a>
    </div>
</div>

<!-- SIDEBAR -->
<aside class="sidebar">
    <nav class="nav">
        <a href="${pageContext.request.contextPath}/admin/dashboard">Dashboard</a>
        <a href="${pageContext.request.contextPath}/admin/registerStudent">Add Student</a>
        <a href="${pageContext.request.contextPath}/admin/students">All Students</a>
        <a href="${pageContext.request.contextPath}/admin/courses" class="active">Courses</a>
        <a href="${pageContext.request.contextPath}/admin/courses/add">Add Course</a>
        <a href="${pageContext.request.contextPath}/admin/marks/list">Marks</a>
        <a href="${pageContext.request.contextPath}/admin/courses/enroll">Enroll Students</a>
        <a href="${pageContext.request.contextPath}/admin/reports">View Final Report</a>
    </nav>
</aside>

<!-- MAIN -->
<div class="main-content">
    <div class="page-header">
        <h2>Courses</h2>
        <div>
            <a href="${pageContext.request.contextPath}/admin/courses/add" class="btn add-btn">+ Add New Course</a>
            <a href="${pageContext.request.contextPath}/admin/dashboard" class="btn back-btn">← Back</a>
        </div>
    </div>

    <div class="table-wrap">
        <c:if test="${empty courses}">
            <div class="no-data">No courses found.</div>
        </c:if>

        <c:if test="${not empty courses}">
            <table>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Course Name</th>
                        <th>Description</th>
                        <th>Credits</th>
                        <th style="text-align:right;">Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="course" items="${courses}">
                        <tr>
                            <td>${course.courseId}</td>
                            <td>${course.courseName}</td>
                            <td>${course.description}</td>
                            <td>${course.credits}</td>
                            <td class="actions">
                                <a href="${pageContext.request.contextPath}/admin/courses/edit/${course.courseId}" class="btn edit-btn">Edit</a>
                                <a href="${pageContext.request.contextPath}/admin/courses/delete/${course.courseId}"
                                   class="btn delete-btn"
                                   onclick="return confirm('Are you sure you want to delete this course?');">
                                    Delete
                                </a>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </c:if>
    </div>
</div>

</body>
</html>