package com.example.newserial.domain.member.config.services;

import com.example.newserial.domain.error.BadRequestException;
import com.example.newserial.domain.error.ErrorCode;
import com.example.newserial.domain.member.repository.Member;
import com.example.newserial.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new BadRequestException("Email not found", ErrorCode.BAD_REQUEST));

        return UserDetailsImpl.build(member);
    }

}
