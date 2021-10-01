package dev.sotoestevez.allforone.repositories

import dev.sotoestevez.allforone.vo.Task

/** Repository to manage all the task related operations */
interface TaskRepository {

	/**
	 * Save a new task in the server
	 *
	 * @param task	Task to create
	 * @param token	Authentication token
	 */
	suspend fun save(task: Task, token: String): Task

}