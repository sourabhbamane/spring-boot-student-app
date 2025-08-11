<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
    <title>Student Dashboard</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <style>
        :root {
            --bg: #f3f6fb;
            --card: #fff;
            --primary: #0b6cf0;
            --muted: #6b7280;
            --danger: #dc3545;
            --accent: #28a745;
        }

        body {
            margin: 0;
            font-family: "Segoe UI", Roboto, Arial, sans-serif;
            background: var(--bg);
            color: #0b2536;
        }

        /* Header */
        .header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            background: var(--card);
            padding: 14px 28px;
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
            color: var(--muted);
        }

        .logout-btn {
            background: var(--danger);
            color: #fff;
            padding: 8px 12px;
            border-radius: 6px;
            text-decoration: none;
            font-weight: 600;
        }

        /* Layout */
        .layout {
            padding: 90px 20px 20px; /* space for fixed header */
            max-width: 1200px;
            margin: 0 auto;
        }

        /* Cards */
        .cards {
            display: flex;
            gap: 16px;
            flex-wrap: wrap;
            margin-bottom: 20px;
        }

        .card {
            background: var(--card);
            padding: 18px;
            border-radius: 10px;
            min-width: 160px;
            flex: 1;
            box-shadow: 0 6px 20px rgba(8, 30, 60, .05);
        }

        .card .label {
            font-size: 14px;
            color: var(--muted);
        }

        .card .num {
            font-size: 20px;
            font-weight: 700;
            margin-top: 6px;
        }

        /* Tabs */
        .tabs {
            display: flex;
            gap: 10px;
            margin-top: 20px;
        }

        .tab {
            padding: 10px 16px;
            border-radius: 6px;
            background: #eef3fb;
            cursor: pointer;
            font-weight: 600;
        }

        .tab.active {
            background: var(--primary);
            color: #fff;
        }

        .tab-content {
            display: none;
            margin-top: 16px;
        }

        .tab-content.active {
            display: block;
        }

        /* Tables */
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 12px;
            background: var(--card);
            box-shadow: 0 2px 6px rgba(0,0,0,0.05);
            border-radius: 8px;
        }

        th, td {
            padding: 12px;
            border-bottom: 1px solid #eee;
            text-align: left;
            font-size: 14px;
        }

        th {
            background: #f9fbfc;
            font-weight: 700;
        }

        /* Buttons */
        .btn {
            padding: 8px 14px;
            border: none;
            border-radius: 6px;
            color: #fff;
            font-weight: 600;
            cursor: pointer;
            text-decoration: none;
            display: inline-block;
        }

        .btn.success {
            background: var(--accent);
        }

        /* Messages */
        .message.success {
            color: #2e7d32;
            margin: 10px 0;
            text-align: center;
            font-weight: bold;
        }

        .message.error {
            color: #c0392b;
            margin: 10px 0;
            text-align: center;
            font-weight: bold;
        }
    </style>
</head>
<body>

<!-- Header -->
<div class="header">
    <div>
        <div class="brand">Student App — Student</div>
        <div class="welcome">Welcome, <strong>${sessionScope.username}</strong></div>
    </div>
    <a href="${pageContext.request.contextPath}/logout" class="logout-btn">Logout</a>
</div>

<div class="layout">

    <!-- Cards -->
    <div class="cards">
        <div class="card">
            <div class="label">Total Enrolled Courses</div>
            <div class="num">${fn:length(enrolledCourses)}</div>
        </div>
        <div class="card">
            <div class="label">Total Marks Assigned</div>
            <div class="num">${fn:length(studentMarks)}</div>
        </div>
    </div>

    <!-- Messages -->
    <c:if test="${not empty message}">
        <div class="message success">${message}</div>
    </c:if>
    <c:if test="${not empty error}">
        <div class="message error">${error}</div>
    </c:if>

    <!-- Tabs -->
    <div class="tabs">
        <div id="tab-profile" class="tab active" onclick="switchTab('profile')">My Profile</div>
        <div id="tab-courses" class="tab" onclick="switchTab('courses')">Enroll Courses</div>
        <div id="tab-marks" class="tab" onclick="switchTab('marks')">My Marks</div>
    </div>

    <!-- Profile -->
    <div id="profile" class="tab-content active">
        <table>
            <tr><th>ID</th><td>${student.studentId}</td></tr>
            <tr><th>Name</th><td>${student.name}</td></tr>
            <tr><th>Email</th><td>${student.email}</td></tr>
            <tr><th>DOB</th><td>${formattedDob}</td></tr>
            <tr><th>Gender</th><td>${student.gender}</td></tr>
            <tr><th>Address</th><td>${student.address}</td></tr>
        </table>
    </div>

    <!-- Courses -->
    <div id="courses" class="tab-content">
        <c:if test="${empty courses}">
            <p>No courses available at the moment.</p>
        </c:if>
        <form action="${pageContext.request.contextPath}/student/enroll" method="post" onsubmit="return validateSelection()">
            <table>
                <thead>
                    <tr>
                        <th>Select</th>
                        <th>Name</th>
                        <th>Description</th>
                        <th>Credits</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="course" items="${courses}">
                        <tr>
                            <td><input type="checkbox" class="course-checkbox" name="courseIds" value="${course.courseId}" onchange="limitSelection(this)"></td>
                            <td>${course.courseName}</td>
                            <td>${course.description}</td>
                            <td>${course.credits}</td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
            <div class="form-actions" style="text-align:center; margin-top:14px;">
                <button type="submit" class="btn success">Enroll Selected</button>
            </div>
        </form>
    </div>

    <!-- Marks -->
    <div id="marks" class="tab-content">
        <c:if test="${empty studentMarks}">
            <p>No marks assigned yet.</p>
        </c:if>
        <table>
            <thead>
                <tr>
                    <th>Course</th>
                    <th>Marks</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="mark" items="${studentMarks}">
                    <tr>
                        <td>${mark.course.courseName}</td>
                        <td>${mark.marks}</td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>

</div>

<!-- Scripts -->
<script>
    function switchTab(tabId) {
        document.querySelectorAll('.tab').forEach(t => t.classList.remove('active'));
        document.querySelectorAll('.tab-content').forEach(c => c.classList.remove('active'));
        document.getElementById('tab-' + tabId).classList.add('active');
        document.getElementById(tabId).classList.add('active');
    }

    function limitSelection(checkbox) {
        const max = 5;
        const checked = document.querySelectorAll('.course-checkbox:checked').length;
        if (checked > max) {
            alert('You can enroll in up to ' + max + ' courses only.');
            checkbox.checked = false;
        }
    }

    function validateSelection() {
        const checked = document.querySelectorAll('.course-checkbox:checked').length;
        if (checked === 0) {
            alert('Please select at least one course to enroll.');
            return false;
        }
        return true;
    }

    window.onload = function() { switchTab('profile'); };
</script>

<%@ include file="../includes/footer.jsp" %>
</body>
</html>