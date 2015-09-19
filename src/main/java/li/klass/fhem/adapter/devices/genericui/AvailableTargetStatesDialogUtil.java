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

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;

import java.util.List;

import li.klass.fhem.R;
import li.klass.fhem.adapter.devices.core.UpdatingResultReceiver;
import li.klass.fhem.constants.Actions;
import li.klass.fhem.constants.BundleExtraKeys;
import li.klass.fhem.domain.core.DeviceStateRequiringAdditionalInformation;
import li.klass.fhem.domain.core.DimmableDevice;
import li.klass.fhem.domain.core.FhemDevice;
import li.klass.fhem.domain.setlist.SetList;
import li.klass.fhem.domain.setlist.SetListGroupValue;
import li.klass.fhem.domain.setlist.SetListSliderValue;
import li.klass.fhem.domain.setlist.SetListValue;
import li.klass.fhem.service.intent.DeviceIntentService;
import li.klass.fhem.util.DialogUtil;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static li.klass.fhem.domain.core.DeviceStateRequiringAdditionalInformation.isValidAdditionalInformationValue;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isEmpty;

public class AvailableTargetStatesDialogUtil {

    public static final TargetStateSelectedCallback STATE_SENDING_CALLBACK = new TargetStateSelectedCallback() {
        @Override
        public <D extends FhemDevice<D>> void onTargetStateSelected(String state, String subState, D device, Context context) {
            if (isBlank(state)) return;

            if (!isBlank(subState)) {
                switchDeviceSubState(state, subState, device, context);
            } else {
                switchDeviceState(state, device, context);
            }
        }

        private <D extends FhemDevice<D>> void switchDeviceState(String newState, D device, final Context context) {
            Intent intent = new Intent(Actions.DEVICE_SET_STATE);
            intent.setClass(context, DeviceIntentService.class);
            intent.putExtra(BundleExtraKeys.DEVICE_NAME, device.getName());
            intent.putExtra(BundleExtraKeys.DEVICE_TARGET_STATE, newState);
            intent.putExtra(BundleExtraKeys.RESULT_RECEIVER, new UpdatingResultReceiver(context));
            context.startService(intent);
        }

        private <D extends FhemDevice<D>> void switchDeviceSubState(String newState, String newSubState, D device, final Context context) {
            if (newState.equalsIgnoreCase("state") || isEmpty(newSubState)) {
                switchDeviceState(newSubState, device, context);
                return;
            }
            Intent intent = new Intent(Actions.DEVICE_SET_SUB_STATE);
            intent.setClass(context, DeviceIntentService.class);
            intent.putExtra(BundleExtraKeys.DEVICE_NAME, device.getName());
            intent.putExtra(BundleExtraKeys.STATE_NAME, newState);
            intent.putExtra(BundleExtraKeys.STATE_VALUE, newSubState);
            intent.putExtra(BundleExtraKeys.RESULT_RECEIVER, new UpdatingResultReceiver(context));
            context.startService(intent);
        }
    };

