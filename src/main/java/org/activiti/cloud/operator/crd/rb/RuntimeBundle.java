package org.activiti.cloud.operator.crd.rb;

import io.fabric8.kubernetes.client.CustomResource;

public class RuntimeBundle extends CustomResource {
    private RuntimeBundleSpec spec;

    public RuntimeBundleSpec getSpec() {
        return spec;
    }

    public void setSpec(RuntimeBundleSpec spec) {
        this.spec = spec;
    }

    @Override
    public String toString() {
        return "RuntimeBundle{" +
                "name='" + getMetadata().getName() + '\'' +
                ", spec=" + spec +
                '}';
    }
}
