package com.sample.loginform.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sample.loginform.Entity.Data;

public interface DataRepository  extends JpaRepository<Data, Long>{

}
