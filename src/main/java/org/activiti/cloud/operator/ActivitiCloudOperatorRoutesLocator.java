package org.activiti.cloud.operator;

import io.fabric8.kubernetes.api.model.apiextensions.CustomResourceDefinition;
import io.fabric8.kubernetes.api.model.apiextensions.CustomResourceDefinitionList;
import io.fabric8.kubernetes.api.model.apiextensions.DoneableCustomResourceDefinition;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.NonNamespaceOperation;
import io.fabric8.kubernetes.client.dsl.Resource;
import org.activiti.cloud.operator.crd.app.ApplicationList;
import org.activiti.cloud.operator.crd.app.Application;
import org.activiti.cloud.operator.crd.app.DoneableApplication;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import reactor.core.publisher.Flux;

import java.net.URI;

public class ActivitiCloudOperatorRoutesLocator implements RouteDefinitionLocator {

    private KubernetesClient kubernetesClient;

    public ActivitiCloudOperatorRoutesLocator(KubernetesClient kubernetesClient) {
        this.kubernetesClient = kubernetesClient;
    }

    @Override
    public Flux<RouteDefinition> getRouteDefinitions() {
        try {
            NonNamespaceOperation<CustomResourceDefinition,
                    CustomResourceDefinitionList,
                    DoneableCustomResourceDefinition,
                    Resource<CustomResourceDefinition,
                            DoneableCustomResourceDefinition>> crds = kubernetesClient.customResourceDefinitions();

            CustomResourceDefinition appResourceDefinition = crds.withName(ActivitiCloudCRDS.APPLICATIONS).get();
            CustomResourceDefinition rbResourceDefinition = crds.withName(ActivitiCloudCRDS.RUNTIME_BUNDLES).get();

            return Flux.fromIterable(kubernetesClient.customResources(appResourceDefinition, Application.class,
                    ApplicationList.class, DoneableApplication.class).list().getItems())
                    .map(app ->
                    {
                        RouteDefinition routeDefinition = new RouteDefinition();
                        routeDefinition.setId(app.getName());
                        routeDefinition.setUri(URI.create("http://" + app.getName()));
                        PredicateDefinition predicateDefinition = new PredicateDefinition();
                        predicateDefinition.setName("Path");
                        predicateDefinition.addArg("pattern", "/" + app.getName() + "/" + app.getSpec().getVersion() + "/**");
                        routeDefinition.getPredicates().add(predicateDefinition);
                        return routeDefinition;
                    });
        } catch (Exception e) {
            e.printStackTrace();

        }
        return null;
    }
}
