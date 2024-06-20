
<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="sponsor.invoice.list.label.code" path="code"/>
	<acme:list-column code="sponsor.sponsorship.list.label.published" path="published" width="10%"/>
	<acme:list-column code="sponsor.invoice..list.label.dueDate" path="dueDate"/>
	<acme:list-column code="sponsor.invoice.list.label.quantity" path="quantity"/>	
</acme:list>

<acme:button code="sponsor.invoice.list.button.create" action="/sponsor/invoice/create"/>