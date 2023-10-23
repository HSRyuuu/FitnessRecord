package com.example.fitnessrecord.global.auth.sercurity.principal;

import com.example.fitnessrecord.domain.user.persist.User;
import com.example.fitnessrecord.domain.user.type.UserType;
import com.example.fitnessrecord.global.util.GrantUtils;
import java.util.Collection;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Data
public class PrincipalDetails implements UserDetails {

  private Long userId;
  private String email;
  private String password;
  private UserType userType;

  public PrincipalDetails(User user) {
    this.userId = user.getId();
    this.email = user.getEmail();
    this.password = user.getPassword();
    this.userType = user.getUserType();
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return GrantUtils.getAuthoritiesByUserType(this.userType);
  }

  @Override
  public String getPassword() {
    return this.password;
  }

  @Override
  public String getUsername() {
    return this.email;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
