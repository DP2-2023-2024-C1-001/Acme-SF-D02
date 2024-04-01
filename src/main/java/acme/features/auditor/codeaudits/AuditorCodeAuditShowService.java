
package acme.features.auditor.codeaudits;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.entities.codeaudit.CodeAudit;
import acme.entities.project.Project;
import acme.roles.Auditor;

@Service
public class AuditorCodeAuditShowService extends AbstractService<Auditor, CodeAudit> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AuditorCodeAuditRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		CodeAudit object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findOneCodeAuditById(id);

		super.getBuffer().addData(object);
	}

	@Override
	public void unbind(final CodeAudit object) {
		assert object != null;

		Dataset dataset;

		Collection<Project> projects;
		SelectChoices choices;
		int id;
		id = super.getRequest().getData("id", int.class);
		projects = this.repository.findProjectByCodeAuditId(id);
		choices = SelectChoices.from(projects, "code", object.getProject());

		dataset = super.unbind(object, "code", "execution", "type", "correctiveActions", "link");
		dataset.put("projects", choices);

		super.getResponse().addData(dataset);
	}
}
