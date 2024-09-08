package com.example.User.Management.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.User.Management.Entity.Users;

public interface UsersRepository extends JpaRepository<Users, Integer> {

	public Users findByEmail(String email);

	public Users findByemailAndpassword(String email, String password);

}
