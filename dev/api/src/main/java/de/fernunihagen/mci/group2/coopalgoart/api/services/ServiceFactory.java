package de.fernunihagen.mci.group2.coopalgoart.api.services;

import java.util.Map;

/**
 * @author bwinzen
 *
 */
public interface ServiceFactory<T> {

	/**
	 * Gets the service description. E.g. what kind of parameter are needed for creation.
	 * @return the service description
	 */
	ServiceDescription<T> getServiceDescription();
	
	
	/**
	 * Creates a service with given parameter.
	 * @param parameter the parameter used for the configuration of the service
	 * @return
	 * @throws IllegalArgumentException if the parameter does not fit
	 */
	T createService(Map<String, Object> parameter);
}
