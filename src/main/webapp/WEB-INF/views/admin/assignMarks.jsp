<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="../includes/header.jsp" %>
<%@ include file="../includes/sidebar.jsp" %>

<div class="layout">
  <main class="main">

    <div class="form-container">
      <h2>Assign Marks</h2>

      <c:if test="${not empty success}">
        <div class="alert success">${success}</div>
      </c:if>
      <c:if test="${not empty error}">
        <div class="alert error">${error}</div>
      </c:if>

      <form action="${pageContext.request.contextPath}/admin/marks/save" method="post" onsubmit="return validateForm()">
        <input type="hidden" name="id" value="${marks.id}" />

        <label for="studentId">Student:</label>
        <select name="studentId" id="studentId" required>
          <option value="">-- Select Student --</option>
          <c:forEach items="${students}" var="student">
            <option value="${student.studentId}" ${marks.studentId == student.studentId ? 'selected' : ''}>${student.name}</option>
          </c:forEach>
        </select>
        <c:if test="${not empty errors.studentId}">
          <div class="form-errors">${errors.studentId}</div>
        </c:if>

        <label for="courseId">Course:</label>
        <select name="courseId" id="courseId" required>
          <option value="">-- Select Course --</option>
        </select>
        <c:if test="${not empty errors.courseId}">
          <div class="form-errors">${errors.courseId}</div>
        </c:if>

        <label for="marks">Marks:</label>
        <input type="number" name="marks" id="marks" min="0" max="100" step="1"
               placeholder="Enter marks (0-100)"
               required value="${marks.marks != null ? marks.marks : ''}" />
        <c:if test="${not empty errors.marks}">
          <div class="form-errors">${errors.marks}</div>
        </c:if>

        <c:if test="${_csrf != null}">
          <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
        </c:if>

        <div style="text-align:center;">
          <button type="submit" class="btn">Save Marks</button>
          <a href="${pageContext.request.contextPath}/admin/dashboard" class="btn back-btn">Back</a>
        </div>
      </form>
    </div>

  </main>
</div>

<script>
  function validateForm() {
    const studentId = document.getElementById('studentId').value;
    const courseId = document.getElementById('courseId').value;
    const marksInput = document.getElementById('marks').value.trim();

    if (!studentId) {
      alert('Please select a student.');
      return false;
    }

    if (!courseId) {
      alert('Please select a course.');
      return false;
    }

    if (marksInput === '') {
      alert('Please enter marks.');
      return false;
    }

    const marks = Number(marksInput);
    if (isNaN(marks) || !Number.isInteger(marks) || marks < 0 || marks > 100) {
      alert('Marks must be an integer between 0 and 100.');
      return false;
    }

    return true;
  }

  function updateCourseDropdown() {
    const studentId = document.getElementById('studentId').value;
    const courseSelect = document.getElementById('courseId');
    courseSelect.innerHTML = '<option value="">-- Select Course --</option>';

    if (studentId) {
      fetch('${pageContext.request.contextPath}/admin/marks/courses/enrolled/' + studentId, {
        headers: { 'Accept': 'application/json' }
      })
        .then(res => {
          if (!res.ok) throw new Error('Failed to load courses');
          return res.json();
        })
        .then(data => {
          data.forEach(course => {
            const option = document.createElement('option');
            option.value = course.courseId;
            option.text = course.name;
            courseSelect.appendChild(option);
          });
          const selectedCourseId = '${marks.courseId}';
          if (selectedCourseId) {
            courseSelect.value = selectedCourseId;
          }
        })
        .catch(err => {
          console.error(err);
          alert('Error loading courses. Please try again.');
        });
    }
  }

  window.onload = function () {
    const studentSelect = document.getElementById('studentId');
    studentSelect.addEventListener('change', updateCourseDropdown);

    const selectedStudentId = '${marks.studentId}';
    if (selectedStudentId) {
      studentSelect.value = selectedStudentId;
      updateCourseDropdown();
    }
  }
</script>