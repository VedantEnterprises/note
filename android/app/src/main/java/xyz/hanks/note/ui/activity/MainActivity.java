package xyz.hanks.note.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import io.realm.Realm;
import xyz.hanks.note.R;
import xyz.hanks.note.model.NoteItem;
import xyz.hanks.note.ui.adapter.NoteAdapter;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupUI();


        final NoteItem noteItem = new NoteItem();
        noteItem._id  =1;
        noteItem.detail = "aaaa";

        // Log.e("...............",getN().detail);
    }

    public NoteItem getN(){
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override public void execute(Realm realm) {
//                NoteItem noteItem = new NoteItem();
//                noteItem._id  =1;
//                noteItem.detail = "aaaa";
//                realm.copyToRealm(noteItem);
            }
        });
//        NoteItem first = realm.where(NoteItem.class).equalTo("_id",22).findFirst();
//        NoteItem result = realm.copyFromRealm(first);
//        Log.e("............",first!=null?first.toString():"null");
//        realm.close();
//        return result;

        return null;
    }

    private void setupUI() {
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("全部文件");
        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        setSupportActionBar(toolbar);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new NoteAdapter());
    }
}
