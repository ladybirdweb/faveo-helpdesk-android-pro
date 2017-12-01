package co.helpdesk.faveo.pro.frontend.fragments.tickets;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.muddzdev.styleabletoastlibrary.StyleableToast;
import com.pixplicity.easyprefs.library.Prefs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.helpdesk.faveo.pro.Helper;
import co.helpdesk.faveo.pro.R;
import co.helpdesk.faveo.pro.backend.api.v1.Helpdesk;
import co.helpdesk.faveo.pro.frontend.activities.MainActivity;
import co.helpdesk.faveo.pro.frontend.activities.NotificationActivity;
import co.helpdesk.faveo.pro.frontend.activities.TicketDetailActivity;
import co.helpdesk.faveo.pro.frontend.activities.TicketFilter;
import co.helpdesk.faveo.pro.frontend.adapters.RecyclerItemClickListener;
import co.helpdesk.faveo.pro.frontend.adapters.TicketOverviewAdapter;
import co.helpdesk.faveo.pro.frontend.receivers.InternetReceiver;
import co.helpdesk.faveo.pro.model.TicketOverview;
import es.dmoral.toasty.Toasty;

import static com.facebook.FacebookSdk.getApplicationContext;

public class InboxTickets extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    Toolbar toolbarmain;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;

    @BindView(R.id.cardList)
    ShimmerRecyclerView recyclerView;

    @BindView(R.id.empty_view)
    TextView empty_view;

    @BindView(R.id.noiternet_view)
    TextView noInternet_view;
    @BindView(R.id.totalcount)
    TextView textView;

    private boolean isMultiSelect = false;
    ArrayList<Integer> checked_items = new ArrayList<>();
    private ActionMode actionMode;
    static String nextPageURL = "";
    TicketOverview ticketOverview;
    View rootView;
    int total;
    Menu menu;
    String ticket;
    String title, url;
    int unassigned;
    int pageno = 1;
    int pageNo = 1;
    int page = 1;
    ProgressDialog progressDialog;
    String check, show, department, source, priority, tickettype, assigned, assignto;
    Toolbar toolbar;
    TicketOverviewAdapter ticketOverviewAdapter;
    List<TicketOverview> ticketOverviewList = new ArrayList<>();
    Toolbar toolbarMain;
    private boolean loading = true;
    String filterwithsorting;
    int pastVisibleItems, visibleItemCount, totalItemCount;
    Activity context;
    public String mParam1;
    public String mParam2;

    private OnFragmentInteractionListener mListener;

    public static InboxTickets newInstance(String param1, String param2) {
        InboxTickets fragment = new InboxTickets();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public InboxTickets() {
    }

    /**
     * @param savedInstanceState under special circumstances, to restore themselves to a previous
     *                           state using the data stored in this bundle.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        try {
            show = Prefs.getString("show", null);
            assigned = Prefs.getString("assigned", null);
            source = Prefs.getString("sourcefinal", null);
            department = Prefs.getString("departmentfinal", null);
            priority = Prefs.getString("priorityfinal", null);
            tickettype = Prefs.getString("typefinal", null);
            assignto = Prefs.getString("assignedtofinal", null);

            if (assigned.equals("yes")) {
                unassigned = 1;

            } else if (assigned.equals("no")) {
                unassigned = 0;
                //Toast.makeText(getActivity(), "unassigned", Toast.LENGTH_SHORT).show();

            } else if (assigned.equals("null")) {
                unassigned = 2;
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        Prefs.putString("newuseremail", "");
        JSONObject jsonObject;
        String json = Prefs.getString("DEPENDENCY", "");
        try {
            jsonObject = new JSONObject(json);
            JSONArray jsonArrayStaffs = jsonObject.getJSONArray("status");
            for (int i = 0; i < jsonArrayStaffs.length(); i++) {
                if (jsonArrayStaffs.getJSONObject(i).getString("name").equals("Open")) {
                    Prefs.putString("openid", jsonArrayStaffs.getJSONObject(i).getString("id"));
                } else if (jsonArrayStaffs.getJSONObject(i).getString("name").equals("Resolved")) {
                    Prefs.putString("resolvedid", jsonArrayStaffs.getJSONObject(i).getString("id"));
                } else if (jsonArrayStaffs.getJSONObject(i).getString("name").equals("Closed")) {
                    Prefs.putString("closedid", jsonArrayStaffs.getJSONObject(i).getString("id"));
                } else if (jsonArrayStaffs.getJSONObject(i).getString("name").equals("Deleted")) {
                    Prefs.putString("deletedid", jsonArrayStaffs.getJSONObject(i).getString("id"));
                } else if (jsonArrayStaffs.getJSONObject(i).getString("name").equals("Archived")) {
                    Prefs.putString("archivedid", jsonArrayStaffs.getJSONObject(i).getString("id"));
                } else if (jsonArrayStaffs.getJSONObject(i).getString("name").equals("Verified")) {
                    Prefs.putString("verifiedid", jsonArrayStaffs.getJSONObject(i).getString("id"));
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param inflater           for loading the fragment.
     * @param container          where the fragment is going to be load.
     * @param savedInstanceState
     * @return after initializing returning the rootview
     * which is having the fragment.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView == null) {
             toolbarmain = (Toolbar) getActivity().findViewById(R.id.toolbar);
            //toolbarmain.setVisibility(View.GONE);
            rootView = inflater.inflate(R.layout.fragment_recycler, container, false);
            ButterKnife.bind(this, rootView);
            Prefs.putString("source", "5");
//            Drawable drawable = ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_sort_black_24dp);

            toolbar = (Toolbar) rootView.findViewById(R.id.toolbar2);
            Toolbar toolbar1 = (Toolbar) rootView.findViewById(R.id.toolbarfilteration);
            toolbar1.setVisibility(View.VISIBLE);
            toolbar1.setOverflowIcon(getResources().getDrawable(R.drawable.ic_if_filter_383135));

            toolbar1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Toast.makeText(getActivity(), "clicked on toolbar", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(), TicketFilter.class);
                    startActivity(intent);

                }
            });

//            toolbar.setOverflowIcon(drawable);
//        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);

//
            Prefs.getString("show", null);
            Prefs.getString("assigned", null);
            /**
             * For changing the MainActivity title according to the scenario.
             */
            try {
                if (show.equalsIgnoreCase("inbox") && unassigned == 0) {

                    ((MainActivity) getActivity()).setActionBarTitle("Unassigned");

                } else if (show.equalsIgnoreCase("inbox") && unassigned == 2) {
                    ((MainActivity) getActivity()).setActionBarTitle("Inbox");
                } else if (show.equalsIgnoreCase("inbox") && unassigned == 0) {
                    ((MainActivity) getActivity()).setActionBarTitle("Inbox");
                } else if (show.equalsIgnoreCase("mytickets")) {
                    ((MainActivity) getActivity()).setActionBarTitle("My Tickets");
                } else if (show.equalsIgnoreCase("closed")) {
                    ((MainActivity) getActivity()).setActionBarTitle("Closed Tickets");
                } else if (show.equalsIgnoreCase("trash")) {
                    ((MainActivity) getActivity()).setActionBarTitle("Trash");
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }


            toolbar.setVisibility(View.VISIBLE);
            toolbar.setOverflowIcon(getResources().getDrawable(R.drawable.ic_sort_black_24dp));
            toolbar.setTitle(getString(R.string.sortbytitle));
            toolbar.setTitleTextColor(Color.parseColor("#3da6d7"));
//        mTitle.setText("Sort By");
            toolbar.inflateMenu(R.menu.menu_for_sorting);
            toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    try {
                        if (item != null) {
                            item.getSubMenu().clearHeader();
                        }
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    Fragment fragment = null;
                    title = getString(R.string.app_name);
                    if (item.getItemId() == R.id.due_ascending) {
                        Prefs.putString("dueasc", "1");

                        //Toast.makeText(getActivity(), "due in ascending", Toast.LENGTH_SHORT).show();
                        title = getString(R.string.duebyasc);
                        fragment = getActivity().getSupportFragmentManager().findFragmentByTag(title);
                        if (fragment == null)
                            fragment = new DueByAsc();
                        if (fragment != null) {
                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.container_body, fragment);
                            // fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                        }
                        return true;
                    }
                    if (item.getItemId() == R.id.due_descending) {
                        Prefs.putString("dueasc", "2");
                        title = getString(R.string.duebydesc);
                        fragment = getActivity().getSupportFragmentManager().findFragmentByTag(title);
                        if (fragment == null)
                            fragment = new DueByDesc();
                        if (fragment != null) {
                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.container_body, fragment);
                            // fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                        }
                        return true;
                    }
                    if (item.getItemId() == R.id.created_ascending) {
                        title = getString(R.string.createdat);
                        fragment = getActivity().getSupportFragmentManager().findFragmentByTag(title);
                        if (fragment == null)
                            fragment = new CreatedAtAsc();
                        if (fragment != null) {
                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.container_body, fragment);
                            // fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                        }
                        return true;
                    }
                    if (item.getItemId() == R.id.created_descending) {
                        title = getString(R.string.createdat);
                        fragment = getActivity().getSupportFragmentManager().findFragmentByTag(title);
                        if (fragment == null)
                            fragment = new CreatedAtDesc();
                        if (fragment != null) {
                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.container_body, fragment);
                            // fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                        }
                        return true;
                    }
                    if (item.getItemId() == R.id.ticketnumber_ascending) {


                        title = getString(R.string.sortbyticketnoasc);
                        fragment = getActivity().getSupportFragmentManager().findFragmentByTag(title);
                        if (fragment == null)
                            fragment = new SortByTicketNumberAscending();
                        if (fragment != null) {
                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.container_body, fragment);
                            // fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();

                        }
                    }
                    if (item.getItemId() == R.id.ticketnumber_descending) {
                        title = getString(R.string.sortbyticketnodesc);
                        fragment = getActivity().getSupportFragmentManager().findFragmentByTag(title);
                        if (fragment == null)
                            fragment = new SortByTicketNumberDescending();
                        if (fragment != null) {
                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.container_body, fragment);
                            // fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                        }
                    }
                    if (item.getItemId() == R.id.priorityasc) {
                        title = getString(R.string.sortbypriorityasc);
                        fragment = getActivity().getSupportFragmentManager().findFragmentByTag(title);
                        if (fragment == null)
                            fragment = new SortByTicketPriorityAsc();
                        if (fragment != null) {
                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.container_body, fragment);
                            // fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                        }
                        return true;
                    }
                    if (item.getItemId() == R.id.prioritydesc) {
                        title = getString(R.string.sortbyprioritydesc);
                        fragment = getActivity().getSupportFragmentManager().findFragmentByTag(title);
                        if (fragment == null)
                            fragment = new SortByTicketPriorityDesc();
                        if (fragment != null) {
                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.container_body, fragment);
                            // fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                        }
                        return true;
                    }
                    if (item.getItemId() == R.id.updatedatasc) {
                        title = getString(R.string.updatedat);
                        fragment = getActivity().getSupportFragmentManager().findFragmentByTag(title);
                        if (fragment == null)
                            fragment = new UpdatedAtAsc();
                        if (fragment != null) {
                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.container_body, fragment);
                            // fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                        }
                        return true;
                    }
                    if (item.getItemId() == R.id.updatedatdesc) {


                        title = getString(R.string.updatedat);
                        fragment = getActivity().getSupportFragmentManager().findFragmentByTag(title);
                        if (fragment == null)
                            fragment = new UpdatedAtDesc();
                        if (fragment != null) {
                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.container_body, fragment);
                            // fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                        }
                        return true;
                    }


                    return true;
                }
            });
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Please wait");
            swipeRefresh.setColorSchemeResources(R.color.faveo_blue);
            try {
                check = Prefs.getString("came from filter", null);
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
//show,department,source,priority,tickettype,assigned,assignto
//            swipeRefresh.setRefreshing(true);
//            new FetchFirst(getActivity()).execute();
            if (InternetReceiver.isConnected()) {
                if (check.equals("true")) {
                    Prefs.putString("source", "6");
                    if (department.equals("all")) {
                        if (source.equals("null") && priority.equals("null") && tickettype.equals("null") && assigned.equals("null") && assignto.equals("null")) {
                            progressDialog.show();
                            progressDialog.setMessage(getString(R.string.pleasewait));
                            new FetchFirstFilter(getActivity(), "show=" + show + "&departments=all", pageno).execute();
                            url = "show=" + show + "&departments=all";
                            Toast.makeText(getActivity(), "URL:" + url, Toast.LENGTH_SHORT).show();
                            Prefs.putString("URLFiltration", url);
                        } else if (!source.equals("null") && priority.equals("null") && tickettype.equals("null") && assigned.equals("null") && assignto.equals("null")) {
                            progressDialog.show();
                            progressDialog.setMessage(getString(R.string.pleasewait));
                            new FetchFirstFilter(getActivity(), "show=" + show + "&departments=all&source=" + source, pageno).execute();
                            url = "show=" + show + "&departments=all&source=" + source;
                            Prefs.putString("URLFiltration", url);
                        } else if (source.equals("null") && !priority.equals("null") && tickettype.equals("null") && assigned.equals("null") && assignto.equals("null")) {
                            progressDialog.show();
                            progressDialog.setMessage(getString(R.string.pleasewait));
                            new FetchFirstFilter(getActivity(), "show=" + show + "&departments=all&priority=" + priority, pageno).execute();
                            url = "show=" + show + "&departments=all&priority=" + priority;
                            Prefs.putString("URLFiltration", url);
                        } else if (source.equals("null") && priority.equals("null") && !tickettype.equals("null") && assigned.equals("null") && assignto.equals("null")) {
                            progressDialog.show();
                            progressDialog.setMessage(getString(R.string.pleasewait));
                            new FetchFirstFilter(getActivity(), "show=" + show + "&departments=all&types=" + tickettype, pageno).execute();
                            url = "show=" + show + "&departments=all&types=" + tickettype;
                            Prefs.putString("URLFiltration", url);
                        } else if (source.equals("null") && priority.equals("null") && tickettype.equals("null") && !assigned.equals("null") && assignto.equals("null")) {
                            progressDialog.show();
                            progressDialog.setMessage(getString(R.string.pleasewait));
                            new FetchFirstFilter(getActivity(), "show=" + show + "&departments=all&assigned=" + unassigned, pageno).execute();
                            url = "show=" + show + "&departments=all&assigned=" + unassigned;
                            Prefs.putString("URLFiltration", url);
                        } else if (source.equals("null") && priority.equals("null") && tickettype.equals("null") && assigned.equals("null") && !assignto.equals("null")) {
                            progressDialog.show();
                            progressDialog.setMessage(getString(R.string.pleasewait));
                            new FetchFirstFilter(getActivity(), "show=" + show + "&departments=all&assigned-to=" + assignto, pageno).execute();
                            url = "show=" + show + "&departments=all&source=" + source;
                            Prefs.putString("URLFiltration", url);
                        } else if (!source.equals("null") && priority.equals("null") && tickettype.equals("null") && assigned.equals("null") && !assignto.equals("null")) {
                            progressDialog.show();
                            progressDialog.setMessage(getString(R.string.pleasewait));
                            new FetchFirstFilter(getActivity(), "show=" + show + "&departments=all&assigned-to=" + assignto + "&source=" + source, pageno).execute();
                            url = "show=" + show + "&departments=all&assigned-to=" + assignto + "&source=" + source;
                            Prefs.putString("URLFiltration", url);
                        } else if (source.equals("null") && !priority.equals("null") && tickettype.equals("null") && assigned.equals("null") && !assignto.equals("null")) {
                            progressDialog.show();
                            progressDialog.setMessage(getString(R.string.pleasewait));
                            new FetchFirstFilter(getActivity(), "show=" + show + "&departments=all&assigned-to=" + assignto + "&priority=" + priority, pageno).execute();
                            url = "show=" + show + "&departments=all&assigned-to=" + assignto + "&priority=" + priority;
                            Prefs.putString("URLFiltration", url);
                        } else if (source.equals("null") && priority.equals("null") && !tickettype.equals("null") && assigned.equals("null") && !assignto.equals("null")) {
                            progressDialog.show();
                            progressDialog.setMessage(getString(R.string.pleasewait));
                            new FetchFirstFilter(getActivity(), "show=" + show + "&departments=all&assigned-to=" + assignto + "&types=" + tickettype, pageno).execute();
                            url = "show=" + show + "&departments=all&assigned-to=" + assignto + "&types=" + tickettype;
                            Prefs.putString("URLFiltration", url);
                        } else if (source.equals("null") && priority.equals("null") && tickettype.equals("null") && !assigned.equals("null") && !assignto.equals("null")) {
                            progressDialog.show();
                            progressDialog.setMessage(getString(R.string.pleasewait));
                            new FetchFirstFilter(getActivity(), "show=" + show + "&departments=all&assigned-to=" + assignto + "&assigned=" + unassigned, pageno).execute();
                            url = "show=" + show + "&departments=all&assigned-to=" + assignto + "&assigned=" + unassigned;
                            Prefs.putString("URLFiltration", url);
                        } else if (!source.equals("null") && !priority.equals("null") && tickettype.equals("null") && assigned.equals("null") && !assignto.equals("null")) {
                            progressDialog.show();
                            progressDialog.setMessage(getString(R.string.pleasewait));
                            new FetchFirstFilter(getActivity(), "show=" + show + "&departments=all&assigned-to=" + assignto + "&source=" + source + "&priority=" + priority, pageno).execute();
                            url = "show=" + show + "&departments=all&assigned-to=" + assignto + "&source=" + source + "&priority=" + priority;
                            Prefs.putString("URLFiltration", url);
                        } else if (!source.equals("null") && priority.equals("null") && !tickettype.equals("null") && assigned.equals("null") && !assignto.equals("null")) {
                            progressDialog.show();
                            progressDialog.setMessage(getString(R.string.pleasewait));
                            new FetchFirstFilter(getActivity(), "show=" + show + "&departments=all&assigned-to=" + assignto + "&source=" + source + "&types=" + tickettype, pageno).execute();
                            url = "show=" + show + "&departments=all&assigned-to=" + assignto + "&source=" + source + "&types=" + tickettype;
                            Prefs.putString("URLFiltration", url);
                        } else if (!source.equals("null") && priority.equals("null") && tickettype.equals("null") && !assigned.equals("null") && !assignto.equals("null")) {
                            progressDialog.show();
                            progressDialog.setMessage(getString(R.string.pleasewait));
                            new FetchFirstFilter(getActivity(), "show=" + show + "&departments=all&assigned-to=" + assignto + "&source=" + source + "&assigned=" + unassigned, pageno).execute();
                            url = "show=" + show + "&departments=all&assigned-to=" + assignto + "&source=" + source + "&assigned=" + unassigned;
                            Prefs.putString("URLFiltration", url);
                        } else if (source.equals("null") && !priority.equals("null") && !tickettype.equals("null") && assigned.equals("null") && !assignto.equals("null")) {
                            progressDialog.show();
                            progressDialog.setMessage(getString(R.string.pleasewait));
                            new FetchFirstFilter(getActivity(), "show=" + show + "&departments=all&assigned-to=" + assignto + "&priority=" + priority + "&types=" + tickettype, pageno).execute();
                            url = "show=" + show + "&departments=all&assigned-to=" + assignto + "&priority=" + priority + "&types=" + tickettype;
                            Prefs.putString("URLFiltration", url);
                        } else if (source.equals("null") && !priority.equals("null") && tickettype.equals("null") && !assigned.equals("null") && !assignto.equals("null")) {
                            progressDialog.show();
                            progressDialog.setMessage(getString(R.string.pleasewait));
                            new FetchFirstFilter(getActivity(), "show=" + show + "&departments=all&assigned-to=" + assignto + "&priority=" + priority + "&assigned=" + unassigned, pageno).execute();
                            url = "show=" + show + "&departments=all&assigned-to=" + assignto + "&priority=" + priority + "&assigned=" + unassigned;
                            Prefs.putString("URLFiltration", url);
                        } else if (source.equals("null") && priority.equals("null") && !tickettype.equals("null") && !assigned.equals("null") && !assignto.equals("null")) {
                            progressDialog.show();
                            progressDialog.setMessage(getString(R.string.pleasewait));
                            new FetchFirstFilter(getActivity(), "show=" + show + "&departments=all&assigned-to=" + assignto + "&types=" + tickettype + "&assigned=" + unassigned, pageno).execute();
                            url = "show=" + show + "&departments=all&assigned-to=" + assignto + "&types=" + tickettype + "&assigned=" + unassigned;
                            Prefs.putString("URLFiltration", url);
                        } else if (!source.equals("null") && !priority.equals("null") && !tickettype.equals("null") && assigned.equals("null") && !assignto.equals("null")) {
                            progressDialog.show();
                            progressDialog.setMessage(getString(R.string.pleasewait));
                            new FetchFirstFilter(getActivity(), "show=" + show + "&departments=all&assigned-to=" + assignto + "&types=" + tickettype + "&source=" + source + "&priority=" + priority, pageno).execute();
                            url = "show=" + show + "&departments=all&assigned-to=" + assignto + "&types=" + tickettype + "&source=" + source + "&priority=" + priority;
                            Prefs.putString("URLFiltration", url);
                        } else if (!source.equals("null") && priority.equals("null") && !tickettype.equals("null") && !assigned.equals("null") && !assignto.equals("null")) {
                            progressDialog.show();
                            progressDialog.setMessage(getString(R.string.pleasewait));
                            new FetchFirstFilter(getActivity(), "show=" + show + "&departments=all&assigned-to=" + assignto + "&types=" + tickettype + "&source=" + source + "&assigned=" + unassigned, pageno).execute();
                            url = "show=" + show + "&departments=all&assigned-to=" + assignto + "&types=" + tickettype + "&source=" + source + "&assigned=" + unassigned;
                            Prefs.putString("URLFiltration", url);
                        } else if (source.equals("null") && !priority.equals("null") && !tickettype.equals("null") && !assigned.equals("null") && !assignto.equals("null")) {
                            progressDialog.show();
                            progressDialog.setMessage(getString(R.string.pleasewait));
                            new FetchFirstFilter(getActivity(), "show=" + show + "&departments=all&assigned-to=" + assignto + "&types=" + tickettype + "&priority=" + priority + "&assigned=" + unassigned, pageno).execute();
                            url = "show=" + show + "&departments=all&assigned-to=" + assignto + "&types=" + tickettype + "&priority=" + priority + "&assigned=" + unassigned;
                            Prefs.putString("URLFiltration", url);
                        } else if (!source.equals("null") && !priority.equals("null") && tickettype.equals("null") && assigned.equals("null") && assignto.equals("null")) {
                            progressDialog.show();
                            progressDialog.setMessage(getString(R.string.pleasewait));
                            new FetchFirstFilter(getActivity(), "show=" + show + "&departments=all&source=" + source + "&priority=" + priority, pageno).execute();
                            url = "show=" + show + "&departments=all&source=" + source + "&priority=" + priority;
                            Prefs.putString("URLFiltration", url);
                        }
                        else if (!source.equals("null") && !priority.equals("null") && !tickettype.equals("null") && !assigned.equals("null") && !assignto.equals("null")){
                            progressDialog.show();
                            progressDialog.setMessage(getString(R.string.pleasewait));
                            new FetchFirstFilter(getActivity(), "show=" + show + "&departments=all&source=" + source + "&priority=" + priority+"&assigned-to="+assignto+"&assigned="+unassigned+"&types="+tickettype, pageno).execute();
                            url = "show=" + show + "&departments=all&source=" + source + "&priority=" + priority+"&assigned-to="+assignto+"&assigned="+unassigned+"&types="+tickettype;
                            Prefs.putString("URLFiltration", url);
                        }
                        else if (source.equals("null") && priority.equals("null") && !tickettype.equals("null") && !assigned.equals("null")) {
                            progressDialog.show();
                            progressDialog.setMessage(getString(R.string.pleasewait));
                            new FetchFirstFilter(getActivity(), "show=" + show + "&departments=all&assigned=" + unassigned + "&types=" + tickettype, pageno).execute();
                            url = "show=" + show + "&departments=all&assigned=" + unassigned + "&types=" + tickettype;
                            Prefs.putString("URLFiltration", url);
                        } else if (!source.equals("null") && priority.equals("null") && tickettype.equals("null") && !assigned.equals("null")) {
                            progressDialog.show();
                            progressDialog.setMessage(getString(R.string.pleasewait));
                            new FetchFirstFilter(getActivity(), "show=" + show + "&departments=all&assigned=" + unassigned + "&source=" + source + "&assigned=" + unassigned, pageno).execute();
                            url = "show=" + show + "&departments=all&assigned=" + unassigned + "&source=" + source + "&assigned=" + unassigned;
                            Prefs.putString("URLFiltration", url);
                        } else if (!source.equals("null") && priority.equals("null") && !tickettype.equals("null") && assigned.equals("null")) {
                            progressDialog.show();
                            progressDialog.setMessage(getString(R.string.pleasewait));
                            new FetchFirstFilter(getActivity(), "show=" + show + "&departments=all&source=" + source + "&types=" + tickettype, pageno).execute();
                            url = "show=" + show + "&departments=all&source=" + source + "&types=" + tickettype;
                            Prefs.putString("URLFiltration", url);
                        } else if (source.equals("null") && !priority.equals("null") && tickettype.equals("null") && !assigned.equals("null")) {
                            progressDialog.show();
                            progressDialog.setMessage(getString(R.string.pleasewait));
                            new FetchFirstFilter(getActivity(), "show=" + show + "&departments=all&assigned=" + unassigned + "&priority=" + priority, pageno).execute();
                            url = "show=" + show + "&departments=all&assigned=" + unassigned + "&priority=" + priority;
                            Prefs.putString("URLFiltration", url);
                        } else if (!source.equals("null") && !priority.equals("null") && !tickettype.equals("null") && assigned.equals("null")) {
                            progressDialog.show();
                            progressDialog.setMessage(getString(R.string.pleasewait));
                            new FetchFirstFilter(getActivity(), "show=" + show + "&departments=all&source=" + source + "&priority=" + priority + "&types=" + tickettype, pageno).execute();
                            url = "show=" + show + "&departments=all&source=" + source + "&priority=" + priority + "&types=" + tickettype;
                            Prefs.putString("URLFiltration", url);
                        } else if (!source.equals("null") && priority.equals("null") && !tickettype.equals("null") && !assigned.equals("null")) {
                            progressDialog.show();
                            progressDialog.setMessage(getString(R.string.pleasewait));
                            new FetchFirstFilter(getActivity(), "show=" + show + "&departments=all&source=" + source + "&types=" + tickettype + "&assigned=" + unassigned, pageno).execute();
                            url = "show=" + show + "&departments=all&source=" + source + "&types=" + tickettype + "&assigned=" + unassigned;
                            Prefs.putString("URLFiltration", url);
                        } else if (source.equals("null") && !priority.equals("null") && !tickettype.equals("null") && !assigned.equals("null")) {
                            progressDialog.show();
                            progressDialog.setMessage(getString(R.string.pleasewait));
                            new FetchFirstFilter(getActivity(), "show=" + show + "&departments=all&priority=" + priority + "&types=" + tickettype + "&assigned=" + unassigned, pageno).execute();
                            url = "show=" + show + "&departments=all&priority=" + priority + "&types=" + tickettype + "&assigned=" + unassigned;
                            Prefs.putString("URLFiltration", url);
                        } else if (!source.equals("null") && !priority.equals("null") && !tickettype.equals("null") && !assigned.equals("null")) {
                            progressDialog.show();
                            progressDialog.setMessage(getString(R.string.pleasewait));
                            new FetchFirstFilter(getActivity(), "show=" + show + "&departments=all&priority=" + priority + "&types=" + tickettype + "&assigned=" + unassigned + "&source=" + source, pageno).execute();
                            url = "show=" + show + "&departments=all&priority=" + priority + "&types=" + tickettype + "&assigned=" + unassigned + "&source=" + source;
                            Prefs.putString("URLFiltration", url);
                        } else if (!assignto.equals("null")) {
                            progressDialog.show();
                            progressDialog.setMessage(getString(R.string.pleasewait));
                            new FetchFirstFilter(getActivity(), "show=" + show + "&departments=all&assigned-to=a-" + assignto, pageno).execute();
                            url = "show=" + show + "&departments=all&assigned-to=a-" + assignto;
                            Prefs.putString("URLFiltration", url);
                        } else if (source.equals("null") && !priority.equals("null") && !tickettype.equals("null") && assigned.equals("null")) {
                            progressDialog.show();
                            progressDialog.setMessage(getString(R.string.pleasewait));
                            new FetchFirstFilter(getActivity(), "show=" + show + "&departments=" + department + "&priority=" + priority + "&types=" + tickettype, pageno).execute();
                            url = "show=" + show + "&departments=" + department + "&priority=" + priority + "&types=" + tickettype;
                            Prefs.putString("URLFiltration", url);
                        }
                        else if (!show.equals("null")&&!department.equals("null")&&!source.equals("null")&&!priority.equals("null")&&!assigned.equals("null")){
                            progressDialog.show();
                            progressDialog.setMessage(getString(R.string.pleasewait));
                            new FetchFirstFilter(getActivity(), "show=" + show + "&departments=all&priority=" + priority + "&source=" + source+"&assigned="+unassigned, pageno).execute();
                            url = "show=" + show + "&departments=all&priority=" + priority + "&source=" + source+"&assigned="+unassigned;
                            Prefs.putString("URLFiltration", url);
                        }


                    } else if (!department.equals("all")) {
                        if (source.equals("null") && priority.equals("null") && tickettype.equals("null") && assigned.equals("null") && assignto.equals("null")) {
                            progressDialog.show();
                            progressDialog.setMessage(getString(R.string.pleasewait));
                            new FetchFirstFilter(getActivity(), "show=" + show + "&departments=" + department, pageno).execute();
                            url = "show=" + show + "&departments=" + department;
                            Prefs.putString("URLFiltration", url);
                        } else if (!source.equals("null") && priority.equals("null") && tickettype.equals("null") && assigned.equals("null") && assignto.equals("null")) {
                            progressDialog.show();
                            progressDialog.setMessage(getString(R.string.pleasewait));
                            new FetchFirstFilter(getActivity(), "show=" + show + "&departments=" + department + "&source=" + source, pageno).execute();
                            url = "show=" + show + "&departments=" + department + "&source=" + source;
                            Prefs.putString("URLFiltration", url);
                        } else if (source.equals("null") && !priority.equals("null") && tickettype.equals("null") && assigned.equals("null") && assignto.equals("null")) {
                            progressDialog.show();
                            progressDialog.setMessage(getString(R.string.pleasewait));
                            new FetchFirstFilter(getActivity(), "show=" + show + "&departments=" + department + "&priority=" + priority, pageno).execute();
                            url = "show=" + show + "&departments=" + department + "&priority=" + priority;
                            Prefs.putString("URLFiltration", url);
                        } else if (source.equals("null") && priority.equals("null") && !tickettype.equals("null") && assigned.equals("null") && assignto.equals("null")) {
                            progressDialog.show();
                            progressDialog.setMessage(getString(R.string.pleasewait));
                            new FetchFirstFilter(getActivity(), "show=" + show + "&departments=" + department + "&types=" + tickettype, pageno).execute();
                            url = "show=" + show + "&departments=" + department + "&types=" + tickettype;
                            Prefs.putString("URLFiltration", url);
                        } else if (source.equals("null") && priority.equals("null") && tickettype.equals("null") && !assigned.equals("null") && assignto.equals("null")) {
                            progressDialog.show();
                            progressDialog.setMessage(getString(R.string.pleasewait));
                            new FetchFirstFilter(getActivity(), "show=" + show + "&departments=" + department + "&assigned=" + unassigned, pageno).execute();
                            url = "show=" + show + "&departments=" + department + "&assigned=" + unassigned;
                            Prefs.putString("URLFiltration", url);
                        } else if (source.equals("null") && priority.equals("null") && tickettype.equals("null") && assigned.equals("null") && !assignto.equals("null")) {
                            progressDialog.show();
                            progressDialog.setMessage(getString(R.string.pleasewait));
                            new FetchFirstFilter(getActivity(), "show=" + show + "&departments=" + department + "&assigned-to=" + assignto, pageno).execute();
                            url = "show=" + show + "&departments=" + department + "&source=" + source;
                            Prefs.putString("URLFiltration", url);
                        } else if (!source.equals("null") && priority.equals("null") && tickettype.equals("null") && assigned.equals("null") && !assignto.equals("null")) {
                            progressDialog.show();
                            progressDialog.setMessage(getString(R.string.pleasewait));
                            new FetchFirstFilter(getActivity(), "show=" + show + "&departments=" + department + "&assigned-to=" + assignto + "&source=" + source, pageno).execute();
                            url = "show=" + show + "&departments=" + department + "&assigned-to=" + assignto + "&source=" + source;
                            Prefs.putString("URLFiltration", url);
                        } else if (source.equals("null") && !priority.equals("null") && tickettype.equals("null") && assigned.equals("null") && !assignto.equals("null")) {
                            progressDialog.show();
                            progressDialog.setMessage(getString(R.string.pleasewait));
                            new FetchFirstFilter(getActivity(), "show=" + show + "&departments=" + department + "&assigned-to=" + assignto + "&priority=" + priority, pageno).execute();
                            url = "show=" + show + "&departments=" + department + "&assigned-to=" + assignto + "&priority=" + priority;
                            Prefs.putString("URLFiltration", url);
                        } else if (source.equals("null") && priority.equals("null") && !tickettype.equals("null") && assigned.equals("null") && !assignto.equals("null")) {
                            progressDialog.show();
                            progressDialog.setMessage(getString(R.string.pleasewait));
                            new FetchFirstFilter(getActivity(), "show=" + show + "&departments=" + department + "&assigned-to=" + assignto + "&types=" + tickettype, pageno).execute();
                            url = "show=" + show + "&departments=" + department + "&assigned-to=" + assignto + "&types=" + tickettype;
                            Prefs.putString("URLFiltration", url);
                        } else if (source.equals("null") && priority.equals("null") && tickettype.equals("null") && !assigned.equals("null") && !assignto.equals("null")) {
                            progressDialog.show();
                            progressDialog.setMessage(getString(R.string.pleasewait));
                            new FetchFirstFilter(getActivity(), "show=" + show + "&departments=" + department + "&assigned-to=" + assignto + "&assigned=" + unassigned, pageno).execute();
                            url = "show=" + show + "&departments=" + department + "&assigned-to=" + assignto + "&assigned=" + unassigned;
                            Prefs.putString("URLFiltration", url);
                        } else if (!source.equals("null") && !priority.equals("null") && tickettype.equals("null") && assigned.equals("null") && !assignto.equals("null")) {
                            progressDialog.show();
                            progressDialog.setMessage(getString(R.string.pleasewait));
                            new FetchFirstFilter(getActivity(), "show=" + show + "&departments=" + department + "&assigned-to=" + assignto + "&source=" + source + "&priority=" + priority, pageno).execute();
                            url = "show=" + show + "&departments=" + department + "&assigned-to=" + assignto + "&source=" + source + "&priority=" + priority;
                            Prefs.putString("URLFiltration", url);
                        } else if (!source.equals("null") && priority.equals("null") && !tickettype.equals("null") && assigned.equals("null") && !assignto.equals("null")) {
                            progressDialog.show();
                            progressDialog.setMessage(getString(R.string.pleasewait));
                            new FetchFirstFilter(getActivity(), "show=" + show + "&departments=" + department + "&assigned-to=" + assignto + "&source=" + source + "&types=" + tickettype, pageno).execute();
                            url = "show=" + show + "&departments=" + department + "&assigned-to=" + assignto + "&source=" + source + "&types=" + tickettype;
                            Prefs.putString("URLFiltration", url);
                        } else if (!source.equals("null") && priority.equals("null") && tickettype.equals("null") && !assigned.equals("null") && !assignto.equals("null")) {
                            progressDialog.show();
                            progressDialog.setMessage(getString(R.string.pleasewait));
                            new FetchFirstFilter(getActivity(), "show=" + show + "&departments=" + department + "&assigned-to=" + assignto + "&source=" + source + "&assigned=" + unassigned, pageno).execute();
                            url = "show=" + show + "&departments=" + department + "&assigned-to=" + assignto + "&source=" + source + "&assigned=" + unassigned;
                            Prefs.putString("URLFiltration", url);
                        } else if (source.equals("null") && !priority.equals("null") && !tickettype.equals("null") && assigned.equals("null") && !assignto.equals("null")) {
                            progressDialog.show();
                            progressDialog.setMessage(getString(R.string.pleasewait));
                            new FetchFirstFilter(getActivity(), "show=" + show + "&departments=" + department + "&assigned-to=" + assignto + "&priority=" + priority + "&types=" + tickettype, pageno).execute();
                            url = "show=" + show + "&departments=" + department + "&assigned-to=" + assignto + "&priority=" + priority + "&types=" + tickettype;
                            Prefs.putString("URLFiltration", url);
                        } else if (source.equals("null") && !priority.equals("null") && tickettype.equals("null") && !assigned.equals("null") && !assignto.equals("null")) {
                            progressDialog.show();
                            progressDialog.setMessage(getString(R.string.pleasewait));
                            new FetchFirstFilter(getActivity(), "show=" + show + "&departments=" + department + "&assigned-to=" + assignto + "&priority=" + priority + "&assigned=" + unassigned, pageno).execute();
                            url = "show=" + show + "&departments=" + department + "&assigned-to=" + assignto + "&priority=" + priority + "&assigned=" + unassigned;
                            Prefs.putString("URLFiltration", url);
                        } else if (source.equals("null") && priority.equals("null") && !tickettype.equals("null") && !assigned.equals("null") && !assignto.equals("null")) {
                            progressDialog.show();
                            progressDialog.setMessage(getString(R.string.pleasewait));
                            new FetchFirstFilter(getActivity(), "show=" + show + "&departments=" + department + "&assigned-to=" + assignto + "&types=" + tickettype + "&assigned=" + unassigned, pageno).execute();
                            url = "show=" + show + "&departments=" + department + "&assigned-to=" + assignto + "&types=" + tickettype + "&assigned=" + unassigned;
                            Prefs.putString("URLFiltration", url);
                        } else if (!source.equals("null") && !priority.equals("null") && !tickettype.equals("null") && assigned.equals("null") && !assignto.equals("null")) {
                            progressDialog.show();
                            progressDialog.setMessage(getString(R.string.pleasewait));
                            new FetchFirstFilter(getActivity(), "show=" + show + "&departments=" + department + "&assigned-to=" + assignto + "&types=" + tickettype + "&source=" + source + "&priority=" + priority, pageno).execute();
                            url = "show=" + show + "&departments=" + department + "&assigned-to=" + assignto + "&types=" + tickettype + "&source=" + source + "&priority=" + priority;
                            Prefs.putString("URLFiltration", url);
                        } else if (!source.equals("null") && priority.equals("null") && !tickettype.equals("null") && !assigned.equals("null") && !assignto.equals("null")) {
                            progressDialog.show();
                            progressDialog.setMessage(getString(R.string.pleasewait));
                            new FetchFirstFilter(getActivity(), "show=" + show + "&departments=" + department + "&assigned-to=" + assignto + "&types=" + tickettype + "&source=" + source + "&assigned=" + unassigned, pageno).execute();
                            url = "show=" + show + "&departments=" + department + " &assigned-to=" + assignto + "&types=" + tickettype + "&source=" + source + "&assigned=" + unassigned;
                            Prefs.putString("URLFiltration", url);
                        } else if (source.equals("null") && !priority.equals("null") && !tickettype.equals("null") && !assigned.equals("null") && !assignto.equals("null")) {
                            progressDialog.show();
                            progressDialog.setMessage(getString(R.string.pleasewait));
                            new FetchFirstFilter(getActivity(), "show=" + show + "&departments=" + department + "&assigned-to=" + assignto + "&types=" + tickettype + "&priority=" + priority + "&assigned=" + unassigned, pageno).execute();
                            url = "show=" + show + "&departments=" + department + "&assigned-to=" + assignto + "&types=" + tickettype + "&priority=" + priority + "&assigned=" + unassigned;
                            Prefs.putString("URLFiltration", url);
                        } else if (!source.equals("null") && !priority.equals("null") && tickettype.equals("null") && assigned.equals("null") && assignto.equals("null")) {
                            progressDialog.show();
                            progressDialog.setMessage(getString(R.string.pleasewait));
                            new FetchFirstFilter(getActivity(), "show=" + show + "&departments=" + department + "&source=" + source + "&priority=" + priority, pageno).execute();
                            url = "show=" + show + "&departments=" + department + "&source=" + source + "&priority=" + priority;
                            Prefs.putString("URLFiltration", url);
                        } else if (source.equals("null") && priority.equals("null") && !tickettype.equals("null") && !assigned.equals("null")) {
                            progressDialog.show();
                            progressDialog.setMessage(getString(R.string.pleasewait));
                            new FetchFirstFilter(getActivity(), "show=" + show + "&departments=" + department + "&assigned=" + unassigned + "&types=" + tickettype, pageno).execute();
                            url = "show=" + show + "&departments=" + department + "&assigned=" + unassigned + "&types=" + tickettype;
                            Prefs.putString("URLFiltration", url);
                        } else if (!source.equals("null") && priority.equals("null") && tickettype.equals("null") && !assigned.equals("null")) {
                            progressDialog.show();
                            progressDialog.setMessage(getString(R.string.pleasewait));
                            new FetchFirstFilter(getActivity(), "show=" + show + "&departments=" + department + "&assigned=" + unassigned + "&source=" + source + "&assigned=" + unassigned, pageno).execute();
                            url = "show=" + show + "&departments=" + department + "&assigned=" + unassigned + "&source=" + source + "&assigned=" + unassigned;
                            Prefs.putString("URLFiltration", url);
                        } else if (!source.equals("null") && priority.equals("null") && !tickettype.equals("null") && assigned.equals("null")) {
                            progressDialog.show();
                            progressDialog.setMessage(getString(R.string.pleasewait));
                            new FetchFirstFilter(getActivity(), "show=" + show + "&departments=" + department + "&source=" + source + "&types=" + tickettype, pageno).execute();
                            url = "show=" + show + "&departments=" + department + "&source=" + source + "&types=" + tickettype;
                            Prefs.putString("URLFiltration", url);
                        } else if (source.equals("null") && !priority.equals("null") && tickettype.equals("null") && !assigned.equals("null")) {
                            progressDialog.show();
                            progressDialog.setMessage(getString(R.string.pleasewait));
                            new FetchFirstFilter(getActivity(), "show=" + show + "&departments=" + department + "&assigned=" + unassigned + "&priority=" + priority, pageno).execute();
                            url = "show=" + show + "&departments=" + department + "&assigned=" + unassigned + "&priority=" + priority;
                            Prefs.putString("URLFiltration", url);
                        } else if (!source.equals("null") && !priority.equals("null") && !tickettype.equals("null") && assigned.equals("null")) {
                            progressDialog.show();
                            progressDialog.setMessage(getString(R.string.pleasewait));
                            new FetchFirstFilter(getActivity(), "show=" + show + "&departments=" + department + "&source=" + source + "&priority=" + priority + "&types=" + tickettype, pageno).execute();
                            url = "show=" + show + "&departments=" + department + "&source=" + source + "&priority=" + priority + "&types=" + tickettype;
                            Prefs.putString("URLFiltration", url);
                        } else if (!source.equals("null") && priority.equals("null") && !tickettype.equals("null") && !assigned.equals("null")) {
                            progressDialog.show();
                            progressDialog.setMessage(getString(R.string.pleasewait));
                            new FetchFirstFilter(getActivity(), "show=" + show + "&departments=" + department + "&source=" + source + "&types=" + tickettype + "&assigned=" + unassigned, pageno).execute();
                            url = "show=" + show + "&departments=" + department + "&source=" + source + "&types=" + tickettype + "&assigned=" + unassigned;
                            Prefs.putString("URLFiltration", url);
                        } else if (source.equals("null") && !priority.equals("null") && !tickettype.equals("null") && !assigned.equals("null")) {
                            progressDialog.show();
                            progressDialog.setMessage(getString(R.string.pleasewait));
                            new FetchFirstFilter(getActivity(), "show=" + show + "&departments=" + department + "&priority=" + priority + "&types=" + tickettype + "&assigned=" + unassigned, pageno).execute();
                            url = "show=" + show + "&departments=" + department + "&priority=" + priority + "&types=" + tickettype + "&assigned=" + unassigned;
                            Prefs.putString("URLFiltration", url);
                        } else if (!source.equals("null") && !priority.equals("null") && !tickettype.equals("null") && !assigned.equals("null")) {
                            progressDialog.show();
                            progressDialog.setMessage(getString(R.string.pleasewait));
                            new FetchFirstFilter(getActivity(), "show=" + show + "&departments=" + department + "&priority=" + priority + "&types=" + tickettype + "&assigned=" + unassigned + "&source=" + source, pageno).execute();
                            url = "show=" + show + "&departments=" + department + "&priority=" + priority + "&types=" + tickettype + "&assigned=" + unassigned + "&source=" + source;
                            Prefs.putString("URLFiltration", url);
                        } else if (source.equals("null") && !priority.equals("null") && !tickettype.equals("null") && assigned.equals("null")) {
                            progressDialog.show();
                            progressDialog.setMessage(getString(R.string.pleasewait));
                            new FetchFirstFilter(getActivity(), "show=" + show + "&departments=" + department + "&priority=" + priority + "&types=" + tickettype, pageno).execute();
                            url = "show=" + show + "&departments=" + department + "&priority=" + priority + "&types=" + tickettype;
                            Prefs.putString("URLFiltration", url);
                        }
                        else if (!source.equals("null") && !priority.equals("null") && !tickettype.equals("null") && !assigned.equals("null") && !assignto.equals("null")){
                            progressDialog.show();
                            progressDialog.setMessage(getString(R.string.pleasewait));
                            new FetchFirstFilter(getActivity(), "show=" + show + "&departments="+department+"&source=" + source + "&priority=" + priority+"&assigned-to="+assignto+"&assigned="+unassigned+"&types="+tickettype, pageno).execute();
                            url = "show=" + show + "&departments="+department+"&source=" + source + "&priority=" + priority+"&assigned-to="+assignto+"&assigned="+unassigned+"&types="+tickettype;
                            Prefs.putString("URLFiltration", url);
                        }
                        else if (!show.equals("null")&&!department.equals("null")&&!source.equals("null")&&!priority.equals("null")&&!assigned.equals("null")){
                            progressDialog.show();
                            progressDialog.setMessage(getString(R.string.pleasewait));
                            new FetchFirstFilter(getActivity(), "show=" + show + "&departments="+department+"&priority=" + priority + "&source=" + source+"&assigned="+unassigned, pageno).execute();
                            url = "show=" + show + "&departments="+department+"&priority=" + priority + "&source=" + source+"&assigned="+unassigned;
                            Prefs.putString("URLFiltration", url);
                        }


//                        else if (!assignto.equals("null")){
//                            new FetchFirstFilter(getActivity(),"show="+show+"&departments=all&assigned-to=a-"+assignto,pageno).execute();
//                            url="show="+show+"&departments=all&assigned-to=a-"+assignto;
//                        }
                    }
//                    else if (!assignto.equals("null")){
//                        new FetchFirstFilter(getActivity(),"show="+show+"&departments="+department+"&assigned-to=a-"+assignto,pageno).execute();
//                        url="show="+show+"&departments="+department+"&assigned-to=a-"+assignto;
//                    }
                } else if (check.equals("false")) {
                    noInternet_view.setVisibility(View.GONE);
                    // swipeRefresh.setRefreshing(true);
                    progressDialog.show();
                    new FetchFirst(getActivity(), page).execute();
                    ((MainActivity) getActivity()).setActionBarTitle("Inbox");

                }
//                else if (filterwithsorting.equals("true")){
//                    Toast.makeText(getActivity(), "filterwithsorting", Toast.LENGTH_SHORT).show();
//                }

            } else {
                noInternet_view.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.INVISIBLE);
                empty_view.setVisibility(View.GONE);
            }

            swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    if (InternetReceiver.isConnected()) {
                        loading = true;
                        if (check.equals("true")) {
                            if (department.equals("all")) {
                                if (source.equals("null") && priority.equals("null") && tickettype.equals("null") && assigned.equals("null") && assignto.equals("null")) {
                                    new FetchFirstFilter(getActivity(), "show=" + show + "&departments=all", pageno).execute();
                                    url = "show=" + show + "&departments=all";
                                    Toast.makeText(getActivity(), "URL:" + url, Toast.LENGTH_SHORT).show();
                                    Prefs.putString("URLFiltration", url);
                                } else if (!source.equals("null") && priority.equals("null") && tickettype.equals("null") && assigned.equals("null") && assignto.equals("null")) {
                                    new FetchFirstFilter(getActivity(), "show=" + show + "&departments=all&source=" + source, pageno).execute();
                                    url = "show=" + show + "&departments=all&source=" + source;
                                    Prefs.putString("URLFiltration", url);
                                } else if (source.equals("null") && !priority.equals("null") && tickettype.equals("null") && assigned.equals("null") && assignto.equals("null")) {
                                    new FetchFirstFilter(getActivity(), "show=" + show + "&departments=all&priority=" + priority, pageno).execute();
                                    url = "show=" + show + "&departments=all&priority=" + priority;
                                    Prefs.putString("URLFiltration", url);
                                } else if (source.equals("null") && priority.equals("null") && !tickettype.equals("null") && assigned.equals("null") && assignto.equals("null")) {
                                    new FetchFirstFilter(getActivity(), "show=" + show + "&departments=all&types=" + tickettype, pageno).execute();
                                    url = "show=" + show + "&departments=all&types=" + tickettype;
                                    Prefs.putString("URLFiltration", url);
                                } else if (source.equals("null") && priority.equals("null") && tickettype.equals("null") && !assigned.equals("null") && assignto.equals("null")) {
                                    new FetchFirstFilter(getActivity(), "show=" + show + "&departments=all&assigned=" + unassigned, pageno).execute();
                                    url = "show=" + show + "&departments=all&assigned=" + unassigned;
                                    Prefs.putString("URLFiltration", url);
                                } else if (source.equals("null") && priority.equals("null") && tickettype.equals("null") && assigned.equals("null") && !assignto.equals("null")) {
                                    new FetchFirstFilter(getActivity(), "show=" + show + "&departments=all&assigned-to=" + assignto, pageno).execute();
                                    url = "show=" + show + "&departments=all&source=" + source;
                                    Prefs.putString("URLFiltration", url);
                                } else if (!source.equals("null") && priority.equals("null") && tickettype.equals("null") && assigned.equals("null") && !assignto.equals("null")) {
                                    new FetchFirstFilter(getActivity(), "show=" + show + "&departments=all&assigned-to=" + assignto + "&source=" + source, pageno).execute();
                                    url = "show=" + show + "&departments=all&assigned-to=" + assignto + "&source=" + source;
                                    Prefs.putString("URLFiltration", url);
                                } else if (source.equals("null") && !priority.equals("null") && tickettype.equals("null") && assigned.equals("null") && !assignto.equals("null")) {
                                    new FetchFirstFilter(getActivity(), "show=" + show + "&departments=all&assigned-to=" + assignto + "&priority=" + priority, pageno).execute();
                                    url = "show=" + show + "&departments=all&assigned-to=" + assignto + "&priority=" + priority;
                                    Prefs.putString("URLFiltration", url);
                                } else if (source.equals("null") && priority.equals("null") && !tickettype.equals("null") && assigned.equals("null") && !assignto.equals("null")) {
                                    new FetchFirstFilter(getActivity(), "show=" + show + "&departments=all&assigned-to=" + assignto + "&types=" + tickettype, pageno).execute();
                                    url = "show=" + show + "&departments=all&assigned-to=" + assignto + "&types=" + tickettype;
                                    Prefs.putString("URLFiltration", url);
                                } else if (source.equals("null") && priority.equals("null") && tickettype.equals("null") && !assigned.equals("null") && !assignto.equals("null")) {
                                    new FetchFirstFilter(getActivity(), "show=" + show + "&departments=all&assigned-to=" + assignto + "&assigned=" + unassigned, pageno).execute();
                                    url = "show=" + show + "&departments=all&assigned-to=" + assignto + "&assigned=" + unassigned;
                                    Prefs.putString("URLFiltration", url);
                                } else if (!source.equals("null") && !priority.equals("null") && tickettype.equals("null") && assigned.equals("null") && !assignto.equals("null")) {
                                    new FetchFirstFilter(getActivity(), "show=" + show + "&departments=all&assigned-to=" + assignto + "&source=" + source + "&priority=" + priority, pageno).execute();
                                    url = "show=" + show + "&departments=all&assigned-to=" + assignto + "&source=" + source + "&priority=" + priority;
                                    Prefs.putString("URLFiltration", url);
                                } else if (!source.equals("null") && priority.equals("null") && !tickettype.equals("null") && assigned.equals("null") && !assignto.equals("null")) {
                                    new FetchFirstFilter(getActivity(), "show=" + show + "&departments=all&assigned-to=" + assignto + "&source=" + source + "&types=" + tickettype, pageno).execute();
                                    url = "show=" + show + "&departments=all&assigned-to=" + assignto + "&source=" + source + "&types=" + tickettype;
                                    Prefs.putString("URLFiltration", url);
                                } else if (!source.equals("null") && priority.equals("null") && tickettype.equals("null") && !assigned.equals("null") && !assignto.equals("null")) {
                                    new FetchFirstFilter(getActivity(), "show=" + show + "&departments=all&assigned-to=" + assignto + "&source=" + source + "&assigned=" + unassigned, pageno).execute();
                                    url = "show=" + show + "&departments=all&assigned-to=" + assignto + "&source=" + source + "&assigned=" + unassigned;
                                    Prefs.putString("URLFiltration", url);
                                } else if (source.equals("null") && !priority.equals("null") && !tickettype.equals("null") && assigned.equals("null") && !assignto.equals("null")) {
                                    new FetchFirstFilter(getActivity(), "show=" + show + "&departments=all&assigned-to=" + assignto + "&priority=" + priority + "&types=" + tickettype, pageno).execute();
                                    url = "show=" + show + "&departments=all&assigned-to=" + assignto + "&priority=" + priority + "&types=" + tickettype;
                                    Prefs.putString("URLFiltration", url);
                                } else if (source.equals("null") && !priority.equals("null") && tickettype.equals("null") && !assigned.equals("null") && !assignto.equals("null")) {
                                    new FetchFirstFilter(getActivity(), "show=" + show + "&departments=all&assigned-to=" + assignto + "&priority=" + priority + "&assigned=" + unassigned, pageno).execute();
                                    url = "show=" + show + "&departments=all&assigned-to=" + assignto + "&priority=" + priority + "&assigned=" + unassigned;
                                    Prefs.putString("URLFiltration", url);
                                } else if (source.equals("null") && priority.equals("null") && !tickettype.equals("null") && !assigned.equals("null") && !assignto.equals("null")) {
                                    new FetchFirstFilter(getActivity(), "show=" + show + "&departments=all&assigned-to=" + assignto + "&types=" + tickettype + "&assigned=" + unassigned, pageno).execute();
                                    url = "show=" + show + "&departments=all&assigned-to=" + assignto + "&types=" + tickettype + "&assigned=" + unassigned;
                                    Prefs.putString("URLFiltration", url);
                                } else if (!source.equals("null") && !priority.equals("null") && !tickettype.equals("null") && assigned.equals("null") && !assignto.equals("null")) {
                                    new FetchFirstFilter(getActivity(), "show=" + show + "&departments=all&assigned-to=" + assignto + "&types=" + tickettype + "&source=" + source + "&priority=" + priority, pageno).execute();
                                    url = "show=" + show + "&departments=all&assigned-to=" + assignto + "&types=" + tickettype + "&source=" + source + "&priority=" + priority;
                                    Prefs.putString("URLFiltration", url);
                                } else if (!source.equals("null") && priority.equals("null") && !tickettype.equals("null") && !assigned.equals("null") && !assignto.equals("null")) {
                                    new FetchFirstFilter(getActivity(), "show=" + show + "&departments=all&assigned-to=" + assignto + "&types=" + tickettype + "&source=" + source + "&assigned=" + unassigned, pageno).execute();
                                    url = "show=" + show + "&departments=all&assigned-to=" + assignto + "&types=" + tickettype + "&source=" + source + "&assigned=" + unassigned;
                                    Prefs.putString("URLFiltration", url);
                                } else if (source.equals("null") && !priority.equals("null") && !tickettype.equals("null") && !assigned.equals("null") && !assignto.equals("null")) {
                                    new FetchFirstFilter(getActivity(), "show=" + show + "&departments=all&assigned-to=" + assignto + "&types=" + tickettype + "&priority=" + priority + "&assigned=" + unassigned, pageno).execute();
                                    url = "show=" + show + "&departments=all&assigned-to=" + assignto + "&types=" + tickettype + "&priority=" + priority + "&assigned=" + unassigned;
                                    Prefs.putString("URLFiltration", url);
                                } else if (!source.equals("null") && !priority.equals("null") && tickettype.equals("null") && assigned.equals("null") && assignto.equals("null")) {
                                    new FetchFirstFilter(getActivity(), "show=" + show + "&departments=all&source=" + source + "&priority=" + priority, pageno).execute();
                                    url = "show=" + show + "&departments=all&source=" + source + "&priority=" + priority;
                                    Prefs.putString("URLFiltration", url);
                                } else if (source.equals("null") && priority.equals("null") && !tickettype.equals("null") && !assigned.equals("null")) {
                                    new FetchFirstFilter(getActivity(), "show=" + show + "&departments=all&assigned=" + unassigned + "&types=" + tickettype, pageno).execute();
                                    url = "show=" + show + "&departments=all&assigned=" + unassigned + "&types=" + tickettype;
                                    Prefs.putString("URLFiltration", url);
                                } else if (!source.equals("null") && priority.equals("null") && tickettype.equals("null") && !assigned.equals("null")) {
                                    new FetchFirstFilter(getActivity(), "show=" + show + "&departments=all&assigned=" + unassigned + "&source=" + source + "&assigned=" + unassigned, pageno).execute();
                                    url = "show=" + show + "&departments=all&assigned=" + unassigned + "&source=" + source + "&assigned=" + unassigned;
                                    Prefs.putString("URLFiltration", url);
                                } else if (!source.equals("null") && priority.equals("null") && !tickettype.equals("null") && assigned.equals("null")) {
                                    new FetchFirstFilter(getActivity(), "show=" + show + "&departments=all&source=" + source + "&types=" + tickettype, pageno).execute();
                                    url = "show=" + show + "&departments=all&source=" + source + "&types=" + tickettype;
                                    Prefs.putString("URLFiltration", url);
                                } else if (source.equals("null") && !priority.equals("null") && tickettype.equals("null") && !assigned.equals("null")) {
                                    new FetchFirstFilter(getActivity(), "show=" + show + "&departments=all&assigned=" + unassigned + "&priority=" + priority, pageno).execute();
                                    url = "show=" + show + "&departments=all&assigned=" + unassigned + "&priority=" + priority;
                                    Prefs.putString("URLFiltration", url);
                                } else if (!source.equals("null") && !priority.equals("null") && !tickettype.equals("null") && assigned.equals("null")) {
                                    new FetchFirstFilter(getActivity(), "show=" + show + "&departments=all&source=" + source + "&priority=" + priority + "&types=" + tickettype, pageno).execute();
                                    url = "show=" + show + "&departments=all&source=" + source + "&priority=" + priority + "&types=" + tickettype;
                                    Prefs.putString("URLFiltration", url);
                                } else if (!source.equals("null") && priority.equals("null") && !tickettype.equals("null") && !assigned.equals("null")) {
                                    new FetchFirstFilter(getActivity(), "show=" + show + "&departments=all&source=" + source + "&types=" + tickettype + "&assigned=" + unassigned, pageno).execute();
                                    url = "show=" + show + "&departments=all&source=" + source + "&types=" + tickettype + "&assigned=" + unassigned;
                                    Prefs.putString("URLFiltration", url);
                                } else if (source.equals("null") && !priority.equals("null") && !tickettype.equals("null") && !assigned.equals("null")) {
                                    new FetchFirstFilter(getActivity(), "show=" + show + "&departments=all&priority=" + priority + "&types=" + tickettype + "&assigned=" + unassigned, pageno).execute();
                                    url = "show=" + show + "&departments=all&priority=" + priority + "&types=" + tickettype + "&assigned=" + unassigned;
                                    Prefs.putString("URLFiltration", url);
                                } else if (!source.equals("null") && !priority.equals("null") && !tickettype.equals("null") && !assigned.equals("null")) {
                                    new FetchFirstFilter(getActivity(), "show=" + show + "&departments=all&priority=" + priority + "&types=" + tickettype + "&assigned=" + unassigned + "&source=" + source, pageno).execute();
                                    url = "show=" + show + "&departments=all&priority=" + priority + "&types=" + tickettype + "&assigned=" + unassigned + "&source=" + source;
                                    Prefs.putString("URLFiltration", url);
                                } else if (!assignto.equals("null")) {
                                    new FetchFirstFilter(getActivity(), "show=" + show + "&departments=all&assigned-to=a-" + assignto, pageno).execute();
                                    url = "show=" + show + "&departments=all&assigned-to=a-" + assignto;
                                    Prefs.putString("URLFiltration", url);
                                } else if (source.equals("null") && !priority.equals("null") && !tickettype.equals("null") && assigned.equals("null")) {
                                    new FetchFirstFilter(getActivity(), "show=" + show + "&departments=" + department + "&priority=" + priority + "&types=" + tickettype, pageno).execute();
                                    url = "show=" + show + "&departments=" + department + "&priority=" + priority + "&types=" + tickettype;
                                    Prefs.putString("URLFiltration", url);
                                }
                                else if (!source.equals("null") && !priority.equals("null") && !tickettype.equals("null") && !assigned.equals("null") && !assignto.equals("null")){
                                    new FetchFirstFilter(getActivity(), "show=" + show + "&departments=all&source=" + source + "&priority=" + priority+"&assigned-to="+assignto+"&assigned="+unassigned+"&types="+tickettype, pageno).execute();
                                    url = "show=" + show + "&departments=all&source=" + source + "&priority=" + priority+"&assigned-to="+assignto+"&assigned="+unassigned+"&types="+tickettype;
                                    Prefs.putString("URLFiltration", url);
                                }
                                else if (!show.equals("null")&&!department.equals("null")&&!source.equals("null")&&!priority.equals("null")&&!assigned.equals("null")){
                                    new FetchFirstFilter(getActivity(), "show=" + show + "&departments=all&priority=" + priority + "&source=" + source+"&assigned="+unassigned, pageno).execute();
                                    url = "show=" + show + "&departments=all&priority=" + priority + "&source=" + source+"&assigned="+unassigned;
                                    Prefs.putString("URLFiltration", url);
                                }



                            } else if (!department.equals("all")) {
                                if (source.equals("null") && priority.equals("null") && tickettype.equals("null") && assigned.equals("null") && assignto.equals("null")) {
                                    new FetchFirstFilter(getActivity(), "show=" + show + "&departments=" + department, pageno).execute();
                                    url = "show=" + show + "&departments=" + department;
                                    Prefs.putString("URLFiltration", url);
                                } else if (!source.equals("null") && priority.equals("null") && tickettype.equals("null") && assigned.equals("null") && assignto.equals("null")) {
                                    new FetchFirstFilter(getActivity(), "show=" + show + "&departments=" + department + "&source=" + source, pageno).execute();
                                    url = "show=" + show + "&departments=" + department + "&source=" + source;
                                    Prefs.putString("URLFiltration", url);
                                } else if (source.equals("null") && !priority.equals("null") && tickettype.equals("null") && assigned.equals("null") && assignto.equals("null")) {
                                    new FetchFirstFilter(getActivity(), "show=" + show + "&departments=" + department + "&priority=" + priority, pageno).execute();
                                    url = "show=" + show + "&departments=" + department + "&priority=" + priority;
                                    Prefs.putString("URLFiltration", url);
                                } else if (source.equals("null") && priority.equals("null") && !tickettype.equals("null") && assigned.equals("null") && assignto.equals("null")) {
                                    new FetchFirstFilter(getActivity(), "show=" + show + "&departments=" + department + "&types=" + tickettype, pageno).execute();
                                    url = "show=" + show + "&departments=" + department + "&types=" + tickettype;
                                    Prefs.putString("URLFiltration", url);
                                } else if (source.equals("null") && priority.equals("null") && tickettype.equals("null") && !assigned.equals("null") && assignto.equals("null")) {
                                    new FetchFirstFilter(getActivity(), "show=" + show + "&departments=" + department + "&assigned=" + unassigned, pageno).execute();
                                    url = "show=" + show + "&departments=" + department + "&assigned=" + unassigned;
                                    Prefs.putString("URLFiltration", url);
                                } else if (source.equals("null") && priority.equals("null") && tickettype.equals("null") && assigned.equals("null") && !assignto.equals("null")) {
                                    new FetchFirstFilter(getActivity(), "show=" + show + "&departments=" + department + "&assigned-to=" + assignto, pageno).execute();
                                    url = "show=" + show + "&departments=" + department + "&source=" + source;
                                    Prefs.putString("URLFiltration", url);
                                } else if (!source.equals("null") && priority.equals("null") && tickettype.equals("null") && assigned.equals("null") && !assignto.equals("null")) {
                                    new FetchFirstFilter(getActivity(), "show=" + show + "&departments=" + department + "&assigned-to=" + assignto + "&source=" + source, pageno).execute();
                                    url = "show=" + show + "&departments=" + department + "&assigned-to=" + assignto + "&source=" + source;
                                    Prefs.putString("URLFiltration", url);
                                } else if (source.equals("null") && !priority.equals("null") && tickettype.equals("null") && assigned.equals("null") && !assignto.equals("null")) {
                                    new FetchFirstFilter(getActivity(), "show=" + show + "&departments=" + department + "&assigned-to=" + assignto + "&priority=" + priority, pageno).execute();
                                    url = "show=" + show + "&departments=" + department + "&assigned-to=" + assignto + "&priority=" + priority;
                                    Prefs.putString("URLFiltration", url);
                                } else if (source.equals("null") && priority.equals("null") && !tickettype.equals("null") && assigned.equals("null") && !assignto.equals("null")) {
                                    new FetchFirstFilter(getActivity(), "show=" + show + "&departments=" + department + "&assigned-to=" + assignto + "&types=" + tickettype, pageno).execute();
                                    url = "show=" + show + "&departments=" + department + "&assigned-to=" + assignto + "&types=" + tickettype;
                                    Prefs.putString("URLFiltration", url);
                                } else if (source.equals("null") && priority.equals("null") && tickettype.equals("null") && !assigned.equals("null") && !assignto.equals("null")) {
                                    new FetchFirstFilter(getActivity(), "show=" + show + "&departments=" + department + "&assigned-to=" + assignto + "&assigned=" + unassigned, pageno).execute();
                                    url = "show=" + show + "&departments=" + department + "&assigned-to=" + assignto + "&assigned=" + unassigned;
                                    Prefs.putString("URLFiltration", url);
                                } else if (!source.equals("null") && !priority.equals("null") && tickettype.equals("null") && assigned.equals("null") && !assignto.equals("null")) {
                                    new FetchFirstFilter(getActivity(), "show=" + show + "&departments=" + department + "&assigned-to=" + assignto + "&source=" + source + "&priority=" + priority, pageno).execute();
                                    url = "show=" + show + "&departments=" + department + "&assigned-to=" + assignto + "&source=" + source + "&priority=" + priority;
                                    Prefs.putString("URLFiltration", url);
                                } else if (!source.equals("null") && priority.equals("null") && !tickettype.equals("null") && assigned.equals("null") && !assignto.equals("null")) {
                                    new FetchFirstFilter(getActivity(), "show=" + show + "&departments=" + department + "&assigned-to=" + assignto + "&source=" + source + "&types=" + tickettype, pageno).execute();
                                    url = "show=" + show + "&departments=" + department + "&assigned-to=" + assignto + "&source=" + source + "&types=" + tickettype;
                                    Prefs.putString("URLFiltration", url);
                                } else if (!source.equals("null") && priority.equals("null") && tickettype.equals("null") && !assigned.equals("null") && !assignto.equals("null")) {
                                    new FetchFirstFilter(getActivity(), "show=" + show + "&departments=" + department + "&assigned-to=" + assignto + "&source=" + source + "&assigned=" + unassigned, pageno).execute();
                                    url = "show=" + show + "&departments=" + department + "&assigned-to=" + assignto + "&source=" + source + "&assigned=" + unassigned;
                                    Prefs.putString("URLFiltration", url);
                                } else if (source.equals("null") && !priority.equals("null") && !tickettype.equals("null") && assigned.equals("null") && !assignto.equals("null")) {
                                    new FetchFirstFilter(getActivity(), "show=" + show + "&departments=" + department + "&assigned-to=" + assignto + "&priority=" + priority + "&types=" + tickettype, pageno).execute();
                                    url = "show=" + show + "&departments=" + department + "&assigned-to=" + assignto + "&priority=" + priority + "&types=" + tickettype;
                                    Prefs.putString("URLFiltration", url);
                                } else if (source.equals("null") && !priority.equals("null") && tickettype.equals("null") && !assigned.equals("null") && !assignto.equals("null")) {
                                    new FetchFirstFilter(getActivity(), "show=" + show + "&departments=" + department + "&assigned-to=" + assignto + "&priority=" + priority + "&assigned=" + unassigned, pageno).execute();
                                    url = "show=" + show + "&departments=" + department + "&assigned-to=" + assignto + "&priority=" + priority + "&assigned=" + unassigned;
                                    Prefs.putString("URLFiltration", url);
                                } else if (source.equals("null") && priority.equals("null") && !tickettype.equals("null") && !assigned.equals("null") && !assignto.equals("null")) {
                                    new FetchFirstFilter(getActivity(), "show=" + show + "&departments=" + department + "&assigned-to=" + assignto + "&types=" + tickettype + "&assigned=" + unassigned, pageno).execute();
                                    url = "show=" + show + "&departments=" + department + "&assigned-to=" + assignto + "&types=" + tickettype + "&assigned=" + unassigned;
                                    Prefs.putString("URLFiltration", url);
                                } else if (!source.equals("null") && !priority.equals("null") && !tickettype.equals("null") && assigned.equals("null") && !assignto.equals("null")) {
                                    new FetchFirstFilter(getActivity(), "show=" + show + "&departments=" + department + "&assigned-to=" + assignto + "&types=" + tickettype + "&source=" + source + "&priority=" + priority, pageno).execute();
                                    url = "show=" + show + "&departments=" + department + "&assigned-to=" + assignto + "&types=" + tickettype + "&source=" + source + "&priority=" + priority;
                                    Prefs.putString("URLFiltration", url);
                                } else if (!source.equals("null") && priority.equals("null") && !tickettype.equals("null") && !assigned.equals("null") && !assignto.equals("null")) {
                                    new FetchFirstFilter(getActivity(), "show=" + show + "&departments=" + department + "&assigned-to=" + assignto + "&types=" + tickettype + "&source=" + source + "&assigned=" + unassigned, pageno).execute();
                                    url = "show=" + show + "&departments=" + department + " &assigned-to=" + assignto + "&types=" + tickettype + "&source=" + source + "&assigned=" + unassigned;
                                    Prefs.putString("URLFiltration", url);
                                } else if (source.equals("null") && !priority.equals("null") && !tickettype.equals("null") && !assigned.equals("null") && !assignto.equals("null")) {
                                    new FetchFirstFilter(getActivity(), "show=" + show + "&departments=" + department + "&assigned-to=" + assignto + "&types=" + tickettype + "&priority=" + priority + "&assigned=" + unassigned, pageno).execute();
                                    url = "show=" + show + "&departments=" + department + "&assigned-to=" + assignto + "&types=" + tickettype + "&priority=" + priority + "&assigned=" + unassigned;
                                    Prefs.putString("URLFiltration", url);
                                } else if (!source.equals("null") && !priority.equals("null") && tickettype.equals("null") && assigned.equals("null") && assignto.equals("null")) {
                                    new FetchFirstFilter(getActivity(), "show=" + show + "&departments=" + department + "&source=" + source + "&priority=" + priority, pageno).execute();
                                    url = "show=" + show + "&departments=" + department + "&source=" + source + "&priority=" + priority;
                                    Prefs.putString("URLFiltration", url);
                                } else if (source.equals("null") && priority.equals("null") && !tickettype.equals("null") && !assigned.equals("null")) {
                                    new FetchFirstFilter(getActivity(), "show=" + show + "&departments=" + department + "&assigned=" + unassigned + "&types=" + tickettype, pageno).execute();
                                    url = "show=" + show + "&departments=" + department + "&assigned=" + unassigned + "&types=" + tickettype;
                                    Prefs.putString("URLFiltration", url);
                                } else if (!source.equals("null") && priority.equals("null") && tickettype.equals("null") && !assigned.equals("null")) {
                                    new FetchFirstFilter(getActivity(), "show=" + show + "&departments=" + department + "&assigned=" + unassigned + "&source=" + source + "&assigned=" + unassigned, pageno).execute();
                                    url = "show=" + show + "&departments=" + department + "&assigned=" + unassigned + "&source=" + source + "&assigned=" + unassigned;
                                    Prefs.putString("URLFiltration", url);
                                } else if (!source.equals("null") && priority.equals("null") && !tickettype.equals("null") && assigned.equals("null")) {
                                    new FetchFirstFilter(getActivity(), "show=" + show + "&departments=" + department + "&source=" + source + "&types=" + tickettype, pageno).execute();
                                    url = "show=" + show + "&departments=" + department + "&source=" + source + "&types=" + tickettype;
                                    Prefs.putString("URLFiltration", url);
                                } else if (source.equals("null") && !priority.equals("null") && tickettype.equals("null") && !assigned.equals("null")) {
                                    new FetchFirstFilter(getActivity(), "show=" + show + "&departments=" + department + "&assigned=" + unassigned + "&priority=" + priority, pageno).execute();
                                    url = "show=" + show + "&departments=" + department + "&assigned=" + unassigned + "&priority=" + priority;
                                    Prefs.putString("URLFiltration", url);
                                } else if (!source.equals("null") && !priority.equals("null") && !tickettype.equals("null") && assigned.equals("null")) {
                                    new FetchFirstFilter(getActivity(), "show=" + show + "&departments=" + department + "&source=" + source + "&priority=" + priority + "&types=" + tickettype, pageno).execute();
                                    url = "show=" + show + "&departments=" + department + "&source=" + source + "&priority=" + priority + "&types=" + tickettype;
                                    Prefs.putString("URLFiltration", url);
                                } else if (!source.equals("null") && priority.equals("null") && !tickettype.equals("null") && !assigned.equals("null")) {
                                    new FetchFirstFilter(getActivity(), "show=" + show + "&departments=" + department + "&source=" + source + "&types=" + tickettype + "&assigned=" + unassigned, pageno).execute();
                                    url = "show=" + show + "&departments=" + department + "&source=" + source + "&types=" + tickettype + "&assigned=" + unassigned;
                                    Prefs.putString("URLFiltration", url);
                                } else if (source.equals("null") && !priority.equals("null") && !tickettype.equals("null") && !assigned.equals("null")) {
                                    new FetchFirstFilter(getActivity(), "show=" + show + "&departments=" + department + "&priority=" + priority + "&types=" + tickettype + "&assigned=" + unassigned, pageno).execute();
                                    url = "show=" + show + "&departments=" + department + "&priority=" + priority + "&types=" + tickettype + "&assigned=" + unassigned;
                                    Prefs.putString("URLFiltration", url);
                                } else if (!source.equals("null") && !priority.equals("null") && !tickettype.equals("null") && !assigned.equals("null")) {
                                    new FetchFirstFilter(getActivity(), "show=" + show + "&departments=" + department + "&priority=" + priority + "&types=" + tickettype + "&assigned=" + unassigned + "&source=" + source, pageno).execute();
                                    url = "show=" + show + "&departments=" + department + "&priority=" + priority + "&types=" + tickettype + "&assigned=" + unassigned + "&source=" + source;
                                    Prefs.putString("URLFiltration", url);
                                } else if (source.equals("null") && !priority.equals("null") && !tickettype.equals("null") && assigned.equals("null")) {
                                    new FetchFirstFilter(getActivity(), "show=" + show + "&departments=" + department + "&priority=" + priority + "&types=" + tickettype, pageno).execute();
                                    url = "show=" + show + "&departments=" + department + "&priority=" + priority + "&types=" + tickettype;
                                    Prefs.putString("URLFiltration", url);
                                }
                                else if (!source.equals("null") && !priority.equals("null") && !tickettype.equals("null") && !assigned.equals("null") && !assignto.equals("null")){
                                    new FetchFirstFilter(getActivity(), "show=" + show + "&departments="+department+"&source=" + source + "&priority=" + priority+"&assigned-to="+assignto+"&assigned="+unassigned+"&types="+tickettype, pageno).execute();
                                    url = "show=" + show + "&departments="+department+"&source=" + source + "&priority=" + priority+"&assigned-to="+assignto+"&assigned="+unassigned+"&types="+tickettype;
                                    Prefs.putString("URLFiltration", url);
                                }
                                else if (!show.equals("null")&&!department.equals("null")&&!source.equals("null")&&!priority.equals("null")&&!assigned.equals("null")){
                                    new FetchFirstFilter(getActivity(), "show=" + show + "&departments="+department+"&priority=" + priority + "&source=" + source+"&assigned="+unassigned, pageno).execute();
                                    url = "show=" + show + "&departments="+department+"&priority=" + priority + "&source=" + source+"&assigned="+unassigned;
                                    Prefs.putString("URLFiltration", url);
                                }


//                        else if (!assignto.equals("null")){
//                            new FetchFirstFilter(getActivity(),"show="+show+"&departments=all&assigned-to=a-"+assignto,pageno).execute();
//                            url="show="+show+"&departments=all&assigned-to=a-"+assignto;
//                        }
                            }

//                    else if (!assignto.equals("null")){
//                        new FetchFirstFilter(getActivity(),"show="+show+"&departments="+department+"&assigned-to=a-"+assignto,pageno).execute();
//                        url="show="+show+"&departments="+department+"&assigned-to=a-"+assignto;
//                    }

                        } else if (check.equals("false")) {
                            noInternet_view.setVisibility(View.GONE);
                            // swipeRefresh.setRefreshing(true);
                            progressDialog.show();
                            new FetchFirst(getActivity(), page).execute();
                            ((MainActivity) getActivity()).setActionBarTitle("Inbox");

                        }
//                        recyclerView.setVisibility(View.VISIBLE);
//                        noInternet_view.setVisibility(View.GONE);
//                        new FetchFirst(getActivity()).execute();
                    } else {
                        recyclerView.setVisibility(View.INVISIBLE);
                        swipeRefresh.setRefreshing(false);
                        empty_view.setVisibility(View.GONE);
                        noInternet_view.setVisibility(View.VISIBLE);
                    }

                }
            });

