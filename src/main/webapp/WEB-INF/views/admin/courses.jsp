<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="../includes/header.jsp" %>
<%@ include file="../includes/sidebar.jsp" %>

<div class="layout">
  <main class="main">

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
                <td>${course.name}</td>
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

  </main>
</div>