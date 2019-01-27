package org.activiti.cloud.operator;

import io.fabric8.kubernetes.api.model.apiextensions.CustomResourceDefinition;
import io.fabric8.kubernetes.api.model.apiextensions.CustomResourceDefinitionList;
import io.fabric8.kubernetes.api.model.apiextensions.DoneableCustomResourceDefinition;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.NonNamespaceOperation;
import io.fabric8.kubernetes.client.dsl.Resource;
import org.activiti.cloud.operator.crd.app.Application;
import org.activiti.cloud.operator.crd.app.ApplicationList;
import org.activiti.cloud.operator.crd.app.DoneableApplication;
import org.activiti.cloud.operator.crd.rb.DoneableRuntimeBundle;
import org.activiti.cloud.operator.crd.rb.RuntimeBundle;
import org.activiti.cloud.operator.crd.rb.RuntimeBundleList;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import reactor.core.publisher.Flux;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

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
            /*
             * For Each Application:
             *  1) Search for Activiti Cloud Custom Resources
             *  2)   create a Route per type inside the application
             */
            List<Application> applications = kubernetesClient.customResources(appResourceDefinition, Application.class,
                    ApplicationList.class, DoneableApplication.class).list().getItems();
            CustomResourceDefinition rbResourceDefinition = crds.withName(ActivitiCloudCRDS.RUNTIME_BUNDLES).get();
            List<RouteDefinition> routeDefinitions = new ArrayList<RouteDefinition>();
            for (Application app : applications) {

                List<RuntimeBundle> runtimeBundles = kubernetesClient.customResources(rbResourceDefinition, RuntimeBundle.class,
                        RuntimeBundleList.class, DoneableRuntimeBundle.class).list().getItems();

                for (RuntimeBundle rb : runtimeBundles) {
                    RouteDefinition routeDefinition = new RouteDefinition();
                    routeDefinition.setId(app.getName());
                    routeDefinition.setUri(URI.create("http://" + rb.getSpec().getServiceName()));
                    PredicateDefinition predicateDefinition = new PredicateDefinition();
                    predicateDefinition.setName("Path");
                    predicateDefinition.addArg("pattern", "/" + app.getName() + "/" + app.getSpec().getVersion() + "/rb/" + rb.getMetadata().getName() + "/**");
                    routeDefinition.getPredicates().add(predicateDefinition);
                    routeDefinitions.add(routeDefinition);
                }
            }

            return Flux.fromIterable(routeDefinitions);
        } catch (Exception e) {
            e.printStackTrace();

        }
        return null;
    }
}
