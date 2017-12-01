package co.helpdesk.faveo.pro.frontend.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.curioustechizen.ago.RelativeTimeTextView;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import agency.tango.android.avatarview.IImageLoader;
import agency.tango.android.avatarview.loader.PicassoLoader;
import agency.tango.android.avatarview.views.AvatarView;
import co.helpdesk.faveo.pro.Helper;
import co.helpdesk.faveo.pro.R;
import co.helpdesk.faveo.pro.frontend.activities.TicketDetailActivity;
import co.helpdesk.faveo.pro.model.TicketOverview;

/**
 * This adapter is for the tickets(inbox,mytickets,trash...) page in the main page.
 */
public class TicketOverviewAdapter extends RecyclerView.Adapter<TicketOverviewAdapter.TicketViewHolder> {
    private List<TicketOverview> ticketOverviewList;
    String subject;
    int length=0;
    private Context context;
    ArrayList<Integer> checked_items= new ArrayList<>();


    public TicketOverviewAdapter(Context context,List<TicketOverview> ticketOverviewList) {
        this.ticketOverviewList = ticketOverviewList;
        this.context=context;
    }

    @Override
    public int getItemCount() {
        return ticketOverviewList.size();
    }

    @Override
    public void onBindViewHolder(final TicketViewHolder ticketViewHolder, final int i) {
        final TicketOverview ticketOverview = ticketOverviewList.get(i);
        int id=ticketOverviewList.get(i).getTicketID();

//        if (checked_items.contains(id)){
//            //if item is selected then,set foreground color of FrameLayout.
//            ticketViewHolder.ticket.setBackgroundColor(Color.parseColor("#B9FCFC"));
//        }
//        else {
//            //else remove selected item color.
//            ticketViewHolder.ticket.setBackgroundColor(Color.parseColor("#FFFFFF"));
//        }

        //Toast.makeText(context, "no of items"+checked_items.toString(), Toast.LENGTH_SHORT).show();

        ticketViewHolder.checkBox1.setOnCheckedChangeListener(null);
       //ticketViewHolder.checkBox1.setVisibility(View.VISIBLE);
        ticketViewHolder.checkBox1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {


                if (b){
                    ticketViewHolder.checkBox1.setChecked(true);
                    ticketOverview.setChecked(true);
                    //stringBuffer.append(""+ticketOverview.getTicketID()+",");
                    checked_items.add(ticketOverview.getTicketID());
//                  length=checked_items.size();
                    Log.d("checkeditems",checked_items.toString().replace(" ",""));
                    Prefs.putString("tickets",checked_items.toString().replace(" ",""));

                    ticketViewHolder.ticket.setBackgroundColor(Color.parseColor("#B9FCFC"));

                }
                else{
                        ticketOverview.setChecked(false);
                    int pos=checked_items.indexOf(ticketOverview.getTicketID());
                    try {
                        checked_items.remove(pos);
                    }
                    catch (ArrayIndexOutOfBoundsException e){
                        e.printStackTrace();
                    }
                    Log.d("Position",""+pos);
                        //checked_items.remove(checked_items.indexOf(ticketOverview.getTicketID()));
                    length--;
                    Log.d("NoOfItems",""+length);
                    Prefs.putInt("totalticketselected",length);
                        Log.d("checkeditems", "" + checked_items);
                    Prefs.putInt("NoOfItems",length);
                        Prefs.putString("tickets", checked_items.toString().replace(" ", ""));
                        ticketViewHolder.checkBox1.setVisibility(View.GONE);
                        ticketViewHolder.checkBox1.setChecked(false);
                        ticketViewHolder.ticket.setBackgroundColor(Color.parseColor("#FFFFFF"));
                        //notifyDataSetChanged();

                }


            }
        });


        if (ticketOverview.getChecked()){
            ticketViewHolder.checkBox1.setVisibility(View.VISIBLE);
            ticketViewHolder.checkBox1.setChecked(true);
            ticketViewHolder.ticket.setBackgroundColor(Color.parseColor("#B9FCFC"));
        }
        else{
            ticketViewHolder.checkBox1.setVisibility(View.GONE);
            ticketViewHolder.checkBox1.setChecked(false);
            ticketViewHolder.ticket.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }

        if (ticketOverview.ticketAttachments.equals("0")) {
            ticketViewHolder.attachementView.setVisibility(View.GONE);
        } else {
            ticketViewHolder.attachementView.setVisibility(View.VISIBLE);
        }
        if (ticketOverview.dueDate != null && !ticketOverview.dueDate.equals("null"))
