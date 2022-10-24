package com.bts.bugstalker.core.project;

import com.bts.bugstalker.api.model.ProjectInfoDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface ProjectMapper {

    ProjectInfoDto mapToDto(ProjectEntity project);

    List<ProjectInfoDto> mapToDto(List<ProjectEntity> projects);

    ProjectEntity mapToEntity(ProjectInfoDto projectInfoDto);

}
