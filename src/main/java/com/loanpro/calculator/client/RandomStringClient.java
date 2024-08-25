package com.loanpro.calculator.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "${client.string.name}", url = "${client.string.url}")
public interface RandomStringClient {

    @GetMapping("/strings")
    String getRandomString(@RequestParam Integer num,
                           @RequestParam Integer len,
                           @RequestParam String digits,
                           @RequestParam(name = "upperalpha") String upperAlpha,
                           @RequestParam(name = "loweralpha") String lowerAlpha,
                           @RequestParam String unique,
                           @RequestParam String rnd,
                           @RequestParam String format);
}
