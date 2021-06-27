package com.sommelier.helper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;

import com.sommelier.model.RegionModel;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


public class RegionMapView extends View {

    public Context mContext;

    private Paint mPaint;

    private int mapRes = -1;

    private List<RegionModel> itemList;

    // Вибрана область
    private RegionModel selectItem;

    // Коефіцієнт масштабування
    private float scale = 1.3f;

    private RectF totalRectF;

    public RegionMapView(Context context) {
        this(context, null);
    }

    public RegionMapView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RegionMapView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int viewWidth = widthSize;
        int viewHeight = heightSize;

        if (totalRectF != null) {
            float widthScale = viewWidth * 1.0f / totalRectF.width();
            float heightScale = viewHeight * 1.0f / totalRectF.height();
            if (widthScale <= heightScale) {
                viewHeight = Math.min(viewHeight, (int) (widthScale * totalRectF.height()));
            } else {
                viewWidth = Math.min(viewWidth, (int) (heightScale * totalRectF.width()));
            }
            scale = Math.min(widthScale, heightScale);
        }
        setMeasuredDimension(MeasureSpec.makeMeasureSpec(viewWidth, widthMeasureSpec),
                MeasureSpec.makeMeasureSpec(viewHeight, heightMeasureSpec));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (itemList != null) {
            canvas.save();
            canvas.scale(scale, scale);
            for (RegionModel item : itemList) {
                if (item != selectItem) {
                    item.drawItem(canvas, mPaint, false);
                }
            }
            if (selectItem != null) {
                selectItem.drawItem(canvas, mPaint, true);
            }
        }
    }

    /**
     * Налаштувати ресурси карти
     *
     * @param mapRes
     */
    public void setMapRes(int mapRes) {
        this.mapRes = mapRes;
    }

    /**
     * 解析地图数据
     */
    public void loadMap() {
        if (mapRes != -1) {
            loadMapThread.start();
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        handleTouch(event.getX(), event.getY());
        return true;
    }

    private void handleTouch(float x, float y) {
        if (itemList == null) {
            return;
        }
        RegionModel tmpItem = null;
        for (RegionModel item : itemList) {
            if (item.isTouch((int) (x / scale), (int) (y / scale))) {
                tmpItem = item;
                if (listener != null) {
                    listener.onItemClick(item);
                }
                break;
            }
        }
        if (tmpItem != null) {
            selectItem = tmpItem;
            postInvalidate();
        }
    }


    /**
     * Аналіз потоків даних карти
     */
    private Thread loadMapThread = new Thread() {
        @Override
        public void run() {
            super.run();
            List<RegionModel> list = new ArrayList<>();
            InputStream inputStream = mContext.getResources().openRawResource(mapRes);
            try {
                // Отримайте екземпляр DocumentBuilderFactory
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                // Отримайте екземпляр DocumentBuilder із заводу
                DocumentBuilder builder = factory.newDocumentBuilder();
                // Проаналізуйте вхідний потік, щоб отримати екземпляр документа
                Document doc = builder.parse(inputStream);
                Element rootElement = doc.getDocumentElement();
                NodeList items = rootElement.getElementsByTagName("path");

                float left = -1;
                float top = -1;
                float right = -1;
                float bottom = -1;
                for (int i = 0; i < items.getLength(); i++) {
                    Element element = (Element) items.item(i);
                    String id = element.getAttribute("android:id");
                    String name = element.getAttribute("android:name");
                    String temperature = element.getAttribute("android:temperature");
                    String pathData = element.getAttribute("android:pathData");
                    String TAG = "MapView";
                    Log.i(TAG, "id: " + id);
                    Log.i(TAG, "name: " + name);
                    Log.i(TAG, "temperature: " + temperature);
                    Log.i(TAG, "pathData: " + pathData);


                    Path path = RegionPathParser.createPathFromPathData(pathData);

                    RectF rectF = new RectF();
                    assert path != null;
                    path.computeBounds(rectF, true);

                    left = left == -1 ? rectF.left : Math.min(rectF.left, left);
                    top = top == -1 ? rectF.top : Math.min(rectF.top, top);

                    right = right == -1 ? rectF.right : Math.max(rectF.right, right);
                    bottom = bottom == -1 ? rectF.bottom : Math.max(rectF.bottom, bottom);


                    int drawColor = RegionColorUtils.getMapColor(id);
                    RegionModel proviceItem = new RegionModel(id, name, temperature, path, drawColor);
                    list.add(proviceItem);
                }

                // Візьміть найбільшу площу
                totalRectF = new RectF(left, top, right, bottom);

            } catch (Exception e) {
                e.printStackTrace();
            }
            itemList = list;
            mHandler.sendEmptyMessage(0);
        }
    };

    /**
     *  Завантаження карти
     */
    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (itemList == null) {
                return;
            }
            requestLayout();
        }
    };

    OnMapItemListener listener;

    public void setOnMapItemListener(OnMapItemListener listener) {
        this.listener = listener;
    }

    public interface OnMapItemListener {
        void onItemClick(RegionModel item);
    }









}
