<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:input-textbox code="sponsor.invoice.form.label.code" path="code" placeholder="IN-0123-4567"/>
	<acme:input-moment code="sponsor.invoice.form.label.registrationTime" path="registrationTime" readonly="true"/>
	<acme:input-moment code="sponsor.invoice.form.label.dueDate" path="dueDate"/>
	<acme:input-money code="sponsor.invoice.form.label.quantity" path="quantity"/>
	<acme:input-double code="sponsor.invoice.form.label.tax" path="tax" placeholder="21"/>
	<acme:input-textbox code="sponsor.invoice.form.label.link" path="link"/>
	<acme:input-select code="sponsor.invoice.form.label.sponsorship" path="sponsorship" choices="${sponsorships}"/>
	<jstl:if test="${_command != 'create'}">
		<acme:input-money code="sponsor.invoice.form.label.totalAmount" path="totalAmount" readonly="true" placeholder="---"/>
	</jstl:if>
	
	<jstl:choose>
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="sponsor.invoice.form.button.create" action="/sponsor/invoice/create"/>
		</jstl:when>
		<jstl:when test="${acme:anyOf(_command, 'show|update|delete|publish') && draftMode == true}">
			<acme:submit code="sponsor.invoice.form.button.delete" action="/sponsor/invoice/delete"/>
			<acme:submit code="sponsor.invoice.form.button.update" action="/sponsor/invoice/update"/>
			<acme:submit code="sponsor.invoice.form.button.publish" action="/sponsor/invoice/publish"/>
		</jstl:when>
	</jstl:choose>

</acme:form>

