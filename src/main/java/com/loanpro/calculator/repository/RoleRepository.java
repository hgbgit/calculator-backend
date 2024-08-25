package com.loanpro.calculator.repository;

import java.util.Optional;

import com.loanpro.calculator.models.ERole;
import com.loanpro.calculator.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
  Optional<Role> findByName(ERole name);
}
