package xyz.hanks.note;

import android.test.mock.MockApplication;

import org.junit.Test;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import xyz.hanks.note.model.NoteItem;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        final NoteItem noteItem = new NoteItem();

        MockApplication application = new MockApplication();
        noteItem._id  =1;
        noteItem.detail = "aaaa";
        RealmConfiguration realmConfig = new RealmConfiguration.Builder(application).build();
        Realm.setDefaultConfiguration(realmConfig);
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override public void execute(Realm realm) {
                realm.copyToRealm(noteItem);
            }
        });
    }
}