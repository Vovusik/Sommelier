package com.sommelier.helper;

import android.graphics.Path;
import android.util.Log;

import java.util.ArrayList;


/**
 * path Клас сумісності з конфігурацією шляху (сумісний зі стандартною svg)
 */
public class RegionPathParser {

    private static final String LOG_TAG = "RegionPathParser";

    // Копіювати з Arrays.copyOfRange (), який доступний лише з рівня 9 API.

    /**
     * Копіює елементи з {@code original} в новий масив, починаючи від індексів (включно) до
     * кінець (ексклюзивний). Оригінальний порядок елементів зберігається.
     * Якщо {@code end} перевищує {@code original.length}, результат буде заповнений
     * з значенням {@code 0.0f}.
     *
     * @param original оригінальний масив
     * @param start    стартовий індекс, включно
     * @param end      кінцевий індекс, ексклюзивний
     * @return новий масив
     * @throws IllegalArgumentException if {@code start > end}
     * @throws NullPointerException     if {@code original == null}
     */
    private static float[] copyOfRange(float[] original, int start, int end) {
        if (start > end) {
            throw new IllegalArgumentException();
        }
        int originalLength = original.length;
        if (start < 0 || start > originalLength) {
            throw new ArrayIndexOutOfBoundsException();
        }
        int resultLength = end - start;
        int copyLength = Math.min(resultLength, originalLength - start);
        float[] result = new float[resultLength];
        System.arraycopy(original, start, result, 0, copyLength);
        return result;
    }

    /**
     * @param pathData Строка представляє шлях, такий самий, як рядок "d" у файлі svg.
     * @ повернути створений об'єкт Path.
     */
    public static Path createPathFromPathData(String pathData) {
        Path path = new Path();
        PathDataNode[] nodes = createNodesFromPathData(pathData);
        if (nodes != null) {
            try {
                PathDataNode.nodesToPath(nodes, path);
            } catch (RuntimeException e) {
                throw new RuntimeException("Error in parsing " + pathData, e);
            }
            return path;
        }
        return null;
    }

    /**
     * @param pathData Строка, що представляє шлях, така ж, як рядок "d" у файлі svg.
     * @ повернути масив PathDataNode.
     */
    public static PathDataNode[] createNodesFromPathData(String pathData) {
        if (pathData == null) {
            return null;
        }
        int start = 0;
        int end = 1;

        ArrayList<PathDataNode> list = new ArrayList<PathDataNode>();
        while (end < pathData.length()) {
            end = nextStart(pathData, end);
            String s = pathData.substring(start, end).trim();
            if (s.length() > 0) {
                float[] val = getFloats(s);
                addNode(list, s.charAt(0), val);
            }

            start = end;
            end++;
        }
        if ((end - start) == 1 && start < pathData.length()) {
            addNode(list, pathData.charAt(start), new float[0]);
        }
        return list.toArray(new PathDataNode[list.size()]);
    }

    /**
     * @param source Матриця PathDataNode буде дублюватися.
     * @ повернути глибоку копію <code> source </ code>.
     */
    public static PathDataNode[] deepCopyNodes(PathDataNode[] source) {
        if (source == null) {
            return null;
        }
        PathDataNode[] copy = new PathDataNode[source.length];
        for (int i = 0; i < source.length; i++) {
            copy[i] = new PathDataNode(source[i]);
        }
        return copy;
    }

    /**
     * @param nodesFrom вихідний шлях, представлений у масиві PathDataNode
     * @param nodesTo Цей шлях представлений у масиві PathDataNode
     * @return, чи <code> вузли від </ code> можуть перетворитися на <code> nodesTo </ code>
     */
    public static boolean canMorph(PathDataNode[] nodesFrom, PathDataNode[] nodesTo) {
        if (nodesFrom == null || nodesTo == null) {
            return false;
        }

        if (nodesFrom.length != nodesTo.length) {
            return false;
        }

        for (int i = 0; i < nodesFrom.length; i++) {
            if (nodesFrom[i].type != nodesTo[i].type
                    || nodesFrom[i].params.length != nodesTo[i].params.length) {
                return false;
            }
        }
        return true;
    }

