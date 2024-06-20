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

<acme:form readonly="${readonly}">
	<acme:input-textbox code="authenticated.claim.form.code" path="code"/>	
	<acme:input-textbox code="authenticated.claim.form.instantiationMoment" path="instantiationMoment"/>	
	<acme:input-textbox code="authenticated.claim.form.heading" path="heading"/>	
	<acme:input-textarea code="authenticated.claim.form.description" path="description"/>	
	<acme:input-textbox code="authenticated.claim.form.department" path="department"/>	
	<acme:input-textbox code="authenticated.claim.form.email" path="email"/>	
	<acme:input-textbox code="authenticated.claim.form.link" path="link"/>	
	
	<jstl:if test="${!readonly}">
		<acme:input-checkbox code="authenticated.claim.form.label.confirmation" path="confirmation"/>
		<acme:submit code="authenticated.claim.form.button.publish" action="/authenticated/claim/create"/>
	</jstl:if>
</acme:form>