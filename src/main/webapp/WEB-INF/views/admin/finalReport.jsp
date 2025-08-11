<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
  <title>Final Grade Report</title>
  <style>
    :root {
      --bg: #f3f6fb;
      --card: #fff;
      --muted: #6b7280;
      --primary: #0b6cf0;
      --danger: #dc3545;
    }

    body {
      font-family: "Segoe UI", Roboto, Arial, sans-serif;
      background: var(--bg);
      margin: 0;
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
    }

    .card {
      background: var(--card);
      border-radius: 10px;
      padding: 20px;
      box-shadow: 0 6px 20px rgba(12,40,80,0.04);
    }

    h2 { color: var(--primary); font-size: 22px; margin-bottom: 20px; }

    .no-data {
      text-align: center;
      padding: 20px;
      color: var(--muted);
    }

    .table-wrap { overflow-x: auto; margin-top: 10px; }

    table {
      width: 100%;
      border-collapse: collapse;
      background: var(--card);
    }

    th, td {
      padding: 12px 14px;
      border-bottom: 1px solid #e3e6ec;
      text-align: center;
      font-size: 14px;
    }

    th {
      background-color: #f0f4ff;
      color: #123;
      font-weight: 700;
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
      <a href="${pageContext.request.contextPath}/admin/marks/list">Marks</a>
      <a href="${pageContext.request.contextPath}/admin/courses/enroll">Enroll Students</a>
      <a href="${pageContext.request.contextPath}/admin/final-report" class="active">Final Report</a>
    </nav>
  </aside>

  <main class="main">
    <div class="container">
      <div class="card">
        <h2>📊 Final Grade Report</h2>

        <c:if test="${empty reports}">
          <div class="no-data">No report data available.</div>
        </c:if>

        <c:if test="${not empty reports}">
          <div class="table-wrap">
            <table>
              <thead>
                <tr>
                  <th>Student Name</th>
                  <th>Average Marks</th>
                  <th>Grade</th>
                  <th>Percentile</th>
                </tr>
              </thead>
              <tbody>
                <c:forEach var="report" items="${reports}">
                  <tr>
                    <td>${report.studentName}</td>
                    <td>${report.averageMarks}</td>
                    <td>${report.grade}</td>
                    <td>${report.percentile}%</td>
                  </tr>
                </c:forEach>
              </tbody>
            </table>
          </div>
        </c:if>
      </div>
    </div>
  </main>
</div>

</body>
</html>