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

<h2>
	<acme:message code="developer.dashboard.form.title.stats"/>
</h2>

<table class="table table-sm">
	<tr>
		<th scope="row">
			<acme:message code="developer.dashboard.form.label.training-modules-with-update-moment"/>
		</th>
		<td>
			<acme:print value="${trainingModulesWithUpdateMoment}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="developer.dashboard.form.label.training-sessions-with-link"/>
		</th>
		<td>
			<acme:print value="${trainingSessionsWithLink}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="developer.dashboard.form.label.average-time-of-training-modules"/>
		</th>
		<td>
			<acme:print value="${averageTimeOfTrainingModules}"/>
		</td>
	</tr>
		<tr>
		<th scope="row">
			<acme:message code="developer.dashboard.form.label.deviation-time-of-training-modules"/>
		</th>
		<td>
			<acme:print value="${deviationTimeOfTrainingModules}"/>
		</td>
	</tr>
		<tr>
		<th scope="row">
			<acme:message code="developer.dashboard.form.label.minimum-time-of-training-modules"/>
		</th>
		<td>
			<acme:print value="${minimumTimeOfTrainingModules}"/>
		</td>
	</tr>
		<tr>
		<th scope="row">
			<acme:message code="developer.dashboard.form.label.maximum-time-of-training-modules"/>
		</th>
		<td>
			<acme:print value="${maximumTimeOfTrainingModules}"/>
		</td>
	</tr>
</table>