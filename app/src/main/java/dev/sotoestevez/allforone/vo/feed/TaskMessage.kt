package dev.sotoestevez.allforone.vo.feed

import dev.sotoestevez.allforone.api.schemas.PlainMessage
import dev.sotoestevez.allforone.vo.Task
import dev.sotoestevez.allforone.vo.User

/**
 * Represents a task related text message that can be sent through the Feed
 *
 * @property task    Task related to the message
 */
data class TaskMessage(
    val task: Task
) : Message {

    constructor(builder: Message.Builder) : this(Task(
        builder.data!!._id!!,
        builder.data!!.title!!,
        User(id = builder.data!!.submitter, displayName = builder.data!!.username),
        builder.data!!.description,
        builder.data!!.done!!,
        builder.data!!.timestamp
    ).apply { lastUpdate = builder.data!!.lastUpdate })

    override val id: String
        get() = task.id

    override val submitter: User
        get() = task.submitter

    override val timestamp: Long
        get() = task.timestamp

    override val content: String
        get() = task.title

    override fun toDto(): PlainMessage = PlainMessage(
        title = content,
        description = task.description,
        done = task.done,
        submitter = submitter.id!!,
        username = submitter.displayName!!,
        timestamp = timestamp,
        lastUpdate = task.lastUpdate,
        type = Message.Type.TASK
    )

}
