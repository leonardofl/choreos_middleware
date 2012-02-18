package eu.choreos.servicedeployer.recipe;

import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;

import eu.choreos.servicedeployer.NodePoolManagerHandler;
import eu.choreos.storagefactory.utils.CommandLineInterfaceHelper;

public class RecipeDeployerTest {

	private static Recipe recipe;
	private RecipeDeployer deployer = new RecipeDeployer(
			new NodePoolManagerHandler());

	@BeforeClass
	public static void setUpBeforeClass() {
		recipe = new Recipe();
		recipe.setName("servlet");
		recipe.setFolder("./src/test/resources/chef/");
		System.out
				.println("deleting previous instances of the recipe. Errors are expected");
		(new CommandLineInterfaceHelper())
				.runLocalCommand("knife cookbook remove servlet -y");
	}

	@Test
	public void testDeployRecipe() {

		deployer.deployRecipe(recipe);

		String commandReturn = "";

		commandReturn = (new CommandLineInterfaceHelper())
				.runLocalCommand("knife cookbook list");

		assertTrue("Did not find the uploaded recipe.",
				commandReturn.contains("servlet"));
	}
}
