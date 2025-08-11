<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <title>Assign Marks</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <style>
        :root {
            --bg: #f3f6fb;
            --card: #fff;
            --muted: #6b7280;
            --primary: #0b6cf0;
            --danger: #dc3545;
            --accent: #28a745;
        }

        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family: "Segoe UI", sans-serif;
            background: var(--bg);
            color: #0b2536;
        }

        .header {
            position: fixed;
            top: 0;
            left: 0;
            right: 0;
            height: 70px;
            background: var(--card);
            box-shadow: 0 1px 4px rgba(0, 0, 0, 0.06);
            padding: 14px 28px;
            display: flex;
            justify-content: space-between;
            align-items: center;
            z-index: 1000;
        }

        .header .brand {
            font-weight: bold;
            color: var(--primary);
            font-size: 18px;
        }

        .header .welcome {
            font-size: 14px;
            color: #333;
        }

        .header .right-side a {
            background: var(--danger);
            color: white;
            padding: 8px 12px;
            border-radius: 6px;
            text-decoration: none;
            font-weight: 600;
        }

        .sidebar {
            position: fixed;
            top: 70px;
            left: 0;
            bottom: 0;
            width: 220px;
            background: var(--card);
            padding: 20px;
            box-shadow: 2px 0 6px rgba(0, 0, 0, 0.05);
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
            color: white;
        }

        .main-content {
            margin-left: 220px;
            margin-top: 70px;
            padding: 20px;
        }

        .form-container {
            background: var(--card);
            max-width: 600px;
            margin: 0 auto;
            padding: 25px 30px;
            border-radius: 10px;
            box-shadow: 0 0 12px rgba(0, 0, 0, 0.05);
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

        input[type="number"],
        select {
            width: 100%;
            padding: 10px;
            margin-top: 6px;
            border: 1px solid #ccc;
            border-radius: 6px;
            font-size: 14px;
            box-sizing: border-box;
        }

        .btn {
            padding: 10px 20px;
            border-radius: 6px;
            border: none;
            font-size: 14px;
            font-weight: 600;
            cursor: pointer;
            color: #fff;
            background-color: var(--primary);
            text-decoration: none;
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

        .form-errors {
            color: red;
            font-size: 13px;
            margin-top: 4px;
        }

        .alert {
            text-align: center;
            font-weight: bold;
            padding: 10px;
            border-radius: 6px;
            margin-bottom: 15px;
        }

        .alert.success {
            background-color: #d4edda;
            color: #155724;
            border: 1px solid #c3e6cb;
        }

        .alert.error {
            background-color: #f8d7da;
            color: #721c24;
            border: 1px solid #f5c6cb;
        }
    </style>
</head>
<body>

<!-- Header -->
<div class="header">
    <div class="left-side">
        <div class="brand">Student App — Admin</div>
        <div class="welcome">Welcome, <strong>${sessionScope.username}</strong></div>
    </div>
    <div class="right-side">
        <a href="${pageContext.request.contextPath}/logout">Logout</a>
    </div>
</div>

<!-- Sidebar -->
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

<!-- Main Content -->
<main class="main-content">
    <div class="form-container">
        <h2>Assign Marks</h2>

        <c:if test="${not empty success}">
            <div class="alert success">${success}</div>
        </c:if>
        <c:if test="${not empty error}">
            <div class="alert error">${error}</div>
        </c:if>

        <form action="${pageContext.request.contextPath}/admin/marks/save" method="post" onsubmit="return validateForm()">
            <input type="hidden" name="id" value="${marks.id}" />

            <label for="studentId">Student:</label>
            <select name="studentId" id="studentId" required>
                <option value="">-- Select Student --</option>
                <c:forEach items="${students}" var="student">
                    <option value="${student.studentId}" ${marks.studentId == student.studentId ? 'selected' : ''}>${student.name}</option>
                </c:forEach>
            </select>
            <c:if test="${not empty errors.studentId}">
                <div class="form-errors">${errors.studentId}</div>
            </c:if>

            <label for="courseId">Course:</label>
            <select name="courseId" id="courseId" required>
                <option value="">-- Select Course --</option>
            </select>
            <c:if test="${not empty errors.courseId}">
                <div class="form-errors">${errors.courseId}</div>
            </c:if>

            <label for="marks">Marks:</label>
            <input type="number" name="marks" id="marks" min="0" max="100" step="1"
                   placeholder="Enter marks (0-100)"
                   required value="${marks.marks != null ? marks.marks : ''}" />
            <c:if test="${not empty errors.marks}">
                <div class="form-errors">${errors.marks}</div>
            </c:if>

            <c:if test="${_csrf != null}">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
            </c:if>

            <div style="text-align:center; margin-top: 20px;">
                <button type="submit" class="btn">Save Marks</button>
                <a href="${pageContext.request.contextPath}/admin/dashboard" class="btn back-btn">Back</a>
            </div>
        </form>
    </div>
</main>

<script>
    function validateForm() {
        const studentId = document.getElementById('studentId').value;
        const courseId = document.getElementById('courseId').value;
        const marksInput = document.getElementById('marks').value.trim();

        if (!studentId) {
            alert('Please select a student.');
            return false;
        }

        if (!courseId) {
            alert('Please select a course.');
            return false;
        }

        if (marksInput === '') {
            alert('Please enter marks.');
            return false;
        }

        const marks = Number(marksInput);
        if (isNaN(marks) || !Number.isInteger(marks) || marks < 0 || marks > 100) {
            alert('Marks must be an integer between 0 and 100.');
            return false;
        }

        return true;
    }

    function updateCourseDropdown() {
        const studentId = document.getElementById('studentId').value;
        const courseSelect = document.getElementById('courseId');
        courseSelect.innerHTML = '<option value="">-- Select Course --</option>';

        if (studentId) {
            fetch('${pageContext.request.contextPath}/admin/marks/courses/enrolled/' + studentId, {
                headers: { 'Accept': 'application/json' }
            })
            .then(res => {
                if (!res.ok) throw new Error('Failed to load courses');
                return res.json();
            })
            .then(data => {
                data.forEach(course => {
                    const option = document.createElement('option');
                    option.value = course.courseId;
                    option.text = course.name;
                    courseSelect.appendChild(option);
                });
                const selectedCourseId = '${marks.courseId}';
                if (selectedCourseId) {
                    courseSelect.value = selectedCourseId;
                }
            })
            .catch(err => {
                console.error(err);
                alert('Error loading courses. Please try again.');
            });
        }
    }

    window.onload = function () {
        const studentSelect = document.getElementById('studentId');
        studentSelect.addEventListener('change', updateCourseDropdown);

        const selectedStudentId = '${marks.studentId}';
        if (selectedStudentId) {
            studentSelect.value = selectedStudentId;
            updateCourseDropdown();
        }
    }
</script>

</body>
</html>