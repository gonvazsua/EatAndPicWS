package com.plateandpic.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class PlateType {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long plateTypeId;
	
	@NotNull
	@Size(min = 4, max = 50)
	private String name;

	
	
	public long getPlateTypeId() {
		return plateTypeId;
	}

	public void setPlateTypeId(long plateTypeId) {
		this.plateTypeId = plateTypeId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
