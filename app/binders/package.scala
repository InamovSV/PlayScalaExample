import daos.TaskDAO
import models.Task
import org.mongodb.scala.bson.ObjectId
import play.api.mvc.PathBindable

package object binders {
  implicit def objIdPathBindable(implicit objectIdBuilder: PathBindable[String]) = new PathBindable[ObjectId]{
    def bind(key: String, value: String): Either[String, ObjectId] =
      for{
        id <- objectIdBuilder.bind(key, value).right
        obj <- Option(new ObjectId(id)).toRight("Task not found").right
      } yield obj

    def unbind(key: String, value: ObjectId): String =
      objectIdBuilder.unbind(key, value.toString)
  }
}
