package com.bts.bugstalker.core.issue;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IssueRepository extends JpaRepository<IssueEntity, Long> {

    List<IssueEntity> getAllByProjectId(Long id);

    IssueEntity getByProjectTagAndId(String tag, Long id);

}
