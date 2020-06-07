package model.repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;


import model.entity.Bond;

@Repository
public interface BondRepository extends JpaRepository<Bond,Integer>{
    
}