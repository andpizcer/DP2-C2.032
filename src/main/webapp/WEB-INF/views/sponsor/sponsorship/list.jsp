<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="sponsor.sponsorship.list.label.code" path="code"/>
	<acme:list-column code="sponsor.sponsorship.list.label.published" path="published" width="10%"/>
	<acme:list-column code="sponsor.sponsorship.list.label.amount" path="amount"/>
	<acme:list-column code="sponsor.sponsorship.list.label.startDate" path="startDate" width="15%"/>
	<acme:list-column code="sponsor.sponsorship.list.label.endDate" path="endDate" width="15%"/>
</acme:list>

<acme:button code="sponsor.sponsorship.list.buttom.create" action="/sponsor/sponsorship/create"/>

