package com.echopen.asso.echopen;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.echopen.asso.echopen.sqlite_database.FeedReaderContract;
import com.echopen.asso.echopen.sqlite_database.FeedReaderDbHelper;
import com.echopen.asso.echopen.sqlite_database.models.Person;
import com.echopen.asso.echopen.utils.Strings;

import java.util.ArrayList;
import java.util.List;

public class ClientActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);

        List<Person> people = GetPeople();

        ListView listView = (ListView) findViewById(R.id.list_view);
        for (Person person : people) {
        }

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        final ImageView clientdetail = (ImageView) findViewById(R.id.clientdetail);
        clientdetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent clientdetail = new Intent(getApplicationContext(), ClientDetailActivity.class);
                startActivity(clientdetail);

            }
        });

    }

    private List GetPeople() {
        FeedReaderDbHelper mDbHelper = new FeedReaderDbHelper(getApplicationContext());
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                FeedReaderContract.FeedEntry._ID,
                FeedReaderContract.FeedEntry.COLUMN_NAME_SEX,
                FeedReaderContract.FeedEntry.COLUMN_NAME_ECHO_TYPE
        };

        Cursor cursor = db.query(
                FeedReaderContract.FeedEntry.TABLE_NAME,                     // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );

        List people = new ArrayList<>();

        while (cursor.moveToNext()) {
            Person person = new Person();
            person.setSex(cursor.getString(cursor.getColumnIndex(
                    FeedReaderContract.FeedEntry.COLUMN_NAME_ECHO_TYPE
            )));
            person.setType(cursor.getString(
                    cursor.getColumnIndex(
                            FeedReaderContract.FeedEntry.COLUMN_NAME_ECHO_TYPE
                    )));
            person.setId(cursor.getLong(cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry._ID)));
            people.add(person);
        }
        cursor.close();

        return people;
    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Intent main = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(main);
                    finish();
                    return true;
                case R.id.navigation_dashboard:

                    return true;
                case R.id.navigation_notifications:
                    Intent search = new Intent(getApplicationContext(), ClientActivity.class);
                    startActivity(search);
                    finish();
                    return true;
                case R.id.navigation_help:

                    return true;
            }
            return false;
        }

    };


}
