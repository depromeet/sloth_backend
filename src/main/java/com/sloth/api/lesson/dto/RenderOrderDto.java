package com.sloth.api.lesson.dto;

import lombok.*;

import java.util.List;

@Getter @Setter
public class RenderOrderDto {

    private List<Response> lessonList;

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter @Setter
    public static class Response {
        private String key;
        private String inputType;
        private String title;
        private String placeHolder;
    }

}
