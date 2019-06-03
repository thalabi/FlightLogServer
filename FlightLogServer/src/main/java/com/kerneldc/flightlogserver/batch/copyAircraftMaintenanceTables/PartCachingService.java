package com.kerneldc.flightlogserver.batch.copyAircraftMaintenanceTables;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@CacheConfig(cacheNames={"parts"})
public class PartCachingService {

	@Autowired
	@Qualifier("outputDataSource")
	private DataSource outputDataSource;

    @Cacheable
    public Map<String, Long> getNameToPartIdMap() {
		Map<String, Long> nameToPartIdMap = new HashMap<>();
		JdbcTemplate jdbcTemplate = new JdbcTemplate(outputDataSource);
		List<Map<String, Object>> rows = jdbcTemplate.queryForList("select id, name from part");
		for (Map<String, Object> row: rows) {
			if (row.get("id") instanceof BigDecimal) {
				nameToPartIdMap.put((String)row.get("name"), ((BigDecimal)row.get("id")).longValue());
			} else {
				nameToPartIdMap.put((String)row.get("name"), (Long)row.get("id"));
			}
		}
    	LOGGER.info("Cached nameToPartIdMap of size: {}", nameToPartIdMap.size());
		return nameToPartIdMap;
    }

    @CacheEvict()
    public void clearCache() {
    	// noop
    }
}