//            if (Helper.compareDates(ticketOverview.dueDate) == 1) {
//                ticketViewHolder.textViewOverdue.setVisibility(View.VISIBLE);
//            } else ticketViewHolder.textViewOverdue.setVisibility(View.GONE);

             if (Helper.compareDates(ticketOverview.dueDate) == 2) {
                ticketViewHolder.textViewOverdue.setVisibility(View.VISIBLE);
                ticketViewHolder.textViewOverdue.setText(R.string.due_today);
                 ticketViewHolder.textViewOverdue.setTextColor(Color.parseColor("#FFD700"));
                //ticketViewHolder.textViewOverdue.setBackgroundColor();

            }
            else  if (Helper.compareDates(ticketOverview.dueDate) == 1) {
            ticketViewHolder.textViewOverdue.setVisibility(View.VISIBLE);
                 ticketViewHolder.textViewOverdue.setText(R.string.overdue);
                 ticketViewHolder.textViewOverdue.setTextColor(Color.parseColor("#ef9a9a"));

        }
        else {
                ticketViewHolder.textViewOverdue.setVisibility(View.GONE);
            }


        ticketViewHolder.textViewTicketID.setText(ticketOverview.ticketID + "");

        ticketViewHolder.textViewTicketNumber.setText(ticketOverview.ticketNumber);
        ticketViewHolder.textViewClientName.setText(ticketOverview.clientName);

        subject=ticketOverview.ticketSubject;
        if (subject.startsWith("=?UTF-8?Q?")&&subject.endsWith("?=")){
            String first=subject.replace("=?UTF-8?Q?","");
            String second=first.replace("_"," ");
            String third=second.replace("=C2=A0","");
            String fourth=third.replace("?=","");
            String fifth=fourth.replace("=E2=80=99","'");
            ticketViewHolder.textViewSubject.setText(fifth);
        }
        else{
            ticketViewHolder.textViewSubject.setText(ticketOverview.ticketSubject);
        }

        if (ticketOverview.ticketPriorityColor.equals("null")){
            ticketViewHolder.ticketPriority.setBackgroundColor(Color.parseColor("#3da6d7"));
        }
        else if (ticketOverview.ticketPriorityColor != null) {
            ticketViewHolder.ticketPriority.setBackgroundColor(Color.parseColor(ticketOverview.ticketPriorityColor));
        }


//        else if (ticketOverview.ticketPriorityColor.equals("null")){
//            ticketViewHolder.ticketPriority.setBackgroundColor(Color.parseColor("#3da6d7"));
//        }
        ticketViewHolder.textViewTime.setReferenceTime(Helper.relativeTime(ticketOverview.ticketTime));

if (!ticketOverview.countthread.equals("0")){
    ticketViewHolder.countThread.setText("("+ticketOverview.getCountthread()+")");
}
else{
    ticketViewHolder.countThread.setVisibility(View.GONE);
}

