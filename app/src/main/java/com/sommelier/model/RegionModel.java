package com.sommelier.model;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;

public class RegionModel {
    //id
    public String id;
    // Ім'я
    public String name;
    // Малювання шляху
    public Path path;
    // Температура
    public String temperature;
    // Намалюйте колір
    public int drawColor;

    public RegionModel(String id, String name, String temperature, Path path, int drawColor) {
        this.id = id;
        this.name = name;
        this.temperature = temperature;
        this.path = path;
        this.drawColor = drawColor;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        this.path = path;
    }

    public int getDrawColor() {
        return drawColor;
    }

    public void setDrawColor(int drawColor) {
        this.drawColor = drawColor;
    }

    /**
     * Елемент малювання
     *
     * @param canvas
     * @param paint
     * @param isSelected
     */
    public void drawItem(Canvas canvas, Paint paint, boolean isSelected) {
        if (isSelected) {
            paint.setStrokeWidth(2);
            paint.setColor(Color.BLACK);
            paint.setStyle(Paint.Style.FILL);
            paint.setShadowLayer(3, 0, 0, Color.RED);
            canvas.drawPath(path, paint);

            paint.clearShadowLayer();
            paint.setColor(Color.RED);// колір натиснутої області
            paint.setStyle(Paint.Style.FILL);
            paint.setStrokeWidth(2);
            canvas.drawPath(path, paint);
        } else {
            // Якщо не вибрано, намалюйте ефект удар
            paint.clearShadowLayer();
            paint.setStrokeWidth(1);
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(drawColor);
            canvas.drawPath(path, paint);

            paint.setStyle(Paint.Style.STROKE);
            int strokeColor = 0xFFD0E8F4;
            paint.setColor(strokeColor);
            canvas.drawPath(path, paint);
        }
    }

    /**
     * Торкніться його
     *
     * @param x
     * @param y
     * @return
     */
    public boolean isTouch(int x, int y) {
        // Побудувати об'єкт зони
        RectF rectF = new RectF();
        // Обчислити межу контрольної точки
        path.computeBounds(rectF, true);
        Region region = new Region();
        region.setPath(path, new Region((int) rectF.left, (int) rectF.top, (int) rectF.right, (int) rectF.bottom));
        return region.contains(x, y);
    }
}