    /**
     * Оновити дані цілі, щоб вони відповідали джерела.
     * Перед тим, як викликати це, переконайтеся, що canMorph (ціль, джерело) є правильним.
     *
     * Параметр @param target Цей шлях представлений у масиві PathDataNode
     * @param source Шлях до джерела, представлений у масиві PathDataNode
     */
    public static void updateNodes(PathDataNode[] target, PathDataNode[] source) {
        for (int i = 0; i < source.length; i++) {
            target[i].type = source[i].type;
            for (int j = 0; j < source[i].params.length; j++) {
                target[i].params[j] = source[i].params[j];
            }
        }
    }

    private static int nextStart(String s, int end) {
        char c;

        while (end < s.length()) {
            c = s.charAt(end);
            // Зауважте, що 'e' або 'E' не є коректними кодами шляху, але можуть бути
            // використовується для наукових позначень числа з плаваючою точкою.
            // Таким чином, при пошуку наступної команди ми повинні ігнорувати "e"
            // і 'E'.
            if ((((c - 'A') * (c - 'Z') <= 0) || ((c - 'a') * (c - 'z') <= 0))
                    && c != 'e' && c != 'E') {
                return end;
            }
            end++;
        }
        return end;
    }

    private static void addNode(ArrayList<PathDataNode> list, char cmd, float[] val) {
        list.add(new PathDataNode(cmd, val));
    }

    private static class ExtractFloatResult {
        // Потрібно повернути позицію наступного роздільника і чи є
        // наступна поплавка починається з '-' або '.'.
        int mEndPosition;
        boolean mEndWithNegOrDot;
    }

    /**
     * Проаналізуйте поплавці в рядку.
     * Це оптимізована версія parseFloat (s.split (", | \\ s"));
     *
     * @param s рядок, що містить команду і список поплавків
     * @return масиву поплавків
     */
    private static float[] getFloats(String s) {
        if (s.charAt(0) == 'z' | s.charAt(0) == 'Z') {
            return new float[0];
        }
        try {
            float[] results = new float[s.length()];
            int count = 0;
            int startPosition = 1;
            int endPosition = 0;

            ExtractFloatResult result = new ExtractFloatResult();
            int totalLength = s.length();

            // StartPosition завжди повинен бути першим символом
            // поточний номер, а endPosition - символ після поточного номера
            while (startPosition < totalLength) {
                extract(s, startPosition, result);
                endPosition = result.mEndPosition;

                if (startPosition < endPosition) {
                    results[count++] = Float.parseFloat(
                            s.substring(startPosition, endPosition));
                }

                if (result.mEndWithNegOrDot) {
                    // Зберегти '-' або '.' підписати з наступним номером
                    startPosition = endPosition;
                } else {
                    startPosition = endPosition + 1;
                }
            }
            return copyOfRange(results, 0, count);
        } catch (NumberFormatException e) {
            throw new RuntimeException("error in parsing \"" + s + "\"", e);
        }
    }

    //     Обчислити позицію наступної коми або простору або негативного знаку
//
//    @param s рядок для пошуку
//    @param розпочинає позицію, щоб розпочати пошук
//    @param призводить до результату вилучення, включаючи позицію
//    початкова позиція наступного номера, незалежно від того, закінчується чи '-'.
    private static void extract(String s, int start, ExtractFloatResult result) {
        // Тепер шукаю '', ',', '.' або "-" з самого початку.
        int currentIndex = start;
        boolean foundSeparator = false;
        result.mEndWithNegOrDot = false;
        boolean secondDot = false;
        boolean isExponential = false;
        for (; currentIndex < s.length(); currentIndex++) {
            boolean isPrevExponential = isExponential;
            isExponential = false;
            char currentChar = s.charAt(currentIndex);
            switch (currentChar) {
                case ' ':
                case ',':
                    foundSeparator = true;
                    break;
                case '-':
                    // Негативний знак, що слідує за "e" або "E", не є роздільником.
                    if (currentIndex != start && !isPrevExponential) {
                        foundSeparator = true;
                        result.mEndWithNegOrDot = true;
                    }
                    break;
                case '.':
                    if (!secondDot) {
                        secondDot = true;
                    } else {
                        // Це друга точка, і вона розглядається як сепаратор.
                        foundSeparator = true;
                        result.mEndWithNegOrDot = true;
                    }
                    break;
                case 'e':
                case 'E':
                    isExponential = true;
                    break;
            }
            if (foundSeparator) {
                break;
            }
        }
        // Якщо нічого не знайдено, то ми поставимо кінцеву позицію до кінця рядка
        result.mEndPosition = currentIndex;
    }

