package nl.mpcjanssen.simpletask.task

import android.app.Activity
import android.content.Intent
import android.os.SystemClock
import android.util.Log
import nl.mpcjanssen.simpletask.*
import nl.mpcjanssen.simpletask.remote.FileStore
import nl.mpcjanssen.simpletask.remote.IFileStore
import nl.mpcjanssen.simpletask.util.*
import java.util.*
import kotlin.collections.ArrayList

/**
 * Implementation of the in memory representation of the Todo list
 * uses an ActionQueue to ensure modifications and access of the underlying todo list are
 * sequential. If this is not done properly the result is a likely ConcurrentModificationException.

 * @author Mark Janssen
 */

data class TodoItem(val task: Task, val selected: Boolean = false, val pendingEdit: Boolean = false)
data class TodoList(val items: List<TodoItem>) {

    private var mLists: MutableList<String>? = null
    private var mTags: MutableList<String>? = null
    internal val TAG = TodoList::class.java.simpleName

    fun add(tasks: List<Task>, atEnd: Boolean): TodoList {
        Log.d(TAG, "Add task ${items.size} atEnd: $atEnd")
        val updatedItems = tasks.map { item ->
            TodoItem(Interpreter.onAddCallback(item) ?: item)
        }
        return if (atEnd) {
            TodoList(items.plus(updatedItems))
        } else {
            TodoList(updatedItems.plus(items))
        }

    }

    fun addOne(t: Task, atEnd: Boolean): TodoList {
        return add(listOf(t), atEnd)
    }

    fun removeAll(tasks: List<Task>) : TodoList {
        Log.d(TAG, "Remove")
        return TodoList(emptyList())
    }

    fun size(): Int {
        return items.size
    }

    val priorities: ArrayList<Priority>
        get() {
            val res = HashSet<Priority>()
            items.forEach {
                res.add(it.task.priority())
            }
            val ret = ArrayList(res)
            ret.sort()
            return ret
        }

    val contexts: List<String>
        get() {
            val lists = mLists
            if (lists != null) {
                return lists
            }
            val res = HashSet<String>()
            items.forEach { t ->
                res.addAll(t.task.lists())

            }
            val newLists = res.toMutableList()
            mLists = newLists
            return newLists
        }

