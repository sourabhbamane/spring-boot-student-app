<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>
<head>
  <title>Edit Profile</title>
  <style>
    body {
      font-family: Arial, sans-serif;
      background-color: #f3f6fb;
      margin: 0;
    }

    .main-content {
      margin-left: 220px; /* Match sidebar width */
      padding: 30px;
    }

    .container {
      max-width: 640px;
      margin: auto;
      padding: 20px;
      background: white;
      border-radius: 10px;
      box-shadow: 0 2px 10px rgba(0,0,0,0.08);
    }

    h2 {
      text-align: center;
      color: #003366;
      margin-bottom: 18px;
    }

    .field {
      margin-bottom: 16px;
      display: flex;
      gap: 12px;
      align-items: center;
    }

    .field label {
      width: 120px;
      font-weight: 600;
      color: #333;
    }

    .field .ctrl {
      flex: 1;
    }

    input[type="text"],
    input[type="email"],
    input[type="date"],
    select,
    textarea {
      width: 100%;
      padding: 10px;
      border-radius: 6px;
      border: 1px solid #ccd0d6;
      box-sizing: border-box;
    }

    textarea {
      min-height: 80px;
      resize: vertical;
    }

    .error {
      color: #b00020;
      font-size: 0.9em;
      margin-top: 6px;
      display: block;
    }

    .global-errors {
      color: #b00020;
      text-align: center;
      margin-bottom: 12px;
    }

    .actions {
      text-align: center;
      margin-top: 20px;
    }

    .btn {
      padding: 10px 18px;
      border-radius: 6px;
      border: none;
      background: #0b6cf0;
      color: white;
      font-weight: bold;
      text-decoration: none;
      cursor: pointer;
    }

    .btn.cancel {
      background: #6c757d;
      margin-left: 10px;
    }

    .btn:hover {
      opacity: 0.9;
    }

    @media (max-width: 600px) {
      .field {
        flex-direction: column;
        align-items: flex-start;
      }

      .field label {
        width: 100%;
      }
    }
  </style>
</head>
<body>

<%@ include file="../includes/studentHeader.jsp" %>
<%@ include file="../includes/studentSidebar.jsp" %>

<div class="main-content">
  <div class="container">
    <h2>Edit My Profile</h2>

    <!-- Global binding errors -->
    <c:if test="${not empty org.springframework.validation.BindingResult.student and org.springframework.validation.BindingResult.student.hasGlobalErrors()}">
      <div class="global-errors">
        <c:forEach var="err" items="${org.springframework.validation.BindingResult.student.globalErrors}">
          <div><c:out value="${err.defaultMessage}"/></div>
        </c:forEach>
      </div>
    </c:if>

    <form:form modelAttribute="student" method="post" action="${pageContext.request.contextPath}/student/updateProfile">
      <form:hidden path="id"/>

      <div class="field">
        <label for="name">Name</label>
        <div class="ctrl">
          <form:input path="name" id="name"/>
          <form:errors path="name" cssClass="error"/>
        </div>
      </div>

      <div class="field">
        <label for="email">Email</label>
        <div class="ctrl">
          <form:input path="email" id="email" type="email"/>
          <form:errors path="email" cssClass="error"/>
        </div>
      </div>

      <div class="field">
        <label for="dob">Date of Birth</label>
        <div class="ctrl">
          <form:input path="dob" id="dob" type="date"/>
          <form:errors path="dob" cssClass="error"/>
        </div>
      </div>

      <div class="field">
        <label for="gender">Gender</label>
        <div class="ctrl">
          <form:select path="gender" id="gender">
            <form:option value="">-- Select --</form:option>
            <form:option value="MALE">Male</form:option>
            <form:option value="FEMALE">Female</form:option>
            <form:option value="OTHER">Other</form:option>
          </form:select>
          <form:errors path="gender" cssClass="error"/>
        </div>
      </div>

      <div class="field">
        <label for="address">Address</label>
        <div class="ctrl">
          <form:textarea path="address" id="address"/>
          <form:errors path="address" cssClass="error"/>
        </div>
      </div>

      <!-- CSRF Token (optional but safe) -->
      <c:if test="${_csrf != null}">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
      </c:if>

      <div class="actions">
        <button type="submit" class="btn">Save Changes</button>
        <a href="${pageContext.request.contextPath}/student/dashboard" class="btn cancel">Cancel</a>
      </div>
    </form:form>
  </div>
</div>

<%@ include file="../includes/footer.jsp" %>
</body>
</html>