package co.edkim.withchildren;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
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
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

import co.edkim.withchildren.helper.AdHelper;
import co.edkim.withchildren.model.Park;

/**
 * A fragment with a Google +1 button.
 * Activities that contain this fragment must implement the
 * {@link co.edkim.withchildren.ToyLibDetailsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link co.edkim.withchildren.ToyLibDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ToyLibDetailsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private String mParam1;

    private OnFragmentInteractionListener mListener;

    private AdView adView;
    MapFragment map;
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
    public static ToyLibDetailsFragment newInstance(String param1) {
        ToyLibDetailsFragment fragment = new ToyLibDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    public ToyLibDetailsFragment() {
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
        View view = inflater.inflate(R.layout.fragment_toy_lib_details, container, false);

        adView = (AdView) view.findViewById(R.id.adView);
        AdHelper.setAdmobAd(adView);

        map = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        GoogleMap iMap = map.getMap();

        Park p = ToyLibListFragment.libSet.get(mParam1);

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
                Toast.makeText(getActivity(), "평가 기능을 준비 중입니다.", Toast.LENGTH_SHORT).show();
            }
        });

        TextView textViewContent = (TextView) view.findViewById(R.id.textViewContent);
        textViewContent.setText(p.content);
        textViewContent.setLineSpacing(9, 1);
        textViewContent.setTextIsSelectable(true);
        textViewContent.setCursorVisible(false);

        Geocoder geocoder = new Geocoder(getActivity());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocationName(p.address, 5);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (addresses != null && addresses.size() > 0) {
            LatLng sLatLng = new LatLng(addresses.get(0).getLatitude(), addresses.get(0).getLongitude());

            iMap.addMarker(new MarkerOptions()
                    .position(sLatLng)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_action_park_pin)).title(p.name));

            CameraUpdate center =
                    CameraUpdateFactory.newLatLng(sLatLng);
            CameraUpdate zoom = CameraUpdateFactory.zoomTo(14);

            iMap.moveCamera(center);
            iMap.animateCamera(zoom);
        }

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

    @Override
    public void onPause() {
        super.onPause();
        FragmentManager fm = getFragmentManager();
        Fragment map = fm.findFragmentById(R.id.map);
        FragmentTransaction t = fm.beginTransaction();
        if (map != null) {
            t.remove(map);
            t.commit();
        }
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
