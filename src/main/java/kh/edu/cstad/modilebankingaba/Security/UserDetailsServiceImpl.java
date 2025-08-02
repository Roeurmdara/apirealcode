package kh.edu.cstad.modilebankingaba.Security;

import kh.edu.cstad.modilebankingaba.domain.User;
import kh.edu.cstad.modilebankingaba.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

  private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

     User user =  userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));

     CustomUserDetails customUserDetails = new CustomUserDetails();
     customUserDetails.setUser(user);




        return customUserDetails;
    }
}
