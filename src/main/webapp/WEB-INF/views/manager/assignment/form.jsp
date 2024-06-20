<%--
- form.jsp
-
- Copyright (C) 2012-2024 Rafael Corchuelo.
-
- In keeping with the traditional purpose of furthering education and research, it is
- the policy of the copyright owner to permit non-commercial use and redistribution of
- this software. It has been tested carefully, but it is not guaranteed for any particular
- purposes.  The copyright owner does not offer any warranties or representations, nor do
- they accept any liabilities with respect to them.
--%>

<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:input-textbox code="manager.assignment.form.label.userstory" path="userStoryTitle" readonly="true"/>
	
	<jstl:choose>
		<jstl:when test="${_command == 'create'}">
			<acme:input-select code="manager.assignment.form.label.unlinked-project" path="projectId" choices="${projectCodes}"/>
			<acme:submit code="manager.assignment.form.button.link" action="/manager/assignment/create?userStoryId=${userStory.id}"/>
		</jstl:when>	
		<jstl:when test="${_command == 'delete'}">
			<acme:input-select code="manager.assignment.form.label.linked-project" path="projectId" choices="${projectCodes}"/>
			<acme:submit code="manager.assignment.form.button.unlink" action="/manager/assignment/delete?userStoryId=${userStory.id}"/>
		</jstl:when>
	</jstl:choose>
</acme:form>
