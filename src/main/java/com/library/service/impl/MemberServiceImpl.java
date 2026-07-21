package com.library.service.impl;

import com.library.dto.MemberRequestDTO;
import com.library.dto.MemberResponseDTO;
import com.library.entity.Member;
import com.library.exception.ResourceNotFoundException;
import com.library.repository.MemberRepository;
import com.library.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public MemberResponseDTO create(MemberRequestDTO request) {
        memberRepository.findByEmail(request.getEmail()).ifPresent(m -> {
            throw new IllegalArgumentException("A member with this email already exists");
        });

        Member member = Member.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .joinDate(LocalDate.now())
                .build();

        Member saved = memberRepository.save(member);
        return toResponse(saved);
    }

    @Override
    public MemberResponseDTO getById(Long id) {
        Member member = findMemberOrThrow(id);
        return toResponse(member);
    }

    @Override
    public Page<MemberResponseDTO> getAll(Pageable pageable) {
        return memberRepository.findAll(pageable).map(this::toResponse);
    }

    @Override
    @Transactional
    public MemberResponseDTO update(Long id, MemberRequestDTO request) {
        Member member = findMemberOrThrow(id);
        member.setFullName(request.getFullName());
        member.setEmail(request.getEmail());
        return toResponse(memberRepository.save(member));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Member member = findMemberOrThrow(id);
        memberRepository.delete(member);
    }

    private Member findMemberOrThrow(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found with id: " + id));
    }

    private MemberResponseDTO toResponse(Member member) {
        return MemberResponseDTO.builder()
                .id(member.getId())
                .fullName(member.getFullName())
                .email(member.getEmail())
                .joinDate(member.getJoinDate())
                .build();
    }
}
