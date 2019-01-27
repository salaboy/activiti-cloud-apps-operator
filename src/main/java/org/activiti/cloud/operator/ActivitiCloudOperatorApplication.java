package org.activiti.cloud.operator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ActivitiCloudOperatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(ActivitiCloudOperatorApplication.class,
                args);
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
