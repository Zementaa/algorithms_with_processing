package de.fernunihagen.mci.group2.coopalgoart.api.services;

import java.util.List;

import de.fernunihagen.mci.group2.coopalgoart.api.services.desc.ParameterDescription;

/**
 * @author bwinzen
 *
 * @param <T>
 */
public class ServiceDescription<T> {
	private Class<T> serviceInterface;
	private String id;
	private String label;
	private String description;
	private List<ParameterDescription> parameterDescriptions;
	
	
	public ServiceDescription(Class<T> serviceInterface, String id, String label, String description,
			List<ParameterDescription> parameterDescriptions) {
		this.serviceInterface = serviceInterface;
		this.id = id;
		this.label = label;
		this.description = description;
		this.parameterDescriptions = parameterDescriptions;
	}
	
	public Class<T> getServiceInterface() {
		return serviceInterface;
	}
	public String getId() {
		return id;
	}
	public String getLabel() {
		return label;
	}
	public String getDescription() {
		return description;
	}
	public List<ParameterDescription> getParameterDescriptions() {
		return parameterDescriptions;
	}
}
