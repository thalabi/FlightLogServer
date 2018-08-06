package com.kerneldc.flightlogserver.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("versionController")
public class VersionController {

	@Value("${build.timestamp}")
	private String buildTimestamp;

    @GetMapping("/getVersion")
	public String getVersion() {
		return buildTimestamp;
	}
}
