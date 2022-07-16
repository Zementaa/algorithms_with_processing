package de.fernunihagen.mci.group2.coopalgoart.api;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import de.fernunihagen.mci.group2.coopalgoart.api.services.ServiceDescription;
import de.fernunihagen.mci.group2.coopalgoart.api.services.ServiceFactory;

/**
 * @author bwinzen
 *
 */
public class ServiceRegistry {
	private static Map<Class<?>, Map<String, ServiceFactory<?>>> serviceRegistry = new HashMap<>();
	public static void register(ServiceFactory<?> serviceFactory){
		ServiceDescription<?> serviceDescription = serviceFactory.getServiceDescription();
		Map<String, ServiceFactory<?>> map = serviceRegistry.computeIfAbsent(serviceDescription.getServiceInterface(), s->new TreeMap<>());
		map.put(serviceDescription.getId(), serviceFactory);
	}
	
	@SuppressWarnings("unchecked")
	public static <T> Collection<ServiceFactory<T>> getServiceFactories(Class<T> clazz){
		List<ServiceFactory<T>> serviceFactories = new LinkedList<>();
		Map<String, ServiceFactory<?>> map = serviceRegistry.get(clazz);
		if(map != null) {
			for (ServiceFactory<?> serviceFactory : map.values()) {
				serviceFactories.add((ServiceFactory<T>) serviceFactory);
			}
		}
		return serviceFactories;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> ServiceFactory<T> getServiceFactory(Class<T> clazz, String id){
		Map<String, ServiceFactory<?>> map = serviceRegistry.get(clazz);
		if(map != null) {
			return (ServiceFactory<T>) map.get(id);
		}
		return null;
	}
}