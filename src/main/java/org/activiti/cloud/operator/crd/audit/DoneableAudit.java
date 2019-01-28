package org.activiti.cloud.operator.crd.audit;

import io.fabric8.kubernetes.api.builder.Function;
import io.fabric8.kubernetes.client.CustomResourceDoneable;

public class DoneableAudit extends CustomResourceDoneable<Audit> {

    public DoneableAudit(Audit resource, Function<Audit, Audit> function) {
        super(resource, function);
    }
}
