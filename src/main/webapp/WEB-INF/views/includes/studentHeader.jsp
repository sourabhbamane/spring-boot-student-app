<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="header">
  <div class="left-side">
    <div class="brand">Student App — Dashboard</div>
    <div class="welcome">Welcome, <strong>${sessionScope.user.username}</strong></div>
  </div>
  <div class="right-side">
    <a href="${pageContext.request.contextPath}/logout" class="logout-btn">Logout</a>
  </div>
</div>

<style>
  .header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 14px 28px;
    background: #fff;
    box-shadow: 0 1px 4px rgba(0,0,0,0.06);
    font-family: "Segoe UI", Roboto, Arial, sans-serif;
  }

  .header .brand {
    font-weight: 700;
    font-size: 18px;
    color: #0b6cf0;
  }

  .header .welcome {
    font-size: 14px;
    color: #333;
    margin-top: 4px;
  }

  .header .right-side {
    display: flex;
    align-items: center;
  }

  .logout-btn {
    background: #dc3545;
    color: #fff;
    padding: 8px 14px;
    border-radius: 6px;
    text-decoration: none;
    font-weight: 600;
    transition: background 0.2s ease-in-out;
  }

  .logout-btn:hover {
    background: #b02a37;
  }

  @media (max-width: 768px) {
    .header {
      flex-direction: column;
      align-items: flex-start;
      gap: 8px;
    }
  }
</style>