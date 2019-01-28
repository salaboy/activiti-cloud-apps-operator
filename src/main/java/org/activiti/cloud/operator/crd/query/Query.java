package org.activiti.cloud.operator.crd.query;

import io.fabric8.kubernetes.client.CustomResource;

public class Query extends CustomResource {
    private QuerySpec spec;

    public QuerySpec getSpec() {
        return spec;
    }

    public void setSpec(QuerySpec spec) {
        this.spec = spec;
    }

    @Override
    public String toString() {
        return "Query{" +
                "name='" + getMetadata().getName() + '\'' +
                ", spec=" + spec +
                '}';
    }
}
