package com.health.houseOfNature.models;

import java.util.HashSet;
import java.util.Set;

import io.micrometer.common.lang.Nullable;
import jakarta.persistence.*;

@Entity
public class Doctor {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private String email;
	@Column(length = 500)
	private String info;
	private String address;
	private String phone;

	@ManyToOne
    @JoinColumn(name="department_id")
    private Department department;
	@Column(length = 1000)
	@ManyToMany
    @JoinTable(
        name = "doctor_skills",
        joinColumns = @JoinColumn(name = "doctor_id"),
        inverseJoinColumns = @JoinColumn(name = "skill_id")
    )
    private Set<Skills> skills = new HashSet<>();
	
	@ManyToMany
    @JoinTable(
        name = "doctor_licences",
        joinColumns = @JoinColumn(name = "doctor_id"),
        inverseJoinColumns = @JoinColumn(name = "licence_id")
    )
    private Set<Licence> licences = new HashSet<>();
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public Set<Skills> getSkills() {
		return skills;
	}

	public void setSkills(Set<Skills> skills) {
		this.skills = skills;
	}

	public Set<Licence> getLicences() {
		return licences;
	}

	public void setLicences(Set<Licence> licences) {
		this.licences = licences;
	}
	
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}


}
