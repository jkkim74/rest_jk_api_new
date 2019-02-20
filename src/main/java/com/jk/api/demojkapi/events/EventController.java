package com.jk.api.demojkapi.events;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.net.URI;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Controller
//@RequestMapping(value="/api/events",produces = MediaTypes.HAL_JSON_UTF8_VALUE)
@RequestMapping(value="/api/events",produces=MediaTypes.HAL_JSON_UTF8_VALUE)
public class EventController {

    private final  EventRepository eventRepository;
    private final ModelMapper modelMapper;
    private final EventValidator eventValidator;

    // 생성자가 하나인 경우, Autowired를 생량할 수 있다. spring4.3부터..
    public EventController(EventRepository eventRepository, ModelMapper modelMapper, EventValidator eventValidator){
        this.eventRepository = eventRepository;
        this.modelMapper = modelMapper;
        this.eventValidator = eventValidator;
    }
    // @Valid를 넣으면 인스턴스시 검증을 수행할 수 있음.
    @PostMapping
    public ResponseEntity createEvent(@RequestBody @Valid EventDto eventDto, Errors errors){//@RequestBody Event event
        if(errors.hasErrors()){
            return ResponseEntity.badRequest().build();
        }
        eventValidator.validate(eventDto,errors);

        if(errors.hasErrors()){
            return ResponseEntity.badRequest().build();
        }
        Event event = modelMapper.map(eventDto, Event.class);
        Event newEvent = this.eventRepository.save(event);
        URI createUri = linkTo(EventController.class).slash(newEvent.getId()).toUri();
        event.setId(10);
        return ResponseEntity.created(createUri).body(event);
    }
}
