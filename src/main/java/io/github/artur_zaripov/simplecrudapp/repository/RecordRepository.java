package io.github.artur_zaripov.simplecrudapp.repository;

import io.github.artur_zaripov.simplecrudapp.model.Record;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RecordRepository extends CrudRepository<Record, Integer> {

    List<Record> findByName(String name);

    List<Record> findByCreationDateBetween(LocalDateTime from, LocalDateTime to);

}
