package com.health.houseOfNature.models;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;

@Entity
public class Skills {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	
	@Column(nullable = false, unique = true)
	private String name;
	
	@JsonIgnore
	@ManyToMany(mappedBy = "skills")
    private Set<Doctor> doctors = new HashSet<>();

	public Skills(String name){
		this.name = name;
	}

	public Skills() {

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public Set<Doctor> getDoctors() {
		return doctors;
	}

	public void setDoctors(Set<Doctor> doctors) {
		this.doctors = doctors;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	
}
