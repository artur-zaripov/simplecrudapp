package io.github.artur_zaripov.simplecrudapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.artur_zaripov.simplecrudapp.exception.RecordNotFoundException;
import io.github.artur_zaripov.simplecrudapp.model.Record;
import io.github.artur_zaripov.simplecrudapp.service.RecordService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(RecordController.class)
public class RecordControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private RecordService service;

    private JacksonTester<Record> json;

    private static final String TEST_1 = "test1";
    private static final String TEST_2 = "test2";

    private static final LocalDateTime DATE_1 = LocalDateTime.of(2018, 5, 19, 18, 0, 0);
    private static final LocalDateTime DATE_2 = LocalDateTime.of(2018, 5, 19, 15, 0, 0);

    private static final Record record1 = new Record(1, TEST_1, DATE_1, "test_description1");
    private static final Record record2 = new Record(2, TEST_1, DATE_2, "test_description2");
    private static final Record record3 = new Record(3, TEST_2, DATE_2, "test_description3");
    private static final Record recordInvalid = new Record(null, TEST_2, DATE_2, null);
    
    @Before
    public void setUp() {
        ObjectMapper objectMapper = new ObjectMapper();
        JacksonTester.initFields(this, objectMapper);

        List<Record> lastTwoRecords = new ArrayList<Record>(){{ add(record1); add(record2); }};

        given(service.getRecordById(3)).willReturn(Optional.of(record3));
        given(service.getRecordsByName(TEST_1)).willReturn(lastTwoRecords);
        given(service.getRecordsByDate(DATE_2.minusDays(1), DATE_2.plusDays(1))).willReturn(lastTwoRecords);
        doThrow(new RecordNotFoundException()).when(service).deleteRecord(100);
    }

    @Test
    public void givenRecords_whenGetRecordById_thenReturnRecord() throws Exception {
        mvc.perform(get("/records/id/3")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(TEST_2)));
    }

    @Test
    public void givenRecords_whenGetRecordByName_thenReturnJsonArray() throws Exception {
        mvc.perform(get("/records/name/" + TEST_1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is(TEST_1)));
    }

    @Test
    public void givenRecords_whenGetRecordByDateBetween_thenReturnJsonArray() throws Exception {
        mvc.perform(get("/records/from/" + DATE_2.minusDays(1).toString() + "/to/" + DATE_2.plusDays(1).toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[1].id", is(2)));
    }

    @Test
    public void givenRecords_whenSaveRecordWithMissingId_thenReturnBadRequest() throws Exception {
        mvc.perform(post("/records/")
                .content(json.write(recordInvalid).getJson())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

    }

    @Test
    public void givenRecords_whenUpdateRecordWithMissingId_thenReturnBadRequest() throws Exception {
        mvc.perform(put("/records/")
                .content(json.write(recordInvalid).getJson())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

    }

    @Test
    public void givenRecords_whenDeletRecordByNotExistingId_thenReturnNotFound() throws Exception {
        mvc.perform(delete("/records/delete/100")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

}
