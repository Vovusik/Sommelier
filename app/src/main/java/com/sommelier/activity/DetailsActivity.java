package com.sommelier.activity;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.bumptech.glide.GenericTransitionOptions;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.transition.ViewPropertyTransition;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;
import com.r0adkll.slidr.model.SlidrPosition;
import com.sommelier.R;
import com.sommelier.model.SpecificationsModel;

import jp.wasabeef.glide.transformations.BlurTransformation;


public class DetailsActivity extends AppCompatActivity {

    private SpecificationsModel mGrapes;
    public SlidrConfig mConfig;
    int onStartCount = 0;
    public static final String EXTRA_GRAPES_ID = "grapesId";
    private Object savedInstanceState;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_activity);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        mGrapes = (SpecificationsModel) getIntent().getSerializableExtra(EXTRA_GRAPES_ID);

        // Включити кнопку Вверх на панелі дій.
        setSupportActionBar(findViewById(R.id.toolbar));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.toolbar_ic_bottom_arrow);// замените своим пользовательским значком
        }

        CollapsingToolbarLayout mCollapsingToolbar = findViewById(R.id.collapsing_toolbar);
        mCollapsingToolbar.setTitle(mGrapes.getName());
        mCollapsingToolbar.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        mCollapsingToolbar.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);

        // Програмно змінюю шрифт CollapsingToolbarLayout
        Typeface typeface = ResourcesCompat.getFont(this, R.font.open_sans_semi_bold);
        mCollapsingToolbar.setCollapsedTitleTypeface(typeface);
        mCollapsingToolbar.setExpandedTitleTypeface(typeface);

        ProgressBar progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(ProgressBar.VISIBLE);

        ImageView photoLarge = findViewById(R.id.photo_large);

        Glide
                .with(this)
                .load(mGrapes.getPhotoLarge())
                .apply(new RequestOptions()
//                        .diskCacheStrategy(DiskCacheStrategy.ALL)
//                        .skipMemoryCache(true)
                                .transform(new BlurTransformation(1, 1))
                )
                .thumbnail(0.5f)
                //.signature(new ObjectKey(System.currentTimeMillis() / (10 * 60 * 1000)))
                .transition(GenericTransitionOptions.with(animationObject))
                .into(photoLarge);

        TextView textDescription = findViewById(R.id.text_description);
        textDescription.setText(mGrapes.getDescription());

        TextView textLink = findViewById(R.id.text_link);
        textLink.setText(mGrapes.getLink());
        View.OnClickListener clickLink = v -> openCustomTab();
        textLink.setOnClickListener(clickLink);

        animationStartActivity();

        // Добавлення слайду для відміни Activity
        slidrConfig();

        // Добавляю анімацію кнопки FAB
        // fabAnimation();
    }


