<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="any.sponsorship.list.label.code" path="code"/>
	<acme:list-column code="any.sponsorship.list.label.amount" path="amount"/>
	<acme:list-column code="any.sponsorship.list.label.startDate" path="startDate" width="15%"/>
	<acme:list-column code="any.sponsorship.list.label.endDate" path="endDate" width="15%"/>
</acme:list>

