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
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import mt.game.wordguess.R;
import mt.game.wordguess.models.GuessModel;
import static mt.game.wordguess.utilities.Constants.STATUS_INVALID;
import static mt.game.wordguess.utilities.Constants.STATUS_NOT_SET;
import static mt.game.wordguess.utilities.Constants.STATUS_VALID;
import static mt.game.wordguess.utilities.Constants.STATUS_WRONG_PLACE;

public class GuessingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public List<GuessModel> arrayList;
    public Context activity;
    public onItemClickListener onItemClickListener;
    public GuessModel item = null;
    private final int VIEW_TYPE_LOADING = 0;
    private final int VIEW_TYPE_ITEM = 1;
    private View view;
    private String originalWord;

    public GuessingAdapter(String originalWord, List<GuessModel> _arrayList, Context activity, onItemClickListener onItemClickListener) {
        this.originalWord = originalWord;
        this.arrayList = _arrayList;
        this.activity = activity;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup paViewGroup, int viewType) {

        switch (viewType) {

            case VIEW_TYPE_ITEM: {

                view = LayoutInflater.from(activity).inflate(R.layout.adapter_guessing, paViewGroup, false);
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

            GridLayoutManager mLayoutManager = new GridLayoutManager(activity, 5);
            holder.rvRecycle.setLayoutManager(mLayoutManager);
            holder.rvRecycle.setItemAnimator(new DefaultItemAnimator());
            holder.rvRecycle.setHasFixedSize(true);

            List<GuessModel> guessList = new ArrayList<>();
            //default data
            GuessModel guessModel = new GuessModel();

            if(item.getCharacter().length()>0){

                for(int i = 0; i < item.getCharacter().length(); i++){

                    char c = item.getCharacter().charAt(i);
                    guessModel = new GuessModel();
                    guessModel.setPos(i);
                    guessModel.setCharacter(""+c);
                    guessModel.setStatus(STATUS_NOT_SET);
                    guessList.add(guessModel);

                }

            }else{


                for (int i = 0; i < 6; i++){
                    guessModel = new GuessModel();
                    guessModel.setPos(i);
                    guessModel.setCharacter("");
                    guessModel.setStatus(STATUS_NOT_SET);
                    guessList.add(guessModel);
                }

            }

            SubGuessingAdapter adapter = new SubGuessingAdapter(originalWord, position, guessList, activity);
            holder.rvRecycle.setAdapter(adapter);

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

        private LinearLayout layItem;

        private RecyclerView rvRecycle;
        private GridLayoutManager mLayoutManager;

        public ViewHolder(@NonNull View view) {
            super(view);

            layItem = view.findViewById(R.id.layItem);
            rvRecycle = view.findViewById(R.id.rvRecycle);

        }
    }

    /**
     * item click
     * listener
     */
    public interface onItemClickListener {
        public void itemClick(int position, GuessModel Conversation);
    }

}
