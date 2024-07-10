package com.adminverification.adminverification.Dao;

import com.adminverification.adminverification.Entity.LoginData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginDataRepository extends JpaRepository<LoginData,Long> {
}
