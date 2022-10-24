package com.bts.bugstalker.core.issue;

import com.bts.bugstalker.core.shared.audit.AuditBaseEntity;
import com.bts.bugstalker.core.shared.enums.IssueSeverity;
import com.bts.bugstalker.core.shared.enums.IssueType;
import com.bts.bugstalker.core.project.ProjectEntity;
import com.bts.bugstalker.core.user.UserEntity;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@Builder
@Entity
@Table(name = "ISSUE")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class IssueEntity extends AuditBaseEntity {

    @Id
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "issue_generator")
    @SequenceGenerator(name="issue_generator", sequenceName = "issue_seq")
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private ProjectEntity project;

    @NotNull
    @Enumerated(EnumType.ORDINAL)
    private IssueType type;

    @NotNull
    @Enumerated(EnumType.ORDINAL)
    private IssueSeverity severity;

    @NotNull
    @NotBlank
    private String name;

    @NotNull
    @NotBlank
    private String status;

    @Length(max = 1000)
    private String description;

    private String summary;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "ISSUE_LABEL")
    private List<String> labels;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reporter_id")
    private UserEntity reporter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignee_id")
    private UserEntity assignee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "epic_id")
    private IssueEntity epic;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_issue_id")
    private IssueEntity parent;

    private String backlogList;
}
