package com.cegeka.cnads.simpleproject;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HelloUserRepository extends JpaRepository<HelloUser, String> {
}
