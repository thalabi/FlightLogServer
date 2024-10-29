package com.kerneldc.flightlogserver.controller;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kerneldc.flightlogserver.batch.bean.TriggerStatus;
import com.kerneldc.flightlogserver.batch.util.ReplicationUtil;
import com.kerneldc.flightlogserver.domain.EntityEnum;
import com.kerneldc.flightlogserver.exception.ApplicationException;

@RestController
@RequestMapping("/protected/replicationController")
public class ReplicationController {
	
	@Autowired
	@Qualifier("outputDataSource")
	public DataSource outputDataSource;

    @Value("${legacy.schema.name}")
    private String legacySchemaName;

    @Value("${output.schema.name}")
    private String outputSchemaName;

    @GetMapping("/getTableReplicationStatus/{entityName}")
	public ResponseEntity<Integer> getTableReplicationStatus(@PathVariable("entityName") String entityName) throws ApplicationException, SQLException {
    	if (EntityEnum.getEnumByEntityName(entityName) == null) {
    		throw new ApplicationException(String.format("Invalid entityName: %s", entityName));
    	}
    	System.out.println(EntityEnum.getEnumByEntityName(entityName).getTableName());
    	Integer replicationStatus = Integer.valueOf(ReplicationUtil.tableReplicationStatus(outputDataSource, legacySchemaName, outputSchemaName, EntityEnum.getEnumByEntityName(entityName).getTableName()));
    	return ResponseEntity.ok(replicationStatus);
    }

    @PutMapping("/setTableReplicationStatus/{entityName}")
	public ResponseEntity<String> setTableReplicationStatus(@PathVariable("entityName") String entityName, @RequestBody TriggerStatus body) throws ApplicationException, SQLException {
    	if (EntityEnum.getEnumByEntityName(entityName) == null) {
    		throw new ApplicationException(String.format("Invalid entityName: %s", entityName));
    	}
    	String tableName = EntityEnum.getEnumByEntityName(entityName).getTableName();
    	System.out.println("body.isStatus(): "+body.getStatus());
		if (body.getStatus()) {
			ReplicationUtil.enableTriggers(outputDataSource, tableName);
		} else {
			ReplicationUtil.disableTriggers(outputDataSource, tableName);
		}
		return ResponseEntity.ok().build();
    }

}
