package com.hack.stock2u.utils;

import com.hack.stock2u.constant.UserRole;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RoleGuard {
  UserRole[] roles() default {UserRole.SELLER, UserRole.ADMIN, UserRole.PURCHASER};
}