    public static <D extends FhemDevice<D>> void showSwitchOptionsMenu(final Context context, final D device, final TargetStateSelectedCallback callback) {
        AlertDialog.Builder contextMenu = new AlertDialog.Builder(context);
        contextMenu.setTitle(context.getResources().getString(R.string.switchDevice));
        final List<String> setOptions = device.getSetList().getSortedKeys();
        final String[] eventMapOptions = device.getAvailableTargetStatesEventMapTexts();

        DialogInterface.OnClickListener clickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int position) {
                final String option = setOptions.get(position);

                if (handleSelectedOption(context, device, option, callback)) return;

                dialog.dismiss();
            }
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, R.layout.list_item_with_arrow, eventMapOptions) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = View.inflate(context, R.layout.list_item_with_arrow, null);
                }
                TextView textView = (TextView) convertView.findViewById(R.id.text);
                ImageView imageView = (ImageView) convertView.findViewById(R.id.image);

                textView.setText(getItem(position));

                String setOption = setOptions.get(position);
                SetList setList = device.getSetList();
                final SetListValue setListValue = setList.get(setOption);

                imageView.setVisibility(setListValue instanceof SetListGroupValue ? VISIBLE : GONE);

                return convertView;
            }
        };
        contextMenu.setAdapter(adapter, clickListener);

        contextMenu.show();
    }

    public static <D extends FhemDevice<D>> TypeHandler<D>
    handlerForSelectedOption(D device, Context context,
                             final String option,
                             final TargetStateSelectedCallback callback) {

        SetList setList = device.getSetList();
        final SetListValue setListValue = setList.get(option);

        final DeviceStateRequiringAdditionalInformation specialDeviceState =
                DeviceStateRequiringAdditionalInformation.deviceStateForFHEM(option);

        if (setListValue instanceof SetListSliderValue) {
            final SetListSliderValue sliderValue = (SetListSliderValue) setListValue;
            return new TypeHandler<D>() {
                private int dimProgress = 0;

                @Override
                public void prepareDialogBuilder(final Context context, AlertDialog.Builder builder, final D device) {
                    TableLayout tableLayout = new TableLayout(context);
                    int initialProgress = 0;
                    if (device instanceof DimmableDevice) {
                        initialProgress = ((DimmableDevice) device).getDimPosition();
                    }

                    tableLayout.addView(new DeviceDimActionRowFullWidth<D>(initialProgress,
                            sliderValue.getStart(), sliderValue.getStep(), sliderValue.getStop(),
                            null, R.layout.device_detail_seekbarrow_full_width) {

                        @Override
                        public void onStopDim(Context context, D device, int progress) {
                            dimProgress = progress;
                        }

                        @Override
                        public String toDimUpdateText(D device, int progress) {
                            return null;
                        }
                    }.createRow(LayoutInflater.from(builder.getContext()), device));

                    builder.setView(tableLayout);
                    builder.setPositiveButton(R.string.okButton, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            callback.onTargetStateSelected(option, "" + dimProgress, device, context);
                            dialog.dismiss();
                        }
                    });
                }
            };
        } else if (setListValue instanceof SetListGroupValue) {
            final SetListGroupValue groupValue = (SetListGroupValue) setListValue;
            final List<String> groupStates = groupValue.getGroupStates();
            return new TypeHandler<D>() {

                @Override
                public void prepareDialogBuilder(final Context context, AlertDialog.Builder builder, final D device) {
                    final ListAdapter adapter = new ArrayAdapter<>(context,
                            android.R.layout.simple_list_item_1, groupStates);
                    builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String selection = groupStates.get(which);
                            callback.onTargetStateSelected(option, selection, device, context);
                        }
                    });
                }
            };
        } else if (specialDeviceState != null) {
            return new TypeHandler<D>() {
                private EditText editText;

                @Override
                public void prepareDialogBuilder(final Context context, AlertDialog.Builder builder, final D device) {
                    editText = new EditText(context);
                    builder.setView(editText);

                    builder.setPositiveButton(R.string.okButton, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Editable value = editText.getText();
                            String text = value == null ? "" : value.toString();

                            if (isValidAdditionalInformationValue(text, specialDeviceState)) {
                                callback.onTargetStateSelected(option, text, device, context);
                                dialog.dismiss();
                            } else {
                                DialogUtil.showAlertDialog(context, R.string.error, R.string.invalidInput);
                            }
                        }
                    });
                }
            };
        } else {
            callback.onTargetStateSelected(option, null, device, context);
            return null;
        }
    }


    public static <D extends FhemDevice<D>> boolean handleSelectedOption(final Context context, final D device, String option, TargetStateSelectedCallback callback) {
        final TypeHandler<D> typeHandler = handlerForSelectedOption(device, context, option, callback);
        if (typeHandler == null) {
            return true;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle(context.getString(R.string.stateAppendix, option))
                .setNegativeButton(R.string.cancelButton, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                });
        typeHandler.prepareDialogBuilder(context, builder, device);

        builder.show();
        return false;
    }

    public static interface TargetStateSelectedCallback {
        <D extends FhemDevice<D>> void onTargetStateSelected(String state, String subState, D device, Context context);
    }

    private interface TypeHandler<D> {
        void prepareDialogBuilder(Context context, AlertDialog.Builder builder, D device);
    }
}
