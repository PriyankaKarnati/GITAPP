package droidninja.filepicker.adapters


import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import droidninja.filepicker.models.BaseFile
import java.util.*

abstract class SelectableAdapter<VH : RecyclerView.ViewHolder?, T : BaseFile?>(items: List<T?>, selectedPaths: List<String?>?) : RecyclerView.Adapter<VH>(), Selectable<T> {
    var items: List<T>
        private set
    protected var selectedPhotos: MutableList<T>
    private fun addPathsToSelections(selectedPaths: List<String?>?) {
        if (selectedPaths == null) return
        for (index in items.indices) {
            for (jindex in selectedPaths.indices) {
                if (items[index]!!.path == selectedPaths[jindex]) {
                    selectedPhotos.add(items[index])
                }
            }
        }
    }

    /**
     * Indicates if the item at position where is selected
     *
     * @param photo Media of the item to check
     * @return true if the item is selected, false otherwise
     */
    override fun isSelected(photo: T): Boolean {
        return selectedPhotos.contains(photo)
    }

    /**
     * Toggle the selection status of the item at a given position
     *
     * @param photo Media of the item to toggle the selection status for
     */
    override fun toggleSelection(photo: T) {
        if (selectedPhotos.contains(photo)) {
            selectedPhotos.remove(photo)
        } else {
            selectedPhotos.add(photo)
        }
    }

    /**
     * Clear the selection status for all items
     */
    override fun clearSelection() {
        selectedPhotos.clear()
        notifyDataSetChanged()
    }

    fun selectAll() {
        selectedPhotos.clear()
        selectedPhotos.addAll(items)
        notifyDataSetChanged()
    }

    override val selectedItemCount: Int
        get() = selectedPhotos.size

    fun setData(items: List<T>) {
        this.items = items
    }

    val selectedPaths: ArrayList<String?>
        get() {
            val paths = ArrayList<String?>()
            for (index in selectedPhotos.indices) {
                paths.add(selectedPhotos[index]!!.path)
            }
            return paths
        }

    companion object {
        private val TAG = SelectableAdapter::class.java.simpleName
    }

    init {
        this.items = items as List<T>
        selectedPhotos = ArrayList()
        addPathsToSelections(selectedPaths)
    }

    //abstract fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): PhotoGridAdapter.PhotoViewHolder
    //abstract fun onBindViewHolder(holder: PhotoGridAdapter.PhotoViewHolder, position: Int)
}