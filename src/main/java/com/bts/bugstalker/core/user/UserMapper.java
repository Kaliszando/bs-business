package com.bts.bugstalker.core.user;

import org.mapstruct.Mapper;

@Mapper
interface UserMapper {

    UserDto mapToDto(User user);

    User mapToModel(UserDto userDto);

}