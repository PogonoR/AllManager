package com.bnp.allmanager;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.bnp.allmanager.NotesDbAdapter;
import com.bnp.allmanager.R;

/**
 * Created by robertp on 29.07.2014.
 */
public class NoteEdit extends Activity{

    private NotesDbAdapter mDbHelper;


    private EditText mTitleText;
    private EditText mBodyText;
    private Long mRowId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDbHelper = new NotesDbAdapter(this);
        mDbHelper.open();

        setContentView(R.layout.note_edit);
        setTitle(R.string.edit_note);
        mTitleText = (EditText) findViewById(R.id.title);
        mBodyText = (EditText) findViewById(R.id.body);
        Button confirmButton = (Button) findViewById(R.id.confirm);

        /*
        mRowId = null;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            // exercise 3 task (introduce direct DB pull)
            //String title = extras.getString(NotesDbAdapter.KEY_TITLE);
            //String body = extras.getString(NotesDbAdapter.KEY_BODY);

            mRowId = extras.getLong(NotesDbAdapter.KEY_ROWID);
            // exercise 3 task (introduce direct DB pull)
            //if (title != null) {
            //    mTitleText.setText(title);
            //}
            //if (body != null) {
            //    mBodyText.setText(body);
            //}

        }*/

        mRowId = (savedInstanceState == null) ? null:
            (Long) savedInstanceState.getSerializable(NotesDbAdapter.KEY_ROWID);
        if (mRowId == null) {
            Bundle extras = getIntent().getExtras();
            mRowId = extras != null ? extras.getLong(NotesDbAdapter.KEY_ROWID)
                    :null;
        }

        populateFields();

        confirmButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                /*
                Bundle bundle = new Bundle();

                bundle.putString(NotesDbAdapter.KEY_TITLE, mTitleText.getText().toString());
                bundle.putString(NotesDbAdapter.KEY_BODY, mBodyText.getText().toString());
                if (mRowId != null) {
                    bundle.putLong(NotesDbAdapter.KEY_ROWID, mRowId);
                }

                Intent mIntent = new Intent();
                mIntent.putExtras(bundle);
                */
                setResult(RESULT_OK);
                finish();
            }
        });

    }
    private void populateFields() {
        if (mRowId != null) {
            Cursor note = mDbHelper.fetchNote(mRowId);
            startManagingCursor(note);
            mTitleText.setText(note.getString(
                    note.getColumnIndexOrThrow(NotesDbAdapter.KEY_TITLE)));
            mBodyText.setText(note.getString(
                    note.getColumnIndexOrThrow(NotesDbAdapter.KEY_BODY)));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveState();
        outState.putSerializable(NotesDbAdapter.KEY_ROWID, mRowId);
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveState();
    }
    private void saveState() {
        String title = mTitleText.getText().toString();
        String body = mBodyText.getText().toString();

        if (mRowId == null) {
            long id = mDbHelper.createNote(title, body);
            if (id > 0) {
                mRowId = id;

            }
            else {
                mDbHelper.updateNote(mRowId, title, body);
            }

        }
    }


}
