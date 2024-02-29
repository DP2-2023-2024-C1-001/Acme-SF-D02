
package acme.entities.banner;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import acme.client.data.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Banner extends AbstractEntity {

	// Serialisation identifier -----------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Temporal(TemporalType.TIMESTAMP)
	@Past
	@NotNull
	private Date				instantiationUpdateMoment;

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	private Date				displayPeriodInitial;

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	private Date				displayPeriodFinal;

	@URL
	@NotBlank
	private String				picture;

	@NotBlank
	@Length(max = 75)
	private String				slogan;

	@URL
	@NotBlank
	private String				link;

}
