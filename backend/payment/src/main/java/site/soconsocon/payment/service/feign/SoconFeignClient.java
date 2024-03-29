package site.soconsocon.payment.service.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import site.soconsocon.payment.service.feign.request.AddMySoconRequest;

@FeignClient(name = "socon-service", url = "http://localhost:8010/api/v1/issues")
public interface SoconFeignClient {

    //소콘북 저장
    @PostMapping("/socon")
    ResponseEntity<Object> saveMySocon(@RequestBody AddMySoconRequest addMySoconRequest);
}
