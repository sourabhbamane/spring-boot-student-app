<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Manage Students</title>
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
            font-family: "Segoe UI", Roboto, Arial, sans-serif;
            background: var(--bg);
            color: #0b2536;
        }

        .header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding: 14px 28px;
            background: var(--card);
            box-shadow: 0 1px 4px rgba(0,0,0,0.06);
            position: fixed;
            top: 0;
            left: 0;
            right: 0;
            z-index: 1000;
        }

        .header .brand {
            font-weight: 700;
            color: var(--primary);
            font-size: 18px;
        }

        .header .welcome {
            font-size: 14px;
            color: #333;
            margin-top: 4px;
        }

        .header .right-side a {
            background: var(--danger);
            color: #fff;
            padding: 8px 12px;
            border-radius: 6px;
            text-decoration: none;
            font-weight: 600;
        }

        .layout {
            display: flex;
            margin-top: 70px;
        }

        .sidebar {
            width: 220px;
            min-height: calc(100vh - 70px);
            background: #ffffff;
            padding: 20px;
            box-shadow: 2px 0 6px rgba(0,0,0,0.05);
            position: fixed;
            left: 0;
            top: 70px;
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
            color: #fff;
        }

        .main-content {
            flex: 1;
            margin-left: 220px;
            padding: 20px;
        }

        .container {
            max-width: 1100px;
            margin: 0 auto;
            padding: 0 20px;
        }

        h2 {
            margin-bottom: 20px;
            color: var(--primary);
            font-size: 22px;
        }

        .button-row {
            display: flex;
            gap: 10px;
            margin-bottom: 18px;
        }

        .btn {
            display: inline-block;
            padding: 8px 14px;
            border-radius: 6px;
            font-weight: 600;
            text-decoration: none;
            font-size: 14px;
            color: white;
            transition: background-color 0.3s;
        }

        .btn.add { background-color: var(--accent); }
        .btn.add:hover { background-color: #218838; }
        .btn.back { background-color: #6c757d; }
        .btn.back:hover { background-color: #5a6268; }

        .message {
            text-align: center;
            font-weight: bold;
            margin-bottom: 16px;
            padding: 10px;
            border-radius: 5px;
        }

        .message.success {
            color: #28a745;
            background-color: #d4edda;
            border: 1px solid #c3e6cb;
        }

        .message.error {
            color: #dc3545;
            background-color: #f8d7da;
            border: 1px solid #f5c6cb;
        }

        .table-wrap {
            background: var(--card);
            padding: 20px;
            border-radius: 10px;
            box-shadow: 0 6px 20px rgba(8, 30, 60, 0.05);
            overflow-x: auto;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            font-size: 14px;
        }

        th, td {
            padding: 12px;
            border-bottom: 1px solid #eef2f6;
            text-align: left;
        }

        tr:nth-child(even) {
            background-color: #f9fbfd;
        }

        td.actions {
            text-align: right;
            white-space: nowrap;
        }

        .action-btn {
            display: inline-block;
            padding: 6px 12px;
            border-radius: 6px;
            font-size: 13px;
            font-weight: 600;
            color: white;
            text-decoration: none;
        }

        .edit-btn { background-color: var(--primary); }
        .edit-btn:hover { background-color: #0a58ca; }
        .delete-btn { background-color: var(--danger); margin-left: 6px; }
        .delete-btn:hover { background-color: #a71d2a; }

        .no-data {
            text-align: center;
            color: var(--muted);
            padding: 20px;
            font-style: italic;
        }

        .search-form {
            margin-bottom: 16px;
        }

        .search-form input[type="text"] {
            padding: 8px;
            width: 250px;
            margin-right: 8px;
        }

        .pagination a {
            padding: 6px 12px;
            margin: 0 4px;
            border: 1px solid #ccc;
            text-decoration: none;
            color: black;
        }

        .pagination a.active {
            background-color: #007bff;
            color: white;
            border-color: #007bff;
        }

        @media (max-width: 768px) {
            .layout {
                flex-direction: column;
            }

            .sidebar {
                position: relative;
                width: 100%;
                min-height: auto;
            }

            .main-content {
                margin-left: 0;
            }
        }
    </style>
</head>
<body>
<div class="header">
    <div class="left-side">
        <div class="brand">Student App — Admin</div>
        <div class="welcome">Welcome, <strong>${sessionScope.username}</strong></div>
    </div>
    <div class="right-side">
        <a href="${pageContext.request.contextPath}/logout">Logout</a>
    </div>
</div>

<div class="layout">
    <aside class="sidebar">
        <nav class="nav">
            <a href="${pageContext.request.contextPath}/admin/dashboard">Dashboard</a>
            <a href="${pageContext.request.contextPath}/admin/registerStudent">Add Student</a>
            <a href="${pageContext.request.contextPath}/admin/students" class="active">All Students</a>
            <a href="${pageContext.request.contextPath}/admin/courses">Courses</a>
            <a href="${pageContext.request.contextPath}/admin/courses/add">Add Course</a>
            <a href="${pageContext.request.contextPath}/admin/marks/list">Marks</a>
            <a href="${pageContext.request.contextPath}/admin/courses/enroll">Enroll Students</a>
        </nav>
    </aside>

    <main class="main-content">
        <div class="container">
            <h2>Manage Students</h2>

            <form action="${pageContext.request.contextPath}/admin/students" method="get" class="search-form">
                <input type="text" name="keyword" placeholder="Search by name..." value="${keyword}" />
                <button type="submit" class="btn add">Search</button>
            </form>

            <div class="button-row">
                <a href="${pageContext.request.contextPath}/admin/dashboard" class="btn back">← Back</a>
                <a href="${pageContext.request.contextPath}/admin/registerStudent" class="btn add">+ Add Student</a>
            </div>

            <c:if test="${not empty message}">
                <div class="message success">${message}</div>
            </c:if>
            <c:if test="${not empty error}">
                <div class="message error">${error}</div>
            </c:if>

            <div class="table-wrap">
                <c:choose>
                    <c:when test="${students == null}">
                        <div class="no-data">Students object is null.</div>
                    </c:when>
                    <c:when test="${empty students}">
                        <div class="no-data">No students found. (Empty list)</div>
                    </c:when>
                    <c:otherwise>
                        <table>
                            <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>Name</th>
                                    <th>Email</th>
                                    <th>DOB</th>
                                    <th>Gender</th>
                                    <th>Address</th>
                                    <th style="text-align:right;">Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="student" items="${students}">
                                    <tr>
                                        <td>${student.studentId}</td>
                                        <td>${student.name}</td>
                                        <td>${student.email}</td>
                                        <td>${student.formattedDob}</td>
                                        <td>${student.gender}</td>
                                        <td>${student.address}</td>
                                        <td class="actions">
                                            <a href="${pageContext.request.contextPath}/admin/editStudent/${student.studentId}" class="action-btn edit-btn">Edit</a>
                                            <a href="${pageContext.request.contextPath}/admin/deleteStudent/${student.studentId}" class="action-btn delete-btn"
                                               onclick="return confirm('Are you sure you want to delete this student?');">Delete</a>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </c:otherwise>
                </c:choose>

                <c:if test="${totalPages > 1}">
                    <div class="pagination" style="margin-top: 20px; text-align: center;">
                        <c:forEach begin="0" end="${totalPages - 1}" var="i">
                            <a href="${pageContext.request.contextPath}/admin/students?page=${i}&keyword=${keyword}" class="${i == currentPage ? 'active' : ''}">${i + 1}</a>
                        </c:forEach>
                    </div>
                </c:if>
            </div>
        </div>
    </main>
</div>
</body>
</html>