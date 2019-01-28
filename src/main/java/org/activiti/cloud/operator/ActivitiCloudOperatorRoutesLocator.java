package org.activiti.cloud.operator;

import io.fabric8.kubernetes.api.model.apiextensions.CustomResourceDefinition;
import io.fabric8.kubernetes.client.KubernetesClient;
import org.activiti.cloud.operator.crd.app.Application;
import org.activiti.cloud.operator.crd.app.ApplicationList;
import org.activiti.cloud.operator.crd.app.DoneableApplication;
import org.activiti.cloud.operator.crd.app.ModuleDescr;
import org.activiti.cloud.operator.crd.audit.Audit;
import org.activiti.cloud.operator.crd.audit.AuditList;
import org.activiti.cloud.operator.crd.audit.DoneableAudit;
import org.activiti.cloud.operator.crd.query.DoneableQuery;
import org.activiti.cloud.operator.crd.query.Query;
import org.activiti.cloud.operator.crd.query.QueryList;
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

    public static final String APPS_PATH = "apps";
    public static final String RUNTIME_BUNDLE_PATH = "rb";
    public static final String AUDIT_SERVICE_PATH = "audit";
    public static final String QUERY_SERVICE_PATH = "query";
    private KubernetesClient kubernetesClient;

    public ActivitiCloudOperatorRoutesLocator(KubernetesClient kubernetesClient) {
        this.kubernetesClient = kubernetesClient;
    }

    @Override
    public Flux<RouteDefinition> getRouteDefinitions() {
        try {


            CustomResourceDefinition appResourceDefinition = kubernetesClient.customResourceDefinitions().withName(ActivitiCloudCRDS.APPLICATIONS).get();
            /*
             * For Each Application:
             *  1) Search for Activiti Cloud Custom Resources
             *  2)   create a Route per type inside the application
             */
            List<Application> applications = kubernetesClient.customResources(appResourceDefinition, Application.class,
                    ApplicationList.class, DoneableApplication.class).list().getItems();

            List<RouteDefinition> allRouteDefinitions = new ArrayList<RouteDefinition>();

            for (Application app : applications) {

                List<RouteDefinition> appRouteDefinitions = new ArrayList<RouteDefinition>();
                List<RouteDefinition> runtimeBundleRoutes = getRuntimeBundleRoutesForApplication(app);
                appRouteDefinitions.addAll(runtimeBundleRoutes);

                List<RouteDefinition> auditServicesRoutes = getAuditRoutesForApplication(app);
                appRouteDefinitions.addAll(auditServicesRoutes);

                List<RouteDefinition> queryServicesRoutes = getQueryRoutesForApplication(app);
                appRouteDefinitions.addAll(queryServicesRoutes);

                if (isApplicationReady(app, appRouteDefinitions)) { // if all the routes for the app are available add to main routes
                    allRouteDefinitions.addAll(appRouteDefinitions);
                }//
            }


            return Flux.fromIterable(allRouteDefinitions);
        } catch (Exception e) {
            e.printStackTrace();

        }
        return null;
    }

    private boolean isApplicationReady(Application app, List<RouteDefinition> appRouteDefinitions) {
        List<ModuleDescr> modules = app.getSpec().getModules();
        int validated = 0;
        System.out.println("> App: " + app.getName() + " validation!");
        for (ModuleDescr md : modules) {
            for (RouteDefinition rd : appRouteDefinitions) {
                System.out.println("\t > Checking : " + rd.getId() + " === " + app.getName() + ":" + md.getName() + "-> " + rd.getId().equals(app.getName() + ":" + md.getName()));
                if (rd.getId().equals(app.getName() + ":" + md.getName())) {
                    validated++;
                }
            }
        }
        System.out.println("> Modules size: " + modules.size() + " and validated: " + validated);
        if (validated == modules.size()) {
            return true;
        }

        return false;

    }

    private List<RouteDefinition> getRuntimeBundleRoutesForApplication(Application app) {
        CustomResourceDefinition rbResourceDefinition = kubernetesClient.customResourceDefinitions().withName(ActivitiCloudCRDS.RUNTIME_BUNDLES).get();
        List<RouteDefinition> routeDefinitions = new ArrayList<RouteDefinition>();
        if (rbResourceDefinition != null) {
            List<RuntimeBundle> runtimeBundles = kubernetesClient.customResources(rbResourceDefinition, RuntimeBundle.class,
                    RuntimeBundleList.class, DoneableRuntimeBundle.class).list().getItems();
            for (RuntimeBundle rb : runtimeBundles) {
                RouteDefinition routeDefinition = new RouteDefinition();
                routeDefinition.setId(app.getName() + ":" + rb.getMetadata().getName());
                routeDefinition.setUri(URI.create("http://" + rb.getSpec().getServiceName()));
                PredicateDefinition predicateDefinition = new PredicateDefinition();
                predicateDefinition.setName("Path");
                predicateDefinition.addArg("pattern", "/" + APPS_PATH + "/" + app.getName() + "/" + app.getSpec().getVersion() + "/" + RUNTIME_BUNDLE_PATH + "/" + rb.getMetadata().getName() + "/**");
                routeDefinition.getPredicates().add(predicateDefinition);
                routeDefinitions.add(routeDefinition);
            }
        }
        return routeDefinitions;
    }

    private List<RouteDefinition> getAuditRoutesForApplication(Application app) {
        CustomResourceDefinition auditResourceDefinition = kubernetesClient.customResourceDefinitions().withName(ActivitiCloudCRDS.AUDIT_SERVICES).get();

        List<RouteDefinition> routeDefinitions = new ArrayList<RouteDefinition>();
        if (auditResourceDefinition != null) {
            List<Audit> auditServices = kubernetesClient.customResources(auditResourceDefinition, Audit.class,
                    AuditList.class, DoneableAudit.class).list().getItems();
            for (Audit as : auditServices) {
                RouteDefinition routeDefinition = new RouteDefinition();
                routeDefinition.setId(app.getName() + ":" + as.getMetadata().getName());
                routeDefinition.setUri(URI.create("http://" + as.getSpec().getServiceName()));
                PredicateDefinition predicateDefinition = new PredicateDefinition();
                predicateDefinition.setName("Path");
                predicateDefinition.addArg("pattern", "/" + APPS_PATH + "/" + app.getName() + "/" + app.getSpec().getVersion() + "/" + AUDIT_SERVICE_PATH + "/" + as.getMetadata().getName() + "/**");
                routeDefinition.getPredicates().add(predicateDefinition);
                routeDefinitions.add(routeDefinition);
            }
        }
        return routeDefinitions;
    }

    private List<RouteDefinition> getQueryRoutesForApplication(Application app) {
        CustomResourceDefinition queryResourceDefinition = kubernetesClient.customResourceDefinitions().withName(ActivitiCloudCRDS.QUERY_SERVICES).get();

        List<RouteDefinition> routeDefinitions = new ArrayList<RouteDefinition>();
        if (queryResourceDefinition != null) {
            List<Query> queryServices = kubernetesClient.customResources(queryResourceDefinition, Query.class,
                    QueryList.class, DoneableQuery.class).list().getItems();
            for (Query q : queryServices) {
                RouteDefinition routeDefinition = new RouteDefinition();
                routeDefinition.setId(app.getName() + ":" + q.getMetadata().getName());
                routeDefinition.setUri(URI.create("http://" + q.getSpec().getServiceName()));
                PredicateDefinition predicateDefinition = new PredicateDefinition();
                predicateDefinition.setName("Path");
                predicateDefinition.addArg("pattern", "/" + APPS_PATH + "/" + app.getName() + "/" + app.getSpec().getVersion() + "/" + QUERY_SERVICE_PATH + "/" + q.getMetadata().getName() + "/**");
                routeDefinition.getPredicates().add(predicateDefinition);
                routeDefinitions.add(routeDefinition);
            }
        }
        return routeDefinitions;
    }

}
