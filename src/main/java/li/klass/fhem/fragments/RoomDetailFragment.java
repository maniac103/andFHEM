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

package li.klass.fhem.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import li.klass.fhem.R;
import li.klass.fhem.constants.Actions;
import li.klass.fhem.constants.BundleExtraKeys;
import li.klass.fhem.fragments.core.DeviceListFragment;
import li.klass.fhem.service.intent.RoomListIntentService;

import static li.klass.fhem.constants.BundleExtraKeys.ROOM_NAME;

public class RoomDetailFragment extends DeviceListFragment {

    private String roomName;

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        roomName = args.getString(BundleExtraKeys.ROOM_NAME);
    }

    @Override
    protected void fillIntent(Intent intent) {
        super.fillIntent(intent);
        intent.putExtra(ROOM_NAME, roomName);
    }

    @Override
    protected Class<?> getUpdateActionIntentTargetClass() {
        return RoomListIntentService.class;
    }

    @Override
    protected String getUpdateAction() {
        return Actions.GET_ROOM_DEVICE_LIST;
    }

    @Override
    public CharSequence getTitle(Context context) {
        return getArguments().getString(BundleExtraKeys.ROOM_NAME);
    }
}
