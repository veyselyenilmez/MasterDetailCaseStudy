package com.veyselyenilmez.masterdetailcasestudy;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

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
    private static boolean mTwoPane;


    private KProgressHUD hud;


    private static APIService mAPIService = APIUtils.getAPIService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        if (findViewById(R.id.item_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

            // showing a loading dialog on the screen until we get results from API.
            showLoadingDialog();

            // Making a request from the api and setting up the recyclerview adapter
            // when we get results.
            // And dismissing the loading dialog.
            getDataFromAPIAndSetupRecyclerView();

    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView, GamesList gamesList) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(this, gamesList, mTwoPane));
    }

    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final ItemListActivity mParentActivity;
        private final GamesList gamesList;
        private final boolean mTwoPane;
        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                // Making a network request for the detailed data of the chosen game
                // according to the id of it.
                // And then transferring the result to the new screen.

                Game game = (Game) view.getTag();
                mAPIService.getDetailedDataById(game.getId()).enqueue(new Callback<Game>() {
                    @Override
                    public void onResponse(Call<Game> call, Response<Game> response) {
                        if (response.isSuccess()) {
                            Game game = response.body();
                            initiateDetailedScreen(mParentActivity, game, view, mTwoPane);
                        }
                    }

                    @Override
                    public void onFailure(Call<Game> call, Throwable t) {
                        Log.e("getDetailedDataById", "Request failed.");
                    }
                });
            }
        };

        SimpleItemRecyclerViewAdapter(ItemListActivity parent,
                                      GamesList gamesList,
                                      boolean mTwoPane) {
            this.gamesList = gamesList;
            mParentActivity = parent;
            this.mTwoPane = mTwoPane;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mIdView.setText(String.valueOf(position + 1));
            holder.mContentView.setText(gamesList.getGames().get(position).getName());

            holder.itemView.setTag(gamesList.getGames().get(position));
            holder.itemView.setOnClickListener(mOnClickListener);
        }

        @Override
        public int getItemCount() {
            return gamesList.getGames().size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final TextView mIdView;
            final TextView mContentView;

            ViewHolder(View view) {
                super(view);
                mIdView = view.findViewById(R.id.id_text);
                mContentView = view.findViewById(R.id.content);
            }
        }
    }

    public void getDataFromAPIAndSetupRecyclerView() {
        mAPIService.getData().enqueue(new Callback<GamesList>() {
            @Override
            public void onResponse(Call<GamesList> call, Response<GamesList> response) {
                if (response.isSuccess()) {

                    GamesList gamesList = response.body();

                    View recyclerView = findViewById(R.id.item_list);
                    assert recyclerView != null;
                    setupRecyclerView((RecyclerView) recyclerView, gamesList);

                    hud.dismiss();
                }
            }

            @Override
            public void onFailure(Call<GamesList> call, Throwable t) {
                hud.dismiss();
                Toasty.error(ItemListActivity.this, "Connection problem!", Toast.LENGTH_LONG).show();

                Log.e("getDataFromAPI", "Request failed.");
            }
        });
    }


            // Taking the data as a parameter. If there are two panes
            // sending it to the ItemDetailFragment
            // and if there is one pane sending it to the ItemDetailActivity.
    public static void initiateDetailedScreen(ItemListActivity mParentActivity, Game game, View view, boolean mTwoPane) {
        if (mTwoPane) {
            Bundle arguments = new Bundle();
            arguments.putSerializable(ItemDetailFragment.ARG_GAME, game);
            ItemDetailFragment fragment = new ItemDetailFragment();
            fragment.setArguments(arguments);
            mParentActivity.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.item_detail_container, fragment)
                    .commit();
        } else {
            Context context = view.getContext();
            Intent intent = new Intent(context, ItemDetailActivity.class);
            intent.putExtra(ItemDetailFragment.ARG_GAME, game);
            context.startActivity(intent);
        }
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
