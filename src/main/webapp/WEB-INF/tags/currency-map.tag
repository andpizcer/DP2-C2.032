<%@tag%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<%@attribute name="code" required="true" type="java.lang.String"%>
<%@attribute name="map" required="true" type="java.util.Map"%>

<div style="padding: 5px 0 5px 0">
	<label>
		<acme:message code="${code}"/>
	</label>
	
	<table style="border-style: solid; border-width:1px; border-radius: 5px; border-color: #CED4DA; padding:0px; border-collapse: separate; width:100%; border-spacing:0">
		<jstl:forEach var="entry" items="${map.entrySet()}" varStatus="loop">
			<tr style="${loop.index %2 == 1 ? 'background-color: #eee' : ''}">
				<td style="width:7.5%">${entry.getKey()}</td>
				<td>${String.format("%.2f",entry.getValue().getAmount())}</td>
			</tr>
		</jstl:forEach>
	</table>
</div>
