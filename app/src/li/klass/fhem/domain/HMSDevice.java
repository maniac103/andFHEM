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

import android.content.Context;
import li.klass.fhem.AndFHEMApplication;
import li.klass.fhem.R;
import li.klass.fhem.appwidget.annotation.*;
import li.klass.fhem.appwidget.view.widget.AppWidgetView;
import li.klass.fhem.appwidget.view.widget.medium.MediumInformationWidgetView;
import li.klass.fhem.appwidget.view.widget.medium.TemperatureWidgetView;
import li.klass.fhem.domain.core.Device;
import li.klass.fhem.domain.core.DeviceChart;
import li.klass.fhem.domain.genericview.FloorplanViewSettings;
import li.klass.fhem.domain.genericview.ShowField;
import li.klass.fhem.service.graph.description.ChartSeriesDescription;
import li.klass.fhem.util.ValueUtil;
import org.w3c.dom.NamedNodeMap;

import java.util.List;

@SuppressWarnings("unused")
@FloorplanViewSettings(showState = true)
@SupportsWidget({TemperatureWidgetView.class, MediumInformationWidgetView.class})
public class HMSDevice extends Device<HMSDevice> {
    @ShowField(description = R.string.temperature, showInOverview = true)
    @WidgetTemperatureField
    @WidgetMediumLine1
    private String temperature;

    @ShowField(description = R.string.battery)
    @WidgetMediumLine3
    private String battery;

    @ShowField(description = R.string.humidity, showInOverview = true)
    @WidgetMediumLine2
    @WidgetTemperatureAdditionalField
    private String humidity;

    @ShowField(description = R.string.model)
    private String model;

    @ShowField(description = R.string.state, showInOverview = true)
    private String switchDetect;

    @Override
    public void onChildItemRead(String tagName, String keyValue, String nodeContent, NamedNodeMap attributes) {
        if (keyValue.equalsIgnoreCase("TEMPERATURE")) {
            temperature = ValueUtil.formatTemperature(nodeContent);
        } else if (keyValue.equalsIgnoreCase("BATTERY")) {
            battery = nodeContent;
        } else if (keyValue.equalsIgnoreCase("HUMIDITY")) {
            humidity = nodeContent;
        } else if (keyValue.equalsIgnoreCase("TYPE")) {
            this.model = nodeContent;
        } else if (keyValue.equalsIgnoreCase("SWITCH_DETECT")) {
            Context context = AndFHEMApplication.getContext();
            this.switchDetect = nodeContent.equalsIgnoreCase("ON")
                    ? context.getString(R.string.on) : context.getString(R.string.off);
        }
    }

    public String getTemperature() {
        return temperature;
    }

    public String getBattery() {
        return battery;
    }

    public String getHumidity() {
        return humidity;
    }

    public String getModel() {
        return model;
    }

    public String getSwitchDetect() {
        return switchDetect;
    }

    @Override
    protected void fillDeviceCharts(List<DeviceChart> chartSeries) {
        addDeviceChartIfNotNull(temperature, new DeviceChart(R.string.temperatureGraph, R.string.yAxisTemperature,
                ChartSeriesDescription.getRegressionValuesInstance(R.string.temperature, "4:T\\x3a:0:")));
        addDeviceChartIfNotNull(humidity, new DeviceChart(R.string.humidityGraph, R.string.yAxisHumidity,
                new ChartSeriesDescription(R.string.temperature, "6:H\\x3a:0:")));
    }

    @Override
    public boolean supportsWidget(Class<? extends AppWidgetView> appWidgetClass) {
        return !(appWidgetClass.equals(TemperatureWidgetView.class) && model.equals("HMS100TFK"));
    }
}