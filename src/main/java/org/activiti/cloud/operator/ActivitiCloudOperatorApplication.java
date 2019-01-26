package org.activiti.cloud.operator;

import io.fabric8.kubernetes.api.model.LabelSelectorBuilder;
import io.fabric8.kubernetes.api.model.apiextensions.CustomResourceDefinition;
import io.fabric8.kubernetes.client.KubernetesClient;
import org.activiti.cloud.operator.crd.app.Application;
import org.activiti.cloud.operator.crd.app.ApplicationList;
import org.activiti.cloud.operator.crd.app.DoneableApplication;
import org.activiti.cloud.operator.crd.rb.DoneableRuntimeBundle;
import org.activiti.cloud.operator.crd.rb.RuntimeBundle;
import org.activiti.cloud.operator.crd.rb.RuntimeBundleList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class ActivitiCloudOperatorApplication {

    @Autowired
    private KubernetesClient kubernetesClient;


//    @Autowired
//    private IstioClient istioClient;

    public static void main(String[] args) {
        SpringApplication.run(ActivitiCloudOperatorApplication.class,
                args);
    }


    @GetMapping("/apps/def")
    public CustomResourceDefinition getApplicationDefinition() {
        return kubernetesClient.customResourceDefinitions().withName(ActivitiCloudCRDS.APPLICATIONS).get();
    }

    @GetMapping("/apps")
    public ApplicationList getApplications() {
        CustomResourceDefinition appResourceDefinition = kubernetesClient.customResourceDefinitions().withName(ActivitiCloudCRDS.APPLICATIONS).get();
        ApplicationList list = kubernetesClient.customResources(appResourceDefinition, Application.class,
                ApplicationList.class, DoneableApplication.class).list();
        return list;
    }

    @GetMapping("/apps/{appName}/rbs")
    public RuntimeBundleList getApplicationsRuntimeBundles(@PathVariable String appName) {
        CustomResourceDefinition rbResourceDefinition = kubernetesClient.customResourceDefinitions().withName(ActivitiCloudCRDS.RUNTIME_BUNDLES).get();
        RuntimeBundleList list = kubernetesClient.customResources(rbResourceDefinition, RuntimeBundle.class,
                RuntimeBundleList.class, DoneableRuntimeBundle.class).withLabel("app",appName).list();

        return list;
    }

    @GetMapping("/rb/def")
    public CustomResourceDefinition getRuntimeBundleDefinition() {
        return kubernetesClient.customResourceDefinitions().withName(ActivitiCloudCRDS.RUNTIME_BUNDLES).get();
    }

    @GetMapping("/rb")
    public RuntimeBundleList getRuntimeBundles() {
        CustomResourceDefinition rbResourceDefinition = kubernetesClient.customResourceDefinitions().withName(ActivitiCloudCRDS.RUNTIME_BUNDLES).get();
        RuntimeBundleList list = kubernetesClient.customResources(rbResourceDefinition, RuntimeBundle.class,
                RuntimeBundleList.class, DoneableRuntimeBundle.class).list();
        return list;
    }


//    @GetMapping("/createRoutes")
//    public void createRoutes(){
//
//    }


//    @Override
//    public void run(String... args) throws Exception {
//        List<String> services = discoveryClient.getServices();
//        services.forEach((s) -> System.out.println("Service: " + s));
//
////        NonNamespaceOperation<CustomResourceDefinition,
////                CustomResourceDefinitionList,
////                DoneableCustomResourceDefinition,
////                Resource<CustomResourceDefinition,
////                        DoneableCustomResourceDefinition>> crds = kubernetesClient.customResourceDefinitions();
////        System.out.println("CML RUNNER ------");
////
////        CustomResourceDefinition apps = crds.withName("activiti-cloud-apps.stable.cloud.activiti.org").get();
////        System.out.println("> apps: " + apps.getSpec().toString() + " - " + apps.getKind());
////
////        MixedOperation<Application,
////                ApplicationList,
////                DoneableApplication,
////                Resource<Application,
////                        DoneableApplication>> appClient = kubernetesClient.customResources(apps, Application.class,
////                ApplicationList.class, DoneableApplication.class);
////
////        System.out.println(">> Resource Def version: " + apps.getMetadata().getResourceVersion());
////
////        ApplicationList list = appClient.list();
////        Application activitiCloudApplicationResource = list.getItems().get(0);
////
////        System.out.println(">> Resource version: " + activitiCloudApplicationResource.getMetadata().getResourceVersion());
////        appClient.withResourceVersion(activitiCloudApplicationResource.getMetadata().getResourceVersion()).watch(new Watcher<Application>() {
////
////            @Override
////            public void eventReceived(Action action, Application activitiCloudApplicationResource) {
////                System.out.printf(">> With RESOURCES TYPE ---------------------------------------------------------");
////                System.out.printf(">> Action: " + action);
////                System.out.printf(">> Event: " + activitiCloudApplicationResource);
////                System.out.printf(">> END With RESOURCES TYPE ---------------------------------------------------------");
////            }
////
////            @Override
////            public void onClose(KubernetesClientException e) {
////
////            }
////        });
////
////
////        appClient.watch(new Watcher<Application>() {
////
////            @Override
////            public void eventReceived(Action action, Application activitiCloudApplicationResource) {
////                System.out.printf(">> Without RESOURCES TYPE ---------------------------------------------------------");
////                System.out.printf(">> Action: " + action);
////                System.out.printf(">> Event: " + activitiCloudApplicationResource);
////                System.out.printf(">> END Without RESOURCES TYPE ---------------------------------------------------------");
////            }
////
////            @Override
////            public void onClose(KubernetesClientException e) {
////
////            }
////        });
//}
}
