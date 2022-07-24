package com.bts.bugstalker.core.user;

import com.bts.bugstalker.api.model.UserInfoDto;
import org.mapstruct.Mapper;

@Mapper
public
interface UserMapper {

    UserInfoDto mapToResponse(UserEntity user);

}
