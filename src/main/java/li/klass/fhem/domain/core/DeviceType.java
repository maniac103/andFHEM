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

package li.klass.fhem.domain.core;

import java.util.Locale;
import java.util.Map;

import li.klass.fhem.adapter.devices.CULHMAdapter;
import li.klass.fhem.adapter.devices.DmxAdapter;
import li.klass.fhem.adapter.devices.DummyAdapter;
import li.klass.fhem.adapter.devices.EnOceanAdapter;
import li.klass.fhem.adapter.devices.EnigmaDeviceAdapter;
import li.klass.fhem.adapter.devices.FHTAdapter;
import li.klass.fhem.adapter.devices.FS20ZDRDeviceAdapter;
import li.klass.fhem.adapter.devices.FloorplanAdapter;
import li.klass.fhem.adapter.devices.GCMSendDeviceAdapter;
import li.klass.fhem.adapter.devices.HarmonyDeviceAdapter;
import li.klass.fhem.adapter.devices.HueDeviceAdapter;
import li.klass.fhem.adapter.devices.LightSceneAdapter;
import li.klass.fhem.adapter.devices.MaxAdapter;
import li.klass.fhem.adapter.devices.MiLightDeviceAdapter;
import li.klass.fhem.adapter.devices.OnkyoAvrDeviceAdapter;
import li.klass.fhem.adapter.devices.OwSwitchDeviceAdapter;
import li.klass.fhem.adapter.devices.PCA9532DeviceAdapter;
import li.klass.fhem.adapter.devices.PCF8574DeviceAdapter;
import li.klass.fhem.adapter.devices.PIDDeviceAdapter;
import li.klass.fhem.adapter.devices.PioneerAvrDeviceAdapter;
import li.klass.fhem.adapter.devices.PioneerAvrZoneDeviceAdapter;
import li.klass.fhem.adapter.devices.ReadingsProxyDeviceAdapter;
import li.klass.fhem.adapter.devices.RemoteControlAdapter;
import li.klass.fhem.adapter.devices.SBPlayerDeviceAdapter;
import li.klass.fhem.adapter.devices.STVDeviceAdapter;
import li.klass.fhem.adapter.devices.SonosPlayerAdapter;
import li.klass.fhem.adapter.devices.SwapDeviceAdapter;
import li.klass.fhem.adapter.devices.ThresholdAdapter;
import li.klass.fhem.adapter.devices.UniRollAdapter;
import li.klass.fhem.adapter.devices.WOLAdapter;
import li.klass.fhem.adapter.devices.WeatherAdapter;
import li.klass.fhem.adapter.devices.WebLinkAdapter;
import li.klass.fhem.adapter.devices.WifiLightDeviceAdapter;
import li.klass.fhem.adapter.devices.YamahaAVRAdapter;
import li.klass.fhem.adapter.devices.core.DeviceAdapter;
import li.klass.fhem.adapter.devices.core.DimmableAdapter;
import li.klass.fhem.adapter.devices.core.GenericDeviceAdapter;
import li.klass.fhem.adapter.devices.core.GenericDeviceAdapterWithSwitchActionRow;
import li.klass.fhem.adapter.devices.core.ToggleableAdapter;
import li.klass.fhem.domain.AtDevice;
import li.klass.fhem.domain.CULFHTTKDevice;
import li.klass.fhem.domain.CULHMDevice;
import li.klass.fhem.domain.DMXDevice;
import li.klass.fhem.domain.DummyDevice;
import li.klass.fhem.domain.EC3000Device;
import li.klass.fhem.domain.EIBDevice;
import li.klass.fhem.domain.EnOceanDevice;
import li.klass.fhem.domain.EnigmaDevice;
import li.klass.fhem.domain.FBCallmonitorDevice;
import li.klass.fhem.domain.FHEMWEBDevice;
import li.klass.fhem.domain.FHTDevice;
import li.klass.fhem.domain.FS20Device;
import li.klass.fhem.domain.FS20ZDRDevice;
import li.klass.fhem.domain.FloorplanDevice;
import li.klass.fhem.domain.GCMSendDevice;
import li.klass.fhem.domain.GPIO4Device;
import li.klass.fhem.domain.GenericDevice;
import li.klass.fhem.domain.HCSDevice;
import li.klass.fhem.domain.HM485Device;
import li.klass.fhem.domain.HMSDevice;
import li.klass.fhem.domain.HOLDevice;
import li.klass.fhem.domain.HUEDevice;
import li.klass.fhem.domain.HarmonyDevice;
import li.klass.fhem.domain.HourCounterDevice;
import li.klass.fhem.domain.LGTVDevice;
import li.klass.fhem.domain.LaCrosseDevice;
import li.klass.fhem.domain.LightSceneDevice;
import li.klass.fhem.domain.MaxDevice;
import li.klass.fhem.domain.MiLightDevice;
import li.klass.fhem.domain.NetatmoDevice;
import li.klass.fhem.domain.OnkyoAvrDevice;
import li.klass.fhem.domain.OpenWeatherMapDevice;
import li.klass.fhem.domain.OregonDevice;
import li.klass.fhem.domain.OwDevice;
import li.klass.fhem.domain.OwSwitchDevice;
import li.klass.fhem.domain.OwcountDevice;
import li.klass.fhem.domain.OwthermDevice;
import li.klass.fhem.domain.PCA301Device;
import li.klass.fhem.domain.PCA9532Device;
import li.klass.fhem.domain.PCF8574Device;
import li.klass.fhem.domain.PIDDevice;
import li.klass.fhem.domain.PioneerAvrDevice;
import li.klass.fhem.domain.PioneerAvrZoneDevice;
import li.klass.fhem.domain.PresenceDevice;
import li.klass.fhem.domain.RFXCOMDevice;
import li.klass.fhem.domain.RFXX10RECDevice;
import li.klass.fhem.domain.RPIGPIODevice;
import li.klass.fhem.domain.ReadingsProxyDevice;
import li.klass.fhem.domain.RemoteControlDevice;
import li.klass.fhem.domain.RevoltDevice;
import li.klass.fhem.domain.RoommateDevice;
import li.klass.fhem.domain.SBPlayerDevice;
import li.klass.fhem.domain.STVDevice;
import li.klass.fhem.domain.SWAPDevice;
import li.klass.fhem.domain.SomfyDevice;
import li.klass.fhem.domain.SonosDevice;
import li.klass.fhem.domain.SonosPlayerDevice;
import li.klass.fhem.domain.StatisticsDevice;
import li.klass.fhem.domain.StructureDevice;
import li.klass.fhem.domain.TCM97001Device;
import li.klass.fhem.domain.TRXDevice;
import li.klass.fhem.domain.TRXLightDevice;
import li.klass.fhem.domain.TRXSecurityDevice;
import li.klass.fhem.domain.TRXWeatherDevice;
import li.klass.fhem.domain.ThresholdDevice;
import li.klass.fhem.domain.TwilightDevice;
import li.klass.fhem.domain.USBWXDevice;
import li.klass.fhem.domain.UniRollDevice;
import li.klass.fhem.domain.WOLDevice;
import li.klass.fhem.domain.WatchdogDevice;
import li.klass.fhem.domain.WeatherDevice;
import li.klass.fhem.domain.WebLinkDevice;
import li.klass.fhem.domain.WifiLightDevice;
import li.klass.fhem.domain.WithingsDevice;
import li.klass.fhem.domain.YamahaAVRDevice;
import li.klass.fhem.domain.log.LogDevice;
import li.klass.fhem.service.room.xmllist.XmlListDevice;

