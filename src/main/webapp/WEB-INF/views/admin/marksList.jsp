<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html>
<head>
    <title>Marks List</title>
    <style>
        /* Your existing styles */
        :root {
            --bg: #f3f6fb;
            --card: #fff;
            --muted: #6b7280;
            --primary: #0b6cf0;
            --danger: #dc3545;
            --table-border: #e3e6ec;
        }

        * { margin: 0; padding: 0; box-sizing: border-box; }

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
            position: fixed;
            top: 0; left: 0; right: 0;
            box-shadow: 0 1px 4px rgba(0,0,0,0.06);
            z-index: 1000;
        }

        .brand { font-weight: 700; color: var(--primary); font-size: 18px; }
        .welcome { font-size: 14px; color: #333; }
        .header a {
            background: var(--danger);
            color: #fff;
            padding: 8px 12px;
            border-radius: 6px;
            text-decoration: none;
            font-weight: 600;
        }

        .layout { display: flex; margin-top: 70px; }
        .sidebar {
            width: 220px;
            background: #fff;
            padding: 20px;
            box-shadow: 2px 0 6px rgba(0,0,0,0.05);
            min-height: calc(100vh - 70px);
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

        .nav a:hover { background-color: #f0f4ff; }
        .nav a.active { background-color: var(--primary); color: #fff; }

        .main {
            flex: 1;
            padding: 20px;
        }

        .container {
            max-width: 1100px;
            margin: 0 auto;
            padding: 0 20px;
        }

        .card {
            background: var(--card);
            border-radius: 10px;
            padding: 20px;
            box-shadow: 0 6px 20px rgba(12,40,80,0.04);
        }

        h2 { color: var(--primary); font-size: 22px; margin-bottom: 16px; }

        .controls {
            display: flex;
            justify-content: space-between;
            margin-bottom: 12px;
            flex-wrap: wrap;
        }

        .controls .left { color: var(--muted); font-size: 14px; }
        .controls .actions { display: flex; gap: 8px; }

        .btn {
            padding: 8px 12px;
            border-radius: 8px;
            text-decoration: none;
            font-weight: 600;
            font-size: 13px;
            color: #fff;
        }

        .btn.assign { background: var(--primary); }
        .btn.back { background: #6c757d; }
        .btn.edit { background: var(--primary); padding: 6px 10px; }
        .btn.delete { background: var(--danger); padding: 6px 10px; }

        .success-message, .error-message {
            padding: 10px 16px;
            border-radius: 6px;
            font-weight: 500;
        }

        .success-message {
            background: #d4edda;
            color: #155724;
            border: 1px solid #c3e6cb;
        }

        .error-message {
            background: #f8d7da;
            color: #721c24;
            border: 1px solid #f5c6cb;
        }

        .table-wrap { overflow-x: auto; margin-top: 10px; }

        table {
            width: 100%;
            border-collapse: collapse;
            background: var(--card);
        }

        th, td {
            padding: 12px 14px;
            border-bottom: 1px solid var(--table-border);
            text-align: left;
            font-size: 14px;
        }

        th { font-weight: 700; }
        td.actions { text-align: right; white-space: nowrap; }

        .muted { color: var(--muted); font-size: 13px; }

        .no-data {
            text-align: center;
            padding: 20px;
            color: var(--muted);
        }

        .pagination {
            margin-top: 20px;
            text-align: center;
        }

        .pagination a {
            padding: 6px 12px;
            margin: 0 4px;
            text-decoration: none;
            color: var(--primary);
            font-weight: bold;
            border: 1px solid var(--primary);
            border-radius: 4px;
        }

        .pagination a.active {
            background-color: var(--primary);
            color: white;
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
            <a href="${pageContext.request.contextPath}/admin/students">All Students</a>
            <a href="${pageContext.request.contextPath}/admin/courses">Courses</a>
            <a href="${pageContext.request.contextPath}/admin/courses/add">Add Course</a>
            <a href="${pageContext.request.contextPath}/admin/marks/list" class="active">Marks</a>
            <a href="${pageContext.request.contextPath}/admin/courses/enroll">Enroll Students</a>
            <a href="${pageContext.request.contextPath}/admin/reports">View Final Report</a>
        </nav>
    </aside>

    <main class="main">
        <div class="container">
            <div class="card">
                <h2>Assigned Marks</h2>

                <div class="controls">
                    <div class="left muted">
                        Showing ${marksPage.totalElements} records
                    </div>
                    <div class="actions">
                        <a href="${pageContext.request.contextPath}/admin/marks/assign" class="btn assign">+ Assign Marks</a>
                        <a href="${pageContext.request.contextPath}/admin/dashboard" class="btn back">← Dashboard</a>
                    </div>
                </div>

                <c:if test="${marksPage.totalElements == 0}">
                    <div class="no-data">No marks assigned yet.</div>
                </c:if>

                <c:if test="${marksPage.totalElements > 0}">
                    <div class="table-wrap">
                        <table>
                            <thead>
                                <tr>
                                    <th>Student</th>
                                    <th>Course</th>
                                    <th>Marks</th>
                                    <th>Assigned By</th>
                                    <th>Assigned On</th>
                                    <th style="text-align:right;">Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="mark" items="${marksPage.content}">
                                    <tr>
                                        <td>
                                            <div style="font-weight:600;">${mark.student.name}</div>
                                            <div class="muted">ID: ${mark.studentId}</div>
                                        </td>
                                        <td>
                                            <div style="font-weight:600;">${mark.course.courseName}</div>
                                            <div class="muted">Course ID: ${mark.courseId}</div>
                                        </td>
                                        <td style="font-weight:700;">${mark.marks}</td>
                                        <td class="muted">${mark.createdBy}</td>
                                        <td class="muted">${mark.formattedCreatedOn}</td>
                                        <td class="actions">
                                            <a class="btn edit" href="${pageContext.request.contextPath}/admin/marks/edit/${mark.id}">Edit</a>
                                            <a class="btn delete" href="${pageContext.request.contextPath}/admin/marks/delete/${mark.id}"
                                               onclick="return confirm('Delete this mark?');">Delete</a>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </c:if>

                <div class="pagination">
                    <c:forEach var="i" begin="0" end="${marksPage.totalPages - 1}">
                        <a href="?page=${i}" class="${i == currentPage ? 'active' : ''}">${i + 1}</a>
                    </c:forEach>
                </div>
            </div>
        </div>
    </main>
</div>

</body>
</html>