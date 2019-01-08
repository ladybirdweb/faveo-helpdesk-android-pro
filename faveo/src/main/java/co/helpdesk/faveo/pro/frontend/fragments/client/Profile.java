package co.helpdesk.faveo.pro.frontend.fragments.client;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pixplicity.easyprefs.library.Prefs;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import co.helpdesk.faveo.pro.R;
import co.helpdesk.faveo.pro.backend.api.v1.Helpdesk;
import co.helpdesk.faveo.pro.frontend.activities.ClientDetailActivity;
import co.helpdesk.faveo.pro.frontend.activities.EditCustomer;
import co.helpdesk.faveo.pro.frontend.adapters.TicketGlimpseAdapter;
import co.helpdesk.faveo.pro.frontend.receivers.InternetReceiver;
import co.helpdesk.faveo.pro.model.TicketGlimpse;
import es.dmoral.toasty.Toasty;

public class Profile extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public String mParam1;
    public String mParam2;
    TextView tv;
    View rootView;
    EditText userName, firstName, email, phoneEditText, mobileEdittext;
    public String clientID;
    private OnFragmentInteractionListener mListener;
    RelativeLayout relativeLayoutmobile,relativeLayoutphone;
    public static Profile newInstance(String param1, String param2) {
        Profile fragment = new Profile();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public Profile() {
    }

    /**
     *
     * @param savedInstanceState under special circumstances, to restore themselves to a previous
     * state using the data stored in this bundle.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    /**
     *
     * @param inflater for loading the fragment.
     * @param container where the fragment is going to be load.
     * @param savedInstanceState
     * @return after initializing returning the rootview
     * which is having the fragment.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.userprofile, container, false);
            clientID=Prefs.getString("clientId",null);
            userName= (EditText) rootView.findViewById(R.id.username);
            firstName= (EditText) rootView.findViewById(R.id.firstname);
            email= (EditText) rootView.findViewById(R.id.email);
            phoneEditText= (EditText) rootView.findViewById(R.id.phone);
            mobileEdittext= (EditText) rootView.findViewById(R.id.mobile);
            phoneEditText= (EditText) rootView.findViewById(R.id.phone);
            relativeLayoutmobile=rootView.findViewById(R.id.usermobileheading);
            relativeLayoutphone=rootView.findViewById(R.id.userphoneheading);
            if (InternetReceiver.isConnected()){
                new FetchClientTickets(getActivity()).execute();
            }


        }
        return rootView;
    }

    private class FetchClientTickets extends AsyncTask<String, Void, String> {
        Context context;
        FetchClientTickets(Context context) {
            this.context = context;
        }

        protected String doInBackground(String... urls) {
            return new Helpdesk().getTicketsByUser(clientID);
        }

        protected void onPostExecute(String result) {
            if (isCancelled()) return;

            if (result == null) return;
            try{
                JSONObject jsonObject = new JSONObject(result);
                String error=jsonObject.getString("error");
                if (error.equals("This is not a client")){
                    Toasty.info(getActivity(), "This is not a client", Toast.LENGTH_LONG).show();
                    return;
                }
            }catch(JSONException e){
                e.printStackTrace();
            }
            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONObject requester = jsonObject.getJSONObject("requester");
                Prefs.putString("clientProfile",requester.toString());
                String firstname = requester.getString("first_name");
                String lastName = requester.getString("last_name");
                String username = requester.getString("user_name");
                String clientname;

                if (!username.equals("")){
                    userName.setText(username);
                }
                else{
                    userName.setText("");
                }
                if (firstname == null || firstname.equals(""))
                    clientname = username;
                else
                    clientname = firstname + " " + lastName;
                if (!clientname.equals("")){
                    email.setVisibility(View.VISIBLE);
                    firstName.setText(clientname);
                    email.setText(requester.getString("email"));
                }
                String phone = "";
                String mobile="";
                phone = requester.getString("phone_number");
                mobile=requester.getString("mobile");


                if (phone.equals("null")||phone.equals("")||phone.equals("Not available")||phone.equals("")){
                    phoneEditText.setVisibility(View.GONE);
                    relativeLayoutphone.setVisibility(View.GONE);
                }else {
                    relativeLayoutphone.setVisibility(View.VISIBLE);
                    phoneEditText.setVisibility(View.VISIBLE);
                    phoneEditText.setText(phone);
                }
                if (mobile.equals("null")||mobile.equals("")||mobile.equals("Not available")){
                    relativeLayoutmobile.setVisibility(View.GONE);
                    mobileEdittext.setVisibility(View.GONE);
                }
                else {
                    relativeLayoutmobile.setVisibility(View.VISIBLE);
                    mobileEdittext.setVisibility(View.VISIBLE);
                    mobileEdittext.setText(mobile);
                }


            } catch (JSONException e) {
                Toasty.error(getActivity(), getString(R.string.unexpected_error), Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }

        }
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    /**
     * When the fragment is going to be attached
     * this life cycle method is going to be called.
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
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

}
