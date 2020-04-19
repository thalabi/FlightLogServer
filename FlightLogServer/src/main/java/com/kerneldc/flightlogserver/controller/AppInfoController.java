package com.kerneldc.flightlogserver.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("appInfoController")
public class AppInfoController {

	@Value("${build.version}_${build.timestamp}")
	private String buildTimestamp;

    @GetMapping("/getBuildTimestamp")
	public String getBuildTimestamp() {
		return buildTimestamp;
	}
}
