package com.example.android.secretsanta;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.*;

import static java.util.Collections.rotate;

public class ListLayout extends AppCompatActivity {

    public ListLayout() {
    }

    //finalList contains final pairs of participants
    public final ArrayList<List<String>> finalList = new ArrayList<List<String>>();

    //contactList contains the names and emails of all participants
    public final ArrayList<List<String>> contactList = new ArrayList<List<String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_layout);

        ListView myList = (ListView)findViewById(R.id.myListofItems);

        final ArrayAdapter<List<String>> myad;

        myad = new ArrayAdapter<List<String>>(this,android.R.layout.simple_list_item_1,contactList);

        myList.setAdapter(myad);

        //get the input
        final EditText textName = (EditText)findViewById(R.id.editName);
        final EditText textEmail = (EditText)findViewById(R.id.editEmail);

        Button btn = (Button)findViewById(R.id.savebutton);

        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View x) {

                //on click of Add Participant button, adds the name and email to the contactList.
                final List<String> newList = new ArrayList<String>();
                newList.add(0,textName.getText().toString());
                newList.add(1, textEmail.getText().toString());

                contactList.add(0,newList);

                myad.notifyDataSetChanged();

                //resets the textbox to empty fields
                textName.setText("");
                textEmail.setText("");

            }
        });

    }

    //called when generate button is clicked.
    //Generates random pairs from the list of participants
    public void makelist(View view) {
        int len = contactList.size();
        Random ran = new Random();
        int n = 1 + ran.nextInt(len);
        if (n == len) --n;

        for (int k = 0; k < len; k++) {
            //randomly choosing a target
            int m;
            if ((k+n)>=len) m = (k+n)-len;
            else m = k+n;

            String recipient = (contactList.get(m)).get(0);

            List<String> nlist = new ArrayList<String>();
            nlist.add(0, (contactList.get(k)).get(0));
            nlist.add(1, recipient);
            finalList.add(0, nlist);
        }

        //sendEmail("testemail1sdasani@gmail.com","createapa");
        displaynew();

    }

    //displays the list showing the pairs of participants
    public void displaynew() {
        ListView myList = (ListView)findViewById(R.id.myListofItems);

        final ArrayAdapter<List<String>> myad;

        myad = new ArrayAdapter<List<String>>(this,android.R.layout.simple_list_item_1,finalList);

        myList.setAdapter(myad);
        myad.notifyDataSetChanged();

    }

    //used to send email everyone about their secret santa target
    public void sendEmail(final String from, final String password){
        //Get properties object
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");


        int n = finalList.size();
        for (int i = 0; i < n; i++) {
            //get Session
            Session session = Session.getDefaultInstance(props,
                    new javax.mail.Authenticator() {
                        protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                            return new javax.mail.PasswordAuthentication(from, password);
                        }
                    });

            String fullmsg = "Your Secret Santa recipient is: "+finalList.get(i).get(0);

            //compose message
            try {
                MimeMessage message = new MimeMessage(session);
                message.addRecipient(MimeMessage.RecipientType.TO,new InternetAddress(contactList.get(i).get(1)));
                message.setSubject("SecretSanta App");
                message.setText(fullmsg);
                //send message
                Transport.send(message);
                System.out.println("message sent successfully");
            } catch (MessagingException e) {throw new RuntimeException(e);}
        }

    }

}
