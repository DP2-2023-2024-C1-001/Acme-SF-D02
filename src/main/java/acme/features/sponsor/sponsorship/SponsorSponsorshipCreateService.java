
package acme.features.sponsor.sponsorship;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.entities.project.Project;
import acme.entities.sponsorship.SponsorType;
import acme.entities.sponsorship.Sponsorship;
import acme.entities.systemconfiguration.SystemConfiguration;
import acme.roles.Sponsor;

@Service
public class SponsorSponsorshipCreateService extends AbstractService<Sponsor, Sponsorship> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private SponsorSponsorshipRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Sponsorship object;
		Sponsor sponsor;

		sponsor = this.repository.findSponsorById(super.getRequest().getPrincipal().getActiveRoleId());
		object = new Sponsorship();
		object.setPublished(false);
		object.setSponsor(sponsor);

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final Sponsorship object) {
		assert object != null;

		int projectId;
		Project project;

		projectId = super.getRequest().getData("project", int.class);
		project = this.repository.findProjectById(projectId);

		super.bind(object, "code", "moment", "initialDate", "finalDate", "amount", "type", "email", "link");
		object.setProject(project);
	}

	@Override
	public void validate(final Sponsorship object) {
		assert object != null;

		if (!super.getBuffer().getErrors().hasErrors("code")) {
			Sponsorship existing;

			existing = this.repository.findOneSponsorshipByCode(object.getCode());
			super.state(existing == null, "code", "sponsor.sponsorship.form.error.duplicated");

		}

		if (!super.getBuffer().getErrors().hasErrors("amount")) {
			Double amount;
			amount = object.getAmount().getAmount();
			super.state(amount >= 0, "amount", "sponsor.sponsorship.form.error.negativeAmount");

			final SystemConfiguration systemConfig = this.repository.findActualSystemConfiguration();
			final String currency = object.getAmount().getCurrency();
			super.state(systemConfig.getAcceptedCurrencies().contains(currency), "amount", "sponsor.sponsorship.form.error.currency");
		}
	}

	@Override
	public void perform(final Sponsorship object) {
		assert object != null;

		this.repository.save(object);
	}

	@Override
	public void unbind(final Sponsorship object) {
		assert object != null;

		Collection<Project> projects;
		SelectChoices choices;
		SelectChoices typesChoices;
		Dataset dataset;

		projects = this.repository.findAllProjects();
		choices = SelectChoices.from(projects, "code", object.getProject());
		typesChoices = SelectChoices.from(SponsorType.class, object.getType());

		dataset = super.unbind(object, "code", "moment", "initialDate", "finalDate", "amount", "email", "link", "published");
		dataset.put("project", choices.getSelected().getKey());
		dataset.put("projects", choices);
		dataset.put("types", typesChoices);

		super.getResponse().addData(dataset);
	}
}
