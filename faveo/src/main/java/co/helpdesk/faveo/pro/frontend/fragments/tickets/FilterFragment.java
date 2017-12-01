//package co.helpdesk.faveo.pro.frontend.fragments.tickets;
//
//
//import android.app.ProgressDialog;
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.Color;
//import android.net.Uri;
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentTransaction;
//import android.support.v4.widget.SwipeRefreshLayout;
//import android.support.v7.widget.Toolbar;
//import android.view.LayoutInflater;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
//import com.pixplicity.easyprefs.library.Prefs;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import co.helpdesk.faveo.pro.R;
//import co.helpdesk.faveo.pro.frontend.activities.MainActivity;
//import co.helpdesk.faveo.pro.frontend.activities.TicketFilter;
//import co.helpdesk.faveo.pro.frontend.adapters.TicketOverviewAdapter;
//import co.helpdesk.faveo.pro.frontend.receivers.InternetReceiver;
//import co.helpdesk.faveo.pro.model.TicketOverview;
//import es.dmoral.toasty.Toasty;
//
///**
// * A simple {@link Fragment} subclass.
// */
//public class FilterFragment extends Fragment {
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//    String show, dept;
//    @BindView(R.id.cardList)
//    ShimmerRecyclerView recyclerView;
//    ProgressDialog progressDialog;
//    String title;
//    @BindView(R.id.empty_view)
//    TextView empty_view;
//    @BindView(R.id.noiternet_view)
//    TextView noInternet_view;
//    int currentPage = 1;
//    static String nextPageURL = "";
//    View rootView;
//    @BindView(R.id.swipeRefresh)
//    SwipeRefreshLayout swipeRefresh;
//    @BindView(R.id.totalcount)
//    TextView textView;
//    TicketOverviewAdapter ticketOverviewAdapter;
//    List<TicketOverview> ticketOverviewList = new ArrayList<>();
//    int pageno;
//    private boolean loading = true;
//    int total;
//    int assigned = 1;
//    int pastVisibleItems, visibleItemCount, totalItemCount;
//
//    public String mParam1;
//    public String mParam2;
//    String ticket;
//
//    private OnFragmentInteractionListener mListener;
//
//    public static FilterFragment newInstance(String param1, String param2) {
//        FilterFragment fragment = new FilterFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    public FilterFragment() {
//        // Required empty public constructor
//    }
//
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setHasOptionsMenu(true);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
//        JSONObject jsonObject;
//        String json = Prefs.getString("DEPENDENCY", "");
//        try {
//            jsonObject = new JSONObject(json);
//            JSONArray jsonArrayStaffs = jsonObject.getJSONArray("status");
//            for (int i = 0; i < jsonArrayStaffs.length(); i++) {
//                if (jsonArrayStaffs.getJSONObject(i).getString("name").equals("Open")) {
//                    Prefs.putString("openid", jsonArrayStaffs.getJSONObject(i).getString("id"));
//                } else if (jsonArrayStaffs.getJSONObject(i).getString("name").equals("Resolved")) {
//                    Prefs.putString("resolvedid", jsonArrayStaffs.getJSONObject(i).getString("id"));
//                } else if (jsonArrayStaffs.getJSONObject(i).getString("name").equals("Closed")) {
//                    Prefs.putString("closedid", jsonArrayStaffs.getJSONObject(i).getString("id"));
//                } else if (jsonArrayStaffs.getJSONObject(i).getString("name").equals("Deleted")) {
//                    Prefs.putString("deletedid", jsonArrayStaffs.getJSONObject(i).getString("id"));
//                } else if (jsonArrayStaffs.getJSONObject(i).getString("name").equals("Archived")) {
//                    Prefs.putString("archivedid", jsonArrayStaffs.getJSONObject(i).getString("id"));
//                } else if (jsonArrayStaffs.getJSONObject(i).getString("name").equals("Verified")) {
//                    Prefs.putString("verifiedid", jsonArrayStaffs.getJSONObject(i).getString("id"));
//                }
//
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        if (rootView == null) {
//            rootView = inflater.inflate(R.layout.fragment_recycler, container, false);
//            ButterKnife.bind(this, rootView);
//            progressDialog = new ProgressDialog(getActivity());
//            progressDialog.setMessage("Please wait");
////            Toolbar toolbar1= (Toolbar) rootView.findViewById(R.id.toolbar3);
////            toolbar1.setVisibility(View.VISIBLE);
////            toolbar1.setOverflowIcon(getResources().getDrawable(R.drawable.ic_filter_list_black_24dp));
////            toolbar1.setTitle(getString(R.string.filter));
////            toolbar1.setTitleTextColor(Color.parseColor("#3da6d7"));
////
////            toolbar1.inflateMenu(R.menu.menu_for_filtering);
////            toolbar1.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
////                @Override
////                public boolean onMenuItemClick(MenuItem item) {
////
////                    return false;
////                }
////            });
//            Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar2);
//            TextView textView = (TextView) toolbar.findViewById(R.id.toolbartextview);
////        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
//            textView.setText(getString(R.string.sortbytitle));
////        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
//            toolbar.setVisibility(View.VISIBLE);
//            Toolbar toolbar1= (Toolbar) rootView.findViewById(R.id.toolbarfilteration);
//            toolbar1.setVisibility(View.VISIBLE);
//            toolbar1.setOverflowIcon(getResources().getDrawable(R.drawable.ic_if_filter_383135));
//            //toolbar1.setOverflowIcon(getResources().getDrawable(R.drawable.ic_if_filter_383135));
//
////            toolbar1.inflateMenu(R.menu.menu_for_filtering);
////            toolbar1.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
////                @Override
////                public boolean onMenuItemClick(MenuItem item) {
////
////                    return false;
////                }
////            });
//
//            toolbar1.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    //Toast.makeText(getActivity(), "clicked on toolbar", Toast.LENGTH_SHORT).show();
//                    Intent intent=new Intent(getActivity(), TicketFilter.class);
//                    startActivity(intent);
//
//                }
//            });
//
//            //toolbar.setTitle(getString(R.string.sortbytitle));
//            toolbar.setOverflowIcon(getResources().getDrawable(R.drawable.ic_sort_black_24dp));
//            toolbar.setTitleTextColor(Color.parseColor("#3da6d7"));
////        mTitle.setText("Sort By");
//            toolbar.inflateMenu(R.menu.menu_for_sorting);
//            toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
//                @Override
//                public boolean onMenuItemClick(MenuItem item) {
//                    try {
//                        if (item != null) {
//                            item.getSubMenu().clearHeader();
//                        }
//                    } catch (NullPointerException e) {
//                        e.printStackTrace();
//                    }
//                    Fragment fragment = null;
//                    title = getString(R.string.app_name);
//                    if (item.getItemId() == R.id.due_ascending) {
//
//                        //Toast.makeText(getActivity(), "due in ascending", Toast.LENGTH_SHORT).show();
//                        title = getString(R.string.duebyasc);
//                        fragment = getActivity().getSupportFragmentManager().findFragmentByTag(title);
//                        if (fragment == null)
//                            fragment = new DueByAsc();
//                        if (fragment != null) {
//                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                            fragmentTransaction.replace(R.id.container_body, fragment);
//                            // fragmentTransaction.addToBackStack(null);
//                            fragmentTransaction.commit();
//                        }
//                        return true;
//                    }
//                    if (item.getItemId() == R.id.due_descending) {
//
//                        title = getString(R.string.duebydesc);
//                        fragment = getActivity().getSupportFragmentManager().findFragmentByTag(title);
//                        if (fragment == null)
//                            fragment = new DueByDesc();
//                        if (fragment != null) {
//                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                            fragmentTransaction.replace(R.id.container_body, fragment);
//                            // fragmentTransaction.addToBackStack(null);
//                            fragmentTransaction.commit();
//                        }
//                        return true;
//                    }
//                    if (item.getItemId() == R.id.created_ascending) {
//                        title = getString(R.string.createdat);
//                        fragment = getActivity().getSupportFragmentManager().findFragmentByTag(title);
//                        if (fragment == null)
//                            fragment = new CreatedAtAsc();
//                        if (fragment != null) {
//                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                            fragmentTransaction.replace(R.id.container_body, fragment);
//                            // fragmentTransaction.addToBackStack(null);
//                            fragmentTransaction.commit();
//                        }
//                        return true;
//                    }
//                    if (item.getItemId() == R.id.created_descending) {
//                        title = getString(R.string.createdat);
//                        fragment = getActivity().getSupportFragmentManager().findFragmentByTag(title);
//                        if (fragment == null)
//                            fragment = new CreatedAtDesc();
//                        if (fragment != null) {
//                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                            fragmentTransaction.replace(R.id.container_body, fragment);
//                            // fragmentTransaction.addToBackStack(null);
//                            fragmentTransaction.commit();
//                        }
//                        return true;
//                    }
//                    if (item.getItemId() == R.id.ticketnumber_ascending) {
//                        title = getString(R.string.sortbyticketnoasc);
//                        fragment = getActivity().getSupportFragmentManager().findFragmentByTag(title);
//                        if (fragment == null)
//                            fragment = new SortByTicketNumberAscending();
//                        if (fragment != null) {
//                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                            fragmentTransaction.replace(R.id.container_body, fragment);
//                            // fragmentTransaction.addToBackStack(null);
//                            fragmentTransaction.commit();
//                        }
//                        return true;
//                    }
//                    if (item.getItemId() == R.id.ticketnumber_descending) {
//                        title = getString(R.string.sortbyticketnodesc);
//                        fragment = getActivity().getSupportFragmentManager().findFragmentByTag(title);
//                        if (fragment == null)
//                            fragment = new SortByTicketNumberDescending();
//                        if (fragment != null) {
//                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                            fragmentTransaction.replace(R.id.container_body, fragment);
//                            // fragmentTransaction.addToBackStack(null);
//                            fragmentTransaction.commit();
//                        }
//                        return true;
//                    }
//                    if (item.getItemId() == R.id.tickettitle_ascending) {
//                        title = getString(R.string.sortbyasc);
//                        fragment = getActivity().getSupportFragmentManager().findFragmentByTag(title);
//                        if (fragment == null)
//                            fragment = new SortByTicketTitleAscending();
//                        if (fragment != null) {
//                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                            fragmentTransaction.replace(R.id.container_body, fragment);
//                            // fragmentTransaction.addToBackStack(null);
//                            fragmentTransaction.commit();
//                        }
//                        return true;
//                    }
//                    if (item.getItemId() == R.id.tickettitle_descending) {
//                        title = getString(R.string.sortbydesc);
//                        fragment = getActivity().getSupportFragmentManager().findFragmentByTag(title);
//                        if (fragment == null)
//                            fragment = new SortByTicketTitleDescending();
//                        if (fragment != null) {
//                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                            fragmentTransaction.replace(R.id.container_body, fragment);
//                            // fragmentTransaction.addToBackStack(null);
//                            fragmentTransaction.commit();
//                        }
//                        return true;
//                    }
//                    if (item.getItemId() == R.id.priorityasc) {
//                        title = getString(R.string.sortbypriorityasc);
//                        fragment = getActivity().getSupportFragmentManager().findFragmentByTag(title);
//                        if (fragment == null)
//                            fragment = new SortByTicketPriorityAsc();
//                        if (fragment != null) {
//                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                            fragmentTransaction.replace(R.id.container_body, fragment);
//                            // fragmentTransaction.addToBackStack(null);
//                            fragmentTransaction.commit();
//                        }
//                        return true;
//                    }
//                    if (item.getItemId() == R.id.prioritydesc) {
//                        title = getString(R.string.sortbyprioritydesc);
//                        fragment = getActivity().getSupportFragmentManager().findFragmentByTag(title);
//                        if (fragment == null)
//                            fragment = new SortByTicketPriorityDesc();
//                        if (fragment != null) {
//                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                            fragmentTransaction.replace(R.id.container_body, fragment);
//                            // fragmentTransaction.addToBackStack(null);
//                            fragmentTransaction.commit();
//                        }
//                        return true;
//                    }
//                    if (item.getItemId() == R.id.updatedatasc) {
//                        title = getString(R.string.updatedat);
//                        fragment = getActivity().getSupportFragmentManager().findFragmentByTag(title);
//                        if (fragment == null)
//                            fragment = new UpdatedAtAsc();
//                        if (fragment != null) {
//                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                            fragmentTransaction.replace(R.id.container_body, fragment);
//                            // fragmentTransaction.addToBackStack(null);
//                            fragmentTransaction.commit();
//                        }
//                        return true;
//                    }
//                    if (item.getItemId() == R.id.updatedatdesc) {
//                        title = getString(R.string.updatedat);
//                        fragment = getActivity().getSupportFragmentManager().findFragmentByTag(title);
//                        if (fragment == null)
//                            fragment = new UpdatedAtDesc();
//                        if (fragment != null) {
//                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                            fragmentTransaction.replace(R.id.container_body, fragment);
//                            // fragmentTransaction.addToBackStack(null);
//                            fragmentTransaction.commit();
//                        }
//                        return true;
//                    }
//
//
//                    return false;
//                }
//            });
//
//
////        ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.inbox));
//
//
//
//        }
//        return rootView;
//    }
//    public interface OnFragmentInteractionListener {
//        void onFragmentInteraction(Uri uri);
//    }
//    /**
//     * When the fragment is going to be attached
//     * this life cycle method is going to be called.
//     * @param context refers to the current fragment.
//     */
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }
//
//    /**
//     * Once the fragment is going to be detached then
//     * this method is going to be called.
//     */
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//        nextPageURL = "";
//    }
//}
//
//
