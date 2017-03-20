package com.example.xyzreader;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Loader;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.xyzreader.data.ArticleLoader;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ashok on 20/3/17.
 */

public class ArticleDetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    public static final String ARG_ITEM_ID = "item_id";
    private long mItemId;

    @BindView(R.id.backdrop)
    ImageView mBackdrop;
    @BindView(R.id.article_body)
    TextView mArticleBody;
    @BindView(R.id.article_title)
    TextView mArticleTitle;
    @BindView(R.id.article_byline)
    TextView mArticleByLine;

    public ArticleDetailFragment() {
    }

    public static ArticleDetailFragment newInstance(long itemId) {
        Bundle arguments = new Bundle();
        arguments.putLong(ARG_ITEM_ID, itemId);
        ArticleDetailFragment fragment = new ArticleDetailFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments().containsKey(ARG_ITEM_ID)) {
            mItemId = getArguments().getLong(ARG_ITEM_ID);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(0, null, this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return ArticleLoader.newInstanceForItemId(getActivity(), mItemId);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                Picasso.with(getActivity()).load(cursor.getString(ArticleLoader.Query.PHOTO_URL)).into(mBackdrop);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    mArticleBody.setText(Html.fromHtml(cursor.getString(ArticleLoader.Query.BODY), Html.FROM_HTML_MODE_LEGACY));
                } else {
                    mArticleBody.setText(Html.fromHtml(cursor.getString(ArticleLoader.Query.BODY)));
                }
                mArticleTitle.setText(cursor.getString(ArticleLoader.Query.TITLE));
                mArticleByLine.setText(cursor.getString(ArticleLoader.Query.AUTHOR));
            }

        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
