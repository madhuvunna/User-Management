package com.example.User.Management.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.User.Management.Entity.States;

public interface StatesRepository extends JpaRepository<States, Integer> {

	public List<States> findByCountryId(Integer countryId);

}
