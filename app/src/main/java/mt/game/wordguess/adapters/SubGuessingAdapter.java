package mt.game.wordguess.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import mt.game.wordguess.R;
import mt.game.wordguess.models.GuessModel;

import static mt.game.wordguess.utilities.Constants.STATUS_INVALID;
import static mt.game.wordguess.utilities.Constants.STATUS_NOT_SET;
import static mt.game.wordguess.utilities.Constants.STATUS_VALID;
import static mt.game.wordguess.utilities.Constants.STATUS_WRONG_PLACE;

public class SubGuessingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public List<GuessModel> arrayList;
    public Context activity;
    public GuessModel item = null;
    private final int VIEW_TYPE_LOADING = 0;
    private final int VIEW_TYPE_ITEM = 1;
    private View view;
    private String originalWord;
    private int guessPos = 0;

    public SubGuessingAdapter(String originalWord,  int guessPos, List<GuessModel> _arrayList, Context activity) {
        this.originalWord = originalWord;
        this.guessPos = guessPos;
        this.arrayList = _arrayList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup paViewGroup, int viewType) {

        switch (viewType) {

            case VIEW_TYPE_ITEM: {

                view = LayoutInflater.from(activity).inflate(R.layout.adapter_sub_guessing, paViewGroup, false);
                return new ViewHolder(view);

            }

            default:
                throw new IllegalArgumentException("Unknown view type");

        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, @SuppressLint("RecyclerView") int position) {

        if (viewHolder instanceof ViewHolder) {

            item = arrayList.get(position);

            final ViewHolder holder = (ViewHolder) viewHolder;


            for(int i = 0; i < originalWord.length(); i++){

                char c = originalWord.charAt(i);

                if(item.getCharacter().equalsIgnoreCase(""+c)){

                    if(i == position){
                        item.setStatus(STATUS_VALID);
                    }else{
                        item.setStatus(STATUS_WRONG_PLACE);
                    }

                }else{
                    item.setStatus(STATUS_INVALID);
                    holder.tvGuess.setText(""+item.getCharacter());
                }

            }

            arrayList.set(guessPos, item);


            settingStatusOfGuess(holder, item);

        }

    }

    /**
     * setting
     * status of user input
     * guessing
     * @param holder
     */
    private void settingStatusOfGuess(ViewHolder holder, GuessModel item) {
        switch (item.getStatus()){

            case STATUS_NOT_SET:{
                holder.layItem.setBackgroundColor(ContextCompat.getColor(activity, R.color.card_gray_color));
                break;
            }

            case STATUS_VALID:{
                holder.layItem.setBackgroundColor(ContextCompat.getColor(activity, R.color.light_green_color));
                break;
            }

            case STATUS_WRONG_PLACE:{
                holder.layItem.setBackgroundColor(ContextCompat.getColor(activity, R.color.light_gray));
                break;
            }

            case STATUS_INVALID:{
                holder.layItem.setBackgroundColor(ContextCompat.getColor(activity, R.color.red));

                break;
            }

        }
    }


    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public int getItemCount() {
        return arrayList == null ? 0 : arrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return VIEW_TYPE_ITEM;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private AppCompatTextView tvGuess;
        private LinearLayout layItem;

        public ViewHolder(@NonNull View view) {
            super(view);

            layItem = view.findViewById(R.id.layItem);
            tvGuess = view.findViewById(R.id.tvGuess);

        }
    }

}