//            toolbarmain.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
//                @Override
//                public boolean onMenuItemClick(MenuItem item) {
//                    int id=item.getItemId();
//                    if (id==R.id.action_statusClosed){
//                        Toast.makeText(getActivity(), "clicked", Toast.LENGTH_SHORT).show();
//                        return true;
//                    }
//                    if (id==R.id.action_noti){
//                        Intent intent = new Intent(getActivity(), NotificationActivity.class);
//                        startActivity(intent);
//                        return true;
//                    }
//                    return false;
//                }
//            });
            ticketOverviewAdapter = new TicketOverviewAdapter(context, ticketOverviewList);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setAdapter(ticketOverviewAdapter);
//            recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(context, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
//                @Override
//                public void onItemClick(View view, int position) {
//                    if (isMultiSelect) {
//                        //if multiple selection is enabled then select item on single click else perform normal click on item.
//                        multiSelect(position);
//                    }
//                    else{
//                        Intent intent = new Intent(getActivity(), TicketDetailActivity.class);
//                        intent.putExtra("ticket_id", ticketOverview.ticketID + "");
//                        Prefs.putString("TICKETid",ticketOverview.ticketID+"");
//                        Prefs.putString("ticketstatus",ticketOverview.getTicketStatus());
//                        intent.putExtra("ticket_number", ticketOverview.ticketNumber);
//                        intent.putExtra("ticket_opened_by", ticketOverview.clientName);
//                        intent.putExtra("ticket_subject", ticketOverview.ticketSubject);
//                        startActivity(intent);
//
//                    }
//                }
//
//                @Override
//                public void onItemLongClick(View view, int position) {
//                    if (!isMultiSelect) {
//                        checked_items = new ArrayList<>();
//                        isMultiSelect = true;
//
//                        if (actionMode == null) {
//                            //toolbarmain.setVisibility(View.GONE);
//                            actionMode = getActivity().startActionMode(mActionModeCallback); //show ActionMode.
//
//                        }
//                    }
//
//
//
//                    multiSelect(position);
//                }
//
//
//            }));



            //((MainActivity) getActivity()).setActionBarTitle("Inbox");

        }
        return rootView;
    }

