package com.bts.bugstalker.feature.issue;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IssueRepository extends JpaRepository<IssueEntity, Long> {
}
