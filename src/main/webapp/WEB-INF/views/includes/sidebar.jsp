<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<aside class="sidebar">
  <nav class="nav">
    <a href="${pageContext.request.contextPath}/admin/dashboard" class="${pageContext.request.requestURI endsWith '/dashboard' ? 'active' : ''}">Dashboard</a>
    <a href="${pageContext.request.contextPath}/admin/registerStudent" class="${pageContext.request.requestURI endsWith '/registerStudent' ? 'active' : ''}">Add Student</a>
    <a href="${pageContext.request.contextPath}/admin/students" class="${pageContext.request.requestURI endsWith '/students' ? 'active' : ''}">All Students</a>
    <a href="${pageContext.request.contextPath}/admin/courses" class="${pageContext.request.requestURI endsWith '/courses' ? 'active' : ''}">Courses</a>
    <a href="${pageContext.request.contextPath}/admin/courses/add" class="${pageContext.request.requestURI endsWith '/courses/add' ? 'active' : ''}">Add Course</a>
    <a href="${pageContext.request.contextPath}/admin/marks/list" class="${pageContext.request.requestURI endsWith '/marks/list' ? 'active' : ''}">Marks</a>
    <a href="${pageContext.request.contextPath}/admin/courses/enroll" class="${pageContext.request.requestURI endsWith '/courses/enroll' ? 'active' : ''}">Enroll Students</a>
  </nav>
</aside>