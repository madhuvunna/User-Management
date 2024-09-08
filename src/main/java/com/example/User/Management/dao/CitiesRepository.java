package com.example.User.Management.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.User.Management.Entity.Cities;

public interface CitiesRepository extends JpaRepository<Cities, Integer> {

	public List<Cities> findByStateId(Integer stateId);

}
