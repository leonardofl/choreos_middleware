/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.ow2.choreos.services.datamodel;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.SerializationUtils;
import org.ow2.choreos.nodes.datamodel.ResourceImpact;
import org.ow2.choreos.services.datamodel.qos.DesiredQoS;

@XmlRootElement
public class DeployableServiceSpec extends ServiceSpec implements Serializable {

    private static final long serialVersionUID = -6366027505813495206L;

    private String packageUri;
    private PackageType packageType;
    private String endpointName;
    private int port;
    private String cloudAccount;
    private int numberOfInstances = 1;
    private ResourceImpact resourceImpact;
    private String version;

    private DesiredQoS desiredQoS;

    public DeployableServiceSpec() {

    }

    public DeployableServiceSpec(String name, ServiceType serviceType, PackageType packageType,
	    ResourceImpact resourceImpact, String version, String packageUri, int port, String endpointName,
	    int numberOfInstances) {
	super.name = name;
	super.serviceType = serviceType;
	this.packageType = packageType;
	this.resourceImpact = resourceImpact;
	this.version = version;
	this.packageUri = packageUri;
	this.port = port;
	this.endpointName = endpointName;
	this.numberOfInstances = numberOfInstances;
    }

    public DeployableServiceSpec(String name, ServiceType serviceType, PackageType packageType,
	    ResourceImpact resourceImpact, String version, String packageUri, String endpointName, int numberOfInstances) {
	super.name = name;
	super.serviceType = serviceType;
	this.packageType = packageType;
	this.resourceImpact = resourceImpact;
	this.version = version;
	this.packageUri = packageUri;
	this.endpointName = endpointName;
	this.numberOfInstances = numberOfInstances;
    }

    public void setNumberOfInstances(int numberOfInstances) {
	if (numberOfInstances > 0)
	    this.numberOfInstances = numberOfInstances;
	else
	    this.numberOfInstances = 1;
    }

    public String getPackageUri() {
	return packageUri;
    }

    public void setPackageUri(String packageUri) {
	this.packageUri = packageUri;
    }

    public PackageType getPackageType() {
	return packageType;
    }

    public void setPackageType(PackageType packageType) {
	this.packageType = packageType;
    }

    public String getEndpointName() {
	return endpointName;
    }

    public void setEndpointName(String endpointName) {
	this.endpointName = endpointName;
    }

    public String getCloudAccount() {
	return cloudAccount;
    }

    public void setCloudAccount(String cloudAccount) {
	this.cloudAccount = cloudAccount;
    }

    public ResourceImpact getResourceImpact() {
	return resourceImpact;
    }

    public void setResourceImpact(ResourceImpact resourceImpact) {
	this.resourceImpact = resourceImpact;
    }

    public String getVersion() {
	return version;
    }

    public void setVersion(String version) {
	this.version = version;
    }

    public int getNumberOfInstances() {
	return numberOfInstances;
    }

    public void setPort(int port) {
	this.port = port;
    }

    public int getPort() {
	int effectivePort = port;
	if (portIsNotDefined()) {
	    PortRetriever portRetriever = new PortRetriever();
	    effectivePort = portRetriever.getPortByPackageType(packageType);
	}
	return effectivePort;
    }

    private boolean portIsNotDefined() {
	return this.port == 0;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = super.hashCode();
	result = prime * result + ((endpointName == null) ? 0 : endpointName.hashCode());
	result = prime * result + numberOfInstances;
	result = prime * result + ((cloudAccount == null) ? 0 : cloudAccount.hashCode());
	result = prime * result + ((packageType == null) ? 0 : packageType.hashCode());
	result = prime * result + ((packageUri == null) ? 0 : packageUri.hashCode());
	result = prime * result + port;
	result = prime * result + ((resourceImpact == null) ? 0 : resourceImpact.hashCode());
	result = prime * result + ((version == null) ? 0 : version.hashCode());
	return result;
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (!super.equals(obj))
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	DeployableServiceSpec other = (DeployableServiceSpec) obj;
	if (endpointName == null) {
	    if (other.endpointName != null)
		return false;
	} else if (!endpointName.equals(other.endpointName))
	    return false;
	if (numberOfInstances != other.numberOfInstances)
	    return false;
	if (cloudAccount == null) {
	    if (other.cloudAccount != null)
		return false;
	} else if (!cloudAccount.equals(other.cloudAccount))
	    return false;
	if (packageType == null) {
	    if (other.packageType != null)
		return false;
	} else if (!packageType.equals(other.packageType))
	    return false;
	if (packageUri == null) {
	    if (other.packageUri != null)
		return false;
	} else if (!packageUri.equals(other.packageUri))
	    return false;
	if (port != other.port)
	    return false;
	if (resourceImpact == null) {
	    if (other.resourceImpact != null)
		return false;
	} else if (!resourceImpact.equals(other.resourceImpact))
	    return false;
	if (version == null) {
	    if (other.version != null)
		return false;
	} else if (!version.equals(other.version))
	    return false;
	return true;
    }

    public DeployableServiceSpec clone() {
	return (DeployableServiceSpec) SerializationUtils.clone(this);
    }

    @Override
    public String toString() {
	return "DeployableServiceSpec [name=" + super.name + ", packageUri=" + packageUri + ", packageType="
		+ packageType + ", endpointName=" + endpointName + ", port=" + port + ", cloudAccount=" + cloudAccount 
		+ ", numberOfInstances=" + numberOfInstances + ", version=" + version + ", roles= "
		+ super.roles + "]";
    }

    public DesiredQoS getDesiredQoS() {
	return this.desiredQoS;
    }

    public void setDesiredQoS(DesiredQoS desiredQoS) {
	this.desiredQoS = desiredQoS;
    }

}