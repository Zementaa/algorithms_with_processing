package de.fernunihagen.mci.group2.coopalgoart.shopf;

import java.util.*;

import de.fernunihagen.mci.group2.coopalgoart.api.Generator;
import de.fernunihagen.mci.group2.coopalgoart.api.ServiceRegistry;
import de.fernunihagen.mci.group2.coopalgoart.api.services.ServiceDescription;
import de.fernunihagen.mci.group2.coopalgoart.api.services.SimpleServiceFactory;
import de.fernunihagen.mci.group2.coopalgoart.api.services.desc.ParameterDescription;
import de.fernunihagen.mci.group2.coopalgoart.shopf.test.cordevolution.CordsEvolutionGenerator;
import de.fernunihagen.mci.group2.coopalgoart.shopf.test.lsystem.LsystemGenerator;
import de.fernunihagen.mci.group2.coopalgoart.shopf.test.swarm.SwarmGenerator;
import de.fernunihagen.mci.group2.coopalgoart.shopf.test.swarm.SwarmParameter;


public class Init {
	
	private static final String CONFIGURATION_BASEPATH = "shopf/";

	public Map<String, Object> exampleConfigurations() {
		Map<String, Object> map = new LinkedHashMap<>();
		Map<String, Object> submenus = new LinkedHashMap<>();
		Map<String, Object> items = new LinkedHashMap<>();

		items = new LinkedHashMap<>();
		items.put("EvoDefault", path("evoDefault.json"));
        items.put("EvoDefaultEnd", path("evoDefaultEnd.json"));
        items.put("EvoEcke", path("evoEcke.json"));
        items.put("EvoWenig", path("evoWenig.json"));
        map.put("SHopf", submenus);
		submenus.put("Evolution", items);
		
	    items = new LinkedHashMap<>();
	    items.put("TreeBeispiel", path("treeBeispiel.json"));
	    items.put("TreeBeispielRichtung", path("treeRichtung.json"));
	    items.put("TreeKopf", path("treeKopf.json"));
	    items.put("TreeRichtung", path("tree10Richtung.json"));
	    items.put("TreeKombi", path("treeKombi.json"));
	    map.put("SHopf", submenus);
        submenus.put("L-System", items);
	    
	    items = new LinkedHashMap<>();
        items.put("SchwarmKurven", path("schwarmKurven.json"));
        items.put("SchwarmQuad", path("schwarmQuad.json"));
        items.put("SchwarmKunst", path("schwarmKunst.json"));
        map.put("SHopf", submenus);
        submenus.put("Schwarm", items);
        
        items = new LinkedHashMap<>();
        items.put("Dschungel", path("dschungel.json"));
        items.put("Feuerwerk", path("feuerwerk.json"));
        items.put("Unterwasserwelt", path("unterwasserwelt.json"));
        map.put("SHopf", submenus);
        submenus.put("Kooperation", items);
	    
	    
		return map;
	}

	private static String path(String config) {
		return CONFIGURATION_BASEPATH + config;
	}

	
	public void registerAlgorithms() {
		System.out.println("Load algorithms from "+ Init.class.getPackage().getName());
		registerLSystem();
		registerSwarm();
		registerEvolution();
	}
	
	
	
	   private void registerSwarm() {
	        List<ParameterDescription> parameterDescription = new LinkedList<>();
	        parameterDescription.add(ParameterDescription.createInteger("countBoids", "Anzahl Boids", 1 , 1000, 150));
	        //parameterDescription.add(ParameterDescription.createInteger("startX", "Startposition X", 0 , 1000, 150));
	        //parameterDescription.add(ParameterDescription.createInteger("startY", "Startposition Y", 0 , 1000, 150));
	        parameterDescription.add(ParameterDescription.createInteger("obstacleDefense", "Abwehrkraft des Hindernisses", 15 , 50, 20));
	        parameterDescription.add(ParameterDescription.createInteger("separateView", "Abstand Boids", 5 , 100, 25));
	        parameterDescription.add(ParameterDescription.createEnumDescription("boidsShape", "Gestalt", "Kurve", new String[] {"Punkte", "Kurve", "Dreieck"}));
	        parameterDescription.add(ParameterDescription.createCheckBox("obstacle", "Zeige Hindernis", false));
	        parameterDescription.add(ParameterDescription.createCheckBox("quadTree", "Zeige QuadTree", false));
	        ServiceDescription<Generator> description = new ServiceDescription<>(Generator.class, "shopf_swarm", "SHopf::Swarm", "", parameterDescription);
	        SimpleServiceFactory<Generator> serviceFactory = new SimpleServiceFactory<>(description, p->{
	            int countBoids = ((Number)p.get("countBoids")).intValue();
	            //int startX = ((Number)p.get("startX")).intValue();
	            //int startY = ((Number)p.get("startY")).intValue();
	            int obstacleDefense = ((Number)p.get("obstacleDefense")).intValue();
	            int separateView = ((Number)p.get("separateView")).intValue();
	            String form = (String) p.get("boidsShape");
	            boolean showQuad = ((boolean)p.get("quadTree"));
	            boolean showObstacle = ((boolean)p.get("obstacle"));
	            
	            return SwarmGenerator.builder() //
	                    .numBoids(countBoids) //
	                    //.startPositionX(startX) //
	                    //.startPositionY(startY) //
	                    .obstacleDefense(obstacleDefense)//
	                    .separateView(separateView) //
	                    .shape(form) //
	                    .showQuad(showQuad)//
	                    .showObstacle(showObstacle)//
	                    .build();
	            });
	        ServiceRegistry.register(serviceFactory);
	    }
	   
