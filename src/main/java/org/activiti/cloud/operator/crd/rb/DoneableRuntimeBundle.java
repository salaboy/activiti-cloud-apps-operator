package org.activiti.cloud.operator.crd.rb;

import io.fabric8.kubernetes.api.builder.Function;
import io.fabric8.kubernetes.client.CustomResourceDoneable;

public class DoneableRuntimeBundle extends CustomResourceDoneable<RuntimeBundle> {

    public DoneableRuntimeBundle(RuntimeBundle resource, Function<RuntimeBundle, RuntimeBundle> function) {
        super(resource, function);
    }
}
