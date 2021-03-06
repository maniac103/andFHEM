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

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

import li.klass.fhem.domain.core.DeviceFunctionality;
import li.klass.fhem.domain.core.DimmableDiscreteStatesDevice;
import li.klass.fhem.domain.core.XmllistAttribute;
import li.klass.fhem.domain.genericview.OverviewViewSettings;
import li.klass.fhem.domain.genericview.ShowField;
import li.klass.fhem.resources.ResourceIdMapper;
import li.klass.fhem.service.room.xmllist.DeviceNode;
import li.klass.fhem.util.NumberSystemUtil;

import static java.util.Arrays.asList;

@OverviewViewSettings(showState = true)
@SuppressWarnings("unused")
public class FS20Device extends DimmableDiscreteStatesDevice<FS20Device> implements Comparable<FS20Device>, Serializable {

    /**
     * List of dim states available for FS20 devices. Careful: this list has to be ordered, to make dim up and
     * down work!
     */
    public static final List<String> DIM_STATES =
            asList("off", "dim6%", "dim12%", "dim18%", "dim25%", "dim31%", "dim37%", "dim43%", "dim50%", "dim56%",
                    "dim62%", "dim68%", "dim75%", "dim81%", "dim87%", "dim93%", "dim100%");
    public static final List<String> DIM_MODELS = asList("FS20DI", "FS20DI10", "FS20DU");
    public static final List<String> OFF_STATES = asList("off", "off-for-timer", "reset");

    @ShowField(description = ResourceIdMapper.model, showAfter = "definition")
    private String model;

    @XmllistAttribute("model")
    public void setModel(String value) {
        this.model = value.toUpperCase(Locale.getDefault());
    }

    @Override
    public void setDefinition(String value) {
        super.setDefinition(value);

        String[] parts = value.split(" ");
        if (parts.length == 2 && parts[0].length() == 4 && parts[1].length() == 2) {
            definition = transformHexTo4System(parts[0]) + " " + transformHexTo4System(parts[1]);
        }
    }

    @Override
    public void setState(String value, DeviceNode node) {
        super.setState(value, node);
        if (node.isStateNode()) {
            setMeasured(node.getMeasured());
        }
    }

    @Override
    public boolean isOffByState() {
        if (super.isOffByState()) {
            return true;
        }

        String internalState = getInternalState() + "";
        for (String offState : OFF_STATES) {
            if (internalState.contains(offState)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean supportsToggle() {
        return true;
    }

    @Override
    public boolean supportsDim() {
        return DIM_MODELS.contains(model);
    }

    @Override
    public List<String> getDimStates() {
        return DIM_STATES;
    }

    @Override
    public DeviceFunctionality getDeviceGroup() {
        return DeviceFunctionality.functionalityForDimmable(this);
    }

    private String transformHexTo4System(String input) {
        return NumberSystemUtil.hexToQuaternary(input, 4);
    }

    public enum FS20State {
        ON, OFF
    }
}
