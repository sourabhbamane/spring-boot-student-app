<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="../includes/header.jsp" %>
<%@ include file="../includes/sidebar.jsp" %>

<html>
<head>
    <title>Edit Student</title>
    <style>
        body {
            font-family: 'Segoe UI', sans-serif;
            background-color: var(--bg);
            margin: 0;
        }

        .form-container {
            max-width: 600px;
            margin: 40px auto;
            background-color: var(--card);
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
            margin-top: 25px;
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
        }

        .btn:hover {
            background-color: #003f7f;
        }

        .btn-cancel {
            background-color: #6c757d;
            margin-left: 10px;
        }

        .btn-cancel:hover {
            background-color: #5a6268;
        }
    </style>
</head>
<body>

<div class="form-container">
    <h2>Edit Student</h2>

    <form action="${pageContext.request.contextPath}/admin/updateStudent/${student.studentId}" method="post">
        <input type="hidden" name="id" value="${student.studentId}" />

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
            <button type="submit" class="btn">Update Student</button>
            <a href="${pageContext.request.contextPath}/admin/students" class="btn btn-cancel">Cancel</a>
        </div>
    </form>
</div>

</body>
</html>