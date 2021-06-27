package com.sommelier.helper;


import com.sommelier.model.SpecificationsModel;

import java.util.Comparator;

// Для сортування даних використовуємо SortableTableView замість звичайного TableView.
// Для того, щоб таблиця сортується по колонці, все, що потрібно зробити, це реалізувати
// компаратор і встановити його в конкретній колонці:

final class SortableComparators {

    private SortableComparators() {
    }

    static Comparator<SpecificationsModel> getGrapesNameComparator() {
        return new GrapesNameComparator();
    }

    static Comparator<SpecificationsModel> getGrapesSortyComparator() {
        return new GrapesSortyComparator();
    }

    static Comparator<SpecificationsModel> getGrapesTermComparator() {
        return new GrapesTermComparator();
    }

    static Comparator<SpecificationsModel> getGrapesFrostComparator() {
        return new GrapesFrostComparator();
    }


    static Comparator<SpecificationsModel> getGrapesColorComparator() {
        return new GrapesColorComparator();
    }

    static Comparator<SpecificationsModel> getGrapesGrowthComparator() {
        return new GrapesGrowthComparator();
    }

    static Comparator<SpecificationsModel> getGrapesWeightComparator() {
        return new WeightComparator();
    }



    private static class GrapesNameComparator implements Comparator<SpecificationsModel> {

        @Override
        public int compare(final SpecificationsModel grapes1, final SpecificationsModel grapes2) {
            return grapes1.getName().compareTo(grapes2.getName());
        }
    }

    private static class GrapesSortyComparator implements Comparator<SpecificationsModel> {

        @Override
        public int compare(final SpecificationsModel grapes1, final SpecificationsModel grapes2) {
            return grapes1.getSort().compareTo(grapes2.getSort());
        }
    }

    private static class GrapesTermComparator implements Comparator<SpecificationsModel> {

        @Override
        public int compare(final SpecificationsModel grapes1, final SpecificationsModel grapes2) {
            return grapes1.getTerm().compareTo(grapes2.getTerm());
        }
    }

    private static class GrapesFrostComparator implements Comparator<SpecificationsModel> {

        @Override
        public int compare(final SpecificationsModel grapes1, final SpecificationsModel grapes2) {
            return Integer.compare(grapes1.getFrost(), grapes2.getFrost());
        }
    }

    private static class GrapesColorComparator implements Comparator<SpecificationsModel> {

        @Override
        public int compare(final SpecificationsModel grapes1, final SpecificationsModel grapes2) {
            return grapes1.getColor().compareTo(grapes2.getColor());
        }
    }

    private static class GrapesGrowthComparator implements Comparator<SpecificationsModel> {

        @Override
        public int compare(final SpecificationsModel grapes1, final SpecificationsModel grapes2) {
            return grapes1.getGrowth().compareTo(grapes2.getGrowth());
        }
    }

    private static class WeightComparator implements Comparator<SpecificationsModel> {

        @Override
        public int compare(final SpecificationsModel grapes1, final SpecificationsModel grapes2) {
            return Double.compare(grapes1.getWeight(), grapes2.getWeight());
        }
    }
}