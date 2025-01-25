package com.bts.bugstalker.feature.membership;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class MembershipService {

    private final MembershipRepositoryImpl membershipRepository;

    public List<Long> getAllProjectIdsByUserId(Long userId) {
        return membershipRepository.findAllProjectIdsByUserId(userId);
    }

    public MembershipEntity create(MembershipEntity membership) {
        return membershipRepository.save(membership);
    }
}
