package Config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class IaConfig {

    @Value("${spring.ia.url}")
    private String url;

    @Value("${spring.ia.apy.key}")
    private String apiKey;

    private GeminiInfo info = new GeminiInfo();

    public String getIaUrl(){
        return info.getLink() + "?key=" + info.getKey();
    }
}
