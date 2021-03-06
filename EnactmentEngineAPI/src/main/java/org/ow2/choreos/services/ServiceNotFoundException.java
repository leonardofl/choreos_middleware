/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.ow2.choreos.services;

public class ServiceNotFoundException extends ServiceDeployerException {

    private static final long serialVersionUID = -4535824877031190147L;

    public ServiceNotFoundException(String serviceUUID) {
        super("Could not find service " + serviceUUID);
    }

}
