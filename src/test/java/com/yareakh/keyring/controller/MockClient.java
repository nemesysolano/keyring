package com.yareakh.keyring.controller;

import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

public interface MockClient {

    static MockHttpServletRequestBuilder withHeaders(MockHttpServletRequestBuilder requestBuilder,  Map<String,String> headers) {
        if(headers != null) {
            headers.forEach(requestBuilder::header);
        }
        return requestBuilder;
    }

    static <T> String post(MockMvc mvc, String url, T object, ResultMatcher resultMatcher, Map<String,String> headers) throws Exception {

        MvcResult result = mvc.perform(
                        withHeaders(MockMvcRequestBuilders.post(url), headers)
                                .content(mapToJson(object))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(resultMatcher)
                .andReturn();

        return result.getResponse().getContentAsString();
    }

    static <T,R> R post(MockMvc mvc, String url, T object, Class<R> clazz, ResultMatcher resultMatcher, Map<String,String> headers) throws Exception{

        MvcResult result = mvc.perform(
                        withHeaders(MockMvcRequestBuilders.post(url), headers)
                                .content(mapToJson(object))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(resultMatcher)
                .andReturn();

        String content = result.getResponse().getContentAsString();
        if(content.length() > 0) {
            return mapFromJson(result.getResponse().getContentAsString(), clazz);
        }

        return null;
    }

    static <T,R> R post(MockMvc mvc, String url, T object, Class<R> clazz, ResultMatcher resultMatcher) throws Exception {
        return post(mvc, url, object, clazz, resultMatcher, Map.of());
    }

    static <R> R postUrlEncoded(MockMvc mvc, String url, String content, Class<R> clazz, ResultMatcher resultMatcher, Map<String,String> headers) throws Exception{

        MvcResult result = mvc.perform(
                        withHeaders(MockMvcRequestBuilders.post(url), headers)
                                .content(content)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(resultMatcher)
                .andReturn();

        String response = result.getResponse().getContentAsString();
        if(response.length() > 0) {
            return mapFromJson(response, clazz);
        }
        return null;
    }

    static <T,R> R put(MockMvc mvc, String url, T object, Class<R> clazz, ResultMatcher resultMatcher, Map<String,String> headers) throws Exception {
        MvcResult result = mvc.perform(
                        withHeaders(MockMvcRequestBuilders.put(url), headers)
                                .content(mapToJson(object))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(resultMatcher)
                .andReturn();

        return mapFromJson(result.getResponse().getContentAsString(), clazz);
    }


    static <T,R> R put(MockMvc mvc, String url, T object, Class<R> clazz, ResultMatcher resultMatcher) throws Exception {
        return put(mvc, url, object, clazz, resultMatcher, Map.of());
    }

    static void delete(MockMvc mvc, String url, ResultMatcher expected, Map<String,String> headers) throws Exception {
        mvc.perform(withHeaders(MockMvcRequestBuilders.delete(url), headers))
                .andExpect(expected)
                .andReturn();
    }

    static String getText(MockMvc mvc, String url, Map<String,String> headers, ResultMatcher resultMatcher) throws Exception {
        MvcResult result = mvc.perform(
                        withHeaders(MockMvcRequestBuilders.get(url), headers)
                                .accept(MediaType.TEXT_PLAIN)
                )
                .andExpect(resultMatcher)
                .andReturn();

        return result.getResponse().getContentAsString();
    }

    static <R> R get(MockMvc mvc, String url, Class<R> clazz, ResultMatcher resultMatcher,  Map<String,String> headers) throws Exception {
        MvcResult result = mvc.perform(
                        withHeaders(MockMvcRequestBuilders.get(url), headers)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(resultMatcher)
                .andReturn();

        return mapFromJson(result.getResponse().getContentAsString(), clazz);
    }

    static String mapToJson(Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }

    static <T> T mapFromJson(String json, Class<T> clazz)
            throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(json, clazz);
    }
}