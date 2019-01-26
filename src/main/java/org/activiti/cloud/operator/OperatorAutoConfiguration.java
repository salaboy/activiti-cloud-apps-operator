package org.activiti.cloud.operator;

import io.fabric8.kubernetes.client.KubernetesClient;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.cloud.gateway.config.GatewayAutoConfiguration;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AutoConfigureBefore(GatewayAutoConfiguration.class)
public class OperatorAutoConfiguration {

    @Bean
    public RouteDefinitionLocator activitiCloudApplicationsRouteDefinitionLocator(
            KubernetesClient kubernetesClient) {
        return new ActivitiCloudOperatorRoutesLocator(kubernetesClient);
    }
}
