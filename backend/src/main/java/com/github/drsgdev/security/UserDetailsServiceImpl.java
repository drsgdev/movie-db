package com.github.drsgdev.security;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import com.github.drsgdev.model.DBObject;
import com.github.drsgdev.repository.DBObjectRepository;
import com.github.drsgdev.service.DBObjectService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

  private final DBObjectRepository objects;
  private final DBObjectService db;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Optional<DBObject> user = objects.findByDescrAndTypeName(username, "user");

    if (!user.isPresent()) {
      throw new UsernameNotFoundException("User " + username + " not found");
    }

    db.mapAttributes(user.get());
    String password = user.get().getAttributeMap().get("password");
    String enabled = user.get().getAttributeMap().get("enabled");

    Collection<GrantedAuthority> authorities =
        Collections.singletonList(new SimpleGrantedAuthority("user"));

    return new User(username, password, Boolean.parseBoolean(enabled), true, true, true,
        authorities);
  }
}
