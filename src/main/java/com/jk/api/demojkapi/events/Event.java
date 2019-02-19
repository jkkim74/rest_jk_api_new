package com.jk.api.demojkapi.events;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

// 롬복 애노테이션은 메타에노테으로 쓸수 없다. 별도의 애노테이션을 만들어서
// 넣을수 없다.
@Builder @AllArgsConstructor @NoArgsConstructor
@Getter @Setter @EqualsAndHashCode(of="id") // @EqualsAndHashCode(of="id,name")
@Entity
public class Event {

    @Id @GeneratedValue
    private Integer id;
    private String name;
    private String description;
    private LocalDateTime beginEnrollmentDateTime;
    private LocalDateTime closeEnrollmentDateTime;
    private LocalDateTime beginEventDateTime;
    private LocalDateTime endEventDateTime;
    private String location; // (optional) 이게 없으면 온라인 모임
    private int basePrice; // (optional)
    private int maxPrice; // (optional)
    private int limitOfEnrollment;
    private boolean free;
    private boolean offLine;
    @Enumerated(EnumType.STRING) // 기본값이 ordinary인데, String으로 해주는것이 좋다. 값의 순서가 바뀔수 있다.
    private EventStatus eventStatus = EventStatus.DRAFT;

}