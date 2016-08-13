package xyz.hanks.note.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import xyz.hanks.note.R;
import xyz.hanks.note.util.VectorDrawableUtils;

/**
 * Created by hanks on 16/7/14.
 */
public class SettingFragment extends Fragment {

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
        return view;
    }

    @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.tv_update).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkUpdate();
            }
        });

        view.findViewById(R.id.tv_share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareApp();
            }
        });

        view.findViewById(R.id.tv_feedback).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                feedback();
            }
        });
    }

    private void feedback() {

    }

    private void checkUpdate() {
    }

    private void shareApp() {

    }
}
