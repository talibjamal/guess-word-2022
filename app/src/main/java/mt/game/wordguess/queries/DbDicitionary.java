package mt.game.wordguess.queries;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import io.realm.Realm;
import io.realm.RealmModel;
import io.realm.RealmResults;
import mt.game.wordguess.models.DicitionaryModel;

public class DbDicitionary {

    private String TAG = DbDicitionary.class.getName();
    private static DbDicitionary dbQueries;

    private DbDicitionary() {

    }

    public static DbDicitionary getInstance() {

        if (dbQueries == null) {
            dbQueries = new DbDicitionary();
        }
        return dbQueries;
    }

    /**
     * inserting item
     * item
     * in database
     */
    public void insertItem(DicitionaryModel item) {

        //inserting in db
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.insertOrUpdate(item);
        realm.commitTransaction();

    }


    /**
     * inserting
     * items
     * in database
     */
    public void insertItemList(List<DicitionaryModel> items) {

        //inserting in db
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.insertOrUpdate(items);
        realm.commitTransaction();

    }

    /**
     * getting item
     * @return
     */
    public DicitionaryModel getItem() {
        Realm realm = Realm.getDefaultInstance();

        DicitionaryModel items = realm.where(DicitionaryModel.class)
                .equalTo("id", 1).findFirst();

        if (items != null) {
            return realm.copyFromRealm(items);
        } else {
            return null;
        }
    }

    /**
     * getting item
     * @return
     */
    public DicitionaryModel getRandomItem(int lenght) {
        Realm realm = Realm.getDefaultInstance();

        RealmResults<DicitionaryModel> items = realm.where(DicitionaryModel.class)
                .equalTo("lenght", lenght)
                .findAll();

        Random random = new Random();
        DicitionaryModel dicitionaryModel = items.get(random.nextInt(items.size()));

        if (items != null) {
            return realm.copyFromRealm(dicitionaryModel);
        } else {
            return null;
        }
    }



    /**
     * getting items
     * list
     *
     * @return
     */
    public List<DicitionaryModel> getItemsList() {
        Realm realm = Realm.getDefaultInstance();

        RealmResults<DicitionaryModel> items = realm.where(DicitionaryModel.class).findAll();

        if (items != null) {
            return realm.copyFromRealm(items);
        } else {
            return new ArrayList<>();
        }
    }

    /**
     * getting items
     * list from table
     * and dlete
     *
     * @return
     */
    public void deleteList() {

        Realm realm = Realm.getDefaultInstance();
        RealmResults<DicitionaryModel> userModel = realm.where(DicitionaryModel.class)
                .equalTo("id", 1)
                .findAll();

        if (userModel != null) {
            realm.beginTransaction();
            userModel.deleteAllFromRealm();
            realm.commitTransaction();
        }
    }

}
