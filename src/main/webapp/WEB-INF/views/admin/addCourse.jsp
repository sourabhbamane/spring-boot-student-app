<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ include file="../includes/header.jsp" %>
<%@ include file="../includes/sidebar.jsp" %>

<div class="layout">
  <main class="main">
    <div class="form-container">
      <h2>Add New Course</h2>

      <c:if test="${not empty success}">
        <div class="alert success">${success}</div>
      </c:if>

      <c:if test="${not empty error}">
        <div class="alert error">${error}</div>
      </c:if>

      <form:form modelAttribute="course"
                 action="${pageContext.request.contextPath}/admin/courses/save"
                 method="post"
                 onsubmit="return validateForm()">

        <form:hidden path="courseId"/>

        <label for="name">Course Name:</label>
        <form:input path="name" id="name" required="true"/>
        <form:errors path="name" cssClass="form-errors"/>

        <label for="description">Description:</label>
        <form:textarea path="description" id="description" rows="3"/>
        <form:errors path="description" cssClass="form-errors"/>

        <label for="credits">Credits:</label>
        <form:input path="credits" id="credits" type="number" min="1" required="true"/>
        <form:errors path="credits" cssClass="form-errors"/>

        <c:if test="${_csrf != null}">
          <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        </c:if>

        <button type="submit" class="btn">Save Course</button>
        <a href="${pageContext.request.contextPath}/admin/dashboard" class="btn back-btn">Back to Dashboard</a>
      </form:form>
    </div>
  </main>
</div>

<script>
  function validateForm() {
    const name = document.getElementById('name').value.trim();
    const credits = document.getElementById('credits').value.trim();

    if (!name) {
      alert('Course name is required.');
      return false;
    }
    if (!credits || isNaN(credits) || parseInt(credits) <= 0) {
      alert('Credits must be a positive number.');
      return false;
    }

    return true;
  }
</script>