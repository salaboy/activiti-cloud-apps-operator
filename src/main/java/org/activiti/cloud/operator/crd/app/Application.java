package org.activiti.cloud.operator.crd.app;

import io.fabric8.kubernetes.client.CustomResource;

public class Application extends CustomResource {

    private ApplicationSpec spec;

    public ApplicationSpec getSpec() {
        return spec;
    }

    public void setSpec(ApplicationSpec spec) {
        this.spec = spec;
    }

    public String getName() {
        return getMetadata().getName();
    }

    @Override
    public String toString() {
        return "Application{" +
                "name='" + getName() + '\'' +
                ", spec=" + spec +
                '}';
    }
}
