<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8"/>
    <meta name="viewport" content="width=device-width,initial-scale=1"/>
    <title>Student Grading System — Register</title>
    <style>
        body {
            font-family: 'Segoe UI', Roboto, Arial, sans-serif;
            background-color: #f2f4f8;
            padding: 40px;
            margin: 0;
        }
        .page {
            min-height: 100%;
            display: flex;
            align-items: center;
            justify-content: center;
            padding: 28px;
        }
        .card {
            width: 100%;
            max-width: 500px;
            background: #fff;
            border-radius: 10px;
            box-shadow: 0 8px 30px rgba(13,38,59,0.08);
            padding: 26px;
            box-sizing: border-box;
        }
        h2 {
            text-align: center;
            color: #333;
            margin: 0 0 6px;
        }
        .lead {
            text-align: center;
            color: #6b7280;
            font-size: 14px;
            margin: 0 0 18px;
        }
        label {
            display: block;
            margin-bottom: 5px;
            font-weight: bold;
            color: #444;
        }
        input, select, textarea {
            width: 100%;
            padding: 10px;
            margin-bottom: 16px;
            border: 1px solid #ccc;
            border-radius: 6px;
            font-size: 14px;
            box-sizing: border-box;
        }
        input:focus, select:focus, textarea:focus {
            outline: none;
            border-color: #0b6cf0;
            box-shadow: 0 6px 18px rgba(11,108,240,0.08);
        }
        .error {
            text-align: center;
            color: #b02a37;
            background: #fff2f2;
            padding: 8px 12px;
            border-radius: 6px;
            margin-bottom: 12px;
        }
        .message {
            text-align: center;
            color: #084b8a;
            background: #eef7ff;
            padding: 8px 12px;
            border-radius: 6px;
            margin-bottom: 12px;
        }
        .form-actions {
            text-align: center;
            margin-top: 18px;
        }
        .btn {
            background: #0b6cf0;
            color: white;
            border: none;
            padding: 10px 20px;
            border-radius: 6px;
            cursor: pointer;
            font-weight: 700;
            font-size: 14px;
        }
        .btn:hover {
            background: #0358d6;
        }
        .username-status {
            font-size: 13px;
            margin-top: -12px;
            margin-bottom: 12px;
            padding-left: 4px;
        }
        .username-status.available { color: green; }
        .username-status.taken { color: red; }
    </style>
    <script>
        function checkUsernameAvailability() {
            const input = document.querySelector('input[name="userUsername"]');
            const message = document.getElementById('usernameStatus');
            input.addEventListener('input', function() {
                const username = this.value.trim();
                if (username.length < 4) {
                    message.textContent = '';
                    return;
                }
                fetch('${pageContext.request.contextPath}/api/checkUsername?username=' + encodeURIComponent(username))
                    .then(res => res.json())
                    .then(data => {
                        message.textContent = data.exists ? 'Username is already taken' : 'Username is available';
                        message.className = 'username-status ' + (data.exists ? 'taken' : 'available');
                    })
                    .catch(() => {
                        message.textContent = '';
                    });
            });
        }
        window.addEventListener('DOMContentLoaded', checkUsernameAvailability);
    </script>
</head>
<body>
<div class="page">
    <div class="card">
        <h2>Student Registration</h2>
        <p class="lead">Create a new account</p>

        <c:if test="${not empty error}">
            <div class="error">${error}</div>
        </c:if>
        <c:if test="${not empty message}">
            <div class="message">${message}</div>
        </c:if>

        <form action="${pageContext.request.contextPath}/register" method="post" autocomplete="on" novalidate>
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            <label for="name">Name:</label>
            <input type="text" id="name" name="name" value="${student.name}" required/>
            <label for="email">Email:</label>
            <input type="email" id="email" name="email" value="${student.email}" required/>
            <label for="dob">DOB:</label>
            <input type="date" id="dob" name="dob" value="${student.dob}" required/>
            <label for="gender">Gender:</label>
            <select id="gender" name="gender" required>
                <option value="Male" ${student.gender == 'Male' ? 'selected' : ''}>Male</option>
                <option value="Female" ${student.gender == 'Female' ? 'selected' : ''}>Female</option>
                <option value="Other" ${student.gender == 'Other' ? 'selected' : ''}>Other</option>
            </select>
            <label for="address">Address:</label>
            <textarea id="address" name="address">${student.address}</textarea>
            <label for="userUsername">Username:</label>
            <input type="text" id="userUsername" name="userUsername" required/>
            <div id="usernameStatus" class="username-status"></div>
            <label for="userPassword">Password:</label>
            <input type="password" id="userPassword" name="userPassword" required/>
            <div class="form-actions">
                <button type="submit" class="btn">Register</button>
            </div>
        </form>
        <div class="form-actions">
            <a href="${pageContext.request.contextPath}/login" class="btn">Back</a>
        </div>
    </div>
</div>
</body>
</html>