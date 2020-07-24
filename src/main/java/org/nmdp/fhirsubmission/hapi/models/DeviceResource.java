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
