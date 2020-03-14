<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="appointments">
    <h2>Appointments</h2>

    <table id="appointmentsTable" class="table table-striped">
        <thead>
        <tr>
            <th>Description</th>
            <th>Date</th>
            <th>Pet name</th>
            <th>Hairdresser</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${appointments}" var="appointment">
            <tr>
				<td>
                    <spring:url value="/appointments/{appointmentId}" var="appointmentUrl">
                        <spring:param name="appointmentId" value="${appointment.id}"/>
                    </spring:url>
                    <a href="${fn:escapeXml(appointmentUrl)}"><c:out value="${appointment.description}"/></a>
                </td>
                <td>
                    <c:out value="${appointment.date}"/>
                </td>
                <td>
                    <c:out value="${appointment.pet.name}"/>
                </td>
				<td>
                    <c:out value="${appointment.hairdresser.firstName} ${appointment.hairdresser.lastName}"/>
                </td>              
            </tr>
        </c:forEach>
        </tbody>
    </table>
    <spring:url value="/appointments/new" var="addUrl">

    </spring:url>
    <a href="${fn:escapeXml(addUrl)}" class="btn btn-default">Add New Appointment</a>
</petclinic:layout>
