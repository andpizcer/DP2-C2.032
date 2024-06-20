
package acme.forms;

import acme.client.data.AbstractForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuditorDashboard extends AbstractForm {

	private static final long	serialVersionUID	= 1L;

	int							numberCodeAuditsStatic;
	int							numberCodeAuditsDynamic;

	double						numberAuditRecordsAverage;
	double						numberAuditRecordsDeviation;
	double						numberAuditRecordsMinimum;
	double						numberAuditRecordsMaximum;

	double						periodLengthAverage;
	double						periodLengthDeviation;
	double						periodLengthMinimum;
	double						periodLengthMaximum;

}
