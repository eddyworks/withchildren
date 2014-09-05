package co.edkim.withchildren;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.plus.PlusOneButton;

import co.edkim.withchildren.model.Park;

/**
 * A fragment with a Google +1 button.
 * Activities that contain this fragment must implement the
 * {@link SangsangParkDetailsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SangsangParkDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SangsangParkDetailsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private String mParam1;

    private OnFragmentInteractionListener mListener;

    private AdView adView;
    private GoogleMap map;
    /* Your ad unit id. Replace with your actual ad unit id. */
    private static final String AD_UNIT_ID = "ca-app-pub-6787467391542523/1310511092";

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment SangsangParkDetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SangsangParkDetailsFragment newInstance(String param1) {
        SangsangParkDetailsFragment fragment = new SangsangParkDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    public SangsangParkDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sangsang_park_details, container, false);

        adView = (AdView) view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("7DDD06BDE922F9125E7B97721D387C5C").build();

        // Start loading the ad in the background.
        adView.loadAd(adRequest);

        Park p = SangsangParkListFragment.parkSet.get(mParam1);

        TextView textViewName = (TextView) view.findViewById(R.id.textViewName);
        textViewName.setText(p.name);

        /*TextView textViewAddress = (TextView) view.findViewById(R.id.textViewAddress);
        textViewAddress.setText(p.address);*/

        RatingBar ratingBar = (RatingBar) view.findViewById(R.id.ratingBar);
        ratingBar.setIsIndicator(true);
        //ratingBar.setNumStars();
        ratingBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "평가 기능을 준비 중입니다.", Toast.LENGTH_SHORT);
            }
        });

        TextView textViewContent = (TextView) view.findViewById(R.id.textViewContent);
        textViewContent.setText(p.content);
        textViewContent.setLineSpacing(9, 1);

        WebView webView = (WebView) view.findViewById(R.id.webView);
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("https://maps.google.co.kr/maps?q=" + p.address + "&output=classic&dg=ntvb");

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
