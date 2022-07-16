package de.fernunihagen.mci.group2.coopalgoart.api.services;

import java.util.Map;
import java.util.function.Function;

public class SimpleServiceFactory<T> implements ServiceFactory<T> {
	private ServiceDescription<T> serviceDescription;
	private Function<Map<String, Object>, T> constructor;

	public SimpleServiceFactory(ServiceDescription<T> serviceDescription, Function<Map<String, Object>, T> constructor) {
		this.constructor = constructor;
		this.serviceDescription = serviceDescription;
	}

	@Override
	public ServiceDescription<T> getServiceDescription() {
		return serviceDescription;
	}

	@Override
	public T createService(Map<String, Object> parameter) {
		return constructor.apply(parameter);
	}

}
