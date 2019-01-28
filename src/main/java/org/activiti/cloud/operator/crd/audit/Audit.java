package org.activiti.cloud.operator.crd.audit;

import io.fabric8.kubernetes.client.CustomResource;

public class Audit extends CustomResource {
    private AuditSpec spec;

    public AuditSpec getSpec() {
        return spec;
    }

    public void setSpec(AuditSpec spec) {
        this.spec = spec;
    }

    @Override
    public String toString() {
        return "Audit{" +
                "name='" + getMetadata().getName() + '\'' +
                ", spec=" + spec +
                '}';
    }
}