import static com.google.common.collect.Maps.newHashMap;

public enum DeviceType {

    WEATHER("Weather", WeatherDevice.class, new WeatherAdapter()),
    FLOORPLAN("FLOORPLAN", FloorplanDevice.class, new FloorplanAdapter(), DeviceVisibility.FHEMWEB_ONLY),
    FHT("FHT", FHTDevice.class, new FHTAdapter()),
    HMS("HMS", HMSDevice.class),
    MAX("MAX", MaxDevice.class, new MaxAdapter()),
    WOL("WOL", WOLDevice.class, new WOLAdapter()),
    CUL_FHTTK("CUL_FHTTK", CULFHTTKDevice.class),
    RFXX10REC("RFXX10REC", RFXX10RECDevice.class),
    OREGON("OREGON", OregonDevice.class),
    OWCOUNT("OWCOUNT", OwcountDevice.class),
    USBWX("USBWX", USBWXDevice.class),
    FS20("FS20", FS20Device.class, new DimmableAdapter<>(FS20Device.class)),
    FILE_LOG("FileLog", LogDevice.class),
    DB_LOG("DbLog", LogDevice.class),
    STATISTICS("statistics", StatisticsDevice.class),
    LGTV("LGTV", LGTVDevice.class),
    RFXCOM("RFXCOM", RFXCOMDevice.class),
    CUL_HM("CUL_HM", CULHMDevice.class, new CULHMAdapter()),
    WATCHDOG("watchdog", WatchdogDevice.class),
    HOLIDAY("HOL", HOLDevice.class, new ToggleableAdapter<>(HOLDevice.class)),
    PID("PID", PIDDevice.class, new PIDDeviceAdapter(PIDDevice.class)),
    PID20("PID20", PIDDevice.class, new PIDDeviceAdapter(PIDDevice.class)),
    TRX_WEATHER("TRX_WEATHER", TRXWeatherDevice.class),
    TRX_LIGHT("TRX_LIGHT", TRXLightDevice.class, new DimmableAdapter<>(TRXLightDevice.class)),
    TRX("TRX", TRXDevice.class),
    DUMMY("dummy", DummyDevice.class, new DummyAdapter()),
    STRUCTURE("structure", StructureDevice.class, new DimmableAdapter<>(StructureDevice.class)),
    TWILIGHT("Twilight", TwilightDevice.class),
    AT("at", AtDevice.class, null),
    EN_OCEAN("EnOcean", EnOceanDevice.class, new EnOceanAdapter()),
    EIB("EIB", EIBDevice.class, new DimmableAdapter<>(EIBDevice.class)),
    HCS("HCS", HCSDevice.class, new GenericDeviceAdapterWithSwitchActionRow<>(HCSDevice.class)),
    OWTHERM("OWTHERM", OwthermDevice.class),
    OWDEVICE("OWDevice", OwDevice.class, new ToggleableAdapter<>(OwDevice.class)),
    UNIROLL("UNIRoll", UniRollDevice.class, new UniRollAdapter()),
    TRXSecurity("TRX_SECURITY", TRXSecurityDevice.class, new GenericDeviceAdapterWithSwitchActionRow<>(TRXSecurityDevice.class)),
    PRESENCE("PRESENCE", PresenceDevice.class),
    SONOS_PLAYER("SONOSPLAYER", SonosPlayerDevice.class, new SonosPlayerAdapter()),
    SONOS("SONOS", SonosDevice.class),
    GPIO4("GPIO4", GPIO4Device.class),
    HUE("HUEDevice", HUEDevice.class, new HueDeviceAdapter()),
    YAMAHA_AVR("YAMAHA_AVR", YamahaAVRDevice.class, new YamahaAVRAdapter()),
    GCM_SEND("gcmsend", GCMSendDevice.class, new GCMSendDeviceAdapter()),
    SWAP("SWAP", SWAPDevice.class, new SwapDeviceAdapter()),
    FB_CALLMONITOR("FB_CALLMONITOR", FBCallmonitorDevice.class),
    FS20_ZDR("fs20_zdr", FS20ZDRDevice.class, new FS20ZDRDeviceAdapter()),
    OPENWEATHERMAP("openweathermap", OpenWeatherMapDevice.class),
    PCA301("PCA301", PCA301Device.class, new ToggleableAdapter<>(PCA301Device.class)),
    REMOTECONTROL("remotecontrol", RemoteControlDevice.class, new RemoteControlAdapter(), DeviceVisibility.FHEMWEB_ONLY),
    RPI_GPIO("RPI_GPIO", RPIGPIODevice.class, new ToggleableAdapter<>(RPIGPIODevice.class)),
    READINGS_PROXY("readingsProxy", ReadingsProxyDevice.class, new ReadingsProxyDeviceAdapter()),
    LACROSSE("LaCrosse", LaCrosseDevice.class),
    WEB_LINK("weblink", WebLinkDevice.class, new WebLinkAdapter()),
    OWSWITCH("OWSWITCH", OwSwitchDevice.class, new OwSwitchDeviceAdapter()),
    HM485("HM485", HM485Device.class, new DimmableAdapter<>(HM485Device.class)),
    LIGHT_SCENE("LightScene", LightSceneDevice.class, new LightSceneAdapter()),
    PCA9532("I2C_PCA9532", PCA9532Device.class, new PCA9532DeviceAdapter()),
    PCF8574("I2C_PCF8574", PCF8574Device.class, new PCF8574DeviceAdapter()),
    FHEMWEB("FHEMWEB", FHEMWEBDevice.class),
    THRESHOLD("THRESHOLD", ThresholdDevice.class, new ThresholdAdapter()),
    WIFILIGHT("WifiLight", WifiLightDevice.class, new WifiLightDeviceAdapter()),
    EC3000("EC3000", EC3000Device.class),
    WITHINGS("withings", WithingsDevice.class),
    DMX("DMXDevice", DMXDevice.class, new DmxAdapter()),
    NETATMO("netatmo", NetatmoDevice.class),
    ROOMMATE("ROOMMATE", RoommateDevice.class),
    SOMFY("SOMFY", SomfyDevice.class, new ToggleableAdapter<>(SomfyDevice.class)),
    ONKYO_AVR("ONKYO_AVR", OnkyoAvrDevice.class, new OnkyoAvrDeviceAdapter()),
    REVOLT("Revolt", RevoltDevice.class),
    ENIGMA2("ENIGMA2", EnigmaDevice.class, new EnigmaDeviceAdapter()),
    PIONEER("PIONEERAVR", PioneerAvrDevice.class, new PioneerAvrDeviceAdapter()),
    MILIGHT("MilightDevice", MiLightDevice.class, new MiLightDeviceAdapter()),
    STV("STV", STVDevice.class, new STVDeviceAdapter()),
    PIONEERAVRZONE("PIONEERAVRZONE", PioneerAvrZoneDevice.class, new PioneerAvrZoneDeviceAdapter()),
    SB_PLAYER("SB_PLAYER", SBPlayerDevice.class, new SBPlayerDeviceAdapter()),
    TCM97001("CUL_TCM97001", TCM97001Device.class),
    HARMONY("harmony", HarmonyDevice.class, new HarmonyDeviceAdapter()),
    HOURCOUNTER("HourCounter", HourCounterDevice.class),

