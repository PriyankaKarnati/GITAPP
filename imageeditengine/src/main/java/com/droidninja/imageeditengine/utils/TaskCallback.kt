package com.droidninja.imageeditengine.utils

import com.droidninja.imageeditengine.model.ImageFilter

interface TaskCallback<T> {
    open fun onTaskDone(data: T)


}