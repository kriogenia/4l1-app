package dev.sotoestevez.allforone.repositories

import dev.sotoestevez.allforone.vo.Task

/** Repository to manage all the task related operations */
interface TaskRepository {

	/**
	 * Retrieves the relevant tasks of the user
	 *
	 * @param token Authentication token
	 * @return		List of relevant tasks
	 */
	suspend fun getTasks(token: String): List<Task>

	/**
	 * Save a new task in the server
	 *
	 * @param task	Task to create
	 * @param token	Authentication token
	 * @return		Saved task
	 */
	suspend fun save(task: Task, token: String): Task

	/**
	 * Updates a task with a new done value
	 *
	 * @param task	Task to update
	 * @param token	Authentication token
	 */
	suspend fun updateDone(task: Task, token: String): Unit

}