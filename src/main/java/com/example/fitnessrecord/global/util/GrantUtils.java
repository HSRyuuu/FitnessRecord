package com.example.fitnessrecord.global.util;

import com.example.fitnessrecord.domain.user.type.UserType;
import java.util.ArrayList;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class GrantUtils {
  public static List<GrantedAuthority> getAuthoritiesByUserType(UserType userType){
    List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
    grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));
    if (userType.equals(UserType.ADMIN)) {
      grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
    }
    return grantedAuthorities;
  }

  public static List<String> getStringAuthoritiesByUserType(UserType userType){
    List<String> list = new ArrayList<>();
    list.add("ROLE_USER");
    if (userType.equals(UserType.ADMIN)) {
      list.add("ROLE_ADMIN");
    }
    return list;
  }
}
