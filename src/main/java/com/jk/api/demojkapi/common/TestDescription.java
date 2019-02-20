package com.jk.api.demojkapi.common;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)// 타겟은 메소드 이름에 붙이는것.
@Retention(RetentionPolicy.SOURCE) // 얼마나 오래 가져갈것이냐 애노테이션을 붙인 코드를 얼마나 오래 가져갈것인가..
public @interface TestDescription {
    String value();
}
