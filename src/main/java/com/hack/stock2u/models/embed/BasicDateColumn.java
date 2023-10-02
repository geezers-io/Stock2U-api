package com.hack.stock2u.models.embed;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Getter;

@Getter
@Embeddable
public class BasicDateColumn {

  @Column(name = "created_at")
  @Temporal(TemporalType.TIMESTAMP)
  private Date createdAt;

  @Column(name = "removed_at")
  @Temporal(TemporalType.TIMESTAMP)
  private Date removedAt;
}
