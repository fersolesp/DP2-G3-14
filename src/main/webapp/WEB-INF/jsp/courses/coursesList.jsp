<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="courses">
    <h2>Announcements</h2>

    <table id="coursesTable" class="table table-striped">
        <thead>
        <tr>
            <th>Title</th>
            <th>Pet Type</th>
            <th>Vacancies</th>
            <th>Dangerous Pets</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${courses}" var="course">
            <tr>
				<td>
                    <spring:url value="/courses/{courseId}" var="courseUrl">
                        <spring:param name="courseId" value="${course.id}"/>
                    </spring:url>
                    <a href="${fn:escapeXml(courseUrl)}"><c:out value="${course.name}"/></a>
                </td>
                <td>
                    <c:out value="${course.petType}"/>
                </td>
                <td>
                    <c:out value="${course.vacancies}"/>
                </td>
				<td>
                    <c:out value="${course.dangerousAllowed}"/>
                </td>              
            </tr>
        </c:forEach>
        </tbody>
    </table>
</petclinic:layout>
