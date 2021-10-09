package dev.sotoestevez.allforone.repositories.impl

import dev.sotoestevez.allforone.api.ApiRequest
import dev.sotoestevez.allforone.api.schemas.TaskRequest
import dev.sotoestevez.allforone.api.schemas.TaskResponse
import dev.sotoestevez.allforone.api.schemas.UserInfoMsg
import dev.sotoestevez.allforone.api.services.TaskService
import dev.sotoestevez.allforone.repositories.TaskRepository
import dev.sotoestevez.allforone.util.extensions.logDebug
import dev.sotoestevez.allforone.vo.Task
import dev.sotoestevez.allforone.vo.User

/**
 * Implementation of [TaskRepository]
 *
 * @constructor
 */
class TaskRepositoryImpl(
	private val service: TaskService
): TaskRepository {

	override suspend fun getTasks(token: String): List<Task> {
		logDebug("Requested the retrieval of tasks")
		val response = ApiRequest(suspend { service.getTasks(token) }).performRequest()
		return response.tasks.map { buildTask(it) }
	}

	override suspend fun save(task: Task, token: String): Task {
		val data = TaskRequest(task.title, task.description, UserInfoMsg(task.submitter.id!!, task.submitter.displayName!!),
			task.done, task.timestamp)
		val response = ApiRequest(suspend { service.postTask(token, data) }).performRequest()
		return buildTask(response)
	}

	override suspend fun updateDone(task: Task, token: String) {
		if (task.done) service.postTaskDone(token, task.id)
		else service.deleteTaskDone(token, task.id)
	}

	private fun buildTask(response: TaskResponse) = Task(
		response._id, response.title,
		User(id = response.submitter.id, displayName = response.submitter.displayName),
		response.description, response.done, response.timestamp)

}