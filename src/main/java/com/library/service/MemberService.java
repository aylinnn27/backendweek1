package com.library.service;

import com.library.dto.MemberRequestDTO;
import com.library.dto.MemberResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MemberService {
    MemberResponseDTO create(MemberRequestDTO request);
    MemberResponseDTO getById(Long id);
    Page<MemberResponseDTO> getAll(Pageable pageable);
    MemberResponseDTO update(Long id, MemberRequestDTO request);
    void delete(Long id);
}
