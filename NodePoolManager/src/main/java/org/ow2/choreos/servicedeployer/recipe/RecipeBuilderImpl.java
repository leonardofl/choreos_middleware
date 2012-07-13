package org.ow2.choreos.servicedeployer.recipe;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.ow2.choreos.servicedeployer.datamodel.Service;
import org.ow2.choreos.servicedeployer.datamodel.ServiceType;


public class RecipeBuilderImpl implements RecipeBuilder {
	
	private Logger logger = Logger.getLogger(RecipeBuilderImpl.class);
	
	private static final String TEMPLATE_DIR = "src/main/resources/service-deploy-recipe-template";
	private static final File DEST_DIR = new File("src/main/resources/recipes");
	private static final String PETALS_RECIPE = "sa";
	
	private String recipeFile;
	
	public Recipe createRecipe(Service service) {
		
		Recipe recipe = new Recipe();
		String absolutePath;

		try {
			
			String recipeName = getRecipeName(service);
			recipe.setName(recipeName);
			recipe.setCookbookName("service" + service.getId());
			this.recipeFile = recipeName + ".rb";
			
			absolutePath = copyTemplate(service);
			recipe.setCookbookFolder(absolutePath);

			changeMetadataRb(service);
			changeAttributesDefaultRb(service);
			changeServerRecipe(service);

			return recipe;
		} catch (IOException e) {
			logger.error("Could not create recipe", e);
		}

		return null;

	}

	private String getRecipeName(Service service) {
		String extension = service.getExtension();
		String recipeName = "";
		ServiceType type = service.getType();
		if (type == ServiceType.JAR || type == ServiceType.WAR) {
			recipeName = extension;
		} else if (type == ServiceType.PETALS) {
			recipeName = PETALS_RECIPE;
		}
		return recipeName;
	}
	
	// methods have "package visibility" to test purposes

	void changeMetadataRb(Service service) throws IOException {
		changeFileContents(service, "chef/service" + service.getId()
				+ "/metadata.rb");
	}

	private void changeServerRecipe(Service service) throws IOException {
		changeFileContents(service, "chef/service" + service.getId()
				+ "/recipes/"+this.recipeFile);
	}

	void changeAttributesDefaultRb(Service service) throws IOException {
		changeFileContents(service, "chef/service" + service.getId()
				+ "/attributes/default.rb");
	}

	private void changeFileContents(Service service, String fileLocation)
			throws IOException {

		File file = new File("src/main/resources/" + fileLocation);
		String fileData = FileUtils.readFileToString(file);

		fileData = fileData.replace("$NAME", service.getId());
		fileData = fileData.replace("$URL", service.getCodeLocationURI());
		fileData = fileData.replace("$WARFILE", service.getFile());

		FileUtils.deleteQuietly(file);
		FileUtils.writeStringToFile(file, fileData);
	}

	String copyTemplate(Service service) throws IOException {
		
		File srcFolder = new File(TEMPLATE_DIR);
		
		String destPath = DEST_DIR.getAbsolutePath() + "/service" + service.getId();
		File destFolder = new File(destPath);
		
		FileUtils.copyDirectory(srcFolder, destFolder);

		return destFolder.getAbsolutePath();
	}
}
