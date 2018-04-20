package co.helpdesk.faveo.pro.frontend.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import agency.tango.android.avatarview.IImageLoader;
import agency.tango.android.avatarview.loader.PicassoLoader;
import agency.tango.android.avatarview.views.AvatarView;
import co.helpdesk.faveo.pro.CircleTransform;
import co.helpdesk.faveo.pro.R;
import co.helpdesk.faveo.pro.model.CollaboratorSuggestion;

/**
 * Created by Sayar Samanta on 4/5/2018.
 */

public class CollaboratorAdapter extends ArrayAdapter<CollaboratorSuggestion> {
    ArrayList<CollaboratorSuggestion> customers, tempCustomer, suggestions ;
        Context context;
    public CollaboratorAdapter(Context context, ArrayList<CollaboratorSuggestion> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
        this.customers = objects ;
        this.tempCustomer =new ArrayList<CollaboratorSuggestion>(objects);
        this.suggestions =new ArrayList<CollaboratorSuggestion>(objects);
        this.context=context;

    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CollaboratorSuggestion customer = getItem(position);
        IImageLoader imageLoader;
        try {
            String letter = String.valueOf(customer.getFirst_name().charAt(0));
        }catch (StringIndexOutOfBoundsException e){
            e.printStackTrace();
        }
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.collaborator_row, parent, false);
        }

        TextView txtCustomer = (TextView) convertView.findViewById(R.id.textView_collaborator_name);
        ImageView ivCustomerImage = (ImageView) convertView.findViewById(R.id.imageView_collaborator);
        TextView textViewEmail= (TextView) convertView.findViewById(R.id.textView_client_email);

        if (txtCustomer != null&&customer.getFirst_name()!=null)
            txtCustomer.setText(customer.getFirst_name() + " " + customer.getLast_name());

        if (ivCustomerImage != null && customer.getProfile_pic() != null) {
            if (customer.getProfile_pic().equals(".jpg")||customer.getProfile_pic().equals(".jpeg")||customer.getProfile_pic().equals(".png")) {
                Picasso.with(context).load(customer.getProfile_pic()).transform(new CircleTransform()).into(ivCustomerImage);
            }
            else{
                Log.d("cameInThisBlock","true");
                Picasso.with(context).load(customer.getProfile_pic()).transform(new CircleTransform()).into(ivCustomerImage);
            }
        }

        if (textViewEmail!=null&&customer.getEmail()!=null)
            textViewEmail.setText(customer.getEmail());

        // Now assign alternate color for rows
//        if (position % 2 == 0)
//            convertView.setBackgroundColor(getContext().getColor(R.color.odd));
//        else
//            convertView.setBackgroundColor(getContext().getColor(R.color.even));

        return convertView;
    }
}
