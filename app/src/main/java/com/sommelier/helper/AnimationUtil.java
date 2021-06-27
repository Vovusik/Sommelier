package com.sommelier.helper;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;

import com.sommelier.adapter.RecyclerAdapter;


public class AnimationUtil {

    public static void animate(RecyclerAdapter.RecyclerViewHolder holder, boolean goesdown)
    {
        AnimatorSet animatorSet=new AnimatorSet();
        @SuppressLint("ObjectAnimatorBinding") ObjectAnimator animatorTransilateY= ObjectAnimator.ofFloat(holder.itemView,"translationY",goesdown==true ? 200 :-200,0);
        animatorTransilateY.setDuration(1200);

//                @SuppressLint("ObjectAnimatorBinding") ObjectAnimator animatorTransilateX= ObjectAnimator.ofFloat(holder.itemView,"translationX",200,0);
//        animatorTransilateX.setDuration(1500);

        animatorSet.playTogether(animatorTransilateY);
        animatorSet.start();
    }

}
