package com.jk.api.demojkapi.events;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.assertj.core.api.Assertions.assertThat;
// 1. JunitParams를 사용하기 위한 정의
@RunWith(JUnitParamsRunner.class)
public class EventTest {

    @Test
    public void builder(){
        Event event = Event.builder()
                .name("Inflean Spring Rest API")
                .description("REST API development with Spring").build();
        assertThat(event).isNotNull();
    }

    @Test
    public void javaBean(){
        // Given
        String name = "Event";
        String description = "Spring";
        //When
        Event event = new Event();
        event.setName(name);
        event.setDescription(description);
        //Then
        assertThat(event.getName()).isEqualTo(name);
        assertThat(event.getDescription()).isEqualTo(description);

    }

    @Test
    //@Parameters({"0,0,true","100,0,false","0,100,true"})// 2. JunitParams를 사용하기 위한 정의
    // @Parameters(method="parametersForTestFree") // 파라미터를 정의한 메소드정의
    @Parameters
    public void testFree(int basePrice, int maxPrice, boolean isFree){
        // Given
        Event event = Event.builder()
                .basePrice(basePrice)
                .maxPrice(maxPrice)
                .build();

        // When
        event.update();

        // Then
        assertThat(event.isFree()).isEqualTo(isFree);
    }

    private Object[] parametersForTestFree(){
        return new Object[]{
                new Object[]{0,0,true},
                new Object[]{0,100,false},
                new Object[]{100,0,false},
                new Object[]{100,200,false}
        };
    }

    @Test
    @Parameters
    public void testOffLine(String strLocation, boolean isOffLine){
        //Given
        Event event = Event.builder()
                      .location(strLocation)
                      .build();
        //when
        event.update();

        //then
        assertThat(event.isOffLine()).isEqualTo(isOffLine);
    }
    // 위의 테스트를 위해서는 parametersFor + method가 되어야함..
    private Object[] parametersForTestOffLine(){
        return new Object[]{
            new Object[]{"",false},
            new Object[]{"강남 테헤란로 11",true}
        };
    }

}