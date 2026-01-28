package com.test.labmedia.data

import android.app.Application
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.test.labmedia.data.entity.AnswersDto
import com.test.labmedia.data.entity.QuestionDto
import com.test.labmedia.data.entity.StatusDto
import com.test.labmedia.data.entity.TestDto
import com.test.labmedia.data.source.NetworkDataSource
import com.test.labmedia.domain.model.AnswersModel
import com.test.labmedia.domain.model.QuestionModel
import com.test.labmedia.domain.model.RewardModel
import com.test.labmedia.domain.model.StatusModel
import com.test.labmedia.domain.model.TestModel
import com.test.labmedia.domain.repository.Repository
import java.io.InputStreamReader
import java.time.LocalDateTime
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val application: Application,
    private val networkDataSource: NetworkDataSource,
    private val gson: Gson
) : Repository {

    override fun loadQuestions(): List<QuestionModel> {
        return try {
            networkDataSource.loadQuestions().map { it.toModel() }
        } catch (e: Exception) {
            readQuestionsFromAssets().map { it.toModel() }
        }
    }

    override fun loadTest(): TestModel {
        return try {
            networkDataSource.loadTest().toModel()
        } catch (e: Exception) {
            mockTest()
        }
    }

    private fun readQuestionsFromAssets(): List<QuestionDto> {
        val type = object : TypeToken<List<QuestionDto>>() {}.type
        application.assets.open("questions.json").use { input ->
            InputStreamReader(input).use { reader ->
                return gson.fromJson(reader, type)
            }
        }
    }

    private fun mockTest(): TestModel {
        return TestModel(
            id = 0,
            title = "Аттестация по информационной безовасности для сотрудников офиса",
            description = "Новый год - это время для новых начинаний и возможностей. Мы с нетерпением ждем продолжения нашего партнерства и совместных проектов в грядущем году. Мы уверены, что совместными усилиями мы сможем достичь еще больших высот и преуспеть в наших общих целях.\n" +
                    "Желаем вам и вашим близким счастья, здоровья и успехов в Новом году. Пусть этот год будет наполнен радостью, достижениями и благополучием. Мы надеемся, что наше сотрудничество будет продолжаться и приносить взаимную пользу и удовлетворение.",
            dateTime = LocalDateTime.of(2026,1,2,15,0,0),
            dop_info = "В итоговый результат попадёт последняя завершённая попытка",
            status = StatusModel.IN_PROGRESS,
            image_url = "url",
            reward = RewardModel(
                stars = 600,
                coins = 20,
                certificate = true
            )
        )
    }

    private fun QuestionDto.toModel(): QuestionModel {
        return QuestionModel(
            question = question,
            topic = topic,
            comment = comment,
            correct_answer = correct_answer,
            id = id,
            score = score,
            answers = answers.toModel()
        )
    }

    private fun AnswersDto.toModel(): AnswersModel {
        return AnswersModel(
            answer1 = answer1,
            answer2 = answer2,
            answer3 = answer3,
            answer4 = answer4,
            answer5 = answer5,
            answer6 = answer6
        )
    }

    private fun TestDto.toModel(): TestModel {
        return TestModel(
            id = id,
            title = title,
            status = status.toModel(),
            dateTime = LocalDateTime.parse(dateTime),
            description = description,
            dop_info = dop_info,
            image_url = image_url,
            reward = RewardModel(
                stars = reward.stars,
                coins = reward.coins,
                certificate = reward.certificate
            )
        )
    }

    private fun StatusDto.toModel(): StatusModel {
        return when (this) {
            StatusDto.IN_PROGRESS -> StatusModel.IN_PROGRESS
            StatusDto.DONE -> StatusModel.DONE
            StatusDto.NOT_STARTED -> StatusModel.NOT_STARTED
        }
    }
}