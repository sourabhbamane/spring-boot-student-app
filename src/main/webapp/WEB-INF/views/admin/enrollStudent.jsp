<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ include file="../includes/header.jsp" %>
<%@ include file="../includes/sidebar.jsp" %>

<html>
<head>
    <title>Enroll Student in Courses</title>
    <style>
        body {
            font-family: 'Segoe UI', sans-serif;
            background-color: var(--bg);
            margin: 0;
        }

        .form-container {
            max-width: 600px;
            margin: 40px auto;
            background: var(--card);
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

        select {
            width: 100%;
            padding: 10px;
            margin-top: 6px;
            border: 1px solid #ccc;
            border-radius: 6px;
            box-sizing: border-box;
            font-size: 14px;
        }

        .form-errors {
            color: red;
            font-size: 0.9em;
            margin-top: 5px;
        }

        .btn {
            margin-top: 20px;
            padding: 10px 20px;
            background-color: var(--primary);
            color: white;
            border: none;
            border-radius: 6px;
            cursor: pointer;
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

        .success {
            color: #2e7d32;
            background: #e7f5e8;
            padding: 10px;
            border-radius: 6px;
            text-align: center;
            margin-bottom: 12px;
            border: 1px solid #c3e6cb;
        }

        .error {
            color: #c0392b;
            background: #fdecea;
            padding: 10px;
            border-radius: 6px;
            text-align: center;
            margin-bottom: 12px;
            border: 1px solid #f5c6cb;
        }
    </style>

    <script>
        function validateForm() {
            const studentId = document.getElementById('studentId').value;
            const courseIds = Array.from(document.getElementById('courseIds').selectedOptions).map(option => option.value);

            if (!studentId) {
                alert('Please select a student.');
                return false;
            }

            if (courseIds.length === 0) {
                alert('Please select at least one course.');
                return false;
            }

            return true;
        }
    </script>
</head>
<body>

<div class="form-container">
    <h2>Enroll Student in Courses</h2>

    <c:if test="${not empty success}">
        <div class="success">${success}</div>
    </c:if>

    <c:if test="${not empty error}">
        <div class="error">${error}</div>
    </c:if>

    <form:form modelAttribute="enrollmentForm"
               action="${pageContext.request.contextPath}/admin/courses/enroll"
               method="post"
               onsubmit="return validateForm()">

        <label for="studentId">Student:</label>
        <form:select path="studentId" id="studentId" required="true">
            <form:option value="" label="-- Select Student --"/>
            <form:options items="${students}" itemValue="id" itemLabel="name"/>
        </form:select>
        <form:errors path="studentId" cssClass="form-errors"/>

        <label for="courseIds">Courses:</label>
        <form:select path="courseIds" id="courseIds" multiple="true" required="true">
            <form:options items="${courses}" itemValue="courseId" itemLabel="name"/>
        </form:select>
        <form:errors path="courseIds" cssClass="form-errors"/>

        <c:if test="${_csrf != null}">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        </c:if>

        <div style="text-align:center;">
            <button type="submit" class="btn">Enroll</button>
            <a href="${pageContext.request.contextPath}/admin/dashboard" class="btn back-btn">Back</a>
        </div>
    </form:form>
</div>

</body>
</html>