package sk.ukf.vd.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "employee")
public class Employee {

	// regularny vyraz pre email podla RFC 5321/5322 (bezny format):
	// - lokalna cast max. 64 znakov, povolene pismena, cislice a znaky !#$%&'*+/=?^_`{|}~-
	// - bodka nesmie byt na zaciatku, na konci ani dve za sebou
	// - domena z casti oddelenych bodkou, kazda max. 63 znakov, nezacina ani nekonci pomlckou
	// - koncova domena (TLD) ma aspon 2 pismena
	public static final String EMAIL_REGEX =
			"^(?=.{1,64}@)[A-Za-z0-9!#$%&'*+/=?^_`{|}~-]+(\\.[A-Za-z0-9!#$%&'*+/=?^_`{|}~-]+)*"
			+ "@([A-Za-z0-9]([A-Za-z0-9-]{0,61}[A-Za-z0-9])?\\.)+[A-Za-z]{2,63}$";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@NotBlank(message = "Meno je povinné")
	@Size(min = 2, max = 50, message = "Meno musí mať 2-50 znakov")
	@Column(name = "first_name", nullable = false)
	private String firstName;

	@NotBlank(message = "Priezvisko je povinné")
	@Size(min = 2, max = 50, message = "Priezvisko musí mať 2-50 znakov")
	@Column(name = "last_name", nullable = false)
	private String lastName;

	@NotNull(message = "Dátum narodenia je povinný")
	@Past(message = "Dátum narodenia musí byť v minulosti")
	@Column(name = "birth_date", nullable = false)
	private LocalDate birthDate;

	@NotBlank(message = "Email je povinný")
	@Email(message = "Neplatný email")
	@Pattern(regexp = EMAIL_REGEX, message = "Email nemá platný formát (napr. meno.priezvisko@firma.sk)")
	@Size(max = 254, message = "Email môže mať najviac 254 znakov")
	@Column(name = "email", nullable = false)
	private String email;

	@NotBlank(message = "Telefón je povinný")
	@Pattern(regexp = "^\\+[1-9]\\d{7,13}$",
			message = "Telefón musí byť v medzinárodnom formáte, napr. +421901234567 (max. 15 znakov)")
	@Column(name = "phone", nullable = false, length = 15)
	private String phone;

	@NotBlank(message = "Pracovná pozícia je povinná")
	@Size(max = 255, message = "Pracovná pozícia môže mať najviac 255 znakov")
	@Column(name = "job_title", nullable = false)
	private String jobTitle;

	@PositiveOrZero(message = "Plat nemôže byť záporný")
	@Digits(integer = 10, fraction = 0, message = "Plat musí byť celé číslo s max. 10 číslicami")
	@Column(name = "salary")
	private BigDecimal salary;

	@NotNull(message = "Údaj fullTime je povinný (true/false)")
	@Column(name = "full_time", nullable = false)
	private Boolean fullTime;

	public Employee() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public LocalDate getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getJobTitle() {
		return jobTitle;
	}

	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}

	public BigDecimal getSalary() {
		return salary;
	}

	public void setSalary(BigDecimal salary) {
		this.salary = salary;
	}

	public Boolean getFullTime() {
		return fullTime;
	}

	public void setFullTime(Boolean fullTime) {
		this.fullTime = fullTime;
	}
}
