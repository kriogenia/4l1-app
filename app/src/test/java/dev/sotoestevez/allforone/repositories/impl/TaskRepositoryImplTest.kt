package dev.sotoestevez.allforone.repositories.impl

import com.haroldadmin.cnradapter.NetworkResponse
import dev.sotoestevez.allforone.api.ApiRequest
import dev.sotoestevez.allforone.api.schemas.*
import dev.sotoestevez.allforone.api.services.TaskService
import dev.sotoestevez.allforone.util.rules.CoroutineRule
import dev.sotoestevez.allforone.vo.Task
import dev.sotoestevez.allforone.vo.User
import dev.sotoestevez.allforone.vo.feed.Message
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.mockkConstructor
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class TaskRepositoryImplTest {

    @get:Rule
    var coroutineRule: CoroutineRule = CoroutineRule()

    // Mocks
    private lateinit var mockTaskService: TaskService

    // Test object
    private lateinit var repo: TaskRepositoryImpl

    @Before
    fun beforeEach() {
        mockTaskService = mockk()
        mockkConstructor(ApiRequest::class)
        repo = TaskRepositoryImpl(mockTaskService)
    }

    @Test
    fun `should return the relevant tasks of the user`(): Unit = coroutineRule.testDispatcher.runBlockingTest {
        val taskList = arrayListOf(
            Task("0", "0", User(id = "0", displayName = "0"), "0",true, 0),
            Task("1", "1", User(id = "1", displayName = "1"), "1",false, 1),
            Task("2", "2", User(id = "2", displayName = "2"), "2",true, 2)
        )

        val taskListResponse = TaskListResponse(taskList.map {
            TaskResponse(it.id, it.title, it.description,
                UserInfoMsg(it.submitter.id!!, it.submitter.displayName!!),
                it.done, it.timestamp, Message.Type.TASK)
        }.toTypedArray())

        val response: NetworkResponse.Success<TaskListResponse> = mockk()
        coEvery { response.code } returns 200
        coEvery { response.body } returns taskListResponse
        coEvery { mockTaskService.getTasks(any()) } returns response

        val result = repo.getTasks("token")

        coVerify(exactly = 1) { mockTaskService.getTasks("token") }
        Assert.assertEquals(result.size, 3)
        Assert.assertEquals(result, taskList)
    }

    @Test
    fun `should save the passed task`(): Unit = coroutineRule.testDispatcher.runBlockingTest {
        val savedTask = Task("1","title", User(id = "id1", displayName = "name1"), "description", false)

        val taskResponse = TaskResponse(savedTask.id, savedTask.title, savedTask.description,
            UserInfoMsg(savedTask.submitter.id!!, savedTask.submitter.displayName!!), savedTask.done,
            savedTask.timestamp, Message.Type.TASK)
        val response: NetworkResponse.Success<TaskResponse> = mockk()
        coEvery { response.code } returns 200
        coEvery { response.body } returns taskResponse
        coEvery { mockTaskService.postTask(any(), any()) } returns response

        val result = repo.save(Task(title = savedTask.title, submitter = savedTask.submitter,
            description = savedTask.description, done = savedTask.done, timestamp = savedTask.timestamp), "token")

        coVerify(exactly = 1) { mockTaskService.postTask("token", TaskRequest(savedTask.title,
            savedTask.description, UserInfoMsg(savedTask.submitter.id!!, savedTask.submitter.displayName!!),
            savedTask.done, savedTask.timestamp)) }
        Assert.assertEquals(result, savedTask)
    }

}