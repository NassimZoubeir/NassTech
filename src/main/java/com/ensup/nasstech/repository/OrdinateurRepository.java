package com.ensup.nasstech.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.ensup.nasstech.entity.Ordinateur;


public interface OrdinateurRepository extends JpaRepository<Ordinateur, Long>  {
	@Modifying
	@Transactional
	@Query("UPDATE Ordinateur o SET o.nombreOrdinateur = o.nombreOrdinateur - 1 WHERE o.id = :id")
	void decrementernombreOrdinateur(@Param("id") Long id);
	
	@Modifying
	@Transactional
	@Query("UPDATE Ordinateur o SET o.nombreOrdinateur = o.nombreOrdinateur + 1 WHERE o.id = :id")
	void incrementernombreOrdinateur(@Param("id") Long id);
}