       private void registerLSystem() {
           List<ParameterDescription> parameterDescription = new LinkedList<>();
           //parameterDescription.add(ParameterDescription.createEnumDescription("treeShape", "Baumart", "geneigt, kleine Zweige", new String[] {"gerade, kleine Zweige", "geneigt, grÃ¶ÃŸere Zweige", "gerade, wenig Zweige", "gerade, symmetrisch", "geneigt, kleine Zweige"}));
           parameterDescription.add(ParameterDescription.createRadioButton("treeShape", "Baumart", new String[] {"gerade, kleine Zweige", "geneigt, grÃ¶ÃŸere Zweige", "gerade, wenig Zweige", "gerade, symmetrisch", "geneigt, kleine Zweige"},
                   "de/fernunihagen/mci/group2/coopalgoart/shopf/test/lsystem/",
                   new String [] {"btree.png", "ctree.png", "dtree.png", "etree.png", "ftree.png"}));
           parameterDescription.add(ParameterDescription.createInteger("startX", "Startposition X", 0 , 1000, 500));
           parameterDescription.add(ParameterDescription.createInteger("startY", "Startposition Y", 0 , 1000, 1000));
           parameterDescription.add(ParameterDescription.createFloat("startRotation", "Anfangsdrehung", 0.0, (float)Math.PI, 0.0));
           parameterDescription.add(ParameterDescription.createColor("treeColor", "Farbe der Zweige", 0x4d804d));
           parameterDescription.add(ParameterDescription.createInteger("numberOfTree", "Anzahl Bäume", 1 , 10, 1));
           parameterDescription.add(ParameterDescription.createClearScreen(false));
           ServiceDescription<Generator> description = new ServiceDescription<>(Generator.class, "shopf_lsystem", "SHopf::LSystem", "", parameterDescription);
           SimpleServiceFactory<Generator> serviceFactory = new SimpleServiceFactory<>(description, p->{
               String treeShape = (String) p.get("treeShape").toString();
               //String treeShape = (String) p.get("treeShape");
               int startX = ((Number)p.get("startX")).intValue();
               int startY = ((Number)p.get("startY")).intValue();
               float startRotation =((Number)p.get("startRotation")).floatValue();
               int treeColor = 0xFF << 24 | ((Number) p.get("treeColor")).intValue();
               int numberOfTree = ((Number)p.get("numberOfTree")).intValue();
           
               return LsystemGenerator.builder() //
                       //.treeShape(treeShape) //
                       .startPositionX(startX) //
                       .startPositionY(startY) //
                       .startRotation(startRotation) //
                       .treeColor(treeColor)//
                       .treeShape(treeShape)//
                       .numberOfTree(numberOfTree)//
                       .build();
               });
           ServiceRegistry.register(serviceFactory);
       }
       
       private void registerEvolution() {
           List<ParameterDescription> parameterDescription = new LinkedList<>();
           parameterDescription.add(ParameterDescription.createInteger("lifespan", "Lebensspanne (Frames pro Generation)", 1 , 500, 150));
           parameterDescription.add(ParameterDescription.createInteger("numCords", "Anzahl Schnüre", 0 , 500, 100));
           //parameterDescription.add(ParameterDescription.createInteger("targetStartX", "Ziel Startposition X", 0 , 1000, 600));
           //parameterDescription.add(ParameterDescription.createInteger("targetStartY", "Ziel Startposition Y", 0 , 1000, 500));
           parameterDescription.add(ParameterDescription.createInteger("cordsStartX", "Schnüre Startposition X", 0 , 1000, 300));
           parameterDescription.add(ParameterDescription.createInteger("cordsStartY", "Schnüre Startposition Y", 0 , 1000, 500));
           parameterDescription.add(ParameterDescription.createInteger("hitPara", "Treffer auf Zielscheibe", 32 , 250, 32));
           parameterDescription.add(ParameterDescription.createCheckBox("cordsConnect", "Verbinde Schnüre", false));
           parameterDescription.add(ParameterDescription.createClearScreen(false));
           ServiceDescription<Generator> description = new ServiceDescription<>(Generator.class, "shopf_evolution", "SHopf::Evolution", "", parameterDescription);
           SimpleServiceFactory<Generator> serviceFactory = new SimpleServiceFactory<>(description, p->{
               int lifespan = ((Number)p.get("lifespan")).intValue();
               int numCords = ((Number)p.get("numCords")).intValue();
               //int targetStartX = ((Number)p.get("targetStartX")).intValue();
               //int targetStartY = ((Number)p.get("targetStartY")).intValue();
               int cordsStartX = ((Number)p.get("cordsStartX")).intValue();
               int cordsStartY = ((Number)p.get("cordsStartY")).intValue();
               int hitPara = ((Number)p.get("hitPara")).intValue();
               boolean cordsConnect = ((boolean)p.getOrDefault("cordsConnect", false));
       
               return CordsEvolutionGenerator.builder() //
                       .lifespan(lifespan) //
                       .numCords(numCords) //
                       //.targetStartX(targetStartX) //
                       //.targetStartY(targetStartY) //
                       .cordsStartX(cordsStartX) //
                       .cordsStartY(cordsStartY) //
                       .hitPara(hitPara)//
                       .cordsConnect(cordsConnect)//
                       .build();
               });
           ServiceRegistry.register(serviceFactory);
       }

}
