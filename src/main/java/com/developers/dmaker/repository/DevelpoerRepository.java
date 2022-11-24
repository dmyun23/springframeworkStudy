package com.developers.dmaker.repository;

import com.developers.dmaker.entity.Developer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Optional;

@Repository
public interface DevelpoerRepository extends JpaRepository<Developer,Long> {

    Optional<Developer> findByMemberId(String memberId);
}
