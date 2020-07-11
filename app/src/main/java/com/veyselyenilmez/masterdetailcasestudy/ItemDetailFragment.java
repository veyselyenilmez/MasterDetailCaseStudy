package com.veyselyenilmez.masterdetailcasestudy;

import android.app.Activity;
import android.os.Bundle;
import com.bumptech.glide.Glide;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.veyselyenilmez.masterdetailcasestudy.data.model.Game;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link ItemListActivity}
 * in two-pane mode (on tablets) or a {@link ItemDetailActivity}
 * on handsets.
 */
public class ItemDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item that this fragment
     * represents.
     */
    static final String ARG_GAME ="game";

    private Game game;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        assert getArguments() != null;
        if (getArguments().containsKey(ARG_GAME)) {
            game = (Game) getArguments().getSerializable(ARG_GAME);
            assert game != null;

            Activity activity = this.getActivity();
            assert activity != null;
            final Toolbar toolbar = activity.findViewById(R.id.toolbar);

            if (toolbar != null) {
                toolbar.setTitle(game.getName());
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.item_detail, container, false);

        if (game != null) {

            ImageView imageView = rootView.findViewById(R.id.imageView);
            Glide.with(this).load(game.getBackgroundImage()).into(imageView);

            ((TextView) rootView.findViewById(R.id.game_name)).setText(game.getName());
            ((TextView) rootView.findViewById(R.id.game_description)).setText(game.getDescription());
            ((TextView) rootView.findViewById(R.id.game_release_date)).setText(game.getReleased());
            ((MaterialRatingBar) rootView.findViewById(R.id.game_rating_bar)).setRating(game.getRating().floatValue());

        }

        return rootView;
    }
}
