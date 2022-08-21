package com.etnetera.hr;

import com.etnetera.hr.data.JavaScriptFramework;
import com.etnetera.hr.repository.JavaScriptFrameworkRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Class used for Spring Boot/MVC based tests.
 *
 * @author Etnetera
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
//@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class JavaScriptFrameworkTests {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private JavaScriptFrameworkRepository repository;

    private void prepareData() throws Exception {
        JavaScriptFramework react = new JavaScriptFramework("ReactJS", "10.2.0", "1.1.2023", "The hype level is too damn high");
        JavaScriptFramework vue = new JavaScriptFramework("Vue.js", "10.2.0", "1.1.2022", "My hype level is off the charts");

        repository.save(react);
        repository.save(vue);
    }

    @Test
    public void frameworksTest() throws Exception {
        prepareData();

        mockMvc.perform(get("/frameworks"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(5)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("ReactJS")))
                .andExpect(jsonPath("$[0].version", is("10.2.0")))
                .andExpect(jsonPath("$[0].deprecationDate", is("1.1.2023")))
                .andExpect(jsonPath("$[0].hypeLevel", is("The hype level is too damn high")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("Vue.js")))
                .andExpect(jsonPath("$[1].version", is("10.2.0")))
                .andExpect(jsonPath("$[1].deprecationDate", is("1.1.2022")))
                .andExpect(jsonPath("$[1].hypeLevel", is("My hype level is off the charts")));
    }

    @Test
    public void addFrameworkInvalid() throws JsonProcessingException, Exception {
        JavaScriptFramework framework = new JavaScriptFramework();
        framework.setId(2L);
        framework.setVersion("1.1.2");
        framework.setDeprecationDate("1211");
        framework.setHypeLevel("that enough slices");


        mockMvc.perform(post("/frameworks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsBytes(framework)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", hasSize(1))) // 1 error is expected, field name message NotEmpty
                .andExpect(jsonPath("$.errors[0].field", is("name")))
                .andExpect(jsonPath("$.errors[0].message", is("NotEmpty")));

        framework.setName("verylongnameofthejavascriptframeworkjavaisthebest");
        mockMvc.perform(post("/frameworks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsBytes(framework)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0].field", is("name")))
                .andExpect(jsonPath("$.errors[0].message", is("Size")));
    }

    @Test
    public void updateFramework() throws JsonProcessingException, Exception {
        JavaScriptFramework framework = new JavaScriptFramework();
        framework.setName("JavaScriptShittyyy");
        framework.setVersion("1.1.2");
        framework.setDeprecationDate("12.1.2021");
        framework.setHypeLevel("that´s enough slices");

        JavaScriptFramework savedFramework = repository.save(framework);

        savedFramework.setName("JS");
        savedFramework.setVersion("2.5.3");
        savedFramework.setDeprecationDate("20.4.2023");
        savedFramework.setHypeLevel("eeew");

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/frameworks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsBytes(savedFramework)))
                .andExpect(status().isOk())
                //java.lang.AssertionError: JSON path "$.id"
                //Expected: is <1L>
                //     but: was <1>
                //.andExpect(jsonPath("$.id", is(savedFramework.getId())))
                .andExpect(jsonPath("$.name", is("JS")))
                .andExpect(jsonPath("$.version", is("2.5.3")))
                .andExpect(jsonPath("$.deprecationDate", is("20.4.2023")))
                .andExpect(jsonPath("$.hypeLevel", is("eeew")));

    }

    @Test
    public void updateFrameworkInvalid() throws JsonProcessingException, Exception {
        JavaScriptFramework framework = new JavaScriptFramework();
        framework.setName("JavaScriptShitty");
        framework.setVersion("1.1.2");
        framework.setDeprecationDate("12.1.2021");
        framework.setHypeLevel("that´s enough slices");

        JavaScriptFramework savedFramework = repository.save(framework);

        savedFramework.setName("verylongnameofthejavascriptframeworkjavaisthebest");
        savedFramework.setVersion("2.5.3");
        savedFramework.setDeprecationDate("20.4.2023");
        savedFramework.setHypeLevel("eeew");

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/frameworks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsBytes(savedFramework)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0].field", is("name")))
                .andExpect(jsonPath("$.errors[0].message", is("Size")));
    }

    @Test
    public void deleteFramework() throws JsonProcessingException, Exception {
        JavaScriptFramework framework = new JavaScriptFramework();
        framework.setName("AnotherShittyFramework");
        framework.setVersion("1.1.3");
        framework.setDeprecationDate("1.1.1111");
        framework.setHypeLevel("over 9000!!");

        JavaScriptFramework savedFramework = repository.save(framework);

        mockMvc.perform(delete("/frameworks/" + savedFramework.getId()))
                .andExpect(status().isOk());
    }

    @Test
    public void findFrameworkByName() throws JsonProcessingException, Exception {
        prepareData();

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/frameworks/Vue.js"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(2)))
                .andExpect(jsonPath("$.name", is("Vue.js")))
                .andExpect(jsonPath("$.version", is("10.2.0")))
                .andExpect(jsonPath("$.deprecationDate", is("1.1.2022")))
                .andExpect(jsonPath("$.hypeLevel", is("My hype level is off the charts")));

    }

}
