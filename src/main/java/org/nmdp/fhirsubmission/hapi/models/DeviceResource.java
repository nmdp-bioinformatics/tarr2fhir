/*
 * Copyright (c) 2020 Be The Match operated by National Marrow Donor Program (NMDP).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES
 * OR CONDITIONS OF ANY KIND, either express or implied. See the License for
 * the specific language governing permissions and limitations under the License.
 */

package org.nmdp.fhirsubmission.hapi.models;

import org.hl7.fhir.r4.model.Device;

import java.util.ArrayList;
import java.util.List;

public class DeviceResource {

    public DeviceResource()
    {
        myDevice = new Device();
    }
    private final Device myDevice;

    public Device getMyDevice() {
        return myDevice;
    }

    public void generateDeviceResource(List<String> theProvenanceReferences)
    {
        Device.DeviceDeviceNameComponent aDeviceName = new Device.DeviceDeviceNameComponent();
        List<Device.DeviceDeviceNameComponent> aDNameList = new ArrayList<>();
        aDeviceName.setName("Tarr2Fhir");
        aDeviceName.setType(Device.DeviceNameType.MODELNAME);
        aDNameList.add(aDeviceName);
        myDevice.setDeviceName(aDNameList);
        myDevice.setText((new NarrativeText()).
                getNarrative("software device: Tarr2Fhir"));
        myDevice.setId(FhirGuid.genereateUrn());
        theProvenanceReferences.add(myDevice.getIdElement().getValue());

    }
}
