package com.swarauto.ui;

import com.swarauto.ui.main.PcMainView;
import com.swarauto.ui.profile.ProfileEditorView;

import java.util.HashMap;
import java.util.Map;

public class ViewRegistry {
    private static Map<String, BaseView> views = new HashMap<String, BaseView>();

    static {
        register(new PcMainView());
        register(new ProfileEditorView());
    }

    public static BaseView get(final Class<? extends BaseView> clazz) {
        return views.get(clazz.getSimpleName());
    }

    public static void register(final BaseView view) {
        views.put(view.getClass().getSimpleName(), view);
    }

    public static void showView(final Class<? extends BaseView> clazz) {
        BaseView view = get(clazz);
        if (view != null) {
            view.start();
        }
    }

    public static void hideView(final Class<? extends BaseView> clazz) {
        BaseView view = get(clazz);
        if (view != null) {
            view.stop();
        }
    }
}
