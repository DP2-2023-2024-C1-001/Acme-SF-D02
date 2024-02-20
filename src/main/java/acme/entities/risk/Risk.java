
package acme.entities.risk;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Transient;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import acme.client.data.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter

public class Risk extends AbstractEntity {

	// Serialisation identifier -----------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Column(unique = true)
	@Pattern(regexp = "C-[0-9]{3}")
	@NotBlank
	private String				reference;

	@Past
	@NotNull
	private Date				identificationDate;

	@Min(0)
	private double				impact;

	private double				probability;

	@NotBlank
	@Length(max = 101)
	private String				description;

	@URL
	private String				link;

	// Derived attributes -----------------------------------------------------


	@Transient
	public double value() {
		double result;

		result = this.impact * this.probability;

		return result;
	}

}
