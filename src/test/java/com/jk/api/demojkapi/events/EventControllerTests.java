package com.jk.api.demojkapi.events;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jk.api.demojkapi.common.TestDescription;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class) // junit4 기준
// @WebMvcTest // web과 관련된 빈이 등록 slicing Test용 annotation , web을위한 slicing Test를 하기 위해 적용 이때는 mocking을 해줘야함.
// 이렇게 하는 경우, 실제 jpa의 repository를 사용하게됨..
// 실제 TEST를 하기 위해서는 SpringBootTest를 많이 사용한다. 별도로 Mocking을 하는게 없어진다..
@SpringBootTest // springBootTest를 쓰기위해서는 AutoConfigureMockMvc를 써줘야함.
@AutoConfigureMockMvc
public class EventControllerTests {

    // dipatcherServlet을 상대로 가짜요청을 만들어서 응답을 테스트 할수 있음.. web과 관련된 bean들만 만들므로 slicing Test라고 할수 있다. 요청을 만들수 있고 검증할수 있음
    // web서버를 뛰우지 않음.
    @Autowired
    MockMvc mocMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @TestDescription("정상적으로 이벤트를 생성한는 테스트")
    public void createEvent() throws Exception {
        EventDto event = EventDto.builder()
                      .name("Spring")
                      .description("REST API Document With Spring")
                      .beginEnrollmentDateTime(LocalDateTime.of(2018,12,25,16,57))
                      .closeEnrollmentDateTime(LocalDateTime.of(2018,12,29,16,57))
                      .beginEventDateTime(LocalDateTime.of(2018,12,10,16,57))
                      .endEventDateTime(LocalDateTime.of(2018,12,30,16,57))
                      .basePrice(100)
                      .maxPrice(200)
                      .limitOfEnrollment(100)
                      .location("강남역 D2 스타텁 팩토리").build();
         // Mockito.when(eventRepository.save(event)).thenReturn(event);
         // perform안에 보내는 것이 요청이다.
         // mocMvc에 post를 사용함.
         // HAL -> Hyper Text Appliction Langauge
         // perform 후에 결과를 확인할 수 있다. andExpect등을 사용하여..
         mocMvc.perform(post("/api/events/")
                              .contentType(MediaType.APPLICATION_JSON_UTF8)
                              .accept(MediaTypes.HAL_JSON)
                              .content(objectMapper.writeValueAsString(event)))
                 .andDo(print())
                 .andExpect(status().isCreated())
                 .andExpect(jsonPath("id").exists())
                 .andExpect(header().exists(HttpHeaders.LOCATION))
                 .andExpect(header().string(HttpHeaders.CONTENT_TYPE,MediaTypes.HAL_JSON_UTF8_VALUE))
                 .andExpect(jsonPath("id").value(Matchers.not(100)))
                 .andExpect(jsonPath("free").value(false))
                 .andExpect(jsonPath("offLine").value(true))
                 .andExpect(jsonPath("eventStatus").value(Matchers.not(EventStatus.DRAFT)))
                 .andExpect(jsonPath("_links.self").exists())
                 .andExpect(jsonPath("_links.query-events").exists())
                 .andExpect(jsonPath("_links.update-event").exists());
    }

    @Test
    @TestDescription("입력받을 수 없는 값을 입력했을때 에러가 발생하는 테스트")
    public void createEvent_bad_request() throws Exception {
        Event event = Event.builder()
                .id(100)
                .name("Spring")
                .description("REST API Document With Spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2018,12,27,16,57))
                .closeEnrollmentDateTime(LocalDateTime.of(2018,12,28,16,57))
                .beginEventDateTime(LocalDateTime.of(2018,12,27,16,57))
                .endEventDateTime(LocalDateTime.of(2018,12,28,16,57))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남역 D2 스타텁 팩토리")
                .free(true)
                .eventStatus(EventStatus.PUBLISHED).build();

        mocMvc.perform(post("/api/events/")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(event)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @TestDescription("입력값이 비워 있는 경우에 수행하는 테스트")
    public void create_Bad_request_Empty_Input() throws Exception {
        EventDto eventDto = EventDto.builder().build();

        mocMvc.perform(post("/api/events")
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content(objectMapper.writeValueAsString(eventDto)))
                    .andExpect(status().isBadRequest());
    }

    @Test
    @TestDescription("잘못된 값을 입력했을때 수행하는 테스트")
    public void create_Bad_request_Empty_Wrong_Input() throws Exception {
        EventDto eventDto = EventDto.builder()
                .name("Spring")
                .description("REST API Document With Spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2018,12,27,16,57))
                .closeEnrollmentDateTime(LocalDateTime.of(2018,12,22,16,57))
                .beginEventDateTime(LocalDateTime.of(2018,12,27,16,57))
                .endEventDateTime(LocalDateTime.of(2018,12,23,16,57))
                .basePrice(10000)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남역 D2 스타텁 팩토리").build();

        mocMvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(eventDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].objectName").exists())
                .andExpect(jsonPath("$[0].field").exists())
                .andExpect(jsonPath("$[0].defaultMessage").exists())
                .andExpect(jsonPath("$[0].code").exists())
                .andExpect(jsonPath("$[0].rejectedValue").exists());
    }

}
