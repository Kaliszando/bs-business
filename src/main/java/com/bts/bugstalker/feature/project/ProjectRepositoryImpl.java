package com.bts.bugstalker.feature.project;

import com.bts.bugstalker.common.repository.BaseRepositoryImpl;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
public class ProjectRepositoryImpl extends BaseRepositoryImpl<ProjectEntity, Long> implements ProjectRepository {

    private final QProjectEntity project = QProjectEntity.projectEntity;

    public ProjectRepositoryImpl(EntityManager em) {
        super(ProjectEntity.class, em);
    }
}
