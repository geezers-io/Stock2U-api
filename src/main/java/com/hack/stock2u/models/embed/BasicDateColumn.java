package com.hack.stock2u.models.embed;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

@Getter
@Setter
@NoArgsConstructor
@Embeddable
public class BasicDateColumn {

  @CreatedDate
  @Column(name = "created_at")
  private Date createdAt;

  @Column(name = "removed_at")
  private Date removedAt;

}
