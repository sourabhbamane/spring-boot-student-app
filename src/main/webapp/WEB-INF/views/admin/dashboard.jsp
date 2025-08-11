<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
  <title>Admin Dashboard</title>
  <meta name="viewport" content="width=device-width,initial-scale=1"/>
  <style>
    :root {
      --bg: #f3f6fb;
      --card: #fff;
      --muted: #6b7280;
      --primary: #0b6cf0;
      --danger: #dc3545;
      --accent: #28a745;
    }

    body {
      font-family: "Segoe UI", Roboto, Arial, sans-serif;
      margin: 0;
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

    .layout {
      display: flex;
      margin-top: 70px;
      min-height: calc(100vh - 70px);
    }

    .sidebar {
      width: 220px;
      background: #fff;
      padding: 20px;
      box-shadow: 2px 0 6px rgba(0,0,0,0.05);
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

    .cards {
      display: flex;
      gap: 16px;
      margin-bottom: 18px;
      flex-wrap: wrap;
    }

    .card {
      background: var(--card);
      padding: 14px;
      border-radius: 10px;
      min-width: 160px;
      box-shadow: 0 6px 18px rgba(10, 30, 60, .04);
    }

    .card .num {
      font-size: 20px;
      font-weight: 700;
      margin-top: 6px;
    }

    .card .label {
      color: var(--muted);
      font-size: 13px;
    }

    .table-wrap {
      background: var(--card);
      padding: 12px;
      border-radius: 10px;
      box-shadow: 0 6px 20px rgba(8, 30, 60, .04);
    }

    table {
      width: 100%;
      border-collapse: collapse;
    }

    th, td {
      padding: 10px 12px;
      border-bottom: 1px solid #eef2f6;
      text-align: left;
      font-size: 14px;
    }

    th {
      color: #233;
      font-weight: 700;
    }

    td.actions {
      text-align: right;
      white-space: nowrap;
    }

    .btn {
      padding: 6px 10px;
      border-radius: 6px;
      text-decoration: none;
      font-weight: 600;
      font-size: 13px;
      color: #fff;
    }

    .btn.edit { background: var(--primary); }
    .btn.delete { background: var(--danger); }

    .pagination {
      margin-top: 12px;
      text-align: right;
    }

    .pagination a {
      margin-left: 6px;
      padding: 6px 10px;
      background: #f5f7fb;
      border-radius: 6px;
      text-decoration: none;
      color: #123;
    }

    .no-data {
      padding: 18px;
      text-align: center;
      color: var(--muted);
    }

    @media (max-width: 900px) {
      .layout { flex-direction: column; }
      .cards { flex-direction: column; }
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
      <a href="${pageContext.request.contextPath}/admin/dashboard" class="active">Dashboard</a>
      <a href="${pageContext.request.contextPath}/admin/registerStudent">Add Student</a>
      <a href="${pageContext.request.contextPath}/admin/students">All Students</a>
      <a href="${pageContext.request.contextPath}/admin/courses">Courses</a>
      <a href="${pageContext.request.contextPath}/admin/courses/add">Add Course</a>
      <a href="${pageContext.request.contextPath}/admin/marks/list">Marks</a>
      <a href="${pageContext.request.contextPath}/admin/courses/enroll">Enroll Students</a>
      <a href="${pageContext.request.contextPath}/admin/reports">View Final Report</a>

    </nav>
  </aside>

  <main class="main">
    <!-- Summary cards -->
    <div class="cards">
      <div class="card">
        <div class="label">Total Students</div>
        <div class="num">${studentCount != null ? studentCount : 0}</div>
      </div>

      <div class="card">
        <div class="label">Total Courses</div>
        <div class="num">${courseCount != null ? courseCount : 0}</div>
      </div>

      <div class="card">
        <div class="label">Marks Records</div>
        <div class="num">${marksCount != null ? marksCount : 0}</div>
      </div>
    </div>

    <!-- Students Table -->
    <section class="table-wrap">
      <h3 style="margin:0 0 10px 0;">Recent Students</h3>

      <c:if test="${empty studentList}">
        <div class="no-data">No students found.</div>
      </c:if>

      <c:if test="${not empty studentList}">
        <table>
          <thead>
            <tr>
              <th>ID</th>
              <th>Name</th>
              <th>Email</th>
              <th style="width:160px;text-align:right;">Actions</th>
            </tr>
          </thead>
          <tbody>
            <c:forEach var="s" items="${studentList}">
              <tr>
                <td>${s.studentId}</td>
                <td>${s.name}</td>
                <td>${s.email}</td>
                <td class="actions">
                  <a class="btn edit" href="${pageContext.request.contextPath}/admin/editStudent/${s.studentId}">Edit</a>
                  <a class="btn delete" href="${pageContext.request.contextPath}/admin/deleteStudent/${s.studentId}" onclick="return confirm('Delete this student?')">Delete</a>
                </td>
              </tr>
            </c:forEach>
          </tbody>
        </table>

        <!-- Pagination -->
        <div class="pagination">
          <c:set var="cp" value="${currentPage != null ? currentPage : 1}" />
          <c:set var="tp" value="${totalPages != null ? totalPages : 1}" />

          <c:if test="${cp > 1}">
            <a href="?page=${cp - 1}">&laquo; Prev</a>
          </c:if>

          <c:forEach var="i" begin="1" end="${tp}">
            <c:choose>
              <c:when test="${i == cp}">
                <a style="background:#0b6cf0;color:#fff;" href="?page=${i}">${i}</a>
              </c:when>
              <c:otherwise>
                <a href="?page=${i}">${i}</a>
              </c:otherwise>
            </c:choose>
          </c:forEach>

          <c:if test="${cp < tp}">
            <a href="?page=${cp + 1}">Next &raquo;</a>
          </c:if>
        </div>
      </c:if>
    </section>
  </main>
</div>

</body>
</html>