package com.sommelier.adapter;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.GenericTransitionOptions;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.bumptech.glide.request.transition.ViewPropertyTransition;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sommelier.R;
import com.sommelier.model.GalleryModel.Photo;

import java.io.IOException;
import java.util.List;


public class GalleryPagerAdapter extends PagerAdapter {

    private final Context context;
    private final List<Photo> listPhotos;
    private final LayoutInflater inflater;
    private FloatingActionButton mFabWallpaper;
    private ImageView photoView;

    public GalleryPagerAdapter(Context context, List<Photo> listPhotos) {
        this.context = context;
        this.listPhotos = listPhotos;
        this.inflater = LayoutInflater.from(context);
    }

    // Переход на другую страницу уничтожит элемент на старой странице
    @Override
    public void destroyItem(ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    // возвращает количество отображаемых элементов
    @Override
    public int getCount() {
        return listPhotos.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view1 = inflater.inflate(R.layout.gallery_item_slide, container, false);

        mFabWallpaper = view1.findViewById(R.id.fab_wallpaper);
        // mFabWallpaper.setOnClickListener(this::showPopupMenu);
        photoView = view1.findViewById(R.id.image_slide);
        Glide.with(context)
                .load(listPhotos.get(position).createURL())
                .into(photoView);
        mFabWallpaper.setOnClickListener(view -> Glide.with(container.getContext())
                .asBitmap()
                .load(listPhotos.get(position).createURL())
                .apply(new RequestOptions())
                .thumbnail(0.5f)
                .transition(GenericTransitionOptions.with(animationObject))
                .into(new CustomTarget<Bitmap>() {
                    @SuppressLint({"WrongConstant", "ShowToast"})
                    public void onResourceReady(@NonNull Bitmap bitmap, Transition<? super Bitmap> transition) {
                        Toast.makeText(context, R.string.message_toast_please_wait, 1).show();
                        try {
                            WallpaperManager.getInstance(context).setBitmap(bitmap);
                            Toast.makeText(context, R.string.message_toast_wallpaper_installed, Toast.LENGTH_LONG).show();

//                            Toast toast = Toast.makeText(context, R.string.message_toast_wallpaper_installed, Toast.LENGTH_LONG);
//                            toast.setGravity(Gravity.CENTER, toast.getXOffset() / 2, toast.getYOffset() / 2);
//
//                            TextView textView = new TextView(context);
//                            textView.setBackgroundResource(R.color.rowBackground);
//                            textView.setTextColor(context.getResources().getColor(R.color.textColorSecondary));
//                            textView.setTextSize(16);
//                            Typeface typeface = Typeface.create(String.valueOf(R.font.open_sans_regular), Typeface.NORMAL);
//                            textView.setTypeface(typeface);
//                            textView.setPadding(16, 16, 16, 16);
//                            textView.setText(R.string.message_toast_wallpaper_installed);
//
//                            toast.setView(textView);
//                            toast.show();


                        } catch (IOException e) {
                            e.printStackTrace();
                            Toast.makeText(context, "" + e, 0).show();
                        }
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                    }
                }));
        container.addView(view1, 0);

        return view1;
    }

    private void showPopupMenu(View view) {
        PopupMenu dropDownMenu = new PopupMenu(context, mFabWallpaper, Gravity.CENTER, 0, R.style.PopupMenuTheme);

        dropDownMenu.getMenuInflater().inflate(R.menu.popup_library, dropDownMenu.getMenu());

        dropDownMenu.getGravity();
        dropDownMenu.setOnMenuItemClickListener(item -> {

            if (item.getItemId() == R.id.downloadBook) {
                //show();
                Toast.makeText(context, "Toast 1", Toast.LENGTH_LONG).show();

                return true;
            } else if (item.getItemId() == R.id.downloadLibrary) {
                Toast.makeText(context, "Toast 2", Toast.LENGTH_LONG).show();

            }
            return false;
        });
        dropDownMenu.show();

    }

    // Метод, который проверяет, связаны ли объекты, возвращаемые
    // instantiateItem (), с предоставленным представлением
    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    // Анімація завантаження картинки Glide
    private final ViewPropertyTransition.Animator animationObject = view -> {
        view.setAlpha(0f);
        ObjectAnimator fadeAnim = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f);
        fadeAnim.setDuration(2500);
        fadeAnim.start();
    };
}
