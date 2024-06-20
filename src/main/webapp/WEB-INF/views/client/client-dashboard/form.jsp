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
	<acme:input-integer code="client.dashboard.form.label.progressLogsWithCompletenessRateBelow25" path="progressLogsWithCompletenessRateBelow25" readonly="true"/>
	<acme:input-integer code="client.dashboard.form.label.progressLogsWithCompletenessRateBetween25And50" path="progressLogsWithCompletenessRateBetween25And50" readonly="true"/>
	<acme:input-integer code="client.dashboard.form.label.progressLogsWithCompletenessRateBetween50And75" path="progressLogsWithCompletenessRateBetween50And75" readonly="true"/>
	<acme:input-integer code="client.dashboard.form.label.progressLogsWithCompletenessRateAbove75" path="progressLogsWithCompletenessRateAbove75" readonly="true"/>
	
    <jstl:forEach var="currency" items="${currencies}">
        <h3>${currency}</h3>
        <table class="table table-sm">
			<tr>
				<th scope="row" style= "width: 30%">
					<acme:message code="client.dashboard.form.label.contractsBudgetAverage"/>
				</th>
				<td>
					<acme:print value="${contractsBudgetAverageByCurrency.get(currency)}"/>
				</td>
			</tr>
			<tr>
				<th scope="row">
					<acme:message code="client.dashboard.form.label.contractsBudgetDeviation"/>
				</th>
				<td>
					<acme:print value="${contractsBudgetDeviationByCurrency.get(currency)}"/>
				</td>
			</tr>
			<tr>
				<th scope="row">
					<acme:message code="client.dashboard.form.label.contractsMinBudget"/>
				</th>
				<td>
					<acme:print value="${contractsMinBudgetByCurrency.get(currency)}"/>
				</td>
			</tr>
			<tr>
				<th scope="row">
					<acme:message code="client.dashboard.form.label.contractsMaxBudget"/>
				</th>
				<td>
					<acme:print value="${contractsMaxBudgetByCurrency.get(currency)}"/>
				</td>
			</tr>
		</table>
    </jstl:forEach>
</acme:form>