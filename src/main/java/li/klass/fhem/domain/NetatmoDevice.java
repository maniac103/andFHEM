/*
 * AndFHEM - Open Source Android application to control a FHEM home automation
 * server.
 *
 * Copyright (c) 2011, Matthias Klass or third-party contributors as
 * indicated by the @author tags or express copyright attribution
 * statements applied by the authors.  All third-party contributions are
 * distributed under license by Red Hat Inc.
 *
 * This copyrighted material is made available to anyone wishing to use, modify,
 * copy, or redistribute it subject to the terms and conditions of the GNU GENERAL PUBLIC LICENSE, as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU GENERAL PUBLIC LICENSE
 * for more details.
 *
 * You should have received a copy of the GNU GENERAL PUBLIC LICENSE
 * along with this distribution; if not, write to:
 *   Free Software Foundation, Inc.
 *   51 Franklin Street, Fifth Floor
 *   Boston, MA  02110-1301  USA
 */

package li.klass.fhem.domain;

import li.klass.fhem.domain.core.DeviceFunctionality;
import li.klass.fhem.domain.core.FhemDevice;
import li.klass.fhem.domain.core.XmllistAttribute;
import li.klass.fhem.domain.genericview.ShowField;
import li.klass.fhem.domain.heating.TemperatureDevice;
import li.klass.fhem.resources.ResourceIdMapper;

import static li.klass.fhem.util.ValueDescriptionUtil.appendPercent;

public class NetatmoDevice extends FhemDevice<NetatmoDevice> implements TemperatureDevice {
    @ShowField(description = ResourceIdMapper.temperature, showInOverview = true)
    @XmllistAttribute("TEMPERATURE")
    private String temperature;

    @ShowField(description = ResourceIdMapper.humidity, showInOverview = true)
    @XmllistAttribute("humidity")
    private String humidity;

    @ShowField(description = ResourceIdMapper.co2, showInOverview = true)
    @XmllistAttribute("co2")
    private String co2;

    @ShowField(description = ResourceIdMapper.noise)
    @XmllistAttribute("noise")
    private String noise;

    @ShowField(description = ResourceIdMapper.pressure)
    @XmllistAttribute("pressure")
    private String pressure;

    @XmllistAttribute("SUBTYPE")
    private String subType;

    public String getTemperature() {
        return temperature;
    }


    public String getHumidity() {
        return humidity;
    }


    public void setHumidity(String humidity) {
        this.humidity = appendPercent(humidity);
    }

    public String getCo2() {
        return co2;
    }

    public String getNoise() {
        return noise;
    }

    public String getPressure() {
        return pressure;
    }

    public String getSubType() {
        return subType;
    }

    @Override
    public DeviceFunctionality getDeviceGroup() {
        return DeviceFunctionality.WEATHER;
    }

    @Override
    public boolean isSupported() {
        return !"ACCOUNT".equalsIgnoreCase(subType);
    }
}
