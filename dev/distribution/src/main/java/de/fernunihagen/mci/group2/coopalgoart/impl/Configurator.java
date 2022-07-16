package de.fernunihagen.mci.group2.coopalgoart.impl;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * @author bwinzen
 *
 */
public class Configurator extends Application {
	private static boolean loadLast = false;
	public static void main(String[] args) {
		if(args != null && args.length > 1 && args[1].equals("latest")) {
			loadLast = true;
		}
		Configurator.launch(new String[] {});
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Configurator - MCI Fachpraktikum Gruppe2");
		FXMLLoader loader = new FXMLLoader(Thread.currentThread().getContextClassLoader().getResource("configurator.fxml"));
		Parent root = (Parent)loader.load();
		ConfiguratorController controller = (ConfiguratorController)loader.getController();
		
		// This sets the size of the Scene to be 400px wide, 200px high
		Scene scene = new Scene(root, 800, 1000);
		scene.getStylesheets().add("style.css");
		primaryStage.setScene(scene);
		primaryStage.show();
		primaryStage.setOnCloseRequest(e->controller.quit(null));
		if(loadLast) {
			controller.openMostRecent();
		}
	}

	
	
}
