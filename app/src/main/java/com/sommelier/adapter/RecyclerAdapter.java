package com.sommelier.adapter;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.GenericTransitionOptions;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.transition.ViewPropertyTransition;
import com.sommelier.helper.AnimationUtil;
import com.sommelier.R;
import com.sommelier.model.SpecificationsModel;
import com.sommelier.activity.DetailsActivity;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.BlurTransformation;


public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerViewHolder> {

    private final LayoutInflater mInflater;
    private List<SpecificationsModel> mItemList;
    private int Previusposition = 0;

    public RecyclerAdapter(Context context, List<SpecificationsModel> itemList) {
        mInflater = LayoutInflater.from(context);
        mItemList = new ArrayList<>(itemList);
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;

        RecyclerViewHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card_academy);
        }
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = mInflater.inflate(R.layout.item_main_card, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        final SpecificationsModel data = mItemList.get(position);
        CardView cardView = holder.cardView;

        TextView textName = cardView.findViewById(R.id.text_name);
        ImageView photoSmall = cardView.findViewById(R.id.photo_small);

        textName.setText(data.getName());

        RequestOptions requestOptions = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.NONE) // потому что имя файла всегда одинаково
                .skipMemoryCache(true);

        Glide
                .with(cardView.getContext())
                .load(data.getPhotoSmall())
                //.apply(requestOptions)
                .apply(new RequestOptions()
//                        .placeholder(R.drawable.placeholder)
//                        .fallback(R.drawable.ic_520016)
//                        .error(R.drawable.oops)
                                //.diskCacheStrategy(DiskCacheStrategy.ALL)
                                //  .skipMemoryCache(true)
                                .transform(new BlurTransformation(1, 1))
                )
                .thumbnail(0.5f)// зменшив розмір попередного перегляду фото у 2 рази
                // Todo: Glide
                //.signature(new ObjectKey(Long.toString(System.currentTimeMillis())))
                // .signature(new ObjectKey(System.currentTimeMillis() / (24 * 60 * 60 * 1000))) // 1 day
                //.signature(new ObjectKey(System.currentTimeMillis() / (10 * 60 * 1000))) // 10 min
                .transition(GenericTransitionOptions.with(animationObject))
                .into(photoSmall);

        cardView.setOnClickListener(v -> {
            Context context = holder.itemView.getContext();
            Intent intent = new Intent(v.getContext(), DetailsActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(DetailsActivity.EXTRA_GRAPES_ID, mItemList.get(position));
            intent.putExtras(bundle);
            context.startActivity(intent);
        });

        // Анімація при завантаженні CardView
        //Animation animation = AnimationUtils.loadAnimation(cardView.getContext(), android.R.anim.slide_in_left);
        //Animation animation = AnimationUtils.loadAnimation(cardView.getContext(), R.anim.fade_in);
        //holder.itemView.startAnimation(animation);

        if (position > Previusposition) {
            AnimationUtil.animate(holder, true);
        } else {
            AnimationUtil.animate(holder, false);
        }
        Previusposition = position;
    }

    @Override
    public int getItemCount() {
        return this.mItemList.size();
    }

    public void setItems(List<SpecificationsModel> datas) {
        mItemList = new ArrayList<>(datas);
        notifyDataSetChanged();
    }

    // Анімація завантаження картинки Glide
    private final ViewPropertyTransition.Animator animationObject = view -> {
        view.setAlpha(0f);

        ObjectAnimator fadeAnim = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f);
        fadeAnim.setDuration(2500);
        fadeAnim.start();
    };

    public void clear() {
        mItemList.clear();
        notifyDataSetChanged();
    }

    // Добавить список предметов - изменить на используемый тип
    public void addAll(List<SpecificationsModel> mItemList) {
        mItemList.addAll(mItemList);
        notifyDataSetChanged();
    }
}
