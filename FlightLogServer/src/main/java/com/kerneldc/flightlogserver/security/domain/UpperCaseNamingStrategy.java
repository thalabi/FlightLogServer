package com.kerneldc.flightlogserver.security.domain;

import java.lang.invoke.MethodHandles;
import java.util.List;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy;

public class UpperCaseNamingStrategy extends SpringPhysicalNamingStrategy {

	private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	private List<String> entitiesThatNeedUpperCaseTables = List.of("user", "group");
	
	@Override
	public Identifier toPhysicalTableName(Identifier name, JdbcEnvironment context) {
		String entityName = name.getText();
		Identifier tableNameIdentifier = super.toPhysicalTableName(name, context);
		for (String entityThatNeedUpperCaseTable : entitiesThatNeedUpperCaseTables) {
			if (entityThatNeedUpperCaseTable.equalsIgnoreCase(entityName)) {
				tableNameIdentifier = context.getIdentifierHelper().toIdentifier(entityName.toUpperCase(), true); // a value of true quotes the tableName
			}
		}
		LOGGER.debug("Entity name [{}] will be mapped to table name [{}]", entityName, tableNameIdentifier.getText());
		return tableNameIdentifier;
	}


}