    val projects: List<String>
        get() {
            val tags = mTags
            if (tags != null) {
                return tags
            }
            val res = HashSet<String>()
            items.forEach { t ->
                res.addAll(t.task.tags())

            }
            val newTags = res.toMutableList()
            mTags = newTags
            return newTags
        }
//
//    fun uncomplete(items: List<Task>) : TodoList {
//        Log.d(TAG, "Uncomplete")
//        items.forEach {
//            it.markIncomplete()
//        }
//    }
//
//    @Synchronized
//    fun complete(tasks: List<Task>, keepPrio: Boolean, extraAtEnd: Boolean) {
//        Log.d(TAG, "Complete")
//        for (task in tasks) {
//            val extra = task.markComplete(todayAsString)
//            if (extra != null) {
//                if (extraAtEnd) {
//                    todoItems.add(extra)
//                } else {
//                    todoItems.add(0, extra)
//                }
//            }
//            if (!keepPrio) {
//                task.priority = Priority.NONE
//            }
//        }
//
//    }
//
//    @Synchronized
//    fun prioritize(tasks: List<Task>, prio: Priority) {
//        Log.d(TAG, "Complete")
//        tasks.map { it.priority = prio }
//    }
//
//    @Synchronized
//    fun defer(deferString: String, tasks: List<Task>, dateType: DateType) {
//        Log.d(TAG, "Defer")
//        tasks.forEach {
//            when (dateType) {
//                DateType.DUE -> it.deferDueDate(deferString, todayAsString)
//                DateType.THRESHOLD -> it.deferThresholdDate(deferString, todayAsString)
//            }
//        }
//    }
//
//    @Synchronized
//    fun update(org: Collection<Task>, updated: List<Task>, addAtEnd: Boolean) {
//        val smallestSize = org.zip(updated) { orgTask, updatedTask ->
//            val idx = todoItems.indexOf(orgTask)
//            if (idx != -1) {
//                todoItems[idx] = updatedTask
//            } else {
//                todoItems.add(updatedTask)
//            }
//            1
//        }.size
//        removeAll(org.toMutableList().drop(smallestSize))
//        add(updated.toMutableList().drop(smallestSize), addAtEnd)
//    }
//
//    val selectedTasks: List<Task>
//        @Synchronized
//        get() {
//            return todoItems.toList().filter { it.selected }
//        }
//
//    val fileFormat : String =  todoItems.toList().joinToString(separator = "\n", transform = {
//        it.inFileFormat(config.useUUIDs)
//    })
//
//
//
//    @Synchronized
//    fun notifyTasklistChanged(todoName: String, save: Boolean, refreshMainUI: Boolean = true) {
//        Log.d(TAG, "Notified changed")
//        if (save) {
//            save(FileStore, todoName, true, config.eol)
//        }
//        if (!config.hasKeepSelection) {
//            clearSelection()
//        }
//        mLists = null
//        mTags = null
//        if (refreshMainUI) {
//            broadcastTasklistChanged(TodoApplication.app.localBroadCastManager)
//        } else {
//            broadcastRefreshWidgets(TodoApplication.app.localBroadCastManager)
//        }
//    }
//
//    @Synchronized
//    private fun startAddTaskActivity(act: Activity, prefill: String) {
//        Log.d(TAG, "Start add/edit task activity")
//        val intent = Intent(act, AddTask::class.java)
//        intent.putExtra(Constants.EXTRA_PREFILL_TEXT, prefill)
//        act.startActivity(intent)
//    }
//
//    @Synchronized
//    fun getSortedTasks(filter: Query, caseSensitive: Boolean): Pair<List<Task>, Int> {
//        val sorts = filter.getSort(config.defaultSorts)
//        Log.d(TAG, "Getting sorted and filtered tasks")
//        val start = SystemClock.elapsedRealtime()
//        val comp = MultiComparator(sorts, TodoApplication.app.today, caseSensitive, filter.createIsThreshold, filter.luaModule)
//        val listCopy = todoItems.toList()
//        val taskCount = listCopy.size
//        val itemsToSort = if (comp.fileOrder) {
//            listCopy
//        } else {
//            listCopy.reversed()
//        }
//        val sortedItems = comp.comparator?.let { itemsToSort.sortedWith(it) } ?: itemsToSort
//        val result = filter.applyFilter(sortedItems, showSelected = true)
//        val end = SystemClock.elapsedRealtime()
//        Log.d(TAG, "Sorting and filtering tasks took ${end - start} ms")
//        return Pair(result, taskCount)
//
//    }
//

//
//    @Synchronized
//    private fun save(fileStore: IFileStore, todoFileName: String, backup: Boolean, eol: String) {
//        broadcastFileSyncStart(TodoApplication.app.localBroadCastManager)
//        val lines = todoItems.map {
//            it.inFileFormat(config.useUUIDs)
//        }
//        // Update cache
//        config.cachedContents = lines.joinToString("\n")
//
//        FileStoreActionQueue.add("Save") {
//            if (backup) {
//                Backupper.backup(todoFileName, lines)
//            }
//            try {
//                Log.i(TAG, "Saving todo list, size ${lines.size}")
//                fileStore.saveTasksToFile(todoFileName, lines, eol = eol)
//                val changesWerePending = config.changesPending
//                config.changesPending = false
//                if (changesWerePending) {
//                    // Remove the red bar
//                    broadcastUpdateStateIndicator(TodoApplication.app.localBroadCastManager)
//                }
//
//            } catch (e: Exception) {
//                Log.e(TAG, "TodoList save to $todoFileName failed", e)
//                config.changesPending = true
//                if (FileStore.isOnline) {
//                    showToastShort(TodoApplication.app, "Saving of todo file failed")
//                }
//            }
//            broadcastFileSyncDone(TodoApplication.app.localBroadCastManager)
//        }
//    }
//
//    @Synchronized
//    fun archive(todoFilename: String, doneFileName: String, tasks: List<Task>, eol: String) {
//        Log.d(TAG, "Archive ${tasks.size} tasks")
//
//        FileStoreActionQueue.add("Append to file") {
//            broadcastFileSyncStart(TodoApplication.app.localBroadCastManager)
//            try {
//                FileStore.appendTaskToFile(doneFileName, tasks.map { it.text }, eol)
//                removeAll(tasks)
//                notifyTasklistChanged(todoFilename, true, true)
//            } catch (e: Exception) {
//                Log.e(TAG, "Task archiving failed", e)
//                showToastShort(TodoApplication.app, "Task archiving failed")
//            }
//            broadcastFileSyncDone(TodoApplication.app.localBroadCastManager)
//        }
//    }
//
//    fun isSelected(item: Task): Boolean = item.selected
//
//    @Synchronized
//    fun numSelected(): Int {
//        return todoItems.toList().count { it.selected }
//    }
//
//    @Synchronized
//    fun selectTasks(items: List<Task>) {
//        Log.d(TAG, "Select")
//        items.forEach { selectTask(it) }
//        broadcastRefreshSelection(TodoApplication.app.localBroadCastManager)
//    }
//
//    @Synchronized
//    private fun selectTask(item: Task?) {
//        item?.selected = true
//    }
//
//    @Synchronized
//    private fun unSelectTask(item: Task) {
//        item.selected = false
//    }
//
//    @Synchronized
//    fun unSelectTasks(items: List<Task>) {
//        Log.d(TAG, "Unselect")
//        items.forEach { unSelectTask(it) }
//        broadcastRefreshSelection(TodoApplication.app.localBroadCastManager)
//
//    }
//
//    @Synchronized
//    fun clearSelection() {
//        Log.d(TAG, "Clear selection")
//        todoItems.iterator().forEach { it.selected = false }
//        broadcastRefreshSelection(TodoApplication.app.localBroadCastManager)
//
//    }
//
//    @Synchronized
//    fun getTaskIndex(t: Task): Int {
//        return todoItems.indexOf(t)
//    }
//
//    @Synchronized
//    fun getTaskAt(idx: Int): Task? {
//        return todoItems.getOrNull(idx)
//    }
//
//    @Synchronized
//    fun each (callback : (Task) -> Unit) {
//        todoItems.forEach { callback.invoke(it) }
//    }
//
//    @Synchronized
//    fun editTasks(from: Activity, tasks: List<Task>, prefill: String) {
//        Log.d(TAG, "Edit tasks")
//        pendingEdits.addAll(tasks)
//        startAddTaskActivity(from, prefill)
//    }
//
//    @Synchronized
//    fun clearPendingEdits() {
//        Log.d(TAG, "Clear selection")
//        pendingEdits.clear()
//    }
}