package org.codeforamerica.formflowstarter.app.data;

import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class SubmissionService {

  @Autowired
  SubmissionRepository repository;

  public void save(Submission submission) {
    repository.save(submission);
  }

  public Optional<Submission> findById(Long id) {
    return repository.findById(id);
  }


}
