package com.bts.bugstalker.feature.project;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter @Setter @Builder
@Entity @Table(name = "PROJECT")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProjectEntity {

    @Setter(AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "project_generator")
    @SequenceGenerator(name="project_generator", sequenceName = "project_seq")
    private Long id;

    @NotBlank @NotNull private String name;

    @NotBlank @NotNull private String tag;

    @Length(max = 1000)
    private String description;
}
