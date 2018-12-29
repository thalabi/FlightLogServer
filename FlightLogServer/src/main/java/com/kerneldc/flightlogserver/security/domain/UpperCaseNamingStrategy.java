package com.kerneldc.flightlogserver.security.domain;

import java.util.Arrays;
import java.util.List;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;
import org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy;

public class UpperCaseNamingStrategy extends SpringPhysicalNamingStrategy {

	List<String> uppercaseEntities = Arrays.asList("User", "Group");
	
	@Override
	public Identifier toPhysicalTableName(Identifier name, JdbcEnvironment context) {
		if (uppercaseEntities.contains(name.getText())) {
			String uppercaseTableName = super.toPhysicalTableName(name, context).getText().toUpperCase();
			return context.getIdentifierHelper().toIdentifier(uppercaseTableName, true);
		} else {
			return super.toPhysicalTableName(name, context);
		}
	}


}
