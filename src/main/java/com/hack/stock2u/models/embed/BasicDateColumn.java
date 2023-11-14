package com.hack.stock2u.models.embed;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class BasicDateColumn implements Serializable {

  @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", locale = "Asia/Seoul")
  @CreatedDate
  @Column(name = "created_at")
  private Date createdAt;

  @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", locale = "Asia/Seoul")
  @Column(name = "removed_at")
  private Date removedAt;

}
