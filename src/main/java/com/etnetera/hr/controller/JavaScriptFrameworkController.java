package com.etnetera.hr.controller;

import com.etnetera.hr.data.JavaScriptFramework;
import com.etnetera.hr.repository.JavaScriptFrameworkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Simple REST controller for accessing application logic.
 *
 * @author Etnetera
 */
@RestController
public class JavaScriptFrameworkController extends EtnRestController {

    private final JavaScriptFrameworkRepository repository;

    @Autowired
    public JavaScriptFrameworkController(JavaScriptFrameworkRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/frameworks")
    public Iterable<JavaScriptFramework> frameworks() {
        return repository.findAll();
    }

    @PostMapping("/frameworks")
    public JavaScriptFramework save(@Valid @RequestBody JavaScriptFramework framework) {
        return repository.save(framework);
    }

    @PutMapping("/frameworks/{id}")
    public JavaScriptFramework update(@Valid @RequestBody JavaScriptFramework framework, @PathVariable(value = "id") Long frameworkId) {

        JavaScriptFramework frameworkToUpdate = repository.findById(frameworkId)
                .orElseThrow(() -> new UnsupportedOperationException("Framework with this id " + frameworkId + " does not exist"));

        frameworkToUpdate.setName(framework.getName());
        frameworkToUpdate.setVersion(framework.getVersion());
        frameworkToUpdate.setDeprecationDate(framework.getDeprecationDate());
        frameworkToUpdate.setHypeLevel(framework.getHypeLevel());

        return repository.save(frameworkToUpdate);
    }

    @DeleteMapping("/frameworks/{frameworkId}")
    public String delete(@PathVariable Long frameworkId) {
        repository.deleteById(frameworkId);
        return "ok";
    }

    @GetMapping("/frameworks/{name}")
    public JavaScriptFramework findByName(@PathVariable String name) {
        return repository.findByName(name);
    }

}
