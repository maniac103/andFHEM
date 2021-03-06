package li.klass.fhem.adapter.devices.genericui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import li.klass.fhem.domain.core.FhemDevice;

public abstract class DeviceDetailViewAction<D extends FhemDevice> {

    public abstract View createView(Context context, LayoutInflater inflater, D device, LinearLayout parent);

    public boolean isVisible(D device) {
        return true;
    }
}
