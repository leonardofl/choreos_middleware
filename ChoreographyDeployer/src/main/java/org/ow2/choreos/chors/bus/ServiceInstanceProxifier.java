/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.ow2.choreos.chors.bus;

import org.ow2.choreos.services.datamodel.ServiceInstance;
import org.ow2.choreos.services.datamodel.ServiceType;

import esstar.petalslink.com.service.management._1_0.ManagementException;

public class ServiceInstanceProxifier {

    public String proxify(ServiceInstance serviceInstance, EasyESBNode esbNode) throws ManagementException {

	ServiceType type = serviceInstance.getServiceSpec().getServiceType();
	if (type != ServiceType.SOAP) {
	    throw new IllegalArgumentException("We can bind only SOAP services, not " + type);
	}

	String url = serviceInstance.getNativeUri();
	url = url.replaceAll("/$", "");
	String wsdl = url + "?wsdl";

	return esbNode.proxifyService(url, wsdl);
    }

}