if (ticketOverview.sourceTicket.equals("chat")){
    int color=Color.parseColor("#3da6d7");
ticketViewHolder.source.setImageResource(R.drawable.chat);
    //ticketViewHolder.source.setColorFilter(color);
}
else if (ticketOverview.sourceTicket.equals("web")){
   int color=Color.parseColor("#3da6d7");
    ticketViewHolder.source.setImageResource(R.drawable.web);
    //ticketViewHolder.source.setColorFilter(color);
}
else if (ticketOverview.sourceTicket.equals("agent")){
    int color=Color.parseColor("#3da6d7");
ticketViewHolder.source.setImageResource(R.drawable.ic_email_black_24dp);
    //ticketViewHolder.source.setColorFilter(color);
}
else if (ticketOverview.sourceTicket.equals("email")){
    int color=Color.parseColor("#3da6d7");
    ticketViewHolder.source.setImageResource(R.drawable.ic_email_black_24dp);
    //ticketViewHolder.source.setColorFilter(color);
}
else if (ticketOverview.sourceTicket.equals("facebook")){
    int color=Color.parseColor("#3da6d7");
    ticketViewHolder.source.setImageResource(R.drawable.facebook);
    //ticketViewHolder.source.setColorFilter(color);
}
else if (ticketOverview.sourceTicket.equals("twitter")){
    int color=Color.parseColor("#3da6d7");
    ticketViewHolder.source.setImageResource(R.drawable.twitter);
    //ticketViewHolder.source.setColorFilter(color);
}
else if (ticketOverview.sourceTicket.equals("call")){
    int color=Color.parseColor("#3da6d7");
    ticketViewHolder.source.setImageResource(R.drawable.ic_call_black_24dp);
    //ticketViewHolder.source.setColorFilter(color);
}
else{
    ticketViewHolder.source.setVisibility(View.GONE);
}

if (!ticketOverview.countcollaborator.equals("0")){

ticketViewHolder.countCollaborator.setImageResource(R.drawable.ic_group_black_24dp);
}
else if (ticketOverview.countcollaborator.equals("0")){
ticketViewHolder.countCollaborator.setVisibility(View.GONE);
}

if (!ticketOverview.agentName.equals("nullnull")){
    ticketViewHolder.agentAssigned.setText(ticketOverview.getAgentName());
}


if (!ticketOverview.lastReply.equals("client")){
    int color=Color.parseColor("#E0E0E0");
ticketViewHolder.ticket.setBackgroundColor(color);
}



if (ticketOverview.clientPicture.equals("")){
    ticketViewHolder.roundedImageViewProfilePic.setVisibility(View.GONE);
}
   else {
    IImageLoader imageLoader = new PicassoLoader();
    imageLoader.loadImage(ticketViewHolder.roundedImageViewProfilePic, ticketOverview.clientPicture, ticketOverview.placeholder);
}

//        if (ticketOverview.clientPicture != null && ticketOverview.clientPicture.trim().length() != 0)
//            Picasso.with(ticketViewHolder.roundedImageViewProfilePic.getContext())
//                    .load(ticketOverview.clientPicture)
//                    .placeholder(R.drawable.default_pic)
//                    .error(R.drawable.default_pic)
//                    .into(ticketViewHolder.roundedImageViewProfilePic);

        ticketViewHolder.ticket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(), TicketDetailActivity.class);
                intent.putExtra("ticket_id", ticketOverview.ticketID + "");
                Prefs.putString("TICKETid",ticketOverview.ticketID+"");
                Prefs.putString("ticketstatus",ticketOverview.getTicketStatus());
                intent.putExtra("ticket_number", ticketOverview.ticketNumber);
                intent.putExtra("ticket_opened_by", ticketOverview.clientName);
                intent.putExtra("ticket_subject", ticketOverview.ticketSubject);
                v.getContext().startActivity(intent);
            }
        });
        ticketViewHolder.ticket.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                ticketViewHolder.checkBox1.setVisibility(View.VISIBLE);
                ticketViewHolder.checkBox1.setChecked(true);
                length++;
                Log.d("noofitems",""+length);
                Prefs.putInt("NoOfItems",length);

