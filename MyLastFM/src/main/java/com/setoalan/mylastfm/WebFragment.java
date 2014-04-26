package com.setoalan.mylastfm;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Fragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

public class WebFragment extends Fragment {

    public static String mUrl;
    private WebView website_vw;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        ActionBar actionBar = getActivity().getActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.RED));
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(EventFragment.mEvent.getTitle());
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_web_view, parent, false);

        final ProgressBar progress_bar = (ProgressBar) v.findViewById(R.id.progress_bar);
        final TextView title_tv = (TextView) v.findViewById(R.id.title_tv);
        website_vw = (WebView) v.findViewById(R.id.website_wv);

        progress_bar.setMax(100);
        website_vw.getSettings().setJavaScriptEnabled(true);
        website_vw.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
        });

        website_vw.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView webView, int progress) {
                if (progress == 100) {
                    progress_bar.setVisibility(View.INVISIBLE);
                } else {
                    progress_bar.setVisibility(View.VISIBLE);
                    progress_bar.setProgress(progress);
                }
            }

            public void onReceivedTitle(WebView webView, String title) {
                title_tv.setText(title);
            }
        });

        website_vw.loadUrl(mUrl);

        return v;
    }

}
