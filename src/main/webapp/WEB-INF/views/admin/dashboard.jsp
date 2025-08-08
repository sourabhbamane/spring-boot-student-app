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
      display: flex;
      flex-direction: column;
      min-height: 100vh;
    }

    .layout {
      display: flex;
      flex: 1;
      min-height: calc(100vh - 60px); /* adjust based on header height */
    }

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
      background: transparent;
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

    .btn.edit {
      background: var(--primary);
    }

    .btn.delete {
      background: var(--danger);
    }

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
      .layout {
        flex-direction: column;
      }

      .cards {
        flex-direction: column;
      }

      .header {
        align-items: flex-start;
        gap: 8px;
      }

      .header .right-side {
        margin-top: 6px;
      }
    }
  </style>
</head>
<body>

  <%@ include file="../includes/header.jsp" %>

  <div class="layout">
    <%@ include file="../includes/sidebar.jsp" %>

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

      <!-- Students Table (paginated) -->
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

          <!-- Pagination controls -->
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