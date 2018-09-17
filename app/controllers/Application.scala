package controllers

import java.text.SimpleDateFormat
import java.util.Calendar

import daos.TaskDAO
import models.Task
import javax.inject._
import org.mongodb.scala.bson.ObjectId
import play.api.data.Forms._
import play.api.data._
import play.api.mvc._

@Singleton
class Application @Inject()(messagesAction: MessagesActionBuilder, cc: ControllerComponents) extends AbstractController(cc) {

//  val uri = "mongodb://testUser:1234@cluster0-shard-00-00-htlg0.mongodb.net:27017,cluster0-shard-00-01-htlg0.mongodb.net:27017,cluster0-shard-00-02-htlg0.mongodb.net:27017/test?ssl=true&streamType=netty&replicaSet=Cluster0-shard-0&authSource=admin&retryWrites=true"

  def tasks = messagesAction{ implicit request: MessagesRequest[AnyContent] =>
    Ok(views.html.index(TaskDAO.findAll().toList, taskForm/*.fill(("Field 1", "Field 2"))*/))
  }

  def newTask = messagesAction { implicit request: MessagesRequest[AnyContent] =>
    taskForm.bindFromRequest.fold(
      errors => BadRequest(views.html.index(Task.all(), errors)),
      x=>x match { case(label,who) =>
        // Получаем текущее время
        val today = Calendar.getInstance().getTime()
        val timeFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss")
        val time = timeFormat.format(today)
        //----------------------------
        TaskDAO.create(Task(new ObjectId(), label, who, time, 0))
        Redirect(routes.Application.tasks)
//        Ok(views.html.test((label, who, time)))
      }

    )
  }

  def deleteTask(id: ObjectId) = Action {
    TaskDAO.delete(id)
    Redirect(routes.Application.tasks)
  }

  def completeTask(id: ObjectId) = Action {
    TaskDAO.update(id, "ready", 1)
    Redirect(routes.Application.tasks)
  }

  val taskForm = Form(
    tuple (
      "label" -> nonEmptyText,
      "who" -> nonEmptyText
    )
  )
}
