package vn.edu.hust.nmcnpm_20242_n3.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import vn.edu.hust.nmcnpm_20242_n3.entity.User;
import vn.edu.hust.nmcnpm_20242_n3.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
        @Autowired
        private UserRepository userRepository;

        @Override
        public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
            User user = userRepository.findByUserName(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
            return new UserDetailsImpl(user);
        }
}
