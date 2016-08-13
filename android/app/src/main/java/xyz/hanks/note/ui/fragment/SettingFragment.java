package xyz.hanks.note.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import xyz.hanks.note.R;
import xyz.hanks.note.util.VectorDrawableUtils;

/**
 * Created by hanks on 16/7/14.
 */
public class SettingFragment extends Fragment {

    private Unbinder unbinder;

    public static SettingFragment newInstance() {
        Bundle args = new Bundle();
        SettingFragment fragment = new SettingFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        getActivity().setTitle("设置");
        ((Toolbar) getActivity().findViewById(R.id.toolbar)).setNavigationIcon(VectorDrawableUtils.getBackDrawable(getContext()));
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }

    @OnClick(R.id.tv_feedback)
    public void feedback() {

    }

    @OnClick(R.id.tv_update)
    public void checkUpdate() {
    }

    @OnClick(R.id.tv_share)
    public void shareApp() {

    }
    @OnClick(R.id.tv_market)
    public void launchAppMarket() {

    }
}
