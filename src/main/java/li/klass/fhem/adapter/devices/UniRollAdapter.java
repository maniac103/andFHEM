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

package li.klass.fhem.adapter.devices;

import android.content.Context;

import java.util.List;

import li.klass.fhem.R;
import li.klass.fhem.adapter.devices.core.GenericDeviceAdapterWithSwitchActionRow;
import li.klass.fhem.adapter.devices.genericui.DeviceDetailViewAction;
import li.klass.fhem.adapter.devices.genericui.DeviceDetailViewButtonAction;
import li.klass.fhem.domain.UniRollDevice;

public class UniRollAdapter extends GenericDeviceAdapterWithSwitchActionRow<UniRollDevice> {

    public UniRollAdapter() {
        super(UniRollDevice.class);
    }

    @Override
    protected List<DeviceDetailViewAction<UniRollDevice>> provideDetailActions() {
        List<DeviceDetailViewAction<UniRollDevice>> detailActions = super.provideDetailActions();

        detailActions.add(new DeviceDetailViewButtonAction<UniRollDevice>(R.string.up) {
            @Override
            public void onButtonClick(Context context, UniRollDevice device) {
                stateUiService.setState(device, "up", context);
            }
        });

        detailActions.add(new DeviceDetailViewButtonAction<UniRollDevice>(R.string.stop) {
            @Override
            public void onButtonClick(Context context, UniRollDevice device) {
                stateUiService.setState(device, "stop", context);
            }
        });

        detailActions.add(new DeviceDetailViewButtonAction<UniRollDevice>(R.string.down) {
            @Override
            public void onButtonClick(Context context, UniRollDevice device) {
                stateUiService.setState(device, "down", context);
            }
        });

        return detailActions;
    }
}
