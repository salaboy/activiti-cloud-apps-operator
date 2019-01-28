package org.activiti.cloud.operator.rest;

import io.fabric8.kubernetes.api.model.apiextensions.CustomResourceDefinition;
import io.fabric8.kubernetes.client.CustomResourceList;
import io.fabric8.kubernetes.client.KubernetesClient;
import org.activiti.cloud.operator.ActivitiCloudCRDS;
import org.activiti.cloud.operator.crd.app.Application;
import org.activiti.cloud.operator.crd.app.ApplicationList;
import org.activiti.cloud.operator.crd.app.DoneableApplication;
import org.activiti.cloud.operator.crd.audit.Audit;
import org.activiti.cloud.operator.crd.audit.AuditList;
import org.activiti.cloud.operator.crd.audit.DoneableAudit;
import org.activiti.cloud.operator.crd.rb.DoneableRuntimeBundle;
import org.activiti.cloud.operator.crd.rb.RuntimeBundle;
import org.activiti.cloud.operator.crd.rb.RuntimeBundleList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ActivitiCloudOperatorController {
    @Autowired
    private KubernetesClient kubernetesClient;


//    @Autowired
//    private IstioClient istioClient;
//

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

    @GetMapping("/apps/{appName}/")
    public CustomResourceList getApplicationsResources(@PathVariable String appName) {
        CustomResourceDefinition rbResourceDefinition = kubernetesClient.customResourceDefinitions().withName(ActivitiCloudCRDS.RUNTIME_BUNDLES).get();
        RuntimeBundleList rbList = kubernetesClient.customResources(rbResourceDefinition, RuntimeBundle.class,
                RuntimeBundleList.class, DoneableRuntimeBundle.class).withLabel("app", appName).list();

        CustomResourceDefinition auditResourceDefinition = kubernetesClient.customResourceDefinitions().withName(ActivitiCloudCRDS.AUDIT_SERVICES).get();
        AuditList auditList = kubernetesClient.customResources(auditResourceDefinition, Audit.class,
                AuditList.class, DoneableAudit.class).withLabel("app", appName).list();


        CustomResourceList customResourceList = new CustomResourceList();
        customResourceList.setApiVersion("stable.cloud.activiti.org/v1");
        customResourceList.setKind("ActivitiCloudResources");
        customResourceList.getItems().addAll(rbList.getItems());
        customResourceList.getItems().addAll(auditList.getItems());

        return customResourceList;
    }

    @GetMapping("/apps/{appName}/rbs")
    public RuntimeBundleList getApplicationsRuntimeBundles(@PathVariable String appName) {
        CustomResourceDefinition rbResourceDefinition = kubernetesClient.customResourceDefinitions().withName(ActivitiCloudCRDS.RUNTIME_BUNDLES).get();
        RuntimeBundleList list = kubernetesClient.customResources(rbResourceDefinition, RuntimeBundle.class,
                RuntimeBundleList.class, DoneableRuntimeBundle.class).withLabel("app", appName).list();

        return list;
    }

    @GetMapping("/apps/{appName}/audit")
    public AuditList getApplicationsAuditServices(@PathVariable String appName) {
        CustomResourceDefinition auditResourceDefinition = kubernetesClient.customResourceDefinitions().withName(ActivitiCloudCRDS.AUDIT_SERVICES).get();
        AuditList auditList = kubernetesClient.customResources(auditResourceDefinition, Audit.class,
                AuditList.class, DoneableAudit.class).withLabel("app", appName).list();
        return auditList;
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

}
