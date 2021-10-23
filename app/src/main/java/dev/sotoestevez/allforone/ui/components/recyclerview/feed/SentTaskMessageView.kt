package dev.sotoestevez.allforone.ui.components.recyclerview.feed

import dev.sotoestevez.allforone.R
import dev.sotoestevez.allforone.ui.components.recyclerview.tasks.listeners.TaskListener
import dev.sotoestevez.allforone.vo.feed.TaskMessage

class SentTaskMessageView(
    data: TaskMessage,
    listener: TaskListener
) : TaskMessageView(data, listener) {

    override val layoutId: Int = R.layout.content_sent_task

    override val viewType: Int = FeedView.Type.TASK_SENT.ordinal

}