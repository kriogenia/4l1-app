package dev.sotoestevez.allforone.repositories.impl

import com.google.gson.Gson
import dev.sotoestevez.allforone.api.ApiRequest
import dev.sotoestevez.allforone.api.schemas.FeedMsg
import dev.sotoestevez.allforone.api.schemas.TaskRequest
import dev.sotoestevez.allforone.api.schemas.UserInfoMsg
import dev.sotoestevez.allforone.api.services.FeedService
import dev.sotoestevez.allforone.api.services.TaskService
import dev.sotoestevez.allforone.repositories.FeedRepository
import dev.sotoestevez.allforone.repositories.TaskRepository
import dev.sotoestevez.allforone.vo.Message
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

	override suspend fun save(task: Task, token: String): Task {
		val data = TaskRequest(task.title, task.description, UserInfoMsg(task.submitter.id!!, task.submitter.displayName!!),
			task.done, task.timestamp)
		val response = ApiRequest(suspend { service.new(token, data) }).performRequest()
		return Task(response._id, response.title, User(id = response.submitter.id, displayName = response.submitter.displayName),
			response.description, response.done, response.timestamp)
	}
}