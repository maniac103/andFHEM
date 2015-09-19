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

package li.klass.fhem.fragments.core;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.Toast;

import javax.inject.Inject;

import li.klass.fhem.R;
import li.klass.fhem.adapter.devices.core.DeviceAdapter;
import li.klass.fhem.constants.Actions;
import li.klass.fhem.constants.BundleExtraKeys;
import li.klass.fhem.constants.ResultCodes;
import li.klass.fhem.domain.core.DeviceType;
import li.klass.fhem.domain.core.FhemDevice;
import li.klass.fhem.service.advertisement.AdvertisementService;
import li.klass.fhem.service.intent.FavoritesIntentService;
import li.klass.fhem.service.intent.RoomListIntentService;
import li.klass.fhem.service.room.FavoritesService;
import li.klass.fhem.util.device.DeviceActionUtil;
import li.klass.fhem.widget.notification.NotificationSettingView;

import static li.klass.fhem.constants.Actions.FAVORITE_ADD;
import static li.klass.fhem.constants.Actions.FAVORITE_REMOVE;

public class DeviceDetailFragment extends BaseFragment {
    @Inject
    FavoritesService favoritesService;

    @Inject
    AdvertisementService advertisementService;
    private String deviceName;
    private FhemDevice device;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        deviceName = args.getString(BundleExtraKeys.DEVICE_NAME);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View superView = super.onCreateView(inflater, container, savedInstanceState);
        if (superView != null) return superView;

        View view = inflater.inflate(R.layout.device_detail_view, container, false);
        advertisementService.addAd(view, getActivity());

        return view;
    }

    @Override
    public void update(boolean doUpdate) {
        hideEmptyView();

        if (doUpdate) getActivity().sendBroadcast(new Intent(Actions.SHOW_EXECUTING_DIALOG));

        Intent intent = new Intent(Actions.GET_DEVICE_FOR_NAME);
        intent.setClass(getActivity(), RoomListIntentService.class);
        intent.putExtra(BundleExtraKeys.DO_REFRESH, doUpdate);
        intent.putExtra(BundleExtraKeys.DEVICE_NAME, deviceName);
        intent.putExtra(BundleExtraKeys.RESULT_RECEIVER, new ResultReceiver(new Handler()) {
            @Override
            protected void onReceiveResult(int resultCode, Bundle resultData) {
                super.onReceiveResult(resultCode, resultData);

                FragmentActivity activity = getActivity();
                if (activity == null) return;

                activity.sendBroadcast(new Intent(Actions.DISMISS_EXECUTING_DIALOG));

                if (resultCode == ResultCodes.SUCCESS && getView() != null) {
                    device = (FhemDevice) resultData.getSerializable(BundleExtraKeys.DEVICE);
                    long lastUpdate = resultData.getLong(BundleExtraKeys.LAST_UPDATE);

                    if (device == null) return;

                    DeviceAdapter adapter = DeviceType.getAdapterFor(device);
                    if (adapter == null) {
                        return;
                    }
                    activity.supportInvalidateOptionsMenu();
                    adapter.attach(DeviceDetailFragment.this.getActivity());
                    ScrollView scrollView = (ScrollView) getView().findViewById(R.id.deviceDetailView);
                    if (scrollView != null) {
                        scrollView.removeAllViews();
                        scrollView.addView(adapter.createDetailView(activity, device, lastUpdate));
                    }
                }
            }
        });
        getActivity().startService(intent);
    }

    @Override
    public CharSequence getTitle(Context context) {
        String name = getArguments().getString(BundleExtraKeys.DEVICE_DISPLAY_NAME);
        if (name == null) {
            name = getArguments().getString(BundleExtraKeys.DEVICE_NAME);
        }
        return name;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if (device != null) {
            inflater.inflate(R.menu.device_menu, menu);
            if (favoritesService.isFavorite(deviceName, getActivity())) {
                menu.removeItem(R.id.menu_favorites_add);
            } else {
                menu.removeItem(R.id.menu_favorites_remove);
            }
            menu.removeItem(R.id.menu_rename);
            menu.removeItem(R.id.menu_delete);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_favorites_add:
            case R.id.menu_favorites_remove: {
                final boolean isAdd = item.getItemId() == R.id.menu_favorites_add;
                Intent favoriteAddIntent = new Intent(isAdd ? FAVORITE_ADD : FAVORITE_REMOVE);
                favoriteAddIntent.setClass(getActivity(), FavoritesIntentService.class);
                favoriteAddIntent.putExtra(BundleExtraKeys.DEVICE, device);
                favoriteAddIntent.putExtra(BundleExtraKeys.RESULT_RECEIVER, new ResultReceiver(new Handler()) {
                    @Override
                    protected void onReceiveResult(int resultCode, Bundle resultData) {
                        if (resultCode != ResultCodes.SUCCESS) return;

                        Toast.makeText(getActivity(),
                                isAdd ? R.string.context_favoriteadded : R.string.context_favoriteremoved,
                                Toast.LENGTH_SHORT).show();
                    }
                });
                getActivity().startService(favoriteAddIntent);
                break;
            }
            case R.id.menu_room:
                DeviceActionUtil.moveDevice(getActivity(), device);
                break;
            case R.id.menu_alias:
                DeviceActionUtil.setAlias(getActivity(), device);
                break;
            case R.id.menu_notification:
                new NotificationSettingView(getActivity(), deviceName).show(getActivity());
                break;
            default:
                return false;
        }
        return super.onOptionsItemSelected(item);
    }
}
