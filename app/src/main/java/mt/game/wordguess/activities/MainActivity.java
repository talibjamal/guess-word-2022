package mt.game.wordguess.activities;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import mt.game.wordguess.R;
import mt.game.wordguess.adapters.GuessingAdapter;
import mt.game.wordguess.interfaces.ReadDataCallBack;
import mt.game.wordguess.models.DicitionaryModel;
import mt.game.wordguess.models.GuessModel;
import mt.game.wordguess.queries.DbDicitionary;
import mt.game.wordguess.utilities.JSONResourceReader;

import static mt.game.wordguess.utilities.Constants.STATUS_NOT_SET;

public class MainActivity extends AppCompatActivity implements ReadDataCallBack, GuessingAdapter.onItemClickListener, View.OnClickListener {

    private Dialog dialog;
    private List<DicitionaryModel> arrayList = new ArrayList<>();
    private String TAG = "wordGuess";

    private RecyclerView rvRecycle;
    private LinearLayoutManager mLayoutManager;
    private AppCompatTextView tvHead;
    private AppCompatEditText etGuess;
    private AppCompatButton btnCheck;
    private List<GuessModel> guessList = new ArrayList<>();
    private GuessingAdapter adapter;
    private int chances = 0;
    private String originalWord = "";
    private DicitionaryModel dicitionaryModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        init();
        readJsonDic();


    }

    /**
     * initilizing
     * views
     */
    public void init() {

        tvHead = findViewById(R.id.tvHead);
        etGuess = findViewById(R.id.etGuess);
        btnCheck = findViewById(R.id.btnCheck);
        btnCheck.setOnClickListener(this);
        editTextWatcher();

        progressDialog();
        settingRecycleView();


    }

    /**
     * reading
     * dictionary json
     * file from raw
     */
    public void readJsonDic() {

        /**
         * getting data from
         * db if size = 0
         * then fetch from dictionar file
         * from raw
         */
        arrayList.addAll(DbDicitionary.getInstance().getItemsList());

        Log.d(TAG, "list size: " + arrayList.size());

        if (arrayList.size() == 0) {
            new JSONResourceReader(getApplicationContext().getResources(), R.raw.dictionary, this);
        } else {
            getRandomValue();
        }

    }

    /**
     * getting
     * random object
     * for guess by user end
     */
    public void getRandomValue() {

        dicitionaryModel = DbDicitionary.getInstance().getRandomItem(5);

        if (dicitionaryModel != null) {
            originalWord = dicitionaryModel.getWord();
            tvHead.setText(tvHead.getText().toString() + ": " + originalWord);
        }

        settingGuessListData();
    }

    /**
     * calling dialog
     * for progress loader
     */
    private void progressDialog() {

        if (dialog == null) {
            dialog = new Dialog(this);
            dialog.setContentView(R.layout.layout_progress);
            dialog.setCancelable(false);
        }
    }

    /**
     * starting
     * progress
     * bar
     */
    public void startProgress() {
        if (!dialog.isShowing()) {
            dialog.show();
        }
    }

    /**
     * stopping progress
     * bar
     */
    public void stopProgress() {
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void startReading() {
        startProgress();
        Log.d(TAG, "startReading");
    }

    @Override
    public void stopReading(String data) {

        if (TextUtils.isEmpty(data)) {
            return;
        }

        Type listType = new TypeToken<ArrayList<DicitionaryModel>>() {
        }.getType();
        arrayList = new Gson().fromJson(data, listType);

        /**
         * so that
         * next time we dont have
         * to itterate the dictionary file again
         */

        if (arrayList != null) {
            if (arrayList.size() > 0) {
                DbDicitionary.getInstance().insertItemList(arrayList);
            }
        }

        stopProgress();

        getRandomValue();

        Log.d(TAG, "stopReading");

    }

    @Override
    public void errorReading() {
        stopProgress();
        Log.d(TAG, "errorReading");
    }


    /**
     * setting
     * recycle view
     */
    private void settingRecycleView() {
        rvRecycle = findViewById(R.id.rvRecycle);
        mLayoutManager = new LinearLayoutManager(this);
        rvRecycle.setLayoutManager(mLayoutManager);
        rvRecycle.setItemAnimator(new DefaultItemAnimator());
        rvRecycle.setHasFixedSize(true);
    }

    /**
     * edit text
     * watcher
     */
    private void editTextWatcher() {

        etGuess.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    /**
     * setting
     * guess list
     * empty data
     */
    public void settingGuessListData() {

        GuessModel guessModel = new GuessModel();
        for (int i = 0; i < 6; i++) {
            guessModel = new GuessModel();
            guessModel.setPos(i);
            guessModel.setCharacter("");
            guessModel.setStatus(STATUS_NOT_SET);
            guessList.add(guessModel);
        }

        settingAdapter(guessList);

    }

    /**
     * setting
     * adapter
     * for guess
     * by user
     */
    private void settingAdapter(List<GuessModel> _arrayList) {
        adapter = new GuessingAdapter(originalWord, _arrayList, this, this);
        rvRecycle.setAdapter(adapter);
    }


    @Override
    public void itemClick(int position, GuessModel Conversation) {

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btnCheck: {

                if (chances > 5) {
                    return;
                }

                if (!TextUtils.isEmpty(etGuess.getText().toString())) {

                    String value = etGuess.getText().toString();

                    if (value.length() < 5) {
                        Toast.makeText(this, "" + getString(R.string.text_enter_five_chars), Toast.LENGTH_SHORT).show();
                        return;
                    }


                    int pos = 0;

                    switch (chances) {

                        case 0: {
                            pos = 0;
                            break;
                        }

                        case 1: {
                            pos = 1;
                            break;
                        }

                        case 2: {
                            pos = 2;
                            break;
                        }

                        case 3: {
                            pos = 3;
                            break;
                        }

                        case 4: {
                            pos = 4;
                            break;
                        }

                        case 5: {
                            pos = 5;
                            break;
                        }

                    }

                    guessList.get(pos).setCharacter(value);
                    adapter.notifyItemChanged(pos);

//                    etGuess.setText(null);
                    chances++;

                } else {
                    Toast.makeText(this, "" + getString(R.string.text_enter_your_guess), Toast.LENGTH_SHORT).show();
                }

                break;
            }

        }

    }
}