    /**
     * Кожний PathDataNode представляє одну команду в атрибуті "d" svg файл
     * Масив PathDataNode може представляти весь атрибут "d".
     */
    public static class PathDataNode {
        /*пакет*/
        char type;
        float[] params;

        private PathDataNode(char type, float[] params) {
            this.type = type;
            this.params = params;
        }

        private PathDataNode(PathDataNode n) {
            type = n.type;
            params = copyOfRange(n.params, 0, n.params.length);
        }

        /**
         * Перетворення масиву PathDataNode в шлях.
         *
         *            * @param вузол вихідний масив PathDataNode.
                   * @param path Об'єкт цільового шляху.
         */
        public static void nodesToPath(PathDataNode[] node, Path path) {
            float[] current = new float[6];
            char previousCommand = 'm';
            for (int i = 0; i < node.length; i++) {
                addCommand(path, current, previousCommand, node[i].type, node[i].params);
                previousCommand = node[i].type;
            }
        }

        /**
         * Поточний PathDataNode буде інтерполізовано між
         * <code> nodeFrom </ code> і <code> nodeTo </ code> відповідно до
         * <code> fraction </ code>.
         *
         * @param nodeFrom Початкове значення як PathDataNode.
         * @param nodeTo Кінцеве значення як PathDataNode
         * @param fraction Фракція для інтерполяції.
         */
        public void interpolatePathDataNode(PathDataNode nodeFrom,
                                            PathDataNode nodeTo, float fraction) {
            for (int i = 0; i < nodeFrom.params.length; i++) {
                params[i] = nodeFrom.params[i] * (1 - fraction)
                        + nodeTo.params[i] * fraction;
            }
        }

