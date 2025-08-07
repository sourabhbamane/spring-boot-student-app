<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Student Management System</title>
    <meta name="viewport" content="width=device-width,initial-scale=1" />
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
            margin: 0;
            background: var(--bg);
            color: #0b2536;
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

        .form-card {
            background: var(--card);
            padding: 25px 30px;
            max-width: 600px;
            margin: 30px auto;
            border-radius: 10px;
            box-shadow: 0 0 12px rgba(0,0,0,0.1);
        }

        h2 {
            text-align: center;
            color: #333;
            margin-bottom: 20px;
        }

        .alert {
            padding: 10px;
            border-radius: 6px;
            text-align: center;
            margin-bottom: 16px;
            font-weight: bold;
        }

        .alert.success {
            background-color: #e7f5e8;
            color: #2e7d32;
            border: 1px solid #c3e6cb;
        }

        .alert.error {
            background-color: #fdecea;
            color: #c0392b;
            border: 1px solid #f5c6cb;
        }

        label {
            display: block;
            margin-top: 15px;
            font-weight: 600;
            color: #444;
        }

        input[type="text"],
        input[type="number"],
        textarea {
            width: 100%;
            padding: 10px;
            margin-top: 6px;
            border: 1px solid #ccc;
            border-radius: 6px;
            box-sizing: border-box;
        }

        .form-errors {
            color: red;
            font-size: 0.85em;
            margin-top: 4px;
        }

        .btn {
            margin-top: 20px;
            padding: 10px 20px;
            background-color: var(--primary);
            color: white;
            border: none;
            border-radius: 6px;
            cursor: pointer;
            display: inline-block;
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

        @media (max-width: 540px) {
            .form-card {
                padding: 18px;
            }
        }
    </style>
</head>
<body>

<div class="header">
    <div class="left-side">
        <div class="brand">Student App — Admin</div>
        <div class="welcome">Welcome, <strong>${username}</strong></div>
    </div>
    <div class="right-side">
        <a href="${pageContext.request.contextPath}/logout">Logout</a>
    </div>
</div>