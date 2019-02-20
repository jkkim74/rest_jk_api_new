package com.jk.api.demojkapi.events;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.time.LocalDateTime;

@Component
public class EventValidator {
    public void validate(EventDto eventDto, Errors errors){
        if(eventDto.getBasePrice() > eventDto.getMaxPrice() && eventDto.getMaxPrice() > 0){
            errors.rejectValue("basePrice","wrongValue", "basePrice is Wrong");
        }

        LocalDateTime endEventDateTime = eventDto.getEndEventDateTime();
        if( endEventDateTime.isBefore(eventDto.getBeginEventDateTime()) ||
            endEventDateTime.isBefore(eventDto.getCloseEnrollmentDateTime()) ||
            endEventDateTime.isBefore(eventDto.getBeginEnrollmentDateTime())){
            errors.rejectValue("endEventDateTime","wrongValue","endEventDateTime is Wrong");
        }

        // TODO beginEventDateTime
        LocalDateTime beginEventTime = eventDto.getBeginEventDateTime();
        if(beginEventTime.isBefore(eventDto.getEndEventDateTime()) ||
           beginEventTime.isBefore(eventDto.getBeginEnrollmentDateTime()) ||
           beginEventTime.isBefore(eventDto.getCloseEnrollmentDateTime())){
           errors.rejectValue("beginEventDateTime","wrongValue","beginEventDateTime is Wrong");

        }
        // TODO CloseEnrollmentDateTime
        LocalDateTime closeEnrollmentDateTime = eventDto.getCloseEnrollmentDateTime();
        if(closeEnrollmentDateTime.isBefore(eventDto.getBeginEnrollmentDateTime()) ||
           closeEnrollmentDateTime.isBefore(eventDto.getBeginEventDateTime()) ||
           closeEnrollmentDateTime.isBefore(eventDto.getEndEventDateTime())){
           errors.rejectValue("closeEnrollmentDateTime","wrongValue","closeEnrollmentDateTime is Wrong");
        }

    }
}
