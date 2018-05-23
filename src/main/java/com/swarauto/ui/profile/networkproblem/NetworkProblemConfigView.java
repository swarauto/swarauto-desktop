package com.swarauto.ui.profile.networkproblem;

import com.swarauto.game.indicator.Indicator;
import com.swarauto.game.profile.Profile;
import com.swarauto.ui.profile.widget.IndicatorConfigView;
import com.swarauto.ui.profile.widget.PointPicker;
import com.swarauto.ui.profile.widget.ValueListener;
import com.swarauto.util.MemImageCache;
import lombok.Getter;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.image.BufferedImage;

import static com.swarauto.util.PcConverter.toAwtPoint;

@Getter
public class NetworkProblemConfigView extends JPanel implements ValueListener, IndicatorConfigView.Listener {
    private Listener listener;

    private IndicatorConfigView networkDelayIndicator;
    private PointPicker networkDelayResendButtonLocation;
    private IndicatorConfigView networkUnstableIndicator;
    private PointPicker networkUnstableResendButtonLocation;

    private int rowCount = 0;

    public NetworkProblemConfigView(Listener listener) {
        this.listener = listener;

        setLayout(new MigLayout("", "[grow,fill]", "[][][][][][][][][]"));

        networkDelayIndicator = new IndicatorConfigView(this, Indicator.networkDelayIndicator, "Network Delay dialog",
                "For detecting Network Delay screen");
        addRow(networkDelayIndicator);
        networkDelayResendButtonLocation = new PointPicker("Network Delay, 'Resend' button click location", "After the battle end, if there is no networks then new screen will be shown with OK option. This is location of OK button");
        addRow(networkDelayResendButtonLocation);

        networkUnstableIndicator = new IndicatorConfigView(this, Indicator.networkUnstableIndicator, "Unstable Network dialog",
                "For detecting Network Unstable screen");
        addRow(networkUnstableIndicator);
        networkUnstableResendButtonLocation = new PointPicker("Unstable Network, 'Resend' button click location", "After click start battle button, if there is no networks then new screen will be shown with RESEND and NO option. This is location of RESEND button");
        addRow(networkUnstableResendButtonLocation);
    }

    private void addRow(PointPicker component) {
        component.setValueListener(this);
        add(component, "cell 0 " + rowCount++);
    }

    private void addRow(IndicatorConfigView component) {
        add(component, "cell 0 " + rowCount++);
    }

    public void populateFromModel(Profile profile) {
        populateFromModel(profile, networkDelayIndicator);
        populateFromModel(profile, networkUnstableIndicator);
        networkDelayResendButtonLocation.setData(toAwtPoint(profile.getConfirmNetworkDelay()));
        networkUnstableResendButtonLocation.setData(toAwtPoint(profile.getResendBattleInfo()));
    }

    private void populateFromModel(Profile profile, IndicatorConfigView indicatorConfigView) {
        indicatorConfigView.setData(MemImageCache.getInstance().get(profile.getIndicatorFile(indicatorConfigView.getIndicator())));
    }

    @Override
    public void valueChanged(Object source, Object newValue) {
        listener.onNetworkProblemConfigViewLocationsChanged();
    }

    @Override
    public void onIndicatorImageChanged(IndicatorConfigView view, BufferedImage bufferedImage) {
        listener.onNetworkProblemConfigViewIndicatorChanged(view.getIndicator(), bufferedImage);
    }

    public interface Listener {
        void onNetworkProblemConfigViewLocationsChanged();

        void onNetworkProblemConfigViewIndicatorChanged(Indicator indicator, BufferedImage bufferedImage);
    }
}
