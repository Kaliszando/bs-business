package com.bts.bugstalker.core.user;

import com.bts.bugstalker.api.model.UserInfoDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {

    UserInfoDto mapToDto(UserEntity user);

    List<UserInfoDto> mapToDto(List<UserEntity> users);

}
