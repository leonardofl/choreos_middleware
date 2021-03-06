/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.ow2.choreos.chors.datamodel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import javax.xml.bind.annotation.XmlRootElement;

import org.ow2.choreos.services.datamodel.DeployableService;
import org.ow2.choreos.services.datamodel.Service;
import org.ow2.choreos.services.datamodel.ServiceInstance;

@XmlRootElement
public class Choreography {

    private String id;
    private ChoreographySpec choreographySpec;

    // For some mysterious reason, this cannot be initialized here
    // nor at any constructor (JAXB weirdness)
    private List<DeployableService> deployableServices;
    private List<LegacyService> legacyServices;

    public DeployableService getDeployableServiceBySpecName(String specName) {
        List<DeployableService> services = getDeployableServices();
        for (DeployableService svc : services) {
            if (specName.equals(svc.getSpec().getName()))
                return svc;
        }
        throw new NoSuchElementException("Service named " + specName + " does not exist");
    }

    public void addService(DeployableService deployableService) {
        if (deployableServices == null)
            deployableServices = new ArrayList<DeployableService>();
        deployableServices.add(deployableService);
    }

    public void addService(LegacyService legacyService) {
        if (legacyServices == null)
            legacyServices = new ArrayList<LegacyService>();
        legacyServices.add(legacyService);
    }

    public List<Service> getServices() {
        List<Service> services = new ArrayList<Service>();
        if (deployableServices != null) {
            for (Service svc : deployableServices) {
                services.add(svc);
            }
        }
        if (legacyServices != null) {
            for (Service svc : legacyServices) {
                services.add(svc);
            }
        }
        return services;
    }

    /**
     * 
     * @return a map whose keys are spec names and values are the respective
     *         deployable services
     */
    public Map<String, DeployableService> getMapOfDeployableServicesBySpecNames() {
        Map<String, DeployableService> map = new HashMap<String, DeployableService>();
        if (deployableServices != null) {
            for (DeployableService svc : deployableServices) {
                map.put(svc.getSpec().getName(), svc);
            }
        }
        return map;
    }

    /**
     * 
     * @return a map whose keys are spec names and values are the respective
     *         services
     */
    public Map<String, Service> getMapOfServicesBySpecNames() {
        Map<String, Service> map = new HashMap<String, Service>();
        if (deployableServices != null) {
            for (Service svc : deployableServices) {
                map.put(svc.getSpec().getName(), svc);
            }
        }
        if (legacyServices != null) {
            for (Service svc : legacyServices) {
                map.put(svc.getSpec().getName(), svc);
            }
        }
        return map;
    }

    public void removeServiceInstance(ServiceInstance instance) {
        if (instance == null || instance.getServiceSpec() == null)
            throw new IllegalArgumentException();
        if (deployableServices != null) {
            for (DeployableService service : deployableServices) {
                if (instance.getServiceSpec().equals(service.getSpec())) {
                    service.removeInstance(instance);
                }
            }
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ChoreographySpec getChoreographySpec() {
        return choreographySpec;
    }

    public void setChoreographySpec(ChoreographySpec spec) {
        this.choreographySpec = spec;
    }

    public List<DeployableService> getDeployableServices() {
        return deployableServices;
    }

    public void setDeployableServices(List<DeployableService> deployableServices) {
        this.deployableServices = deployableServices;
    }

    public List<LegacyService> getLegacyServices() {
        return legacyServices;
    }

    public void setLegacyServices(List<LegacyService> legacyServices) {
        this.legacyServices = legacyServices;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((id == null) ? 0 : id.hashCode());
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
        Choreography other = (Choreography) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Choreography [id=" + id + ", choreographySpec=" + choreographySpec + ", deployableServices="
                + deployableServices + ", legacyServices=" + legacyServices + "]";
    }

}