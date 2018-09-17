package models

import org.mongodb.scala.bson.ObjectId

case class Task(_id: ObjectId,
                label: String,
                who: String,
                mytime: String,
                ready: Short)


object Task {

  def all(): List[Task] = Nil

  def create(label: String, who: String, time: String) {}

  def delete(id: Long) {}

  def complete(id: Long) {}

}
