package dev.sotoestevez.allforone.repositories

import dev.sotoestevez.allforone.vo.Task

/** Repository to manage all the task related operations */
interface TaskRepository {

	/**
	 * Sends a new task to create
	 *
	 * @param task	Task to create
	 * @param token	Authentication token
	 */
	suspend fun save(task: Task, token: String): Task

}