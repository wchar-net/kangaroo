package com.latmn.kangaroo.framework.convention.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.latmn.kangaroo.framework.convention.exception.ConventionException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;

import java.io.IOException;

public class ViewUtil {

    public static <T> void writeXml(XmlMapper xmlMapper, T result, HttpServletResponse response) throws IOException {
        if (null == xmlMapper) {
            throw new ConventionException("ViewUtil writeXml XmlMapper,Not Null!");
        }
        if (null == response) {
            throw new ConventionException("ViewUtil writeXml  HttpServletResponse,Not Null!");
        }
        response.setContentType(MediaType.APPLICATION_XML_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(xmlMapper.writeValueAsString(result));
    }

    public static <T> void writeJson(ObjectMapper objectMapper, T result, HttpServletResponse response) throws IOException {
        if (null == objectMapper) {
            throw new ConventionException("ViewUtil writeJson ObjectMapper,Not Null!");
        }
        if (null == response) {
            throw new ConventionException("ViewUtil writeJson  HttpServletResponse,Not Null!");
        }
        String jsonStr = objectMapper.writeValueAsString(result);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(jsonStr);
    }
}
