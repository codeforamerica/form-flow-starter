package org.codeforamerica.formflowstarter.app.data;

import static javax.persistence.TemporalType.TIMESTAMP;

import com.vladmihalcea.hibernate.type.json.JsonType;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.stereotype.Component;


@TypeDef(
    name = "json", typeClass = JsonType.class
)
@Entity
@Table(name = "submissions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
@Builder
public class Submission {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "flow")
  private String flow;

  @Type(type = "json")
  @Column(name = "input_data", columnDefinition = "jsonb")
  private Map<String, Object> inputData = new HashMap<>();

  @CreationTimestamp
  @Temporal(TIMESTAMP)
  @Column(name = "created_at")
  private Date createdAt;

  @UpdateTimestamp
  @Temporal(TIMESTAMP)
  @Column(name = "updated_at")
  private Date updatedAt;

  @Temporal(TIMESTAMP)
  @Column(name = "submitted_at")
  private Date submittedAt;

  public static Map<String, Object> getSubflowEntryByUuid(String subflowName, String uuid, Submission submission) {
    if (submission.getInputData().containsKey(subflowName)) {
      ArrayList<Map<String, Object>> subflow = (ArrayList<Map<String, Object>>) submission.getInputData().get(subflowName);
      return subflow.stream().filter(entry -> entry.get("uuid").equals(uuid)).toList().get(0);
    }
    return null;
  }
}
