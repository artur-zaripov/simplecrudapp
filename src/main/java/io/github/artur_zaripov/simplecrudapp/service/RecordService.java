package io.github.artur_zaripov.simplecrudapp.service;

import io.github.artur_zaripov.simplecrudapp.exception.RecordNotFoundException;
import io.github.artur_zaripov.simplecrudapp.model.Record;
import io.github.artur_zaripov.simplecrudapp.repository.RecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class RecordService {

    @Autowired
    private RecordRepository recordRepository;

    public Optional<Record> getRecordById(Integer id) {
        return recordRepository.findById(id);
    }

    public List<Record> getRecordsByName(String name) {
        return recordRepository.findByName(name);
    }

    public List<Record> getRecordsByDate(LocalDateTime from , LocalDateTime to) {
        return recordRepository.findByCreationDateBetween(from, to);
    }

    public void saveRecord(Record record) {
        record.setId(null);
        recordRepository.save(record);
    }

    public void updateRecord(Record record) {
        if (record.getId() != null && recordRepository.findById(record.getId()).isPresent())
            recordRepository.save(record);
        else
            throw new RecordNotFoundException(record.getId());
    }

    public void deleteRecord(Integer recordId) {
        if (recordId != null && recordRepository.findById(recordId).isPresent())
            recordRepository.deleteById(recordId);
        else
            throw new RecordNotFoundException(recordId);
    }

}
