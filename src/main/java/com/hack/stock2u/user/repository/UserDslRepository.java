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

  public String getProfileUrl(UserId id) {
    Long avatarId =
        factory.select(QUser.user.avatarId).from(QUser.user).where(QUser.user.id.eq(id.id()))
            .fetchOne();
    if (avatarId == null) {
      return null;
    }
    return getProfileUrlQuery(avatarId);
  }

  public String getProfileUrl(AvatarId id) {
    return getProfileUrlQuery(id.id());
  }

  private String getProfileUrlQuery(Long avatarId) {
    if (avatarId == null) {
      return null;
    }
    return factory.select(QAttach.attach.uploadPath)
        .from(QAttach.attach)
        .where(QAttach.attach.id.eq(avatarId))
        .fetchOne();
  }

}
