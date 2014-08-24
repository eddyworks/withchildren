package co.edkim.withchildren;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import co.edkim.withchildren.helper.NetHelper;
import co.edkim.withchildren.model.Park;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SangsangParkListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SangsangParkListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SangsangParkListFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SangsangParkListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SangsangParkListFragment newInstance(String param1, String param2) {
        SangsangParkListFragment fragment = new SangsangParkListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public SangsangParkListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //7a565a674e6c737738336f7072776b 서울시 발급 키
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_sangsang_park_list, container, false);

        String data = NetHelper.SendRESTRequest(getActivity(), "http://openapi.seoul.go.kr:8088/7a565a674e6c737738336f7072776b/json/ListDreamParksService/1/400/"); //넉넉하게 400번까지
        try {
            JSONObject oPack = new JSONObject(data);
            //{"ListDreamParksService":{"list_total_count":304,"RESULT":{"CODE":"INFO-000","MESSAGE":"정상 처리되었습니다"},"row":[{"P_PARK":"연지 상상어린이공원","P_LIST_CONTENT":"
            JSONArray list = oPack.getJSONObject("ListDreamParksService").getJSONArray("row");

            for (int i = 0; i < list.length(); i++) {
                JSONObject p = list.getJSONObject(i);
                String addr = p.getString("P_ADDR");
                String gu = addr.split(" ")[2];
                String dong = addr.split(" ")[3];
                String keyName = "(" + dong + ") " + p.getString("P_PARK");
                if (districts.contains(gu)) {
                    ArrayList<String> parkByGu = parks.get(districts.indexOf(gu));
                    if (parkByGu == null)
                        parkByGu = new ArrayList<String>();

                    parkByGu.add(keyName);
                } else {
                    districts.add(gu);
                    ArrayList<String> parkByGu = new ArrayList<String>();
                    parkByGu.add(keyName);
                    parks.add(parkByGu);
                }

                parkSet.put(keyName, new Park(keyName, p.getString("P_PARK"), addr, p.getString("P_LIST_CONTENT")));
            }

            Collections.sort(districts);
            for (int i = 0; i < parks.size(); i++) {
                Collections.sort(parks.get(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        ExpandableListView elv = (ExpandableListView) v.findViewById(R.id.listPark);
        elv.setAdapter(new ParkListAdapter());
        return v;
    }

    private static ArrayList<String> districts = new ArrayList<String>();
    private static ArrayList<ArrayList<String>> parks = new ArrayList<ArrayList<String>>();
    public static HashMap<String, Park> parkSet = new HashMap<String, Park>();

    //https://gist.github.com/bowmanb/4052030 bowmanb 씨의 공개 코드 참고.
    public class ParkListAdapter extends BaseExpandableListAdapter {
        private String[] groups = districts.toArray(new String[districts.size()]);// {"People Names", "Dog Names", "Cat Names", "Fish Names"}
        private String[][] children = new String[districts.size()][];

        /*private String[][] children = {
                {"Arnold", "Barry", "Chuck", "David"},
                {"Ace", "Bandit", "Cha-Cha", "Deuce"},
                {"Fluffy", "Snuggles"},
                {"Goldy", "Bubbles"}
        };*/
        public ParkListAdapter() {
            for (int i = 0; i < children.length; i++) {
                children[i] = parks.get(i).toArray(new String[parks.get(i).size()]);
            }
        }

        @Override
        public int getGroupCount() {
            return groups.length;
        }

        @Override
        public int getChildrenCount(int i) {
            return children[i].length;
        }

        @Override
        public Object getGroup(int i) {
            return groups[i];
        }

        @Override
        public Object getChild(int i, int i1) {
            return children[i][i1];
        }

        @Override
        public long getGroupId(int i) {
            return i;
        }

        @Override
        public long getChildId(int i, int i1) {
            return i1;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
            TextView textView = new TextView(SangsangParkListFragment.this.getActivity());
            textView.setText(getGroup(i).toString());
            textView.setTextSize(14);
            textView.setPadding(70, 20, 0, 20);
            return textView;
        }

        @Override
        public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
            final TextView textView = new TextView(SangsangParkListFragment.this.getActivity());
            textView.setText(getChild(i, i1).toString());
            textView.setTextSize(14);
            textView.setPadding(100, 20, 0, 20);
            //http://maps.googleapis.com/maps/api/geocode/json?address=%EC%A2%85%EB%A1%9C%EA%B5%AC%20%EC%97%B0%EC%A7%80%EB%8F%99%20136-92

            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder alertDlg = new AlertDialog.Builder(
                            getActivity());
                    alertDlg.setTitle("공원 상세 정보");
                    String content = Jsoup.parse(parkSet.get(textView.getText()).content).text();
                    alertDlg.setMessage(content);
                    alertDlg.setPositiveButton("닫기",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int whichButton) {

                                }
                            });
                    alertDlg.show();
                }
            });

            return textView;
        }

        @Override
        public boolean isChildSelectable(int i, int i1) {
            return true;
        }

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
            //mListener = (OnFragmentInteractionListener) activity;
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