    GENERIC("__generic__", GenericDevice.class, new DimmableAdapter<>(GenericDevice.class));

    private static final Map<Class<?>, DeviceType> DEVICE_TO_DEVICE_TYPE = newHashMap();
    private static final Map<String, DeviceType> TAG_TO_DEVICE_TYPE = newHashMap();

    static {
        for (DeviceType deviceType : values()) {
            DEVICE_TO_DEVICE_TYPE.put(deviceType.getDeviceClass(), deviceType);
            TAG_TO_DEVICE_TYPE.put(deviceType.getXmllistTag().toLowerCase(Locale.getDefault()), deviceType);
        }
    }

    private String xmllistTag;
    private Class<? extends FhemDevice> deviceClass;
    private DeviceAdapter<? extends FhemDevice<?>> adapter;
    private DeviceVisibility visibility = null;

    <T extends FhemDevice<T>> DeviceType(String xmllistTag, Class<T> deviceClass) {
        this(xmllistTag, deviceClass, new GenericDeviceAdapter<>(deviceClass));
    }

    DeviceType(String xmllistTag, Class<? extends FhemDevice> deviceClass, DeviceAdapter<? extends FhemDevice<?>> adapter) {
        this.xmllistTag = xmllistTag;
        this.deviceClass = deviceClass;
        this.adapter = adapter;
    }

