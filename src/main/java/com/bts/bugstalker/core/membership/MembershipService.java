package com.bts.bugstalker.core.membership;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class MembershipService {

    private final MembershipRepository membershipRepository;

    public List<MembershipEntity> getAllByUserId(Long userId) {
        return membershipRepository.findAllByUserId(userId);
    }

    public List<Long> getAllIdsByUserId(Long userId) {
        return membershipRepository.findAllIdsByUserId(userId);
    }

    public List<Long> getAllProjectIdsByUserId(Long userId) {
        return membershipRepository.findAllProjectIdsByUserId(userId);
    }

    public MembershipEntity create(MembershipEntity membership) {
        return membershipRepository.save(membership);
    }
}