        private static void addCommand(Path path, float[] current,
                                       char previousCmd, char cmd, float[] val) {

            int incr = 2;
            float currentX = current[0];
            float currentY = current[1];
            float ctrlPointX = current[2];
            float ctrlPointY = current[3];
            float currentSegmentStartX = current[4];
            float currentSegmentStartY = current[5];
            float reflectiveCtrlPointX;
            float reflectiveCtrlPointY;

            switch (cmd) {
                case 'z':
                case 'Z':
                    path.close();
                    // Шлях закрито тут, але ми повинні рухати перо до
                    // закрите положення. Таким чином, ми зберігаємо вихідну позицію сегмента,
                    // і відновити його тут.
                    currentX = currentSegmentStartX;
                    currentY = currentSegmentStartY;
                    ctrlPointX = currentSegmentStartX;
                    ctrlPointY = currentSegmentStartY;
                    path.moveTo(currentX, currentY);
                    break;
                case 'm':
                case 'M':
                case 'l':
                case 'L':
                case 't':
                case 'T':
                    incr = 2;
                    break;
                case 'h':
                case 'H':
                case 'v':
                case 'V':
                    incr = 1;
                    break;
                case 'c':
                case 'C':
                    incr = 6;
                    break;
                case 's':
                case 'S':
                case 'q':
                case 'Q':
                    incr = 4;
                    break;
                case 'a':
                case 'A':
                    incr = 7;
                    break;
            }

            for (int k = 0; k < val.length; k += incr) {
                switch (cmd) {
                    case 'm': // moveto - Почати новий sub-path (відносний)
                        currentX += val[k + 0];
                        currentY += val[k + 1];
                        if (k > 0) {
                            // За специфікацією, якщо за moveto слідувати декілька
                            // пари координат, подальші пари розглядаються як
                            // implicit lineto команди.
                            path.rLineTo(val[k + 0], val[k + 1]);
                        } else {
                            path.rMoveTo(val[k + 0], val[k + 1]);
                            currentSegmentStartX = currentX;
                            currentSegmentStartY = currentY;
                        }
                        break;
                    case 'M': // moveto - Почати новий підтеп
                        currentX = val[k + 0];
                        currentY = val[k + 1];
                        if (k > 0) {
                            // За специфікацією, якщо за moveto слідувати декілька
                            // пари координат, подальші пари розглядаються як
                            // implicit lineto команди.
                            path.lineTo(val[k + 0], val[k + 1]);
                        } else {
                            path.moveTo(val[k + 0], val[k + 1]);
                            currentSegmentStartX = currentX;
                            currentSegmentStartY = currentY;
                        }
                        break;
                    case 'l': // lineto - намалювати рядок з поточної точки (відносний)
                        path.rLineTo(val[k + 0], val[k + 1]);
                        currentX += val[k + 0];
                        currentY += val[k + 1];
                        break;
                    case 'L': // lineto - намалювати рядок з поточної точки
                        path.lineTo(val[k + 0], val[k + 1]);
                        currentX = val[k + 0];
                        currentY = val[k + 1];
                        break;
                    case 'h': // horizontal lineto - Малює горизонтальну лінію (відносна);
                        path.rLineTo(val[k + 0], 0);
                        currentX += val[k + 0];
                        break;
                    case 'H': // horizontal lineto - Малює горизонтальну лінію
                        path.lineTo(val[k + 0], currentY);
                        currentX = val[k + 0];
                        break;
                    case 'v': // vertical lineto - Малює вертикальну лінію від поточної точки (r)
                        path.rLineTo(0, val[k + 0]);
                        currentY += val[k + 0];
                        break;
                    case 'V': // vertical lineto - Малює вертикальну лінію від поточної точки
                        path.lineTo(currentX, val[k + 0]);
                        currentY = val[k + 0];
                        break;
                    case 'c': // curveto - Малює кубічну криву Безезера (відносний)
                        path.rCubicTo(val[k + 0], val[k + 1], val[k + 2], val[k + 3],
                                val[k + 4], val[k + 5]);

                        ctrlPointX = currentX + val[k + 2];
                        ctrlPointY = currentY + val[k + 3];
                        currentX += val[k + 4];
                        currentY += val[k + 5];

                        break;
                    case 'C': // curveto - Малює кубічну криву Безье
                        path.cubicTo(val[k + 0], val[k + 1], val[k + 2], val[k + 3],
                                val[k + 4], val[k + 5]);
                        currentX = val[k + 4];
                        currentY = val[k + 5];
                        ctrlPointX = val[k + 2];
                        ctrlPointY = val[k + 3];
                        break;
                    case 's': // smooth curveto - Малює кубічну криву Безье (рефлексивний к.п.н.)
                        reflectiveCtrlPointX = 0;
                        reflectiveCtrlPointY = 0;
                        if (previousCmd == 'c' || previousCmd == 's'
                                || previousCmd == 'C' || previousCmd == 'S') {
                            reflectiveCtrlPointX = currentX - ctrlPointX;
                            reflectiveCtrlPointY = currentY - ctrlPointY;
                        }
                        path.rCubicTo(reflectiveCtrlPointX, reflectiveCtrlPointY,
                                val[k + 0], val[k + 1],
                                val[k + 2], val[k + 3]);

                        ctrlPointX = currentX + val[k + 0];
                        ctrlPointY = currentY + val[k + 1];
                        currentX += val[k + 2];
                        currentY += val[k + 3];
                        break;
                    case 'S': // shorthand/smooth curveto - Малює кубічну криву Безье (рефлексивний к.п.н.)
                        reflectiveCtrlPointX = currentX;
                        reflectiveCtrlPointY = currentY;
                        if (previousCmd == 'c' || previousCmd == 's'
                                || previousCmd == 'C' || previousCmd == 'S') {
                            reflectiveCtrlPointX = 2 * currentX - ctrlPointX;
                            reflectiveCtrlPointY = 2 * currentY - ctrlPointY;
                        }
                        path.cubicTo(reflectiveCtrlPointX, reflectiveCtrlPointY,
                                val[k + 0], val[k + 1], val[k + 2], val[k + 3]);
                        ctrlPointX = val[k + 0];
                        ctrlPointY = val[k + 1];
                        currentX = val[k + 2];
                        currentY = val[k + 3];
                        break;
                    case 'q': // Намалює квадратний біз'є (відносний)
                        path.rQuadTo(val[k + 0], val[k + 1], val[k + 2], val[k + 3]);
                        ctrlPointX = currentX + val[k + 0];
                        ctrlPointY = currentY + val[k + 1];
                        currentX += val[k + 2];
                        currentY += val[k + 3];
                        break;
                    case 'Q': // Малює квадратне Bézier
                        path.quadTo(val[k + 0], val[k + 1], val[k + 2], val[k + 3]);
                        ctrlPointX = val[k + 0];
                        ctrlPointY = val[k + 1];
                        currentX = val[k + 2];
                        currentY = val[k + 3];
                        break;
                    case 't': // Малює квадратичну криву Безье (рефлексивну контрольну точку) (відносний)
                        reflectiveCtrlPointX = 0;
                        reflectiveCtrlPointY = 0;
                        if (previousCmd == 'q' || previousCmd == 't'
                                || previousCmd == 'Q' || previousCmd == 'T') {
                            reflectiveCtrlPointX = currentX - ctrlPointX;
                            reflectiveCtrlPointY = currentY - ctrlPointY;
                        }
                        path.rQuadTo(reflectiveCtrlPointX, reflectiveCtrlPointY,
                                val[k + 0], val[k + 1]);
                        ctrlPointX = currentX + reflectiveCtrlPointX;
                        ctrlPointY = currentY + reflectiveCtrlPointY;
                        currentX += val[k + 0];
                        currentY += val[k + 1];
                        break;
                    case 'T': // малює квадратичну криву Безье (рефлексивну контрольну точку)
                        reflectiveCtrlPointX = currentX;
                        reflectiveCtrlPointY = currentY;
                        if (previousCmd == 'q' || previousCmd == 't'
                                || previousCmd == 'Q' || previousCmd == 'T') {
                            reflectiveCtrlPointX = 2 * currentX - ctrlPointX;
                            reflectiveCtrlPointY = 2 * currentY - ctrlPointY;
                        }
                        path.quadTo(reflectiveCtrlPointX, reflectiveCtrlPointY,
                                val[k + 0], val[k + 1]);
                        ctrlPointX = reflectiveCtrlPointX;
                        ctrlPointY = reflectiveCtrlPointY;
                        currentX = val[k + 0];
                        currentY = val[k + 1];
                        break;
                    case 'a': // малює еліптичну дугу
                        // (rx ry x-ось-обертання великий дуг-прапор sweep-flag x y)
                        drawArc(path,
                                currentX,
                                currentY,
                                val[k + 5] + currentX,
                                val[k + 6] + currentY,
                                val[k + 0],
                                val[k + 1],
                                val[k + 2],
                                val[k + 3] != 0,
                                val[k + 4] != 0);
                        currentX += val[k + 5];
                        currentY += val[k + 6];
                        ctrlPointX = currentX;
                        ctrlPointY = currentY;
                        break;
                    case 'A': // Малює еліптичну дугу
                        drawArc(path,
                                currentX,
                                currentY,
                                val[k + 5],
                                val[k + 6],
                                val[k + 0],
                                val[k + 1],
                                val[k + 2],
                                val[k + 3] != 0,
                                val[k + 4] != 0);
                        currentX = val[k + 5];
                        currentY = val[k + 6];
                        ctrlPointX = currentX;
                        ctrlPointY = currentY;
                        break;
                }
                previousCmd = cmd;
            }
            current[0] = currentX;
            current[1] = currentY;
            current[2] = ctrlPointX;
            current[3] = ctrlPointY;
            current[4] = currentSegmentStartX;
            current[5] = currentSegmentStartY;
        }