//    private void fabAnimation() {
//
//        final FloatingActionButton mFab = findViewById(R.id.floatingActionButton);
//        @ColorInt final int colorActive = ContextCompat.getColor(this, android.R.color.white);
//        @ColorInt final int colorPassive = ContextCompat.getColor(this, R.color.colorAccent);
//
//
//        final float from = 1.0f;
//        final float to = 1.2f; //Збільшую FAB
//
//        ObjectAnimator scaleX = ObjectAnimator.ofFloat(mFab, View.SCALE_X, from, to);
//        ObjectAnimator scaleY = ObjectAnimator.ofFloat(mFab, View.SCALE_Y, from, to);
//        ObjectAnimator translationZ = ObjectAnimator.ofFloat(mFab, View.TRANSLATION_Z, from, to);
//
//        AnimatorSet set1 = new AnimatorSet();
//        set1.playTogether(scaleX, scaleY, translationZ);
//        set1.setDuration(100);
//        set1.setInterpolator(new AccelerateInterpolator());
//
//        set1.addListener(new AnimatorListenerAdapter() {
//            @Override
//            public void onAnimationEnd(Animator animation) {
//                mFab.setImageResource(isActive ? R.drawable.detail_ic_favorite_def : R.drawable.detail_ic_favorite_act);
//                mFab.setBackgroundTintList(ColorStateList.valueOf(isActive ? colorPassive : colorActive));
//                isActive = !isActive;
//            }
//        });
//
//        ObjectAnimator scaleXBack = ObjectAnimator.ofFloat(mFab, View.SCALE_X, to, from);
//        ObjectAnimator scaleYBack = ObjectAnimator.ofFloat(mFab, View.SCALE_Y, to, from);
//        ObjectAnimator translationZBack = ObjectAnimator.ofFloat(mFab, View.TRANSLATION_Z, to, from);
//
//        Path path = new Path();
//        path.moveTo(0.0f, 0.0f);
//        path.lineTo(0.5f, 1.3f);
//        path.lineTo(0.75f, 0.8f);
//        path.lineTo(1.0f, 1.0f);
//        PathInterpolator pathInterpolator = new PathInterpolator(path);
//
//        AnimatorSet set2 = new AnimatorSet();
//        set2.playTogether(scaleXBack, scaleYBack, translationZBack);
//        set2.setDuration(300);
//        set2.setInterpolator(pathInterpolator);
//
//        final AnimatorSet set = new AnimatorSet();
//        set.playSequentially(set1, set2);
//
//        set.addListener(new AnimatorListenerAdapter() {
//            @Override
//            public void onAnimationEnd(Animator animation) {
//                mFab.setClickable(true);
//            }
//
//            @Override
//            public void onAnimationStart(Animator animation) {
//                mFab.setClickable(false);
//            }
//        });
//
//        AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
//        alphaAnimation.setDuration(700);
//        alphaAnimationShowIcon = new AlphaAnimation(0.2f, 1.0f);
//        alphaAnimationShowIcon.setDuration(300);
//
//        mFab.setOnClickListener(v -> {
//            if (isActive) {
//                mFab.setImageResource(R.drawable.detail_ic_favorite_def);
//                mFab.startAnimation(alphaAnimationShowIcon);
//                isActive = true;
//
//                Toast toast = Toast.makeText(getApplicationContext(),
//                        "Видалено з улюблених", Toast.LENGTH_LONG);
//                toast.setGravity(Gravity.CENTER, 0, 0);
//                toast.show();
//            } else {
//                mFab.setImageResource(R.drawable.detail_ic_favorite_act);
//                mFab.startAnimation(alphaAnimationShowIcon);
//                isActive = false;
//
//                Toast toast = Toast.makeText(getApplicationContext(),
//                        "Добавлено до улюблених", Toast.LENGTH_LONG);
//                toast.setGravity(Gravity.CENTER, 0, 0);
//                toast.show();
//            }
//
//            set.start();
//        });
//    }


    private void slidrConfig() {
        mConfig = new SlidrConfig.Builder()
                .position(SlidrPosition.TOP)
                .sensitivity(0.5f)
                .build();
        Slidr.attach(this, mConfig);
    }


    @Override
    protected void onResume() {
        super.onResume();

        Configuration configuration = getResources().getConfiguration();
        if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

            CollapsingToolbarLayout collapsing_toolbar_layout = findViewById(R.id.collapsing_toolbar);
            collapsing_toolbar_layout.setExpandedTitleTextColor(ColorStateList.valueOf(Color.TRANSPARENT));
        } else {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }


    private void openCustomTab() {

        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();

        // установить цвета панели инструментов
        builder.setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        builder.setSecondaryToolbarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        // поширити додаток
        builder.addDefaultShareMenuItem();
        // показувати заголовок
        builder.setShowTitle(true);
        // змінюю хрестик на стрілочку повернення додому
        builder.setCloseButtonIcon(BitmapFactory.decodeResource(
                getResources(), R.drawable.toolbar_ic_bottom_arrow));

        // настроить анимацию начала и выхода
        builder.setStartAnimations(this, R.anim.slide_in_bottom, R.anim.slide_out_top);
        builder.setExitAnimations(this, R.anim.slide_in_top, R.anim.slide_out_bottom);

        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(this, Uri.parse(mGrapes.getLink()));
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            //finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    // Анімація завантаження картинки
    public ViewPropertyTransition.Animator animationObject = view -> {
        view.setAlpha(0f);

        ObjectAnimator fadeAnim = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f);
        fadeAnim.setDuration(2500);
        fadeAnim.start();
    };


    private void animationStartActivity() {
        onStartCount = 1;
        if (savedInstanceState == null) {  // 1st time
            this.overridePendingTransition(R.anim.slide_in_bottom,
                    R.anim.slide_in_bottom);
        } else { // уже создана, поэтому обратная анимация
            onStartCount = 2;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        //KitchenDetailsActivity.this.overridePendingTransition(R.anim.nothing,R.anim.nothing);
        overridePendingTransition(R.anim.slide_out_bottom, R.anim.slide_out_bottom);
    }
}