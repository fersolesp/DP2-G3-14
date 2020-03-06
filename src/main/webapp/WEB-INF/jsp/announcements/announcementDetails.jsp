<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="owners">

    <h2>Announcement Information</h2>


    <table class="table table-striped">
    	<tr>
            <th>Title</th>
            <td><c:out value="${announcement.name}"/></td>
        </tr>
        <tr>
            <th>Owner</th>
            <td><c:out value="${announcement.owner}"/></td>
        </tr>
        <tr>
            <th>Pet name</th>
            <td><c:out value="${announcement.petName}"/></td>
        </tr>
        <tr>
            <th>Description</th>
            <td><c:out value="${announcement.description}"/></td>
        </tr>
        <tr>
            <th>Can be adopted</th>
            <td><c:out value="${announcement.canBeAdopted}"/></td>
        </tr>
        <tr>
            <th>Type</th>
            <td><c:out value="${announcement.type}"/></td>
        </tr>
    </table>

</petclinic:layout>