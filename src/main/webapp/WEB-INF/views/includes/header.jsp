<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

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
</style>

<div class="header">
    <div class="left-side">
        <div class="brand">Student App — Admin</div>
        <div class="welcome">Welcome, <strong>${username}</strong></div>
    </div>
    <div class="right-side">
        <a href="${pageContext.request.contextPath}/logout">Logout</a>
    </div>
</div>