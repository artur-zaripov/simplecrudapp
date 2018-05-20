package io.github.artur_zaripov.simplecrudapp.controller;

import io.github.artur_zaripov.simplecrudapp.exception.RecordNotFoundException;
import io.github.artur_zaripov.simplecrudapp.model.ApiResponse;
import io.github.artur_zaripov.simplecrudapp.model.Record;
import io.github.artur_zaripov.simplecrudapp.service.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(value = "records")
public class RecordController {

    private static final String DATE_PATTERN = "yyyy-MM-dd'T'HH:mm";

    private final RecordService recordService;

    @Autowired
    public RecordController(RecordService recordService) {
        this.recordService = recordService;
    }

    @GetMapping(value = "/id/{recordId}")
    public Record getRecord(@PathVariable("recordId") Integer recordId) {
        return recordService.getRecordById(recordId)
                .orElseThrow(() -> new RecordNotFoundException(recordId));
    }

    @GetMapping(value = "/name/{name}")
    public List<Record> getRecords(@PathVariable("name") String name) {
        return recordService.getRecordsByName(name);
    }

    @GetMapping(value = "/from/{from}/to/{to}")
    public List<Record> getRecords(
            @PathVariable("from") @DateTimeFormat(pattern = DATE_PATTERN) LocalDateTime from,
            @PathVariable("to") @DateTimeFormat(pattern = DATE_PATTERN) LocalDateTime to
    ) {
        return recordService.getRecordsByDate(from, to);
    }

    @PostMapping(value = "/")
    public ResponseEntity<ApiResponse> saveRecord(@RequestBody Record record) {
        recordService.saveRecord(record);
        return new ResponseEntity<>(new ApiResponse("Record successfully saved"), HttpStatus.CREATED);
    }

    @PutMapping(value = "/")
    public ResponseEntity<ApiResponse> updateRecord(@RequestBody Record record) {
        recordService.updateRecord(record);
        return new ResponseEntity<>(new ApiResponse("Record successfully updated"), HttpStatus.OK);
    }

    @DeleteMapping(value = "/id/{recordId}")
    public ResponseEntity<ApiResponse> deleteRecord(@PathVariable("recordId") Integer recordId) {
        recordService.deleteRecord(recordId);
        return new ResponseEntity<>(new ApiResponse("Record successfully deleted"), HttpStatus.OK);
    }

}