//                ticketOverviewList.get(i).getTicketID();
//                Log.d("position",""+ticketOverviewList.get(i).getTicketID());
//                if (ticketViewHolder.checkBox1.isEnabled()){
//
//                }
//                else{
//                    ticketViewHolder.checkBox1.setVisibility(View.GONE);
//                }
//                if (ticketViewHolder.checkBox1.isChecked()){
//
//                }else{
//                    ticketViewHolder.checkBox1.setVisibility(View.GONE);
//                }
                return true;
            }
        });


//        ticketViewHolder.checkBox1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (ticketViewHolder.checkBox1.isChecked())
//                    ticketViewHolder.checkBox1.setChecked(false);
//                else
//                    ticketViewHolder.checkBox1.setChecked(true);
//            }
//        });



//        ticketViewHolder.checkBox1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
//
//                if (isChecked){
////                    ticketViewHolder.checkBox1.setChecked(true);
////                    Prefs.putString("checkboxstate","checked");
//
//                    stringBuffer.append(ticketOverview.getTicketID()+",");
//                    Log.d("ids",stringBuffer.toString());
//
//                }
//                else{
//                    Prefs.putString("checkboxstate","unchecked");
//                    stringBuffer.toString().replace(""+ticketOverview.getTicketID(),"");
//                    Log.d("ids",stringBuffer.toString());
//                    ticketViewHolder.checkBox1.setVisibility(View.GONE);
//                    notifyDataSetChanged();
//                }
////
////                String state=Prefs.getString("checkboxstate",null);
////
////                if (state.equals("checked")){
////                    ticketViewHolder.checkBox1.setSelected(true);
////                    notifyDataSetChanged();
////                }
////                else{
////                    ticketViewHolder.checkBox1.setSelected(false);
////                    notifyDataSetChanged();
////                }
//
//
//            }
//        });

    }
    public void setSelectedIds(ArrayList<Integer> checked_items) {
        this.checked_items = checked_items;
        notifyDataSetChanged();
    }
    public TicketOverview getItem(int position){
        return ticketOverviewList.get(position);
    }
    @Override
    public TicketViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.card_ticket, viewGroup, false);
        return new TicketViewHolder(itemView);
    }

    static class TicketViewHolder extends RecyclerView.ViewHolder {

        protected View ticket;
        AvatarView roundedImageViewProfilePic;
        TextView textViewTicketID;
        TextView textViewTicketNumber;
        TextView textViewClientName;
        TextView textViewSubject;
        RelativeTimeTextView textViewTime;
        TextView textViewOverdue;
        View ticketPriority;
        // TextView ticketStatus;
        ImageView attachementView;
        CheckBox checkBox1;
        ImageView countCollaborator;
        ImageView source;
        TextView countThread;
        TextView agentAssigned;
        TicketViewHolder(View v) {
            super(v);
            ticket = v.findViewById(R.id.ticket);
            attachementView = (ImageView) v.findViewById(R.id.attachment_icon);
            ticketPriority = v.findViewById(R.id.priority_view);
            roundedImageViewProfilePic = (AvatarView) v.findViewById(R.id.imageView_default_profile);
            textViewTicketID = (TextView) v.findViewById(R.id.textView_ticket_id);
            textViewTicketNumber = (TextView) v.findViewById(R.id.textView_ticket_number);
            textViewClientName = (TextView) v.findViewById(R.id.textView_client_name);
            textViewSubject = (TextView) v.findViewById(R.id.textView_ticket_subject);
            textViewTime = (RelativeTimeTextView) v.findViewById(R.id.textView_ticket_time);
            textViewOverdue = (TextView) v.findViewById(R.id.overdue_view);
            checkBox1= (CheckBox) v.findViewById(R.id.checkbox);
            countCollaborator= (ImageView) v.findViewById(R.id.collaborator);
            countThread= (TextView) v.findViewById(R.id.countthread);
            source= (ImageView) v.findViewById(R.id.source);
            agentAssigned= (TextView) v.findViewById(R.id.agentassigned);

        }

    }

}
