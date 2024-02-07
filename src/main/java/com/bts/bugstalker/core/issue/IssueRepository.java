package com.bts.bugstalker.core.issue;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
//TODO
@Repository
public interface IssueRepository extends JpaRepository<IssueEntity, Long> {

    @EntityGraph(attributePaths = {"labels", "project", "reporter", "assignee"})
    List<IssueEntity> getAllByProjectId(Long id);

    IssueEntity getByProjectTagAndId(String tag, Long id);

}