    DeviceType(String xmllistTag, Class<? extends FhemDevice> deviceClass, DeviceAdapter<? extends FhemDevice<?>> adapter, DeviceVisibility visibility) {
        this(xmllistTag, deviceClass, adapter);
        this.visibility = visibility;
    }

    public static <T extends FhemDevice<T>> DeviceAdapter<T> getAdapterFor(T device) {
        DeviceType type = getDeviceTypeFor(device);
        return type == null ? null : type.<T>getAdapter();
    }

    public static <T extends FhemDevice> DeviceType getDeviceTypeFor(T device) {
        if (device == null) return null;
        XmlListDevice xmlListDevice = device.getXmlListDevice();
        if (xmlListDevice == null) return null;
        return getDeviceTypeFor(xmlListDevice.getType());
    }

    @SuppressWarnings("unchecked")
    public <T extends FhemDevice<T>> DeviceAdapter<T> getAdapter() {
        return (DeviceAdapter<T>) adapter;
    }

    public static <T extends FhemDevice> DeviceType getDeviceTypeFor(Class<T> clazz) {
        DeviceType result = DEVICE_TO_DEVICE_TYPE.get(clazz);
        return result == null ? GENERIC : result;
    }

    public static DeviceType getDeviceTypeFor(String xmllistTag) {
        DeviceType result = TAG_TO_DEVICE_TYPE.get(xmllistTag.toLowerCase(Locale.getDefault()));
        return result == null ? GENERIC : result;
    }

    public Class<? extends FhemDevice> getDeviceClass() {
        return deviceClass;
    }

    public String getXmllistTag() {
        return xmllistTag;
    }

    public DeviceVisibility getVisibility() {
        return visibility;
    }
}