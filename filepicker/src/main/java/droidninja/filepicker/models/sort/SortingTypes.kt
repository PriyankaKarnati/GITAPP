package droidninja.filepicker.models.sort

import droidninja.filepicker.models.Document


/**
 * Created by gabriel on 10/2/17.
 */
enum class SortingTypes(comparator: Comparator<Document?>?) {
    names(NameComparator()), none(null);

    private val comparator: Comparator<Document?>?
    fun getComparator(): Comparator<Document?>? {
        return comparator
    }

    init {
        this.comparator = comparator
    }
}