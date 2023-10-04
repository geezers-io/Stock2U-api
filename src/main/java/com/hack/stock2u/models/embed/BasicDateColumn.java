package com.hack.stock2u.models.embed;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.Getter;

@Getter
@Embeddable
public class BasicDateColumn {

  @Column(name = "created_at")
  private Date createdAt;

  @Column(name = "removed_at")
  private Date removedAt;
}
