package com.bts.bugstalker.core.project;

import org.mapstruct.Mapper;
import org.openapitools.model.ProjectInfoDto;

import java.util.List;

@Mapper
public interface ProjectMapper {

    ProjectInfoDto mapToDto(ProjectEntity project);

    List<ProjectInfoDto> mapToDto(List<ProjectEntity> projects);

    ProjectEntity mapToEntity(ProjectInfoDto projectInfoDto);

}
