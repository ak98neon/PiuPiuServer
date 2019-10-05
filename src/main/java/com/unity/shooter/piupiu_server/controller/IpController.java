package com.unity.shooter.piupiu_server.controller;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetAddress;
import java.net.UnknownHostException;

@RestController
@RequestMapping("/v1/getIpAddress")
public class IpController {

    @GetMapping
    public String getAddress() throws UnknownHostException {
        return InetAddress.getLocalHost().toString().replaceAll("\\S+/", "").trim();
    }

    @ExceptionHandler(value = UnknownHostException.class)
    public void unknownHost(UnknownHostException e) {
        System.out.println(e.getMessage());
    }
}
