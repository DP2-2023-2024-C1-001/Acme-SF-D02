
package acme.entities.banner;

import java.util.Date;

import javax.persistence.Entity;
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

	private static final long	serialVersionUID	= 1L;

	@Past
	@NotNull
	private Date				instantiationUpdateMoment;

	//private Date				displayPeriod;

	@URL
	@NotBlank
	private String				picture;

	@NotBlank
	@Length(max = 76)
	private String				slogan;

	@URL
	@NotBlank
	private String				link;

}