        private static void drawArc(Path p,
                                    float x0,
                                    float y0,
                                    float x1,
                                    float y1,
                                    float a,
                                    float b,
                                    float theta,
                                    boolean isMoreThanHalf,
                                    boolean isPositiveArc) {

            /* Перетворення кута повороту від градусів до радіана */
            double thetaD = Math.toRadians(theta);
            /* Попередньо обчислити матричні записи ротації */
            double cosTheta = Math.cos(thetaD);
            double sinTheta = Math.sin(thetaD);
            /* Перетворення (x0, y0) та (x1, y1) в одиничне простір */
            /* використовуючи (зворотне) обертання, а потім (зворотний) масштаб */
            double x0p = (x0 * cosTheta + y0 * sinTheta) / a;
            double y0p = (-x0 * sinTheta + y0 * cosTheta) / b;
            double x1p = (x1 * cosTheta + y1 * sinTheta) / a;
            double y1p = (-x1 * sinTheta + y1 * cosTheta) / b;

            /* Обчислити відмінності та середні значення */
            double dx = x0p - x1p;
            double dy = y0p - y1p;
            double xm = (x0p + x1p) / 2;
            double ym = (y0p + y1p) / 2;
            /* Вирішіть для пересічних одиничних колам */
            double dsq = dx * dx + dy * dy;
            if (dsq == 0.0) {
                Log.w(LOG_TAG, " Points are coincident");
                return; /* Окуляри збігаються */
            }
            double disc = 1.0 / dsq - 1.0 / 4.0;
            if (disc < 0.0) {
                Log.w(LOG_TAG, "Points are too far apart " + dsq);
                float adjust = (float) (Math.sqrt(dsq) / 1.99999);
                drawArc(p, x0, y0, x1, y1, a * adjust,
                        b * adjust, theta, isMoreThanHalf, isPositiveArc);
                return; /* Points are too far apart */
            }
            double s = Math.sqrt(disc);
            double sdx = s * dx;
            double sdy = s * dy;
            double cx;
            double cy;
            if (isMoreThanHalf == isPositiveArc) {
                cx = xm - sdy;
                cy = ym + sdx;
            } else {
                cx = xm + sdy;
                cy = ym - sdx;
            }

            double eta0 = Math.atan2((y0p - cy), (x0p - cx));

            double eta1 = Math.atan2((y1p - cy), (x1p - cx));

            double sweep = (eta1 - eta0);
            if (isPositiveArc != (sweep >= 0)) {
                if (sweep > 0) {
                    sweep -= 2 * Math.PI;
                } else {
                    sweep += 2 * Math.PI;
                }
            }

            cx *= a;
            cy *= b;
            double tcx = cx;
            cx = cx * cosTheta - cy * sinTheta;
            cy = tcx * sinTheta + cy * cosTheta;

            arcToBezier(p, cx, cy, a, b, x0, y0, thetaD, eta0, sweep);
        }

