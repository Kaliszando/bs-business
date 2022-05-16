package com.bts.bugstalker.core.user;

import org.mapstruct.Mapper;

@Mapper
interface UserMapper {

    UserDto mapToDto(UserEntity user);

    UserEntity mapToModel(UserDto userDto);

}