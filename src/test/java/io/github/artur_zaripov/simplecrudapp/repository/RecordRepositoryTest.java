package io.github.artur_zaripov.simplecrudapp.repository;

import io.github.artur_zaripov.simplecrudapp.model.Record;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class RecordRepositoryTest {

    @Autowired
    private RecordRepository recordRepository;


    @Test
    public void whenFindById_thenReturnRecord() {
        Optional<Record> found = recordRepository.findById(2);

        assertThat(found.isPresent()).isEqualTo(true);
        assertThat(found.orElse(null).getName()).isEqualTo("test1");
    }

    @Test
    public void whenFindByName_thenReturnRecord() {
        List<Record> found = recordRepository.findByName("test2");

        assertThat(found.size()).isEqualTo(1);
        assertThat(found.get(0).getId()).isEqualTo(3);
    }

    @Test
    public void whenFindByCreationDateBetween_thenReturnRecord() {
        List<Record> found = recordRepository.findByCreationDateBetween(
                LocalDateTime.of(2018, 5, 18, 0, 0),
                LocalDateTime.of(2018, 5, 20, 0, 0)
        );

        assertThat(found.size()).isEqualTo(1);
        assertThat(found.get(0).getId()).isEqualTo(1);
    }

    @Test
    public void whenSave_thenCanBeFound() {
        recordRepository.save(
                new Record(null, "test3", LocalDateTime.now(), "test_description")
        );

        List<Record> found = recordRepository.findByName("test3");

        assertThat(found.size()).isEqualTo(1);
        assertThat(found.get(0).getId()).isEqualTo(4);
    }

    @Test
    public void whenUpdate_thenCanBeFoundUpdated() {
        recordRepository.save(
                new Record(1, "test4", LocalDateTime.now(), "test_description")
        );

        List<Record> found = recordRepository.findByName("test4");

        assertThat(found.size()).isEqualTo(1);
        assertThat(found.get(0).getId()).isEqualTo(1);
    }

    @Test
    public void whenDelete_thenCanNotBeFound() {
        recordRepository.deleteById(3);

        Optional<Record> found = recordRepository.findById(3);

        assertThat(found.isPresent()).isEqualTo(false);
    }

}