        /**
         * Перетворює дугу на кубічні сегменти Безье і записує їх на стор.
         *
         * @param p     Ціль для кубічних сегментів Безье
         * @param cx    X координатний центр еліпса
         * @param cy    Y координатний центр еліпса
         * @param a     Радіус еліпса в горизонтальному напрямку
         * @param b     Радіус еліпса в вертикальному напрямку
         * @param e1x   E (eta1) x координата відправної точки дуги
         * @param e1y   E (eta2) y координата відправної точки дуги
         * @param theta Кут, який робить прямокутник, що обмежує еліпс, з горизонтальною площиною
         * @param start Кут початку дуги на еліпсі
         * @param sweep Кут (позитивний чи негативний) розгортки дуги на еліпсі
         */
        private static void arcToBezier(Path p,
                                        double cx,
                                        double cy,
                                        double a,
                                        double b,
                                        double e1x,
                                        double e1y,
                                        double theta,
                                        double start,
                                        double sweep) {
            // Знято з рівнянь за адресою: http://spaceroots.org/documents/ellipse/node8.html
            // і http://www.spaceroots.org/documents/ellipse/node22.html

            // Максимум 45 градусів на кубічний сегмент Безье
            int numSegments = (int) Math.ceil(Math.abs(sweep * 4 / Math.PI));

            double eta1 = start;
            double cosTheta = Math.cos(theta);
            double sinTheta = Math.sin(theta);
            double cosEta1 = Math.cos(eta1);
            double sinEta1 = Math.sin(eta1);
            double ep1x = (-a * cosTheta * sinEta1) - (b * sinTheta * cosEta1);
            double ep1y = (-a * sinTheta * sinEta1) + (b * cosTheta * cosEta1);

            double anglePerSegment = sweep / numSegments;
            for (int i = 0; i < numSegments; i++) {
                double eta2 = eta1 + anglePerSegment;
                double sinEta2 = Math.sin(eta2);
                double cosEta2 = Math.cos(eta2);
                double e2x = cx + (a * cosTheta * cosEta2) - (b * sinTheta * sinEta2);
                double e2y = cy + (a * sinTheta * cosEta2) + (b * cosTheta * sinEta2);
                double ep2x = -a * cosTheta * sinEta2 - b * sinTheta * cosEta2;
                double ep2y = -a * sinTheta * sinEta2 + b * cosTheta * cosEta2;
                double tanDiff2 = Math.tan((eta2 - eta1) / 2);
                double alpha =
                        Math.sin(eta2 - eta1) * (Math.sqrt(4 + (3 * tanDiff2 * tanDiff2)) - 1) / 3;
                double q1x = e1x + alpha * ep1x;
                double q1y = e1y + alpha * ep1y;
                double q2x = e2x - alpha * ep2x;
                double q2y = e2y - alpha * ep2y;

                p.cubicTo((float) q1x,
                        (float) q1y,
                        (float) q2x,
                        (float) q2y,
                        (float) e2x,
                        (float) e2y);
                eta1 = eta2;
                e1x = e2x;
                e1y = e2y;
                ep1x = ep2x;
                ep1y = ep2y;
            }
        }
    }
}