//    private void multiSelect(int position) {
//        TicketOverview data = ticketOverviewAdapter.getItem(position);
//        if (data != null) {
//            if (actionMode != null) {
//                if (checked_items.contains(data.getTicketID()))
//                    checked_items.remove(Integer.valueOf(data.getTicketID()));
//                else
//                    checked_items.add(data.getTicketID());
//
//                if (checked_items.size() > 0)
//                    actionMode.setTitle(String.valueOf(checked_items.size())); //show selected item count on action mode.
//                else {
//                    actionMode.setTitle(""); //remove item count from action mode.
//                    actionMode.finish(); //hide action mode.
//                }
//                ticketOverviewAdapter.setSelectedIds(checked_items);
//
//            }
//        }
//    }

    public void setTitle(String title) {
        toolbar.setTitle(title);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    getActivity().getMenuInflater().inflate(R.menu.menu_inbox, menu);





//        }else{
//            getActivity().getMenuInflater().inflate(R.menu.search_menu, menu);
//            //toolbarmain.inflateMenu(R.menu.search_menu);
//            toolbarmain.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
//                @Override
//                public boolean onMenuItemClick(MenuItem item) {
//                    int id=item.getItemId();
//                    if (id==R.id.action_noti){
//                        Intent intent = new Intent(getActivity(), NotificationActivity.class);
//                        startActivity(intent);
//                        return true;
//                    }
//                    if (id==R.id.action_statusClosed){
//                        Toast.makeText(getActivity(), "clicked", Toast.LENGTH_SHORT).show();
//                        return true;
//                    }
//                    return false;
//                }
//            });


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            if (item != null) {
                item.getSubMenu().clearHeader();
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        int id = item.getItemId();
        StringBuffer stringBuffer = new StringBuffer();
        if (id == R.id.action_statusClosed) {

            try {
                if (!Prefs.getString("tickets", null).isEmpty()) {
                    String tickets = Prefs.getString("tickets", null);
                    int pos = tickets.indexOf("[");
                    int pos1 = tickets.lastIndexOf("]");
                    String text1 = tickets.substring(pos + 1, pos1);
                    String[] namesList = text1.split(",");
                    for (String name : namesList) {
                        stringBuffer.append(name + ",");
                    }
                    int pos2 = stringBuffer.toString().lastIndexOf(",");
                    ticket = stringBuffer.toString().substring(0, pos2);

                    Log.d("tickets", ticket);
                    try {
                        new StatusChange(ticket, Integer.parseInt(Prefs.getString("closedid", null))).execute();
                        Prefs.putString("tickets", null);
                        progressDialog.show();
                        progressDialog.setMessage(getString(R.string.pleasewait));
                    } catch (NumberFormatException e) {
                        e.printStackTrace();

                    }
                    return true;
                } else {
                    Toasty.info(getActivity(), getString(R.string.noticket), Toast.LENGTH_LONG).show();
                    return false;
                }
            } catch (NullPointerException e) {
                Toasty.info(getActivity(), getString(R.string.noticket), Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
            if (!Prefs.getString("tickets", null).equals("") || !Prefs.getString("tickets", null).equals("null") || !Prefs.getString("tickets", null).equals(null)) {


                Log.d("tickets", ticket);
                if (ticket.equals("") || ticket.equals(null)) {
                    Toasty.warning(getActivity(), getString(R.string.noticket), Toast.LENGTH_SHORT).show();
                    return false;
                } else {



                }

            }
        }
 else if (id == R.id.action_statusResolved) {
            try {
                if (!Prefs.getString("tickets", null).isEmpty()) {
                    String tickets = Prefs.getString("tickets", null);
                    int pos = tickets.indexOf("[");
                    int pos1 = tickets.lastIndexOf("]");
                    String text1 = tickets.substring(pos + 1, pos1);
                    String[] namesList = text1.split(",");
                    for (String name : namesList) {
                        stringBuffer.append(name + ",");
                    }
                    int pos2 = stringBuffer.toString().lastIndexOf(",");
                    ticket = stringBuffer.toString().substring(0, pos2);

                    Log.d("tickets", ticket);
                    try {
                        new StatusChange(ticket, Integer.parseInt(Prefs.getString("resolvedid", null))).execute();
                        Prefs.putString("tickets", null);
                        progressDialog.show();
                        progressDialog.setMessage(getString(R.string.pleasewait));
                    } catch (NumberFormatException e) {
                        e.printStackTrace();

                    }
                    return true;
                } else {
                    Toasty.info(getActivity(), getString(R.string.noticket), Toast.LENGTH_LONG).show();
                    return false;
                }
            } catch (NullPointerException e) {
                Toasty.info(getActivity(), getString(R.string.noticket), Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
else if (id == R.id.action_statusDeleted) {
            try {
                if (!Prefs.getString("tickets", null).isEmpty()) {
                    String tickets = Prefs.getString("tickets", null);
                    int pos = tickets.indexOf("[");
                    int pos1 = tickets.lastIndexOf("]");
                    String text1 = tickets.substring(pos + 1, pos1);
                    String[] namesList = text1.split(",");
                    for (String name : namesList) {
                        stringBuffer.append(name + ",");
                    }
                    int pos2 = stringBuffer.toString().lastIndexOf(",");
                    ticket = stringBuffer.toString().substring(0, pos2);

                    Log.d("tickets", ticket);
                    try {
                        new StatusChange(ticket, Integer.parseInt(Prefs.getString("deletedid", null))).execute();
                        Prefs.putString("tickets", null);
                        progressDialog.show();
                        progressDialog.setMessage(getString(R.string.pleasewait));
                    } catch (NumberFormatException e) {
                        e.printStackTrace();

                    }
                    return true;
                } else {
                    Toasty.info(getActivity(), getString(R.string.noticket), Toast.LENGTH_LONG).show();
                    return false;
                }
            } catch (NullPointerException e) {
                Toasty.info(getActivity(), getString(R.string.noticket), Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
 if (id == R.id.action_noti) {
            Intent intent = new Intent(getActivity(), NotificationActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

//    @Override
//    public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
//        getActivity().findViewById(R.id.toolbar).setVisibility(View.GONE);
//
////        getActivity().invalidateOptionsMenu();
////        actionMode.getMenuInflater().inflate(R.menu.search_menu, menu);
////        getActivity().findViewById(R.id.toolbar).setVisibility(View.VISIBLE);
//        //MenuInflater inflater = actionMode.getMenuInflater();
//        //inflater.inflate(R.menu.search_menu, menu);
//
//        //toolbarmain.getMenu().clear();
//        //toolbarmain.inflateMenu(R.menu.search_menu);
//        return true;
//    }
//
//    @Override
//    public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
//        return false;
//    }
//
//    @Override
//    public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
//        int id=menuItem.getItemId();
//        if (id==R.id.action_statusClosed){
//            Log.d("came here","TRUE");
//            Toast.makeText(getActivity(), "clicked", Toast.LENGTH_SHORT).show();
//            actionMode.finish();
//        }
//       return false;
//    }
//
//    @Override
//    public void onDestroyActionMode(ActionMode actionMode) {
//        actionMode = null;
//        //toolbarmain.getMenu().clear();
//        //toolbarmain.inflateMenu(R.menu.menu_inbox);
//        //toolbarmain.setVisibility(View.VISIBLE);
//        isMultiSelect = false;
//        getActivity().findViewById(R.id.toolbar).setVisibility(View.VISIBLE);
//        checked_items = new ArrayList<>();
//        ticketOverviewAdapter.setSelectedIds(new ArrayList<Integer>());
//    }

//    @Override
//    public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
//
//        MenuInflater inflater = actionMode.getMenuInflater();
//        inflater.inflate(R.menu.create_ticket_menu, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
//        return false;
//    }
//
//    @Override
//    public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
//        switch (menuItem.getItemId()){
//            case R.id.action_attach:
//                //just to show selected items.
//
//                //Toast.makeText(this, "Selected items are :", Toast.LENGTH_SHORT).show();
//                Toast.makeText(getActivity(), "action mode", Toast.LENGTH_SHORT).show();
//                return true;
//        }
//        return false;
//    }
//
//    @Override
//    public void onDestroyActionMode(ActionMode actionMode) {
//        actionMode = null;
//        isMultiSelect = false;
//        checked_items = new ArrayList<>();
//        ticketOverviewAdapter.setSelectedIds(new ArrayList<Integer>());
//    }


    //    public class ReadFromDatabase extends AsyncTask<String, Void, String> {
//        Context context;
//
//        public ReadFromDatabase(Context context) {
//            this.context = context;
//        }
//
//        protected String doInBackground(String... urls) {
//            DatabaseHandler databaseHandler = new DatabaseHandler(context);
//            ticketOverviewList = databaseHandler.getTicketOverview();
//            databaseHandler.close();
//            return "success";
//        }
//
//        protected void onPostExecute(String result) {
//            if (swipeRefresh.isRefreshing())
//                swipeRefresh.setRefreshing(false);
//            if (progressDialog.isShowing())
//                progressDialog.dismiss();
//            ticketOverviewAdapter = new TicketOverviewAdapter(ticketOverviewList);
//            recyclerView.setAdapter(ticketOverviewAdapter);
//            if (ticketOverviewAdapter.getItemCount() == 0) {
//                tv.setVisibility(View.VISIBLE);
//            } else tv.setVisibility(View.GONE);
//        }
//    }

    private class StatusChange extends AsyncTask<String, Void, String> {
        int statusId;
        String ticketId;

        StatusChange(String ticketId, int statusId) {

            this.ticketId = ticketId;
            this.statusId = statusId;

        }

        protected String doInBackground(String... urls) {
            return new Helpdesk().postStatusChangedMultiple(ticketId, statusId);
            //return new Helpdesk().postStatusChanged(ticketId,statusId);
        }

        protected void onPostExecute(String result) {
            progressDialog.dismiss();
            String state = Prefs.getString("403", null);
            //progressDialog.dismiss();
            if (result == null) {
                Toasty.error(getActivity(), getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                return;
            }

            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONObject jsonObject1 = jsonObject.getJSONObject("response");
                JSONArray jsonArray=jsonObject1.getJSONArray("message");
                for (int i=0;i<jsonArray.length();i++){
                    String message=jsonArray.getString(i);
                    if (message.equals("Permission denied, you do not have permission to access the requested page.")){
                        Toasty.warning(getActivity(), getString(R.string.permission), Toast.LENGTH_LONG).show();
                        Prefs.putString("403", "null");
                        return;
                    }
                }

            }catch (JSONException e){
                e.printStackTrace();
            }


            try {

                JSONObject jsonObject = new JSONObject(result);
                //JSONObject jsonObject1 = jsonObject.getJSONObject("response");
                JSONObject jsonObject2 = jsonObject.getJSONObject("error");
                JSONArray jsonArray = jsonObject2.getJSONArray("ticket_id");
                String value = jsonArray.getString(0);
                Log.d("VALUE", value);
                //String message1=jsonObject2.getString("ticket_id");
                //String message2 = jsonObject1.getString("message");


//                if (message2.contains("permission denied")&&Prefs.getString("403",null).equals("403")){
//
//                }
                if (value.contains("The ticket id field is required.")) {
                    Toasty.info(getActivity(), getString(R.string.noticket), Toast.LENGTH_LONG).show();
                    return;
                }
//               else if (message2.contains("Status changed to Deleted")) {
//                    Toasty.success(getActivity(), getString(R.string.status_deleted), Toast.LENGTH_LONG).show();
//                    Prefs.putString("tickets", null);
//                    //Prefs.putString("ticketstatus", "Deleted");
//                    getActivity().finish();
//                    startActivity(new Intent(getActivity(), MainActivity.class));
//                } else if (message2.contains("Status changed to Open")) {
//                    Toasty.success(getActivity(), getString(R.string.status_opened), Toast.LENGTH_LONG).show();
//                    Prefs.putString("tickets", null);
//                    getActivity().finish();
//                    startActivity(new Intent(getActivity(), MainActivity.class));
//                } else if (message2.contains("Status changed to Closed")) {
//                    Toasty.success(getActivity(), getString(R.string.status_closed), Toast.LENGTH_LONG).show();
//                    Prefs.putString("tickets", null);
//                    getActivity().finish();
//                    startActivity(new Intent(getActivity(), MainActivity.class));
//                } else if (message2.contains("Status changed to Resolved")) {
//                    Toasty.success(getActivity(), getString(R.string.status_resolved), Toast.LENGTH_LONG).show();
//                    Prefs.putString("tickets", null);
//                    getActivity().finish();
//                    startActivity(new Intent(getActivity(), MainActivity.class));
//                }


            } catch (JSONException | NullPointerException e) {
                e.printStackTrace();

            }
            try {

                JSONObject jsonObject = new JSONObject(result);
                JSONObject jsonObject1 = jsonObject.getJSONObject("response");
                //String message1=jsonObject2.getString("ticket_id");
                String message2 = jsonObject1.getString("message");


                if (message2.contains("Status changed to Deleted")) {
                    Toasty.success(getActivity(), getString(R.string.status_deleted), Toast.LENGTH_LONG).show();
                    Prefs.putString("tickets", null);
                    //Prefs.putString("ticketstatus", "Deleted");
                    getActivity().finish();
                    startActivity(new Intent(getActivity(), MainActivity.class));
                }
                if (message2.contains("Status changed to Open")) {
                    Toasty.success(getActivity(), getString(R.string.status_opened), Toast.LENGTH_LONG).show();
                    Prefs.putString("tickets", null);
                    getActivity().finish();
                    startActivity(new Intent(getActivity(), MainActivity.class));
                }
                if (message2.contains("Status changed to Closed")) {
                    Toasty.success(getActivity(), getString(R.string.status_closed), Toast.LENGTH_LONG).show();
                    Prefs.putString("tickets", null);
                    getActivity().finish();
                    startActivity(new Intent(getActivity(), MainActivity.class));
                }
                if (message2.contains("Status changed to Resolved")) {
                    Toasty.success(getActivity(), getString(R.string.status_resolved), Toast.LENGTH_LONG).show();
                    Prefs.putString("tickets", null);
                    getActivity().finish();
                    startActivity(new Intent(getActivity(), MainActivity.class));
                }


            } catch (JSONException | NullPointerException e) {
                e.printStackTrace();

            }


        }


    }


    /**
     * Fetch the first page tickets
     */
    private class FetchFirst extends AsyncTask<String, Void, String> {
        Context context;
        int page;

        FetchFirst(Context context, int page) {
            this.context = context;
            this.page = page;
        }

        protected String doInBackground(String... urls) {
//            if (nextPageURL.equals("null")) {
//                return "all done";
//            }
            String result = new Helpdesk().getInboxTicket(page);
            if (result == null)
                return null;
            String data;
            ticketOverviewList.clear();
            try {
                JSONObject jsonObject = new JSONObject(result);
                total = jsonObject.getInt("total");
                try {
                    data = jsonObject.getString("data");
                    nextPageURL = jsonObject.getString("next_page_url");
                } catch (JSONException e) {
                    data = jsonObject.getString("result");
                }
                JSONArray jsonArray = new JSONArray(data);
                for (int i = 0; i < jsonArray.length(); i++) {
                     ticketOverview = Helper.parseTicketOverview(jsonArray, i);
                    if (ticketOverview != null)
                        ticketOverviewList.add(ticketOverview);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return "success";
        }

        protected void onPostExecute(String result) {
//            ticketOverviewAdapter.notifyDataSetChanged();
            //progressBar.setVisibility(View.GONE);
            Prefs.putString("filterwithsorting", "false");
            progressDialog.dismiss();

            textView.setText("" + total + " tickets");
            if (swipeRefresh.isRefreshing())
                swipeRefresh.setRefreshing(false);

            if (result == null) {
                Toasty.error(getActivity(), getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                return;
            }
//            if (result.equals("all done")) {
//                Toast.makeText(context, "All tickets loaded", Toast.LENGTH_SHORT).show();
//                return;
//            }


            if (result.equals("all done")) {

                Toasty.info(context, getString(R.string.all_caught_up), Toast.LENGTH_SHORT).show();
                //return;
            }
            //  recyclerView = (ShimmerRecyclerView) rootView.findViewById(R.id.cardList);
            recyclerView.setHasFixedSize(false);
            final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    if (dy > 0) {
                        visibleItemCount = linearLayoutManager.getChildCount();
                        totalItemCount = linearLayoutManager.getItemCount();
                        pastVisibleItems = linearLayoutManager.findFirstVisibleItemPosition();
                        if (loading) {
                            if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                                loading = false;
                                pageNo++;
                                new FetchNextPage(getActivity(), pageNo, "inbox").execute();
                                StyleableToast st = new StyleableToast(getContext(), getString(R.string.loading), Toast.LENGTH_SHORT);
                                st.setBackgroundColor(Color.parseColor("#3da6d7"));
                                st.setTextColor(Color.WHITE);
                                st.setIcon(R.drawable.ic_autorenew_black_24dp);
                                st.spinIcon();
                                st.setMaxAlpha();
                                st.show();
                                //Toast.makeText(getActivity(), "Loading!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
            });

            ticketOverviewAdapter = new TicketOverviewAdapter(getContext(), ticketOverviewList);
            recyclerView.setAdapter(ticketOverviewAdapter);

            if (ticketOverviewAdapter.getItemCount() == 0) {
                empty_view.setVisibility(View.VISIBLE);
            } else empty_view.setVisibility(View.GONE);
        }
    }

    /**
     * Fetch the nextpage tickets by next_pageUrl.
     */
    private class FetchNextPage extends AsyncTask<String, Void, String> {
        Context context;
        int page;
        String show;

        FetchNextPage(Context context, int page, String show) {
            this.context = context;
            this.page = page;
            this.show = show;
        }

        protected String doInBackground(String... urls) {
            if (nextPageURL.equals("null")) {
                pageNo=1;
                return "all done";
            }
            String result = new Helpdesk().nextpageurl(show, page);
            if (result == null)
                return null;
            // DatabaseHandler databaseHandler = new DatabaseHandler(context);
            //databaseHandler.recreateTable();
            try {
                JSONObject jsonObject = new JSONObject(result);
                nextPageURL = jsonObject.getString("next_page_url");
                String data = jsonObject.getString("data");
                int count = jsonObject.getInt("total");
                if (count > 999)
                    Prefs.putString("inboxTickets", "999+");
                else
                    Prefs.putString("inboxTickets", count + "");

                JSONArray jsonArray = new JSONArray(data);
                for (int i = 0; i < jsonArray.length(); i++) {
                    TicketOverview ticketOverview = Helper.parseTicketOverview(jsonArray, i);
                    if (ticketOverview != null) {
                        ticketOverviewList.add(ticketOverview);
                        // databaseHandler.addTicketOverview(ticketOverview);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            // databaseHandler.close();
            return "success";
        }

        protected void onPostExecute(String result) {
            Prefs.putString("filterwithsorting", "false");
            if (result == null) {
                Toasty.error(getActivity(), getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                return;
            }
            if (result.equals("all done")) {
                pageNo=1;
                Toasty.info(context, getString(R.string.all_caught_up), Toast.LENGTH_SHORT).show();
                return;
            }
            ticketOverviewAdapter.notifyDataSetChanged();
            loading = true;
        }
    }

    private class FetchFirstFilter extends AsyncTask<String, Void, String> {
        Context context;
        int page;
        String url;

        FetchFirstFilter(Context context, String url, int page) {
            this.context = context;
            this.url = url;
            this.page = page;

        }

        protected String doInBackground(String... urls) {
//            if (nextPageURL.equals("null")) {
//                return "all done";
//            }

            String result = new Helpdesk().ticketFiltration(url, page);

            if (result == null)
                return null;

            ticketOverviewList.clear();
            try {
                JSONObject jsonObject = new JSONObject(result);
                total = jsonObject.getInt("total");
                nextPageURL = jsonObject.getString("next_page_url");
//                try {
//                    data = jsonObject.getString("data");
//
//                } catch (JSONException e) {
//                    data = jsonObject.getString("result");
//                }
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                for (int i = 0; i < jsonArray.length(); i++) {
                    TicketOverview ticketOverview = Helper.parseTicketOverviewSort(jsonArray, i);
                    if (ticketOverview != null)
                        ticketOverviewList.add(ticketOverview);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return "success";
        }

        protected void onPostExecute(String result) {
            Prefs.putString("URLFiltration", url);
            Prefs.putString("came from filter", "false");
            Prefs.putString("filterwithsorting", "true");
            Log.d("URL", url);
            progressDialog.dismiss();
            textView.setText("" + total + " tickets");
            if (swipeRefresh.isRefreshing())
                swipeRefresh.setRefreshing(false);

            if (result == null) {
                Toasty.error(getActivity(), getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                return;
            }
            if (result.equals("all done")) {

                Toasty.info(context, getString(R.string.all_caught_up), Toast.LENGTH_SHORT).show();
                //return;
            }
            //recyclerView = (ShimmerRecyclerView) rootView.findViewById(R.id.cardList);
            recyclerView.setHasFixedSize(false);
            final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    if (dy > 0) {
                        visibleItemCount = linearLayoutManager.getChildCount();
                        totalItemCount = linearLayoutManager.getItemCount();
                        pastVisibleItems = linearLayoutManager.findFirstVisibleItemPosition();
                        if (loading) {
                            if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                                loading = false;
                                pageno++;
                                new FetchNextPageFilter(getActivity(), pageno, url).execute();
                                StyleableToast st = new StyleableToast(getContext(), getString(R.string.loading), Toast.LENGTH_SHORT);
                                st.setBackgroundColor(Color.parseColor("#3da6d7"));
                                st.setTextColor(Color.WHITE);
                                st.setIcon(R.drawable.ic_autorenew_black_24dp);
                                st.spinIcon();
                                st.setMaxAlpha();
                                st.show();
                                //Toast.makeText(getActivity(), "Loading!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
            });

            ticketOverviewAdapter = new TicketOverviewAdapter(getContext(), ticketOverviewList);
            recyclerView.setAdapter(ticketOverviewAdapter);

            if (ticketOverviewAdapter.getItemCount() == 0) {
                empty_view.setVisibility(View.VISIBLE);
            } else empty_view.setVisibility(View.GONE);
        }
    }

    private class FetchNextPageFilter extends AsyncTask<String, Void, String> {
        Context context;
        int page;
        String url;

        FetchNextPageFilter(Context context, int page, String url) {
            this.context = context;
            this.page = page;
            this.url = url;
        }

        protected String doInBackground(String... urls) {
            if (nextPageURL.equals("null")) {
                pageno=1;
                return "all done";
            }
            String result = new Helpdesk().nextPageUrlFilter(url, page);
            if (result == null)
                return null;
            // DatabaseHandler databaseHandler = new DatabaseHandler(context);
            // databaseHandler.recreateTable();
            try {
                JSONObject jsonObject = new JSONObject(result);
                nextPageURL = jsonObject.getString("next_page_url");
                String data = jsonObject.getString("data");
                int my_tickets = jsonObject.getInt("total");
                if (my_tickets > 999)
                    Prefs.putString("myTickets", "999+");
                else
                    Prefs.putString("myTickets", my_tickets + "");
                JSONArray jsonArray = new JSONArray(data);
                for (int i = 0; i < jsonArray.length(); i++) {
                    TicketOverview ticketOverview = Helper.parseTicketOverviewSort(jsonArray, i);
                    if (ticketOverview != null) {
                        ticketOverviewList.add(ticketOverview);
                        // databaseHandler.addTicketOverview(ticketOverview);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //databaseHandler.close();
            return "success";
        }

        protected void onPostExecute(String result) {
            if (result == null)
                return;
            if (result.equals("all done")) {
                pageno=1;
                Toasty.info(context, getString(R.string.all_caught_up), Toast.LENGTH_SHORT).show();
                return;
            }
            ticketOverviewAdapter.notifyDataSetChanged();
            loading = true;
        }
    }


    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    /**
     * Whenever the this method is going to be called then the
     * async task will be cancelled .
     */
    @Override
    public void onStop() {
        // notice here that I keep a reference to the task being executed as a class member:
        if (this.new FetchFirst(getActivity(), page) != null && this.new FetchFirst(getActivity(), page).getStatus() == AsyncTask.Status.RUNNING)
            this.new FetchFirst(getActivity(), page).cancel(true);
        super.onStop();
    }

    /**
     * When the fragment is going to be attached
     * this life cycle method is going to be called.
     *
     * @param context refers to the current fragment.
     */
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

    /**
     * Once the fragment is going to be detached then
     * this method is going to be called.
     */
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        nextPageURL = "";
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
//    @Override
//    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
//        MenuInflater inflater = mode.getMenuInflater();
//        inflater.inflate(R.menu.create_ticket_menu, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
//        return false;
//    }
//
//    @Override
//    public boolean onActionItemClicked(ActionMode mode, MenuItem menuItem) {
//        switch (menuItem.getItemId()){
//            case R.id.action_attach:
//                //just to show selected items.
//
//                //Toast.makeText(this, "Selected items are :", Toast.LENGTH_SHORT).show();
//                Toast.makeText(getActivity(), "action mode", Toast.LENGTH_SHORT).show();
//                return true;
//        }
//        return false;
//    }
//
//    @Override
//    public void onDestroyActionMode(ActionMode mode) {
//        actionMode = null;
//        isMultiSelect = false;
//        checked_items = new ArrayList<>();
//        ticketOverviewAdapter.setSelectedIds(new ArrayList<Integer>());
//
//    }
//    private void multiSelect(int position) {
//        TicketOverview data = ticketOverviewAdapter.getItem(position);
//        if (data != null){
//            if (actionMode != null) {
//                if (checked_items.contains(data.getTicketID()))
//                    checked_items.remove(Integer.valueOf(data.getTicketID()));
//                else
//                    checked_items.add(data.getTicketID());
//
//                if (checked_items.size() > 0)
//                    actionMode.setTitle(String.valueOf(checked_items.size())); //show selected item count on action mode.
//                else{
//                    actionMode.setTitle(""); //remove item count from action mode.
//                    actionMode.finish(); //hide action mode.
//                }
//                ticketOverviewAdapter.setSelectedIds(checked_items);
//
//            }
//        }
//    }
//    private class ActionModeCallback implements ActionMode.Callback {
//
//
//    @Override
//    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
//        MenuInflater inflater = mode.getMenuInflater();
//        inflater.inflate(R.menu.create_ticket_menu, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
//        return false;
//    }
//
//    @Override
//    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
//        switch (item.getItemId()){
//            case R.id.action_attach:
//                //just to show selected items.
//
//                //Toast.makeText(this, "Selected items are :", Toast.LENGTH_SHORT).show();
//                Toast.makeText(getActivity(), "action mode", Toast.LENGTH_SHORT).show();
//                return true;
//        }
//        return false;
//    }
//
//    @Override
//    public void onDestroyActionMode(ActionMode actionMode) {
//        actionMode = null;
//        isMultiSelect = false;
//        checked_items = new ArrayList<>();
//        ticketOverviewAdapter.setSelectedIds(new ArrayList<Integer>());
//    }
//}

    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.search_menu, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem menuItem) {
            switch (menuItem.getItemId()) {
//            case R.id.action_delete:
//                //just to show selected items.
//                StringBuilder stringBuilder = new StringBuilder();
//                for (MyData data : getList()) {
//                    if (selectedIds.contains(data.getId()))
//                        stringBuilder.append("\n").append(data.getTitle());
//                }
                //Toast.makeText(this, "Selected items are :" + stringBuilder.toString(), Toast.LENGTH_SHORT).show();
                //return true;
            }
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            toolbarmain.setVisibility(View.VISIBLE);
            actionMode = null;
            isMultiSelect = false;
            checked_items = new ArrayList<>();
            ticketOverviewAdapter.setSelectedIds(new ArrayList<Integer>());
        }

    };
}
 class ActionModeCallback implements ActionMode.Callback {

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        // inflate contextual menu
        mode.getMenuInflater().inflate(R.menu.search_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

//        switch (item.getItemId()) {
//            case R.id.menu_delete:
//                // retrieve selected items and delete them out
//                SparseBooleanArray selected = laptopListAdapter
//                        .getSelectedIds();
//                for (int i = (selected.size() - 1); i >= 0; i--) {
//                    if (selected.valueAt(i)) {
//                        Laptop selectedItem = laptopListAdapter
//                                .getItem(selected.keyAt(i));
//                        laptopListAdapter.remove(selectedItem);
//                    }
//                }
//                mode.finish(); // Action picked, so close the CAB
//                return true;
//            default:
//                return false;
//        }
            return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        // remove selection

    }
}



