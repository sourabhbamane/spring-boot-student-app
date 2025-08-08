<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<style>
  .sidebar {
    width: 220px;
    min-height: 100vh;
    background: #ffffff;
    padding: 20px;
    box-shadow: 2px 0 6px rgba(0,0,0,0.05);
    position: relative;
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
    background-color: #0b6cf0;
    color: #fff;
  }

  /* Ensure layout behaves properly */
  .layout {
    display: flex;
    flex-direction: row;
  }

  .main {
    flex: 1;
    padding: 20px;
  }
</style>

<aside class="sidebar">
  <nav class="nav">
    <a href="${pageContext.request.contextPath}/admin/dashboard"
       class="${pageContext.request.requestURI.endsWith('/dashboard') ? 'active' : ''}">Dashboard</a>

    <a href="${pageContext.request.contextPath}/admin/registerStudent"
       class="${pageContext.request.requestURI.endsWith('/registerStudent') ? 'active' : ''}">Add Student</a>

    <a href="${pageContext.request.contextPath}/admin/students"
       class="${pageContext.request.requestURI.endsWith('/students') ? 'active' : ''}">All Students</a>

    <a href="${pageContext.request.contextPath}/admin/courses"
       class="${pageContext.request.requestURI.endsWith('/courses') ? 'active' : ''}">Courses</a>

    <a href="${pageContext.request.contextPath}/admin/courses/add"
       class="${pageContext.request.requestURI.endsWith('/courses/add') ? 'active' : ''}">Add Course</a>

    <a href="${pageContext.request.contextPath}/admin/marks/list"
       class="${pageContext.request.requestURI.endsWith('/marks/list') ? 'active' : ''}">Marks</a>

    <a href="${pageContext.request.contextPath}/admin/courses/enroll"
       class="${pageContext.request.requestURI.endsWith('/courses/enroll') ? 'active' : ''}">Enroll Students</a>
  </nav>
</aside>