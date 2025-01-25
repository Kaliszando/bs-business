package com.bts.bugstalker.feature.user;

import org.mapstruct.Mapper;
import org.openapitools.model.UserInfoDto;

import java.util.List;

@Mapper
public interface UserMapper {

    UserInfoDto mapToDto(UserEntity user);

    List<UserInfoDto> mapToDto(List<UserEntity> users);

}
