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
	<acme:input-integer code="manager.dashboard.form.label.totalMust" path="mustStories" readonly="true"/>
	<acme:input-integer code="manager.dashboard.form.label.totalShould" path="shouldStories" readonly="true"/>
	<acme:input-integer code="manager.dashboard.form.label.totalCould" path="couldStories" readonly="true"/>
	<acme:input-integer code="manager.dashboard.form.label.totalWont" path="wontStories" readonly="true"/>
	
	<acme:input-double code="manager.dashboard.form.label.avgEstCost" path="averageEstCost" readonly="true"/>
	<acme:input-double code="manager.dashboard.form.label.devEstCost" path="deviationEstCost" readonly="true"/>
	<acme:input-integer code="manager.dashboard.form.label.minEstCost" path="minimumEstCost" readonly="true"/>
	<acme:input-integer code="manager.dashboard.form.label.maxEstCost" path="maximumEstCost" readonly="true"/>
	
    <jstl:forEach var="entry" items="${metricsOfProjectsByCurrency}">
    	<div style="margin-top: 20px">
    		<h2>Metrics of ${entry.key} projects</h2>
			<table border="1" height="200" style="text-align: center; font-weight: bold;" >
	       		<tr>
	       			<td width="150">Average cost</td>
	       			<td width="250">${entry.value.get(0)}</td>
	       		</tr>
	       		<tr>
	       			<td>Deviation cost</td>
	       			<td>${entry.value.get(1)}</td>
	       		</tr>
	       		<tr>
	       			<td>Minimum cost</td>
	       			<td>${entry.value.get(2)}</td>
	       		</tr>
	       		<tr>
	       			<td>Maximum cost</td>
	       			<td>${entry.value.get(3)}</td>
	       		</tr>
       		</table>
    	</div>
    </jstl:forEach>
</acme:form>
