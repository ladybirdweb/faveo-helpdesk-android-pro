package co.helpdesk.faveo.pro.frontend.activities;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import co.helpdesk.faveo.pro.R;

public class TicketList extends AppCompatActivity {
TextView textView,textView1;
ImageView imageView;
String ticketlist,ticketsearch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_list);
        Window window = TicketList.this.getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(TicketList.this,R.color.faveo));
        textView= (TextView) findViewById(R.id.ticketlistview);
        imageView= (ImageView) findViewById(R.id.imageViewBack);
        textView1= (TextView) findViewById(R.id.ticketsearch);
        ticketlist="Once you log in you will be able to see all the tickets in your helpdesk.By default <B>inbox</B> " +
                "page will open up,it will show only the <B>open</B> tickets.If you want to see the tickets of different " +
                "status then you have to click on the hamburger icon on the top left corner and you have to select the " +
                "corresponding status to get the tickets.<br/><br/><br/>If you want to sort the tickets then you have to click on the sort by icon.You" +
                "will get the list of options to sort the tickets in your helpdesk.Some options are <B>due date</B>,<B>ticket creation date</B>,<B>ticket number</B>,<B>priority etc.</B><br/><br/><br/>" +
                "You can also filter tickets in your helpdesk based on your requirement.To filter tickets you have to click on the filter option in the main page.You will get the below screen.Presently " +
                "you will be able to filter tickets on the basis of <B>department</B>,<B>source</B>,<B>priority</B>,<B>agent name</B>,<B>ticket type</B> etc." +
                "Once you select all the required field then you have to click on the tick mark on the top right corner and " +
                "you will get the desired result. " ;
        ticketsearch="You can search for tickets and customers in your helpdesk.To get into the search page you have to click on the search icon in the main page.<br/><B>Ticket</B> : Here you can search for tickets by their <B>number</B> and <B>title</B>.<br/>" +
                "<B>Customers</B> : You can also search for the customers based on their <B>email id</B>,<B>name</B>. ";
        textView1.setText(Html.fromHtml(ticketsearch));
        textView.setText(Html.fromHtml(ticketlist));
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(TicketList.this,TicketsRelated.class);
                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(TicketList.this,TicketsRelated.class);
        startActivity(intent);
        finish();
}
}
