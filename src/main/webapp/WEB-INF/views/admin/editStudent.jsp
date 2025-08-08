<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Edit Student</title>
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
            background-color: var(--bg);
            margin: 0;
            padding: 0;
        }

        .header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding: 14px 28px;
            background: var(--card);
            box-shadow: 0 1px 4px rgba(0,0,0,0.06);
        }

        .header .brand {
            font-weight: 700;
            color: var(--primary);
            font-size: 18px;
        }

        .header .welcome {
            font-size: 14px;
            color: #333;
        }

        .header .right-side a {
            background: var(--danger);
            color: #fff;
            padding: 8px 12px;
            border-radius: 6px;
            text-decoration: none;
            font-weight: 600;
        }

        .container {
            max-width: 600px;
            margin: 40px auto;
            background-color: var(--card);
            padding: 30px;
            border-radius: 10px;
            box-shadow: 0 0 12px rgba(0,0,0,0.05);
        }

        h2 {
            text-align: center;
            color: var(--primary);
            margin-bottom: 25px;
        }

        label {
            display: block;
            margin-top: 15px;
            font-weight: 600;
            color: #333;
        }

        input[type="text"],
        input[type="email"],
        input[type="date"],
        textarea,
        select {
            width: 100%;
            padding: 10px;
            margin-top: 6px;
            border: 1px solid #ccc;
            border-radius: 6px;
            box-sizing: border-box;
            font-size: 14px;
        }

        .form-actions {
            text-align: center;
            margin-top: 30px;
        }

        .btn {
            padding: 10px 20px;
            border-radius: 6px;
            font-size: 14px;
            font-weight: 600;
            cursor: pointer;
            color: white;
            text-decoration: none;
            border: none;
        }

        .btn.update {
            background-color: var(--primary);
        }

        .btn.update:hover {
            background-color: #004bb7;
        }

        .btn.cancel {
            background-color: #6c757d;
            margin-left: 10px;
        }

        .btn.cancel:hover {
            background-color: #5a6268;
        }

        .error {
            text-align: center;
            color: red;
            font-weight: bold;
            margin-bottom: 10px;
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

<!-- Edit Form -->
<div class="container">
    <h2>Edit Student</h2>

    <c:if test="${not empty error}">
        <div class="error">${error}</div>
    </c:if>

    <form action="${pageContext.request.contextPath}/admin/updateStudent/${student.studentId}" method="post">
        <label for="name">Name:</label>
        <input type="text" name="name" id="name" value="${student.name}" required />

        <label for="email">Email:</label>
        <input type="email" name="email" id="email" value="${student.email}" required />

        <label for="dob">Date of Birth:</label>
        <input type="date" name="dob" id="dob" value="${student.dob}" required />

        <label for="gender">Gender:</label>
        <select name="gender" id="gender" required>
            <option value="MALE" ${student.gender == 'MALE' ? 'selected' : ''}>Male</option>
            <option value="FEMALE" ${student.gender == 'FEMALE' ? 'selected' : ''}>Female</option>
            <option value="OTHER" ${student.gender == 'OTHER' ? 'selected' : ''}>Other</option>
        </select>

        <label for="address">Address:</label>
        <textarea name="address" id="address" rows="3">${student.address}</textarea>

        <div class="form-actions">
            <button type="submit" class="btn update">Update Student</button>
            <a href="${pageContext.request.contextPath}/admin/students" class="btn cancel">Cancel</a>
        </div>
    </form>
</div>

</body>
</html>