package com.example.User.Management.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.User.Management.Entity.Countries;

public interface CountriesRepository extends JpaRepository<Countries, Integer> {

}
