<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
  <title>My Enrolled Courses</title>
  <style>
    body {
      font-family: "Segoe UI", Arial, sans-serif;
      margin: 0;
      background-color: #f3f6fb;
    }

    .main-content {
      margin-left: 220px;
      padding: 30px;
    }

    .container {
      background: #fff;
      padding: 20px;
      border-radius: 10px;
      box-shadow: 0 2px 10px rgba(0,0,0,0.05);
      max-width: 1000px;
      margin: auto;
    }

    h2 {
      text-align: center;
      margin-bottom: 20px;
      color: #003366;
    }

    table {
      width: 100%;
      border-collapse: collapse;
      margin-top: 10px;
    }

    th, td {
      border: 1px solid #e2e8f0;
      padding: 12px;
      text-align: left;
      font-size: 14px;
    }

    th {
      background-color: #f1f5f9;
      color: #333;
    }

    tr:hover {
      background-color: #fbfdff;
    }

    .no-courses {
      text-align: center;
      padding: 20px;
      font-size: 16px;
      color: #666;
    }

    @media (max-width: 768px) {
      .main-content {
        margin-left: 0;
        padding: 20px;
      }

      table, th, td {
        font-size: 13px;
      }
    }
  </style>
</head>
<body>

<%@ include file="../includes/studentHeader.jsp" %>
<%@ include file="../includes/studentSidebar.jsp" %>

<div class="main-content">
  <div class="container">
    <h2>My Enrolled Courses</h2>

    <c:if test="${empty courses}">
      <div class="no-courses">You are not enrolled in any courses.</div>
    </c:if>

    <c:if test="${not empty courses}">
      <table>
        <thead>
          <tr>
            <th>Name</th>
            <th>Description</th>
            <th>Credits</th>
          </tr>
        </thead>
        <tbody>
          <c:forEach var="course" items="${courses}">
            <tr>
              <td>${course.name}</td>
              <td>${course.description}</td>
              <td>${course.credits}</td>
            </tr>
          </c:forEach>
        </tbody>
      </table>
    </c:if>
  </div>
</div>

</body>
</html>