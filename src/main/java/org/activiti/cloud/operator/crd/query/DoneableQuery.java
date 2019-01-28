package org.activiti.cloud.operator.crd.query;

import io.fabric8.kubernetes.api.builder.Function;
import io.fabric8.kubernetes.client.CustomResourceDoneable;

public class DoneableQuery extends CustomResourceDoneable<Query> {

    public DoneableQuery(Query resource, Function<Query, Query> function) {
        super(resource, function);
    }
}
