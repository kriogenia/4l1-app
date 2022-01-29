package dev.sotoestevez.allforone.ui.components.exchange.dialog

import android.content.Context
import dev.sotoestevez.allforone.R
import dev.sotoestevez.allforone.ui.components.recyclerview.bonds.BondView
import dev.sotoestevez.allforone.ui.components.recyclerview.tasks.TaskView
import dev.sotoestevez.allforone.vo.User

/** [DialogConfirmationRequest] for setting task as done or not done */
data class SetTaskDoneConfirmation(private val task: TaskView, private val confirmAction: (TaskView) -> Unit) :
    DialogConfirmationRequest {

    private val title: Int = if (task.done) R.string.revert_task_title else R.string.complete_task_title
    private val message = if (task.done) R.string.revert_task_msg else R.string.complete_task_msg

    override fun onConfirm() = confirmAction(task)

    override fun getTitle(context: Context): String = context.getString(title)

    override fun getMessage(context: Context): String = String.format(context.getString(message), task.data.title)

}

/** [DialogConfirmationRequest] to delete a task */
data class DeleteTaskConfirmation(private val task: TaskView, private val confirmAction: (TaskView) -> Unit) :
    DialogConfirmationRequest {

    override fun onConfirm() = confirmAction(task)

    override fun getTitle(context: Context): String = context.getString(R.string.delete_task_title)

    override fun getMessage(context: Context): String =
        String.format(context.getString(R.string.delete_task_msg), task.data.title)

}

/** [DialogConfirmationRequest] to delete a bond from a bond view */
data class DeleteBondViewConfirmation(private val bond: BondView, private val confirmAction: (BondView) -> Unit) :
    DialogConfirmationRequest {

    override fun onConfirm() = confirmAction(bond)

    override fun getTitle(context: Context): String = context.getString(R.string.delete_bond_title)

    override fun getMessage(context: Context): String =
        String.format(context.getString(R.string.delete_bond_msg), bond.data.displayName)

}

/** [DialogConfirmationRequest] to delete a bond */
data class DeleteBondUserConfirmation(private val bond: User, private val confirmAction: (User) -> Unit) :
    DialogConfirmationRequest {

    override fun onConfirm() = confirmAction(bond)

    override fun getTitle(context: Context): String = context.getString(R.string.delete_bond_title)

    override fun getMessage(context: Context): String =
        String.format(context.getString(R.string.delete_bond_msg), bond.displayName)

}