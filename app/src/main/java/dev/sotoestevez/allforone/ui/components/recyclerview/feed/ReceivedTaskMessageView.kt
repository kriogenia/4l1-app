package dev.sotoestevez.allforone.ui.components.recyclerview.feed

import dev.sotoestevez.allforone.R
import dev.sotoestevez.allforone.ui.components.recyclerview.tasks.listeners.TaskListener
import dev.sotoestevez.allforone.vo.feed.TaskMessage

class ReceivedTaskMessageView(
    data: TaskMessage,
    listener: TaskListener
) : TaskMessageView(data, listener) {

    override val layoutId: Int = R.layout.content_received_task

    override val viewType: Int = FeedView.Type.TASK_RECEIVED.ordinal

    /** Author of the message */
    val author: String = data.submitter.displayName!!

}