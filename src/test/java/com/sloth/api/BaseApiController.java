package com.sloth.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class BaseApiController {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    public ModelMapper modelMapper;

    @Autowired
    public ObjectMapper objectMapper;

}
