package com.etnetera.hr.data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

/**
 * Simple data entity describing basic properties of every JavaScript framework.
 *
 * @author Etnetera
 */
@Entity
public class JavaScriptFramework {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotEmpty
    @Size(max = 30)
    @Column(nullable = false, length = 30)
    private String name;

    @Column(nullable = true, length = 30)
    private String version;

    @Column(nullable = true)
    private String deprecationDate;

    @Column(nullable = true)
    private String hypeLevel;

    public JavaScriptFramework() {
    }

    public JavaScriptFramework(String name, String version, String deprecationDate, String hypeLevel) {
        this.name = name;
        this.version = version;
        this.deprecationDate = deprecationDate;
        this.hypeLevel = hypeLevel;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDeprecationDate() {
        return deprecationDate;
    }

    public void setDeprecationDate(String deprecationDate) {
        this.deprecationDate = deprecationDate;
    }

    public String getHypeLevel() {
        return hypeLevel;
    }

    public void setHypeLevel(String hypeLevel) {
        this.hypeLevel = hypeLevel;
    }

    @Override
    public String toString() {
        return "JavaScriptFramework [id=" + id + ", name=" + name + ", version=" + version + ", deprecation date=" + deprecationDate + ", hype level=" + hypeLevel + "]";
    }

}
