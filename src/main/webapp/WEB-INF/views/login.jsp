<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8"/>
    <meta name="viewport" content="width=device-width,initial-scale=1"/>
    <title>Student Grading System — Login</title>
    <style>
        :root {
            --bg: #f4f7fb;
            --card: #ffffff;
            --accent: #0b6cf0;
            --accent-dark: #0358d6;
            --muted: #6b7280;
            --danger: #b02a37;
            --radius: 10px;
            font-family: "Segoe UI", Roboto, "Helvetica Neue", Arial, sans-serif;
        }
        html, body { height: 100%; margin: 0; background: var(--bg); color: #102a43; }
        .page {
            min-height: 100%;
            display: flex;
            align-items: center;
            justify-content: center;
            padding: 28px;
        }
        .card {
            width: 100%;
            max-width: 420px;
            background: var(--card);
            border-radius: var(--radius);
            box-shadow: 0 8px 30px rgba(13,38,59,0.08);
            padding: 26px;
            box-sizing: border-box;
        }
        h1 {
            margin: 0 0 6px 0;
            font-size: 20px;
            color: #04263b;
            text-align: center;
        }
        p.lead {
            margin: 0 0 18px 0;
            text-align: center;
            color: var(--muted);
            font-size: 14px;
        }
        .messages { text-align: center; margin-bottom: 12px; }
        .messages .error {
            color: var(--danger);
            background: #fff2f2;
            display: inline-block;
            padding: 8px 12px;
            border-radius: 6px;
        }
        .messages .info {
            color: #084b8a;
            background: #eef7ff;
            display: inline-block;
            padding: 8px 12px;
            border-radius: 6px;
        }
        form { display: block; margin-top: 6px; }
        label {
            display: block;
            font-weight: 600;
            font-size: 13px;
            margin: 10px 0 6px;
            color: #123;
        }
        input[type="text"], input[type="password"] {
            width: 100%;
            padding: 10px 12px;
            border: 1px solid #d6e0ef;
            border-radius: 8px;
            font-size: 14px;
            box-sizing: border-box;
        }
        input:focus {
            outline: none;
            border-color: var(--accent);
            box-shadow: 0 6px 18px rgba(11,108,240,0.08);
        }
        .row {
            display: flex;
            gap: 12px;
            align-items: center;
            margin-top: 12px;
            justify-content: space-between;
        }
        .row .remember {
            display: flex;
            align-items: center;
            gap: 8px;
            color: var(--muted);
            font-size: 13px;
        }
        .actions {
            margin-top: 18px;
            display: flex;
            gap: 12px;
            justify-content: center;
            align-items: center;
            flex-wrap: wrap;
        }
        button.btn {
            background: var(--accent);
            color: white;
            border: none;
            padding: 10px 18px;
            border-radius: 8px;
            cursor: pointer;
            font-weight: 700;
            font-size: 14px;
        }
        button.btn:hover { background: var(--accent-dark); }
        a.link {
            color: var(--accent);
            text-decoration: none;
            font-size: 13px;
        }
        a.link:hover { text-decoration: underline; }
        .small {
            text-align: center;
            margin-top: 12px;
            color: var(--muted);
            font-size: 13px;
        }
        @media (max-width: 420px) {
            .card { padding: 18px; }
        }
    </style>
</head>
<body>
<div class="page">
    <main class="card" role="main" aria-labelledby="loginHeading">
        <h1 id="loginHeading">Student Grading Management</h1>
        <p class="lead">Sign in to your account</p>

        <div class="messages" aria-live="polite">
            <c:if test="${param.error != null}">
                <div class="error">Invalid username or password.</div>
            </c:if>
            <c:if test="${param.logout != null}">
                <div class="info">You have been logged out.</div>
            </c:if>
            <c:if test="${param.error == 'access_denied'}">
                <div class="error">Access denied: Unrecognized role.</div>
            </c:if>
            <c:if test="${param.expired != null}">
                <div class="error">Session expired, please log in again.</div>
            </c:if>
            <c:if test="${not empty message}">
                <div class="info">${message}</div>
            </c:if>
        </div>

        <form action="${pageContext.request.contextPath}/processLogin" method="post" autocomplete="on" novalidate>
            <label for="username">Username</label>
            <input id="username" name="username" type="text" required minlength="4" aria-required="true" autofocus />
            <label for="password">Password</label>
            <input id="password" name="password" type="password" required minlength="6" aria-required="true" />
            <div class="row">
                <label class="remember">
                    <input type="checkbox" name="remember-me" aria-label="Remember me"> Remember me
                </label>
                <a class="link" href="${pageContext.request.contextPath}/register">Create account</a>
            </div>
            <div class="actions">
                <button type="submit" class="btn">Sign in</button>
            </div>
        </form>
    </main>
</div>
</body>
</html>