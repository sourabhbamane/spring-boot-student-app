<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ include file="../includes/header.jsp" %>
<%@ include file="../includes/sidebar.jsp" %>

<html>
<head>
    <title>Marks List</title>
    <style>
        .container {
            max-width: 1100px;
            margin: 22px auto;
            padding: 0 20px;
        }

        .card {
            background: var(--card);
            border-radius: 10px;
            padding: 18px;
            box-shadow: 0 6px 20px rgba(12,40,80,0.04);
        }

        h2 {
            margin: 0 0 12px 0;
            color: var(--primary);
            font-size: 22px;
        }

        .controls {
            display: flex;
            justify-content: space-between;
            align-items: center;
            gap: 12px;
            margin-bottom: 14px;
            flex-wrap: wrap;
        }

        .controls .left {
            color: var(--muted);
            font-size: 14px;
        }

        .controls .actions {
            display: flex;
            gap: 8px;
        }

        .btn {
            padding: 8px 12px;
            border-radius: 8px;
            text-decoration: none;
            font-weight: 600;
            font-size: 13px;
            color: #fff;
        }

        .btn.assign {
            background: var(--primary);
        }

        .btn.back {
            background: #6c757d;
        }

        .btn.edit {
            background: var(--primary);
            padding: 6px 10px;
        }

        .btn.delete {
            background: var(--danger);
            padding: 6px 10px;
        }

        .success-message {
            color: #155724;
            background: #d4edda;
            padding: 8px 12px;
            border-radius: 6px;
            display: inline-block;
        }

        .error-message {
            color: #721c24;
            background: #f8d7da;
            padding: 8px 12px;
            border-radius: 6px;
            display: inline-block;
        }

        .table-wrap {
            overflow-x: auto;
            margin-top: 10px;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            background: var(--card);
        }

        th, td {
            padding: 12px 14px;
            border-bottom: 1px solid var(--table-border);
            text-align: left;
            font-size: 14px;
        }

        th {
            background: transparent;
            color: #233;
            font-weight: 700;
        }

        tr:hover {
            background: #fbfdff;
        }

        td.actions {
            text-align: right;
            white-space: nowrap;
        }

        .muted {
            color: var(--muted);
            font-size: 13px;
        }

        .no-data {
            padding: 20px;
            text-align: center;
            color: var(--muted);
        }

        @media (max-width: 760px) {
            th, td {
                padding: 10px;
                font-size: 13px;
            }

            .controls {
                flex-direction: column;
                align-items: flex-start;
                gap: 8px;
            }
        }
    </style>
</head>
<body>

<div class="container">
    <div class="card">
        <div style="display:flex; justify-content:space-between; align-items:center; flex-wrap:wrap;">
            <h2>Assigned Marks</h2>
            <div class="controls">
                <div class="left muted">
                    Showing ${marksList != null ? fn:length(marksList) : 0} records
                </div>
                <div class="actions">
                    <a href="${pageContext.request.contextPath}/admin/marks/assign" class="btn assign">+ Assign Marks</a>
                    <a href="${pageContext.request.contextPath}/admin/dashboard" class="btn back">← Dashboard</a>
                </div>
            </div>
        </div>

        <c:if test="${not empty success}">
            <div style="margin:12px 0;"><span class="success-message">${success}</span></div>
        </c:if>

        <c:if test="${not empty error}">
            <div style="margin:12px 0;"><span class="error-message">${error}</span></div>
        </c:if>

        <div class="table-wrap">
            <c:if test="${empty marksList}">
                <div class="no-data">No marks assigned yet.</div>
            </c:if>

            <c:if test="${not empty marksList}">
                <table>
                    <thead>
                    <tr>
                        <th>Student</th>
                        <th>Course</th>
                        <th style="width:120px">Marks</th>
                        <th>Assigned By</th>
                        <th>Assigned On</th>
                        <th style="width:160px;text-align:right;">Actions</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="mark" items="${marksList}">
                        <tr>
                            <td>
                                <div style="font-weight:600;">${mark.student != null ? mark.student.name : '—'}</div>
                                <div class="muted" style="font-size:12px;">ID: ${mark.studentId}</div>
                            </td>
                            <td>
                                <div style="font-weight:600;">${mark.course != null ? mark.course.name : '—'}</div>
                                <div class="muted" style="font-size:12px;">Course ID: ${mark.courseId}</div>
                            </td>
                            <td style="font-weight:700;">${mark.marks}</td>
                            <td class="muted">${mark.createdBy != null ? mark.createdBy : '-'}</td>
                            <td class="muted">
                                <c:choose>
                                    <c:when test="${not empty mark.createdOn}">
                                        <fmt:formatDate value="${mark.createdOn}" pattern="yyyy-MM-dd HH:mm" />
                                    </c:when>
                                    <c:otherwise>-</c:otherwise>
                                </c:choose>
                            </td>
                            <td class="actions">
                                <a class="btn edit" href="${pageContext.request.contextPath}/admin/marks/edit/${mark.id}">Edit</a>
                                <a class="btn delete" href="${pageContext.request.contextPath}/admin/marks/delete/${mark.id}"
                                   onclick="return confirm('Delete this mark?');">Delete</a>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </c:if>
        </div>
    </div>
</div>

</body>
</html>