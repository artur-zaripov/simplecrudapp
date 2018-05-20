package io.github.artur_zaripov.simplecrudapp.service;

import io.github.artur_zaripov.simplecrudapp.exception.RecordNotFoundException;
import io.github.artur_zaripov.simplecrudapp.model.Record;
import io.github.artur_zaripov.simplecrudapp.repository.RecordRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
public class RecordServiceTest {

    @TestConfiguration
    static class RecordServiceTestContextConfiguration {

        @Bean
        public RecordService recordService() {
            return new RecordService();
        }
    }

    @Autowired
    private RecordService recordService;

    @MockBean
    private RecordRepository recordRepository;

    private static final String TEST_1 = "test1";
    private static final String TEST_2 = "test2";

    private static final LocalDateTime DATE_1 = LocalDateTime.of(2018, 5, 19, 18, 0, 0);
    private static final LocalDateTime DATE_2 = LocalDateTime.of(2018, 5, 19, 15, 0, 0);

    private static final Record record1 = new Record(1, TEST_1, DATE_1, "test_description1");
    private static final Record record2 = new Record(2, TEST_1, DATE_2, "test_description2");
    private static final Record record3 = new Record(3, TEST_2, DATE_2, "test_description3");
    private static final Record recordInvalid = new Record(null, TEST_1, null, TEST_2);

    @Before
    public void setUp() {
        Mockito.when(recordRepository.findById(record2.getId()))
                .thenReturn(Optional.of(record2));
        Mockito.when(recordRepository.findByName(record3.getName()))
                .thenReturn(new ArrayList<Record>() {{ add(record3); }});
        Mockito.when(recordRepository.findByCreationDateBetween(DATE_1.minusDays(1), DATE_1.plusDays(1)))
                .thenReturn(new ArrayList<Record>() {{ add(record1); }});
        Mockito.when(recordRepository.findById(100))
                .thenReturn(Optional.empty());
        Mockito.when(recordRepository.save(recordInvalid))
                .thenThrow(ConstraintViolationException.class);
    }

    @Test
    public void whenValidId_thenRecordShouldBeFound() {
        Optional<Record> found = recordService.getRecordById(2);

        assertThat(found.isPresent()).isEqualTo(true);
        assertThat(found.orElseGet(null).getDescription()).isEqualTo("test_description2");
    }

    @Test
    public void whenValidName_thenRecordShouldBeFound() {
        List<Record> found = recordService.getRecordsByName(TEST_2);

        assertThat(found.size()).isEqualTo(1);
        assertThat(found.get(0).getId()).isEqualTo(3);
    }

    @Test
    public void whenValidDateInterval_thenRecordShouldBeFound() {
        List<Record> found = recordService.getRecordsByDate(DATE_1.minusDays(1), DATE_1.plusDays(1));

        assertThat(found.size()).isEqualTo(1);
        assertThat(found.get(0).getId()).isEqualTo(1);
    }

    @Test(expected = ConstraintViolationException.class)
    public void whenSavingWithMissingRequiredField_thenThrowException() {
        recordService.saveRecord(recordInvalid);
    }

    @Test(expected = RecordNotFoundException.class)
    public void whenUpdateNotExisting_thenThrowException() {
        recordService.updateRecord(new Record(100, TEST_1, DATE_1, TEST_2));
    }

    @Test(expected = RecordNotFoundException.class)
    public void whenDeleteNotExisting_thenThrowException() {
        recordService.deleteRecord(100);
    }
}
