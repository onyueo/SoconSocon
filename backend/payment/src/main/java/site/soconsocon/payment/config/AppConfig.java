package site.soconsocon.payment.config;

import com.siot.IamportRestClient.IamportClient;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Value("${iamport.key}")
    private String restApiKey;

    @Value("${iamport.secret}")
    private String restApiSecret;

    @Bean
    public IamportClient iamportClient() {
        return new IamportClient(restApiKey, restApiSecret);
    }

}
