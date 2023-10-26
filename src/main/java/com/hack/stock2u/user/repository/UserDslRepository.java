package com.hack.stock2u.user.repository;

import com.hack.stock2u.models.QAttach;
import com.hack.stock2u.models.QUser;
import com.hack.stock2u.user.dto.AvatarId;
import com.hack.stock2u.user.dto.UserId;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class UserDslRepository {
  private final JPAQueryFactory factory;

  public String getAvatarUrl(UserId id) {
    Long avatarId =
        factory.select(QUser.user.avatarId).from(QUser.user).where(QUser.user.id.eq(id.id()))
            .fetchOne();
    if (avatarId == null) {
      return null;
    }
    return getAvatarUrlQuery(avatarId);
  }

  public String getAvatarUrl(AvatarId id) {
    return getAvatarUrlQuery(id.id());
  }

  private String getAvatarUrlQuery(Long avatarId) {
    if (avatarId == null) {
      return null;
    }
    return factory.select(QAttach.attach.uploadPath)
        .from(QAttach.attach)
        .where(QAttach.attach.id.eq(avatarId))
        .fetchOne();
  }

}
