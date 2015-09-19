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

package li.klass.fhem.adapter.devices.genericui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ToggleButton;

import java.util.List;

import li.klass.fhem.R;
import li.klass.fhem.domain.core.FhemDevice;

import static li.klass.fhem.adapter.devices.genericui.AvailableTargetStatesDialogUtil.STATE_SENDING_CALLBACK;
import static li.klass.fhem.adapter.devices.genericui.AvailableTargetStatesDialogUtil.handleSelectedOption;

public class WebCmdActionRow<D extends FhemDevice<D>> extends HolderActionRow<D, String> {
    public WebCmdActionRow(int layout, Context context) {
        super(context.getString(R.string.webcmd), layout);
    }

    public WebCmdActionRow(String description, int layout) {
        super(description, layout);
    }

    @Override
    public List<String> getItems(D device) {
        return device.getWebCmd();
    }

    @Override
    public View viewFor(final String command, final D device, LayoutInflater inflater,
                        final Context context, ViewGroup viewGroup) {

        View container = inflater.inflate(R.layout.webcmd_row_element, viewGroup, false);
        ToggleButton button = (ToggleButton) container.findViewById(R.id.toggleButton);
        assert button != null;

        button.setText(device.getEventMapStateFor(command));
        button.setTextOn(device.getEventMapStateFor(command));
        button.setTextOff(device.getEventMapStateFor(command));

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                handleSelectedOption(context, device, command, STATE_SENDING_CALLBACK);
            }
        });

        return container;
    }
}
