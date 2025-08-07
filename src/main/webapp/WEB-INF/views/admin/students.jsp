<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ include file="../includes/header.jsp" %>
<%@ include file="../includes/sidebar.jsp" %>

<html>
<head>
    <title>Manage Students</title>
    <style>
        .container {
            max-width: 1100px;
            margin: 24px auto;
            padding: 0 20px;
        }

        h2 {
            text-align: left;
            margin-bottom: 20px;
            color: var(--primary);
            font-size: 22px;
        }

        .button-row {
            display: flex;
            gap: 10px;
            margin-bottom: 18px;
        }

        .btn {
            padding: 8px 14px;
            border-radius: 6px;
            font-weight: 600;
            text-decoration: none;
            font-size: 14px;
            color: white;
        }

        .btn.add {
            background-color: var(--accent);
        }

        .btn.add:hover {
            background-color: #218838;
        }

        .btn.back {
            background-color: #6c757d;
        }

        .btn.back:hover {
            background-color: #5a6268;
        }

        .message {
            text-align: center;
            font-weight: bold;
            margin-bottom: 16px;
        }

        .message.success {
            color: #28a745;
        }

        .message.error {
            color: #dc3545;
        }

        .table-wrap {
            background: var(--card);
            padding: 20px;
            border-radius: 10px;
            box-shadow: 0 6px 20px rgba(8, 30, 60, 0.05);
        }

        table {
            width: 100%;
            border-collapse: collapse;
            font-size: 14px;
        }

        th, td {
            padding: 12px;
            border-bottom: 1px solid #eef2f6;
            text-align: left;
        }

        th {
            background: transparent;
            color: #233;
            font-weight: 700;
        }

        tr:nth-child(even) {
            background-color: #f9fbfd;
        }

        td.actions {
            text-align: right;
            white-space: nowrap;
        }

        .action-btn {
            padding: 6px 12px;
            border-radius: 6px;
            font-size: 13px;
            font-weight: 600;
            color: white;
            text-decoration: none;
        }

        .edit-btn {
            background-color: var(--primary);
        }

        .edit-btn:hover {
            background-color: #0a58ca;
        }

        .delete-btn {
            background-color: var(--danger);
            margin-left: 6px;
        }

        .delete-btn:hover {
            background-color: #a71d2a;
        }

        .no-data {
            text-align: center;
            color: var(--muted);
            padding: 20px;
        }

        @media (max-width: 768px) {
            th, td {
                font-size: 13px;
                padding: 10px;
            }
            .btn, .action-btn {
                font-size: 12px;
            }
        }
    </style>
</head>
<body>

<div class="container">
    <h2>Manage Students</h2>

    <div class="button-row">
        <a href="${pageContext.request.contextPath}/admin/dashboard" class="btn back">← Back</a>
        <a href="${pageContext.request.contextPath}/admin/registerStudent" class="btn add">+ Add Student</a>
    </div>

    <c:if test="${not empty message}">
        <div class="message success">${message}</div>
    </c:if>
    <c:if test="${not empty error}">
        <div class="message error">${error}</div>
    </c:if>

    <div class="table-wrap">
        <c:if test="${empty students}">
            <div class="no-data">No students found.</div>
        </c:if>

        <c:if test="${not empty students}">
            <table>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Name</th>
                        <th>Email</th>
                        <th>DOB</th>
                        <th>Gender</th>
                        <th>Address</th>
                        <th style="text-align:right;">Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="student" items="${students}">
                        <tr>
                            <td>${student.id}</td>
                            <td>${student.name}</td>
                            <td>${student.email}</td>
                            <td><fmt:formatDate value="${student.dob}" pattern="yyyy-MM-dd" /></td>
                            <td>${student.gender}</td>
                            <td>${student.address}</td>
                            <td class="actions">
                                <a href="${pageContext.request.contextPath}/admin/editStudent/${student.id}" class="action-btn edit-btn">Edit</a>
                                <a href="${pageContext.request.contextPath}/admin/deleteStudent/${student.id}"
                                   class="action-btn delete-btn"
                                   onclick="return confirm('Are you sure you want to delete this student?');">
                                   Delete
                                </a>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </c:if>
    </div>
</div>

</body>
</html>