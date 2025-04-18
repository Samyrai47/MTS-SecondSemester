package app.springproject.service;

import app.springproject.entity.OutboxRecord;
import app.springproject.repository.OutboxRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OutboxScheduler {
  private final OutboxRepository outboxRepository;
  private final KafkaProducerService producerService;

  @Transactional
  @Scheduled(fixedDelay = 10000)
  public void processOutbox() throws JsonProcessingException {
    List<OutboxRecord> result = outboxRepository.findAll();
    for (OutboxRecord outboxRecord : result) {
      producerService.sendMessage(outboxRecord);
    }
    outboxRepository.deleteAll(result);
  }
}
