package org.ow2.choreos.servicedeployer.recipe;

import org.ow2.choreos.servicedeployer.datamodel.ServiceType;

public class RecipeBuilderFactory {

	public static RecipeBuilder getRecipeBuilderInstance(ServiceType serviceType) {

		switch (serviceType) {
		case WAR:
			return new RecipeBuilderImpl();
		case JAR:
			return new RecipeBuilderImpl();
		case PETALS:
			return new RecipeBuilderImpl();
		default:
			throw new IllegalArgumentException("Service type " + serviceType
					+ " not supported");
		}
	}
}
