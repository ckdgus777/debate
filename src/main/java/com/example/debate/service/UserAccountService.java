package com.example.debate.service;

import com.example.debate.domain.UserAccount;
import com.example.debate.dto.UserAccountDto;
import com.example.debate.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class UserAccountService {
    
    private final UserAccountRepository userAccountRepository;

    // 회원가입
    public void registerUserAccount(UserAccountDto dto) {
        userAccountRepository.save(dto.toEntity());
    }

    // 회원정보 조회
    public UserAccountDto getUserAccount(Long userAccountId) {
        return userAccountRepository.findById(userAccountId)
                .map(UserAccountDto::from)
                .orElseThrow(() -> new EntityNotFoundException("유저계정이 없습니다 - userAccountId: " + userAccountId));
    }

    // 회원정보수정
    public void updateUserAccount(UserAccountDto dto) {
        try {
            UserAccount userAccount = userAccountRepository.getReferenceById(dto.id());
            if (dto.userPassword() != null) userAccount.setUserPassword(dto.userPassword());
            if (dto.telephone() != null) userAccount.setTelephone(dto.telephone());
            userAccount.setEmail(dto.email());
            userAccount.setDescription(dto.description());
        } catch (EntityNotFoundException e) {
            log.warn("회원정보수정 실패. 회원정보를 찾을 수 없습니다. - {}", e.getLocalizedMessage());
        }

    }

    // 계정삭제
    public void deleteUserAccount(Long userAccountId) {
        userAccountRepository.deleteById(userAccountId);
    }
}
