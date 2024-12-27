package com.health.houseOfNature.models;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;

@Entity
public class Department {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	@Column(length = 2000)
	private String Info;
	@Column(length = 2000)
	private List<String> conditions;
	@Column(length = 2000)
	private List<String> benefits;
	@JsonIgnore
	@OneToMany(mappedBy = "department")
    private List<Doctor> doctors;

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
		return Info;
	}

	public void setInfo(String info) {
		Info = info;
	}

	public List<String> getConditions() {
		return conditions;
	}

	public void setConditions(List<String> conditions) {
		this.conditions = conditions;
	}

	public List<String> getBenefits() {
		return benefits;
	}

	public void setBenefits(List<String> benefits) {
		this.benefits = benefits;
	}

	public List<Doctor> getDoctors() {
		return doctors;
	}

	public void setDoctors(List<Doctor> doctors) {
		this.doctors = doctors;
	}

	
}
