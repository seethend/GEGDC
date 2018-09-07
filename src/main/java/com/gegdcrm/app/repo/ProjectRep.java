package com.gegdcrm.app.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gegdcrm.app.model.ProjectBean;

@Repository
public interface ProjectRep extends JpaRepository<ProjectBean, Integer>{

}
