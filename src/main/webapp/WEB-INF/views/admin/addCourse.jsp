<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>
<html>
<head>
    <title>Add New Course</title>
    <meta charset="UTF-8">
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
            color: #0b2536;
        }

        .header {
            background: var(--card);
            padding: 14px 28px;
            display: flex;
            justify-content: space-between;
            align-items: center;
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

        .nav a.active {
            background-color: var(--primary);
            color: #fff;
        }

        .main-content {
            flex: 1;
            padding: 40px;
            margin-left: 220px;
        }

        .form-container {
            max-width: 600px;
            background: var(--card);
            padding: 25px 30px;
            border-radius: 10px;
            box-shadow: 0 0 12px rgba(0,0,0,0.05);
            margin: 0 auto;
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
        }

        input[type="text"],
        input[type="number"],
        textarea {
            width: 100%;
            padding: 10px;
            margin-top: 6px;
            border: 1px solid #ccc;
            border-radius: 6px;
            font-size: 14px;
            box-sizing: border-box;
        }

        .form-errors {
            color: red;
            font-size: 0.9em;
            margin-top: 5px;
        }

        .alert {
            padding: 10px;
            border-radius: 6px;
            text-align: center;
            margin-bottom: 15px;
            font-weight: 600;
        }

        .success {
            color: #155724;
            background-color: #d4edda;
        }

        .error {
            color: #721c24;
            background-color: #f8d7da;
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

        @media (max-width: 768px) {
            .layout {
                flex-direction: column;
            }

            .sidebar {
                width: 100%;
                position: relative;
                min-height: auto;
            }

            .main-content {
                margin-left: 0;
                padding: 20px;
            }
        }
    </style>

    <script>
        function validateForm() {
            const name = document.getElementById('courseName').value.trim();
            const credits = document.getElementById('credits').value.trim();

            if (!name) {
                alert('Course name is required.');
                return false;
            }
            if (!credits || isNaN(credits) || parseInt(credits) <= 0) {
                alert('Credits must be a positive number.');
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
            <a href="${pageContext.request.contextPath}/admin/courses/add" class="active">Add Course</a>
            <a href="${pageContext.request.contextPath}/admin/marks/list">Marks</a>
            <a href="${pageContext.request.contextPath}/admin/courses/enroll">Enroll Students</a>
             <a href="${pageContext.request.contextPath}/admin/reports">View Final Report</a>
        </nav>
    </aside>

    <main class="main-content">
        <div class="form-container">
            <h2>Add New Course</h2>

            <c:if test="${not empty success}">
                <div class="alert success">${success}</div>
            </c:if>

            <c:if test="${not empty error}">
                <div class="alert error">${error}</div>
            </c:if>

            <form:form modelAttribute="course"
                       action="${pageContext.request.contextPath}/admin/courses/save"
                       method="post"
                       onsubmit="return validateForm()">

                <form:hidden path="courseId"/>

                <label for="courseName">Course Name:</label>
                <form:input path="courseName" id="courseName" required="true"/>
                <form:errors path="courseName" cssClass="form-errors"/>

                <label for="description">Description:</label>
                <form:textarea path="description" id="description" rows="3"/>
                <form:errors path="description" cssClass="form-errors"/>

                <label for="credits">Credits:</label>
                <form:input path="credits" id="credits" type="number" min="1" required="true"/>
                <form:errors path="credits" cssClass="form-errors"/>

                <c:if test="${_csrf != null}">
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                </c:if>

                <div style="text-align:center;">
                    <button type="submit" class="btn">Save Course</button>
                    <a href="${pageContext.request.contextPath}/admin/dashboard" class="btn back-btn">Back</a>
                </div>
            </form:form>
        </div>
    </main>
</div>

</body>
</html>