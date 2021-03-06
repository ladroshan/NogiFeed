package shts.jp.android.nogifeed.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import shts.jp.android.nogifeed.R;
import shts.jp.android.nogifeed.adapters.BindableAdapter;
import shts.jp.android.nogifeed.utils.IntentUtils;
import shts.jp.android.nogifeed.utils.PicassoHelper;

public class AboutFragment extends Fragment {

    private static final String URL_ICON = "https://avatars1.githubusercontent.com/u/7928836?v=3&s=460";
    private ListView listView;

    private void setupAboutListAdapter() {
        List<AboutItem> abouts = new ArrayList<>();
        abouts.add(new AboutItem(getResources().getString(R.string.about_item_share),
                R.drawable.ic_social_share, new OnClickListener() {
            @Override
            public void onClick() {
                IntentUtils.recommendApp(getActivity());
            }
        }));
        abouts.add(new AboutItem(getResources().getString(R.string.about_item_rate),
                R.drawable.ic_action_thumb_up, new OnClickListener() {
            @Override
            public void onClick() {
                IntentUtils.rateApp(getActivity());
            }
        }));
        abouts.add(new AboutItem(getResources().getString(R.string.about_item_mention),
                R.drawable.ic_communication_messenger, new OnClickListener() {
            @Override
            public void onClick() {
                IntentUtils.inquiryApp(getActivity());
            }
        }));
        listView.setAdapter(new AboutListAdapter(getActivity(), abouts));
    }

    private void setupListHeader(LayoutInflater inflater, ListView listView) {
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.about_header, null);

        ImageView developerIcon = (ImageView) view.findViewById(R.id.developer_icon);
        developerIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentUtils.showDeveloper(getActivity());
            }
        });
        PicassoHelper.loadAndCircleTransform(getActivity(), developerIcon, URL_ICON);

        listView.addHeaderView(view);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.fragment_about, null);

        final Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.nav_menu_about_app);
        toolbar.setNavigationIcon(R.drawable.ic_clear_white_18dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        listView = (ListView) view.findViewById(R.id.about_list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ListView listView = (ListView) adapterView;
                AboutItem aboutItem = (AboutItem) listView.getItemAtPosition(i);

                if (aboutItem != null && aboutItem.listener != null) {
                    aboutItem.listener.onClick();
                }
            }
        });
        setupListHeader(inflater, listView);
        setupAboutListAdapter();
        return view;
    }

    class AboutItem {
        public final String title;
        final int iconRes;
        final OnClickListener listener;

        AboutItem(String title, int iconRes, OnClickListener listener) {
            this.title = title;
            this.iconRes = iconRes;
            this.listener = listener;
        }
    }

    public interface OnClickListener {
        void onClick();
    }

    static class AboutListAdapter extends BindableAdapter<AboutItem> {

        class ViewHolder {
            TextView textView;
            ImageView imageView;

            ViewHolder(View view) {
                textView = (TextView) view.findViewById(R.id.title);
                imageView = (ImageView) view.findViewById(R.id.icon);
            }
        }

        AboutListAdapter(Context context, List<AboutItem> list) {
            super(context, list);
        }

        @Override
        public View newView(LayoutInflater inflater, int position, ViewGroup container) {
            @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.list_item_about_action, null);
            final ViewHolder holder = new ViewHolder(view);
            view.setTag(holder);
            return view;
        }

        @Override
        public void bindView(AboutItem item, int position, View view) {
            final ViewHolder holder = (ViewHolder) view.getTag();
            holder.imageView.setImageResource(item.iconRes);
            holder.textView.setText(item.title);
        }
    }
}
