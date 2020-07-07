package com.veyselyenilmez.masterdetailcasestudy;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.veyselyenilmez.masterdetailcasestudy.data.model.GamesList;
import com.veyselyenilmez.masterdetailcasestudy.data.model.Game;
import com.veyselyenilmez.masterdetailcasestudy.data.network.APIService;
import com.veyselyenilmez.masterdetailcasestudy.data.network.APIUtils;
import com.veyselyenilmez.masterdetailcasestudy.dummy.DummyContent;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * An activity representing a list of Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link ItemDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class ItemListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    private APIService mAPIService;
    public GamesList gamesListGames = new GamesList();

    private KProgressHUD hud;

    public List<Game> games = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        if (findViewById(R.id.item_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }


        View recyclerView = findViewById(R.id.item_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);
        showLoadingDialog();

        getDetailedDataFromAPI(3498);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(this, DummyContent.ITEMS, mTwoPane));
    }


    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final ItemListActivity mParentActivity;
        private final List<DummyContent.DummyItem> mValues;
        private final boolean mTwoPane;
        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DummyContent.DummyItem item = (DummyContent.DummyItem) view.getTag();
                if (mTwoPane) {
                    Bundle arguments = new Bundle();
                    arguments.putString(ItemDetailFragment.ARG_ITEM_ID, item.id);
                    ItemDetailFragment fragment = new ItemDetailFragment();
                    fragment.setArguments(arguments);
                    mParentActivity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.item_detail_container, fragment)
                            .commit();
                } else {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, ItemDetailActivity.class);
                    intent.putExtra(ItemDetailFragment.ARG_ITEM_ID, item.id);

                    context.startActivity(intent);
                }
            }
        };

        SimpleItemRecyclerViewAdapter(ItemListActivity parent,
                                      List<DummyContent.DummyItem> items,
                                      boolean twoPane) {
            mValues = items;
            mParentActivity = parent;
            mTwoPane = twoPane;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mIdView.setText(mValues.get(position).id);
            holder.mContentView.setText(mValues.get(position).content);

            holder.itemView.setTag(mValues.get(position));
            holder.itemView.setOnClickListener(mOnClickListener);
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final TextView mIdView;
            final TextView mContentView;

            ViewHolder(View view) {
                super(view);
                mIdView = (TextView) view.findViewById(R.id.id_text);
                mContentView = (TextView) view.findViewById(R.id.content);
            }
        }
    }


    public void getDataFromAPI() {

        mAPIService = APIUtils.getAPIService();


        mAPIService.getData().enqueue(new Callback<GamesList>() {
            @Override
            public void onResponse(Call<GamesList> call, Response<GamesList> response) {

                if (response.isSuccess()) {

                    GamesList gamesList = response.body();

                    System.out.println(gamesList.toString());

                    if (response.body() == null)
                        Log.d("", "response body is null");

                    hud.dismiss();
                }
            }
            @Override
            public void onFailure(Call<GamesList> call, Throwable t) {
                hud.dismiss();
                Toasty.error(ItemListActivity.this, "There is a problem about connection to the server!", Toast.LENGTH_SHORT).show();
                Log.e("", "Unable to submit post to API.");

            }
        });
    }


    public void getDetailedDataFromAPI(int id) {

        mAPIService = APIUtils.getAPIService();

        String ids= String.valueOf(id);

        mAPIService.getDetailedDataById(ids).enqueue(new Callback<Game>() {
            @Override
            public void onResponse(Call<Game> call, Response<Game> response) {

                if (response.isSuccess()) {

                    Game game = response.body();

                    System.out.println(game.toString());

                    if (response.body() == null)
                        Log.d("", "response body is null");

                    hud.dismiss();
                }
            }
            @Override
            public void onFailure(Call<Game> call, Throwable t) {
                hud.dismiss();
                Toasty.error(ItemListActivity.this, "There is a problem about connection to the server!", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void showLoadingDialog() {
        hud = KProgressHUD.create(ItemListActivity.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setDetailsLabel("Getting data from API...")
                .setCancellable(true)
                .setAnimationSpeed(1)
                .setDimAmount(0.78f)
                .setCornerRadius(12)
                .setWindowColor(Color.DKGRAY)
                .show();
    }
}
