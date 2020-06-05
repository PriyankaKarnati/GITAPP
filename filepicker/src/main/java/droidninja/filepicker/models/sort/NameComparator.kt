package droidninja.filepicker.models.sort


import droidninja.filepicker.models.Document
import java.util.*

/**
 * Created by gabriel on 10/2/17.
 */
class NameComparator : Comparator<Document?> {
    override fun compare(o1: Document?, o2: Document?): Int {
        return o1!!.title!!.toLowerCase().compareTo(o2!!.title!!.toLowerCase())
    }
}