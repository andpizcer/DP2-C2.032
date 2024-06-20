<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>
<%@taglib prefix="acme-one" tagdir="/WEB-INF/tags"%>

<acme:input-double code="sponsor.sponsor-dashborad.form.lowTaxInvoices" path="lowTaxInvoices"/>
<acme:input-double code="sponsor.sponsor-dashborad.form.linkedSponsorships" path="linkedSponsorships"/>
<acme-one:currency-map code="sponsor.sponsor-dashborad.form.averageAmountByCurrency" map="${averageAmountByCurrency}"/>
<acme-one:currency-map code="sponsor.sponsor-dashborad.form.amountDeviationByCurrency" map="${amountDeviationByCurrency}"/>
<acme-one:currency-map code="sponsor.sponsor-dashborad.form.minAmountByCurrency" map="${minAmountByCurrency}"/>
<acme-one:currency-map code="sponsor.sponsor-dashborad.form.maxAmountByCurrency" map="${maxAmountByCurrency}"/>
<acme-one:currency-map code="sponsor.sponsor-dashborad.form.averageQuantityByCurrency" map="${averageQuantityByCurrency}"/>
<acme-one:currency-map code="sponsor.sponsor-dashborad.form.quantityDeviationByCurrency" map="${quantityDeviationByCurrency}"/>
<acme-one:currency-map code="sponsor.sponsor-dashborad.form.minQuantityByCurrency" map="${minQuantityByCurrency}"/>
<acme-one:currency-map code="sponsor.sponsor-dashborad.form.maxQuantityByCurrency" map="${maxQuantityByCurrency}"/>