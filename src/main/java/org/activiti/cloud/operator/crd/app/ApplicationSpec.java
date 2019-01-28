package org.activiti.cloud.operator.crd.app;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.fabric8.kubernetes.api.model.KubernetesResource;

import java.util.List;

@JsonDeserialize(
        using = JsonDeserializer.None.class
)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApplicationSpec implements KubernetesResource {

    private String version;
    private String selector;

    private List<ModuleDescr> modules;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getSelector() {
        return selector;
    }

    public void setSelector(String selector) {
        this.selector = selector;
    }

    public List<ModuleDescr> getModules() {
        return modules;
    }

    public void setModules(List<ModuleDescr> modules) {
        this.modules = modules;
    }

    @Override
    public String toString() {
        return "ApplicationSpec{" +
                "version='" + version + '\'' +
                ", selector='" + selector + '\'' +
                ", modules=" + modules +
                '}';
    }
}
