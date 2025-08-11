<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
    <title>Enroll Student in Courses</title>
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
            font-family: 'Segoe UI', sans-serif;
            background-color: var(--bg);
            margin: 0;
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

        .brand {
            font-weight: 700;
            color: var(--primary);
            font-size: 18px;
        }

        .welcome {
            font-size: 14px;
            color: #333;
        }

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
        }

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

        .nav a:hover {
            background-color: #f0f4ff;
        }

        .nav a.active {
            background-color: var(--primary);
            color: #fff;
        }

        .main {
            flex: 1;
            padding: 30px;
        }

        .form-container {
            max-width: 600px;
            margin: 0 auto;
            background: var(--card);
            padding: 25px 30px;
            border-radius: 10px;
            box-shadow: 0 0 12px rgba(0,0,0,0.05);
        }

        h2 {
            text-align: center;
            color: var(--primary);
            margin-bottom: 20px;
        }

        label {
            display: block;
            margin-top: 15px;
            font-weight: 600;
            color: #333;
        }

        select {
            width: 100%;
            padding: 10px;
            margin-top: 6px;
            border: 1px solid #ccc;
            border-radius: 6px;
            box-sizing: border-box;
            font-size: 14px;
        }

        .form-errors {
            color: red;
            font-size: 0.9em;
            margin-top: 5px;
        }

        .btn {
            margin-top: 20px;
            padding: 10px 20px;
            background-color: var(--primary);
            color: white;
            border: none;
            border-radius: 6px;
            cursor: pointer;
            font-weight: 600;
        }

        .btn:hover {
            background-color: #003f7f;
        }

        .back-btn {
            background-color: #6c757d;
            margin-left: 10px;
        }

        .back-btn:hover {
            background-color: #5a6268;
        }

        .success, .error {
            text-align: center;
            font-size: 14px;
            padding: 10px;
            border-radius: 6px;
            margin-bottom: 12px;
            font-weight: 600;
        }

        .success {
            color: #2e7d32;
            background: #e7f5e8;
            border: 1px solid #c3e6cb;
        }

        .error {
            color: #c0392b;
            background: #fdecea;
            border: 1px solid #f5c6cb;
        }
    </style>

    <script>
        function validateForm() {
            const studentId = document.getElementById('studentId').value;
            const courseIds = Array.from(document.getElementById('courseIds').selectedOptions).map(option => option.value);

            if (!studentId) {
                alert('Please select a student.');
                return false;
            }

            if (courseIds.length === 0) {
                alert('Please select at least one course.');
                return false;
            }

            return true;
        }
    </script>
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
            <a href="${pageContext.request.contextPath}/admin/courses/enroll" class="active">Enroll Students</a>
            <a href="${pageContext.request.contextPath}/admin/reports">View Final Report</a>
        </nav>
    </aside>

    <main class="main">
        <div class="form-container">
            <h2>Enroll Student in Courses</h2>

            <c:if test="${not empty success}">
                <div class="success">${success}</div>
            </c:if>

            <c:if test="${not empty error}">
                <div class="error">${error}</div>
            </c:if>

            <form:form modelAttribute="enrollmentForm"
                       action="${pageContext.request.contextPath}/admin/courses/enroll"
                       method="post"
                       onsubmit="return validateForm()">

                <label for="studentId">Student:</label>
                <form:select path="studentId" id="studentId" required="true">
                    <form:option value="" label="-- Select Student --"/>
                    <form:options items="${students}" itemValue="studentId" itemLabel="name"/>
                </form:select>
                <form:errors path="studentId" cssClass="form-errors"/>

                <label for="courseIds">Courses:</label>
                <form:select path="courseIds" id="courseIds" multiple="true" required="true">
                    <form:options items="${courses}" itemValue="courseId" itemLabel="courseName"/>
                </form:select>
                <form:errors path="courseIds" cssClass="form-errors"/>

                <c:if test="${_csrf != null}">
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                </c:if>

                <div style="text-align:center;">
                    <button type="submit" class="btn">Enroll</button>
                    <a href="${pageContext.request.contextPath}/admin/dashboard" class="btn back-btn">Back</a>
                </div>
            </form:form>
        </div>
    </main>
</div>

</body>
</html>