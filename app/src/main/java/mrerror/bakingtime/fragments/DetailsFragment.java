package mrerror.bakingtime.fragments;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.danikula.videocache.HttpProxyCacheServer;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import mrerror.bakingtime.App;
import mrerror.bakingtime.R;
import mrerror.bakingtime.databinding.FragmentDetailsBinding;
import mrerror.bakingtime.models.Step;

public class DetailsFragment extends Fragment implements ExoPlayer.EventListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private SimpleExoPlayer mExoPlayer;
    private Step mStep;
    private int stepsNum;
    private long savedPosition;
    private OnFragmentInteractionListener mListener;
    private FragmentDetailsBinding binding;

    public DetailsFragment() {
        // Required empty public constructor
    }


    public static DetailsFragment newInstance(Step param1, int param2) {
        DetailsFragment fragment = new DetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM1, param1);
        args.putInt(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mStep = getArguments().getParcelable(ARG_PARAM1);
            stepsNum = getArguments().getInt(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_details, container, false);
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("seek")) {
                savedPosition = savedInstanceState.getLong("seek");
            }
        }
        View view = binding.getRoot();
        binding.playerView.requestFocus();
        binding.playerView.setControllerHideOnTouch(false);
        Log.e("hi", mStep.getShortDescription());
        if (mStep.getVideoURL() != null && mStep.getVideoURL().length() > 0) {
            HttpProxyCacheServer proxy = App.getProxy(getActivity());
            String proxyUrl = proxy.getProxyUrl(mStep.getVideoURL());
            initializePlayer(Uri.parse(proxyUrl));
        } else {
            try {
                binding.videoFrame.setVisibility(View.GONE);
                binding.thumbnailImg.setVisibility(View.VISIBLE);
            } catch (Exception e) {
                e.printStackTrace();
            }
            setImage();
        }
        if (binding.landFrame != null) {
            // Landscape mode
            // Hide Actionbar
            hideActionBar();
            //get height of the screen
            Point size = new Point();
            getActivity().getWindowManager().getDefaultDisplay().getSize(size);
            int screenHeight = size.y;
            ViewGroup.LayoutParams params = binding.layout1.getLayoutParams();
            params.height = screenHeight - dpToPx(24);
        }
        setNextAndPref();
        binding.description.setText(mStep.getDescription());


        // Next button listener
        binding.nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonPressed(mStep.getId() + 1);
            }
        });
        // Previous button listener
        binding.prevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonPressed(mStep.getId() - 1);
            }
        });

        return view;
    }

    private void setNextAndPref() {

        if (mStep.getId() == 0) {
            binding.prevBtn.setEnabled(false);
        } else {
            binding.prevBtn.setEnabled(true);
        }
        if (mStep.getId() == (stepsNum - 1)) {
            binding.nextBtn.setEnabled(false);
        } else {
            binding.nextBtn.setEnabled(true);
        }


    }

    // convert dp to px
    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    // Hide ActionBar method
    public void hideActionBar() {
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
    }

    // Initialize the exoPlayer
    private void initializePlayer(Uri mediaUri) {
        if (mExoPlayer == null) {
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
            binding.playerView.setPlayer(mExoPlayer);
            mExoPlayer.addListener(this);
            TextView title = (TextView) binding.playerView.findViewById(R.id.video_title);
            if (title != null) {
                title.setText(mStep.getShortDescription());
            }
            String userAgent = Util.getUserAgent(getContext(), "Let's bake");
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    getContext(), userAgent), new DefaultExtractorsFactory(), null, null);
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(true);
            mExoPlayer.seekTo(savedPosition);
        }
    }

    // Release exoPlayer
    private void releasePlayer() {
        // Genymotion has a problem with ExoPlayer issue #951
        if (mExoPlayer != null) {
            Log.e("destroyed", "this fragment has been destroyed");
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mExoPlayer != null) {
            mExoPlayer.setPlayWhenReady(false);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mExoPlayer != null) {
            mExoPlayer.setPlayWhenReady(true);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }

    //    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(int index) {
        if (mListener != null) {
            mListener.onFragmentInteraction(index);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
//        mListener = null;
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
        // ON ERROR THEN TRY TO LOAD IMAGE
        try {
            binding.videoFrame.setVisibility(View.GONE);
            binding.thumbnailImg.setVisibility(View.VISIBLE);
            releasePlayer();
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
        setImage();
    }

    // in case of No video or error on playback
    private void setImage() {
        if (mStep.getThumbnailURL().length() > 0 && mStep.getThumbnailURL() != null) {
            Picasso.with(getContext()).load(mStep.getThumbnailURL())
                    .error(getContext().getResources().getDrawable(R.drawable.no_img))
                    .into(binding.thumbnailImg);
        } else {
            Toast.makeText(getContext(), "No media provided!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPositionDiscontinuity() {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong("seek", mExoPlayer.getCurrentPosition());
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(int index);
    }
}
